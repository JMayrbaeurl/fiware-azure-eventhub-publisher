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

    private List<Map<String,Object>> entities;

    private Map<String, List<String>> condition;

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

    public static SubscriptionSubject create(final String entityName, final String attributeName ) {

        SubscriptionSubject subject = new SubscriptionSubject();

        Map<String, Object> anEntry = new HashMap<String, Object>();
        anEntry.put("idPattern", ".*"); 
        if (entityName != null)
            anEntry.put("type", entityName);
        else
        anEntry.put("typePattern", ".*");
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