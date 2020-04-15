package com.microsoft.samples.iot.fiware.publisher;

import java.util.List;

import com.microsoft.samples.iot.fiware.ngsiv2.Subscription;

public interface FiwareCtxBrokerService {

    public String subscribeToAllChanges();
    public String subscribeToEntityWithType(String entityTypeName);
    public String subscribeToEntityWithType(String entityTypeName, String attrName);

    public List<Subscription> queryForSubscriptions();
}