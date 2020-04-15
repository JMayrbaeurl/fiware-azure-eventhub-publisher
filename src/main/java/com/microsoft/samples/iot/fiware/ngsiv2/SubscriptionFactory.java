package com.microsoft.samples.iot.fiware.ngsiv2;

import java.util.HashMap;
import java.util.Map;

public class SubscriptionFactory {

    public Subscription create(final String entityName, final String attributeName, final String url) {
        Subscription result = new Subscription();
        result.setSubject(SubscriptionSubject.create(entityName, attributeName));

        Map<String, String> urlEntry = new HashMap<String, String>();
        urlEntry.put("url", url);
        result.setNotification(new Notification(urlEntry));

        return result;
    }
}