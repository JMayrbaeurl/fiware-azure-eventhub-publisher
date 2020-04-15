package com.microsoft.samples.iot.fiware.publisher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

@ExtendWith(SpringExtension.class)
public class HttpFiwareCtxBrokerServiceImplTests {

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
    public void testContextCreation() {
        Assertions.assertNotNull(this.service);
    }

    @Test
    public void testSubscribeToProduct() {
        Assertions.assertNotNull(this.service.subscribeToEntityWithType("Product", "size"));
    }

    @Test
    public void testSubscribeToAllProducts() {
        Assertions.assertNotNull(this.service.subscribeToEntityWithType("Product"));
    }

    @Test
    public void testSubscribeToAllChanges() {
        Assertions.assertNotNull(this.service.subscribeToAllChanges());
    }

    @Test
    public void testReadAllSubscriptions() {

        Assertions.assertNotNull(this.service.queryForSubscriptions());
    }
}