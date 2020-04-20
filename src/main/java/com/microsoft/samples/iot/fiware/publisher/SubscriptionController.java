package com.microsoft.samples.iot.fiware.publisher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.microsoft.samples.iot.fiware.ngsiv2.Subscription;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class SubscriptionController {

    private final Log logger = LogFactory.getLog(SubscriptionController.class);

    @Autowired
    private FiwareCtxBrokerService ctxBrokerService;

    @PostMapping("subscriptions/{entityType}")
    public Map<String, Object> subscribeToAllEntitiesOfType(@NonNull @PathVariable String entityType) {

        logger.info("Adding subscription for all entites of type ' " + entityType + "'");

        String subID = this.ctxBrokerService.subscribeToEntityWithType(entityType);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("Subscription ID", subID);

        return result;
    }

    @PostMapping("subscriptions")
    public Map<String, Object> createSubscription(@NonNull @RequestBody Subscription subscription) {

        logger.info("Adding subscription with description '" + subscription.getDescription() + "'");

        String subID = this.ctxBrokerService.createSubscription(subscription);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("Subscription ID", subID);

        return result;
    }

    @GetMapping("subscriptions")
    public List<Subscription> getAllSubscriptions(@RequestParam(required = false) Boolean onlyOwn) {

        if (onlyOwn == null | onlyOwn == Boolean.FALSE)
            return this.ctxBrokerService.queryForSubscriptions();
        else
            return this.ctxBrokerService.queryForOwnSubscriptions();
    }
    
}