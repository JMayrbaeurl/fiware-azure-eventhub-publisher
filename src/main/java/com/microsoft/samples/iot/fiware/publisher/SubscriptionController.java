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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class SubscriptionController {

    private final Log logger = LogFactory.getLog(SubscriptionController.class);

    @Autowired
    private FiwareCtxBrokerService ctxBrokerService;

    @PostMapping("subscription/{entityType}")
    public Map<String, Object> publishNotification(@NonNull @PathVariable String entityType) {

        logger.info("Adding subscription for all entites of type ' " + entityType + "'");

        String subID = this.ctxBrokerService.subscribeToEntityWithType(entityType);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("Subscription ID", subID);

        return result;
    }

    @GetMapping("subscriptions")
    public List<Subscription> getAllSubscriptions() {

        return this.ctxBrokerService.queryForSubscriptions();
    }
    
}