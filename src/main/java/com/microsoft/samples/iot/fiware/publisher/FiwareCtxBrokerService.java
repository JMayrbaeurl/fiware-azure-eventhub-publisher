package com.microsoft.samples.iot.fiware.publisher;

import java.util.List;
import java.util.function.Predicate;

import com.microsoft.samples.iot.fiware.ngsiv2.Subscription;

public interface FiwareCtxBrokerService {

    public static final String ALL_PATHEXT = "all";

    public String subscribeToAllChanges();
    public String subscribeToEntityWithType(String entityTypeName);
    public String subscribeToEntityWithType(String entityTypeName, String attrName);

    public List<Subscription> queryForSubscriptions();
    public List<Subscription> queryForSubscriptions(int limit, int offset);

    public boolean hasSubscriptionForAnyType();
    public boolean hasSubscriptionForAnyType(Predicate<Subscription> filter);
    public boolean hasSubscriptionForAllChanges();
}