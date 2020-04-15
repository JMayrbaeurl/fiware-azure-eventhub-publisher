package com.microsoft.samples.iot.fiware.ngsiv2;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Subscription {

    public enum Status { failed };

    private String id;

    private String description;

    private String status;

    private SubscriptionSubject subject;

    private Notification notification;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SubscriptionSubject getSubject() {
        return subject;
    }

    public void setSubject(SubscriptionSubject subject) {
        this.subject = subject;
    }

    public Subscription() {
        this.subject = new SubscriptionSubject();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

}