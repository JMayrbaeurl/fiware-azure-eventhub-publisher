package com.microsoft.samples.iot.fiware.publisher;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubProducerClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    private final Log logger = LogFactory.getLog(NotificationController.class);

    @Autowired
    private EventHubProducerClient eventHubClient;

    @PostMapping("notification/{entityName}")
    public void publishNotification(@PathVariable String entityName, @RequestBody String jsonString) {

        logger.info("Subscription notification called for entity " + entityName + " and message '" + jsonString + "'");

        EventDataBatch batch = this.eventHubClient.createBatch();
        batch.tryAdd(new EventData(jsonString));
        this.eventHubClient.send(batch);
    }
}