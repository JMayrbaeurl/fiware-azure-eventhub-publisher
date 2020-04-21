package com.microsoft.samples.iot.fiware.publisher;

import java.util.Iterator;
import java.util.Map;

import com.azure.core.amqp.exception.AmqpException;
import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.samples.iot.fiware.ngsiv2.NotificationMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

public class EventHubPublisher implements Publisher {

    private final Log logger = LogFactory.getLog(EventHubPublisher.class);

    @Autowired
    private EventHubProducerClient eventHubClient;

    private ObjectMapper objectMapper = new ObjectMapper();

    public EventHubPublisher() {
    }

    @Override
    public void publishNotificationForEntityChange(@NonNull final String message) {
        
        logger.info(
                "Subscription notification called for any entity type and message '" + message + "'");

        this.doPublishNotificationForEntityChange("Any", message);
    }

    @Override
    public void publishNotificationForEntityChange(@NonNull final NotificationMessage message) {
        
        logger.info(
            "Subscription notification called for any entity type and message '" + message + "'");

        this.doPublishNotificationForEntityChange("Any", message);
    }

    @Override
    public void publishNotificationForEntityChange(final String entityType, @NonNull final String message) {

        logger.info(
                "Subscription notification called for entity type " + entityType + " and message '" + message + "'");

        this.doPublishNotificationForEntityChange(entityType, message);
    }

    protected void doPublishNotificationForEntityChange(final String entityType, @NonNull final String message) {

        EventDataBatch batch = this.doCreateBatch();
        try {
            batch.tryAdd(new EventData(message));
            this.eventHubClient.send(batch);
        } catch (AmqpException ex) {
            logger.error(
                    "Notification message is too large to be sent in one batch. Splitting it into multiple events");
            try {
                this.publishNotificationForEntityChange(entityType,
                        this.objectMapper.readValue(message, NotificationMessage.class));
            } catch (JsonProcessingException e) {
                logger.error("Exception reading Json notification message", e);
                throw new RuntimeException("Exception reading Json notification message", e);
            }
        }
    }

    @Override
    public void publishNotificationForEntityChange(final String entityType,
            @NonNull final NotificationMessage message) {

        logger.info(
                "Subscription notification called for entity type " + entityType + " and message '" + message + "'");

        this.doPublishNotificationForEntityChange(entityType, message);
    }

    protected void doPublishNotificationForEntityChange(final String entityType,
            @NonNull final NotificationMessage message) {

        if (message.getData() != null && message.getData().size() > 0) {
            EventDataBatch batch = this.doCreateBatch();

            Iterator<Map<String, Object>> iter = message.getData().iterator();
            while (iter.hasNext()) {
                NotificationMessage singleMessage = new NotificationMessage(message.getSubscriptionId(), iter.next());
                boolean wasAdded = false;
                EventData eventData = null;
                try {
                    eventData = new EventData(this.objectMapper.writeValueAsString(singleMessage));
                    wasAdded = batch.tryAdd(eventData);
                } catch (JsonProcessingException e) {
                    logger.error("Exception serializing notification message", e);
                    throw new RuntimeException("Exception serializing notification message", e);
                }
                if (!wasAdded) {
                    this.eventHubClient.send(batch);
                    batch = this.doCreateBatch();
                    batch.tryAdd(eventData);
                }
            }
            this.eventHubClient.send(batch);
        }
    }

    protected EventDataBatch doCreateBatch() {

        EventDataBatch batch = this.eventHubClient.createBatch();
        return batch;
    }

    public EventHubProducerClient getEventHubClient() {
        return eventHubClient;
    }

    public void setEventHubClient(EventHubProducerClient eventHubClient) {
        this.eventHubClient = eventHubClient;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}