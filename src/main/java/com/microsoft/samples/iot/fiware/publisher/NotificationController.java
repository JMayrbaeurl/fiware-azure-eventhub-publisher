package com.microsoft.samples.iot.fiware.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.samples.iot.fiware.ngsiv2.NotificationMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    private final Log logger = LogFactory.getLog(NotificationController.class);

    @Value("${fiware.publisher.singleMessages:true}")
    private boolean singleMessages;

    @Autowired
    private Publisher publisher;

    @PostMapping("notification/{entityType}")
    public void publishNotification(@PathVariable String entityType, @RequestBody String jsonString) {

        logger.info("Received notification for entity type " + entityType + " and message '" + jsonString + "'");

        if (!this.singleMessages)
            this.publisher.publishNotificationForEntityChange(entityType, jsonString);
        else {
            NotificationMessage message = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                message = objectMapper.readValue(jsonString, NotificationMessage.class);
            } catch (JsonProcessingException ex) {
                logger.error("Exception reading notification message '" + jsonString + "'", ex);
                throw new RuntimeException("Exception reading notification message '" + jsonString + "'", ex);
            }
            this.publisher.publishNotificationForEntityChange(entityType, message);
        }
    }

    public boolean isSingleMessages() {
        return singleMessages;
    }

    public void setSingleMessages(boolean singleMessages) {
        this.singleMessages = singleMessages;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
    
}