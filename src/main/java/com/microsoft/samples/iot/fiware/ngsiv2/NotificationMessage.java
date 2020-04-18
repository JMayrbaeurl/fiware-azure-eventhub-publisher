package com.microsoft.samples.iot.fiware.ngsiv2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class NotificationMessage {

    private String subscriptionId;

    private List<Map<String, Object>> data;

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public NotificationMessage() {
    }

    public NotificationMessage(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public NotificationMessage(String subscriptionId, List<Map<String, Object>> data) {
        this.subscriptionId = subscriptionId;
        this.data = data;
    }

    public NotificationMessage(String subscriptionId, Map<String, Object> data) {

        this.subscriptionId = subscriptionId;

        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        dataList.add(data);
        this.data = dataList;
    }
    
}