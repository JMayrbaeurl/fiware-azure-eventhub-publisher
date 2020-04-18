package com.microsoft.samples.iot.fiware.publisher;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.samples.iot.fiware.ngsiv2.NotificationMessage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.FileCopyUtils;

@ExtendWith(SpringExtension.class)
public class EventHubPublisherTests {

    @TestConfiguration
    static class EventHubPublisherTestsConfiguration {

        @Value("${FIWARE_EHNS_CONNSTRING}")
        private String connString;

        @Bean
        public EventHubPublisher createPublisher(EventHubProducerClient client) {

            EventHubPublisher aPublisher = new EventHubPublisher();
            aPublisher.setEventHubClient(client);
            return aPublisher;
        }

        @Bean
        public EventHubProducerClient createEventHubClient() {

            // create a producer using the namespace connection string and event hub name
            EventHubProducerClient producer = new EventHubClientBuilder()
                    .connectionString(this.connString, "fiware-notifications").buildProducerClient();

            return producer;
        }
    }

    @Autowired
    private EventHubPublisher publisher;

    @Value("classpath:notificationMsgSample1.json")
    private Resource resource;

    @Test
    public void testContextLoading() {
        Assertions.assertNotNull(this.publisher);
    }

    @Test
    public void testBatchedSending() throws JsonMappingException, JsonProcessingException {

        NotificationMessage sampleMsg = this.readNotificationMessageSample1();
        Assertions.assertNotNull(sampleMsg);

        this.publisher.publishNotificationForEntityChange("Room", sampleMsg);
    }

    @Test
    public void testMultipleBatchSending() {

        NotificationMessage sampleMsg = new NotificationMessage("12345");
        sampleMsg.setData(new ArrayList<Map<String, Object>>());
        for (int i = 0; i < 25; i++)
            sampleMsg.getData().add(this.createRoomData("Room" + i, 25+i, 30+1));

        this.publisher.publishNotificationForEntityChange("Room", sampleMsg);
    }

    private NotificationMessage readNotificationMessageSample1() throws JsonMappingException, JsonProcessingException {

        String aString;

        try (Reader reader = new InputStreamReader(resource.getInputStream())) {
            aString = FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(aString, NotificationMessage.class);
    }

    private Map<String, Object> createRoomData(String roomname, int temperature, int humidity) {
        
        Map<String, Object> room = new HashMap<String, Object>();

        room.put("id", roomname);
        room.put("type", "Room");
        
        Map<String, Object> temperatureMap = new HashMap<String, Object>();
        temperatureMap.put("value", Integer.valueOf(temperature));
        temperatureMap.put("type", "Number");
        temperatureMap.put("metadata", new HashMap<String, Object>());
        room.put("temperature", temperatureMap);

        Map<String, Object> humMap = new HashMap<String, Object>();
        humMap.put("value", Integer.valueOf(humidity));
        humMap.put("type", "percentage");
        humMap.put("metadata", new HashMap<String, Object>());
        room.put("humidity", humMap);

        return room;
    }
}