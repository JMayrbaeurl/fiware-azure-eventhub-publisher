package com.microsoft.samples.iot.fiware.publisher;

import java.util.Iterator;
import java.util.List;

import com.microsoft.samples.iot.fiware.ngsiv2.Subscription;

public class SubscriptionIterator implements Iterator<Subscription> {

    private final FiwareCtxBrokerService ctxBrokerService;

    private final int batchSize;
    private int offset;
    private int index;

    private List<Subscription> listOfSubscriptions;

    @Override
    public boolean hasNext() {
       
        if (this.listOfSubscriptions == null || this.listOfSubscriptions.size() == this.index) {
            this.listOfSubscriptions = 
                this.ctxBrokerService.queryForSubscriptions(this.batchSize, offset);
            this.offset += this.batchSize;
            this.index = 0;
        }

        return this.listOfSubscriptions != null && this.listOfSubscriptions.size() > this.index;
    }

    @Override
    public Subscription next() {
        
        Subscription result = this.listOfSubscriptions != null ? this.listOfSubscriptions.get(this.index) : null;
        this.index++;
        return result;
    }

    public SubscriptionIterator(FiwareCtxBrokerService ctxBrokerService, int batchSize) {
        this.ctxBrokerService = ctxBrokerService;
        this.batchSize = batchSize;
    }
}