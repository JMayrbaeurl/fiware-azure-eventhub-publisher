package com.microsoft.samples.iot.fiware.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.samples.iot.fiware.ngsiv2.Subscription;
import com.microsoft.samples.iot.fiware.ngsiv2.SubscriptionFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class SubscriptionRequestTests {

    @Test
    public void testCreation() throws JsonProcessingException {

        SubscriptionFactory factory = new SubscriptionFactory();
        Subscription request = factory.create("Product", "price", "http://tutorial:3000/subscription/price-change");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
        System.out.println(json);
    }
}