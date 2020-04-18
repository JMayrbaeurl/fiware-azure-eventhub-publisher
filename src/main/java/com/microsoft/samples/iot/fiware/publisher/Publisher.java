package com.microsoft.samples.iot.fiware.publisher;

import com.microsoft.samples.iot.fiware.ngsiv2.NotificationMessage;

public interface Publisher {

    public void publishNotificationForEntityChange(String message);
    public void publishNotificationForEntityChange(NotificationMessage message);
    public void publishNotificationForEntityChange(String entityType, String message);
    public void publishNotificationForEntityChange(String entityType, NotificationMessage message);
}