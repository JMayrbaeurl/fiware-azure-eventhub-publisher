package com.microsoft.samples.iot.fiware.publisher;

import java.util.Arrays;
import java.util.List;

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

    @Value("${fiware.publisher.notificationurl:http://localhost:8080/subscription/}")
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

        RequestHeadersSpec<?> request = this.webClient.get().uri(SUBSCRIPTIONS_PATH);
        Subscription[] response = null;

        try {
            response = request.retrieve().bodyToMono(Subscription[].class).block();
        } catch (WebClientResponseException ex) {
            logger.error("Response: " + ex.getResponseBodyAsString() + ", " + ex.getMessage(), ex);
            throw ex;
        }

        logger.debug("Found " + response.length + " existing subscriptions");

        return Arrays.asList(response);
    }

    private Subscription createSubscriptionRequestObject(final String entityName, final String attrName) {

        Subscription result = this.subFactory.create(entityName, attrName,
                this.notificationURL + (entityName != null ? entityName.toLowerCase() : "all"));
        result.setDescription("description");
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