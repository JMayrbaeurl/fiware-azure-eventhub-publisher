package com.microsoft.samples.iot.fiware.ngsiv2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * SubscriptionSubject
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionSubject {

    private static final String TYPE_PATTERN = "typePattern";

    public static final String PLACEHOLDER_VALUE = ".*";

    private List<Map<String,Object>> entities;

    private Map<String, List<String>> condition;

    /**
     * 
     * @return true if this subject is for any type
     */
    public boolean isForAnyType() {

        boolean result = false;

        if (this.entities != null && this.entities.size() == 1) {
            result = this.entities.get(0).containsKey(TYPE_PATTERN) 
                && PLACEHOLDER_VALUE.equals(this.entities.get(0).get(TYPE_PATTERN));
        }

        return result;
    }

    public List<Map<String, Object>> getEntities() {
        return entities;
    }

    public void setEntities(List<Map<String, Object>> entities) {
        this.entities = entities;
    }

    public Map<String, List<String>> getCondition() {
        return condition;
    }

    public void setCondition(Map<String, List<String>> condition) {
        this.condition = condition;
    }

    public SubscriptionSubject() {
        this.entities = new ArrayList<Map<String,Object>>();
    }

    public static SubscriptionSubject create(final String entityType, final String attributeName ) {

        SubscriptionSubject subject = new SubscriptionSubject();

        Map<String, Object> anEntry = new HashMap<String, Object>();
        anEntry.put("idPattern", PLACEHOLDER_VALUE); 
        if (entityType != null)
            anEntry.put("type", entityType);
        else
            anEntry.put(TYPE_PATTERN, PLACEHOLDER_VALUE);
        subject.entities.add(anEntry);

        if (attributeName != null) {
            subject.condition = new HashMap<String, List<String>>();
            List<String> attrNames = new ArrayList<String>();
            attrNames.add(attributeName);
            subject.condition.put("attrs", attrNames);
        }

        return subject;
    }
}