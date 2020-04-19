package com.microsoft.samples.iot.fiware.publisher;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import com.microsoft.samples.iot.fiware.ngsiv2.ContextBrokerException;
import com.microsoft.samples.iot.fiware.ngsiv2.Subscription;
import com.microsoft.samples.iot.fiware.ngsiv2.SubscriptionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;

@Service
public class HttpFiwareCtxBrokerServiceImpl implements FiwareCtxBrokerService {

    private final Log logger = LogFactory.getLog(HttpFiwareCtxBrokerServiceImpl.class);

    private static final String SUBSCRIPTIONS_PATH = "subscriptions";

    @Value("${fiware.publisher.notificationurl:http://localhost:8080/notification/}")
    private String notificationURL;

    @Autowired
    private WebClient webClient;

    private SubscriptionFactory subFactory = new SubscriptionFactory();

    @Override
    public String subscribeToEntityWithType(@NonNull final String entityName) {
        return this.subscribeToEntityWithType(entityName, null);
    }

    @Override
    public String subscribeToEntityWithType(@NonNull final String entityName, final String attrName) {

        if (entityName != null)
            logger.debug("Sending subscription request to FiWare Context broker for " + entityName);

        RequestHeadersSpec<?> request = this.webClient.post().uri(SUBSCRIPTIONS_PATH).accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(this.createSubscriptionRequestObject(entityName, attrName)));
        ClientResponse response = request.exchange().block();

        logger.debug("Subscription request with status code " + response.statusCode().name());

        if (response.statusCode() != HttpStatus.CREATED) {
            String errMsg = response.bodyToMono(String.class).block();
            logger.error("Could not add subscription. Status code = " + response.statusCode().name() + " ," + errMsg);
            throw new ContextBrokerException(errMsg);
        }

        return response.headers().header("Location").get(0);
    }

    @Override
    public String subscribeToAllChanges() {

        logger.debug("Sending subscription request to FiWare Context broker for all changes");

        return this.subscribeToEntityWithType(null, null);
    }

    @Override
    public List<Subscription> queryForSubscriptions() {

        logger.debug("Querying for existing subscriptions");
        List<Subscription> result = this.doQueryForSubscriptions(-1, -1);
        logger.debug("Found " + (result != null ? result.size() : 0) + " existing subscriptions");

        return result;
    }

    @Override
    public List<Subscription> queryForSubscriptions(int limit, int offset) {

        logger.debug("Querying for existing subscriptions with limit " + limit + " and offset " + offset);
        List<Subscription> result = this.doQueryForSubscriptions(limit, offset);
        logger.debug("Found " + (result != null ? result.size() : 0) + " existing subscriptions");

        return result;
    }

    protected List<Subscription> doQueryForSubscriptions(int limit, int offset) {

        RequestHeadersSpec<?> request = null;
        if (limit > 0 || offset > 0) {
            request = this.webClient.get().uri(uriBuilder -> uriBuilder.path(SUBSCRIPTIONS_PATH)
                    .queryParam("limit", limit).queryParam("offset", offset).build());
        } else
            request = this.webClient.get().uri(SUBSCRIPTIONS_PATH);

        Subscription[] response = null;

        try {
            response = request.retrieve().bodyToMono(Subscription[].class).block();
        } catch (WebClientResponseException ex) {
            logger.error("Response: " + ex.getResponseBodyAsString() + ", " + ex.getMessage(), ex);
            throw ex;
        }

        return Arrays.asList(response);
    }

    @Override
    public boolean hasSubscriptionForAnyType() {

        boolean result = false;

        SubscriptionIterator iter = new SubscriptionIterator(this, 10);
        while (iter.hasNext()) {
            if (iter.next().isForAnyType()) {
                return true;
            }
        }

        return result;
    }

    @Override
    public boolean hasSubscriptionForAnyType(Predicate<Subscription> filter) {

        boolean result = false;

        if (filter == null)
            return this.hasSubscriptionForAnyType();
        else {
            SubscriptionIterator iter = new SubscriptionIterator(this, 10);
            while (iter.hasNext()) {
                Subscription subscription = iter.next();
                if (subscription.isForAnyType() && filter.test(subscription)) {
                    return true;
                }
            }
        }
        return result;
    }

    @Override
    public boolean hasSubscriptionForAllChanges() {

        return this.hasSubscriptionForAnyType(
                sub -> sub.getNotification() != null && sub.getNotification().getHttp() != null
                        && (this.notificationURL + ALL_PATHEXT).equals(sub.getNotification().getHttp().get("url")));
    }

    /**
     * Returns true if subscription was already done
     */
    @Override
    public boolean subscribeToAllChangesIfNotAlready() {
        
        logger.info("Adding subscription for all changes");

        boolean result = this.hasSubscriptionForAllChanges();
        if (!result) 
            this.subscribeToAllChanges();
        else
            logger.info("Subscription for all changes was already available");
        
        return result;
    }

    private Subscription createSubscriptionRequestObject(final String entityName, final String attrName) {

        Subscription result = this.subFactory.create(entityName, attrName,
                this.notificationURL + (entityName != null ? entityName.toLowerCase() : ALL_PATHEXT));
        result.setDescription("Subscription for publishing changes to Azure services");
        return result;
    }

    public String getNotificationURL() {
        return notificationURL;
    }

    public void setNotificationURL(String notificationURL) {
        this.notificationURL = notificationURL;
    }

    public WebClient getWebClient() {
        return webClient;
    }

    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

}