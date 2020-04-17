package com.microsoft.samples.iot.fiware.publisher;

import com.microsoft.samples.iot.fiware.ngsiv2.Subscription;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

@ExtendWith(SpringExtension.class)
public class SubscriptionIteratorTests {

    @TestConfiguration
    static class HttpFiwareCtxBrokerServiceImplTestsConfiguration {
  
        @Bean
        public WebClient webClient() {
            WebClient client = WebClient.builder().baseUrl("http://localhost:1026/v2/").build();
            return client;
        }

        @Bean
        public FiwareCtxBrokerService ctxService(WebClient webClient) {
            HttpFiwareCtxBrokerServiceImpl svcImpl = new HttpFiwareCtxBrokerServiceImpl();
            svcImpl.setWebClient(webClient);
            return svcImpl;
        }
    }

    @Autowired
    private FiwareCtxBrokerService service;

    @Test
    public void testWithoutSetup() {

        SubscriptionIterator iter = new SubscriptionIterator(this.service, 3);
        while(iter.hasNext()) {
            Subscription subscription = iter.next();
            Assertions.assertNotNull(subscription);
        }
    }

    @Test
    public void testForAnyType() {

        Assertions.assertFalse(this.service.hasSubscriptionForAnyType());
    }

    @Test
    public void testForAnyTypeWithPredicate() {

        Assertions.assertTrue(this.service.hasSubscriptionForAnyType(
            sub -> "Notify me of all changes".equals(sub.getDescription())));

        Assertions.assertFalse(this.service.hasSubscriptionForAnyType(
                sub -> "Notify me of any changes".equals(sub.getDescription())));
    }

    @Test
    public void testForAllChanges() {

        Assertions.assertTrue(this.service.hasSubscriptionForAllChanges());
    }
}