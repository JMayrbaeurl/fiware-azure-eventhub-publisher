package com.microsoft.samples.iot.fiware.ngsiv2;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Notification {

    public enum AttributesFormat { normalized, keyValues, values };

    public static final AttributesFormat DEFAULT_ATTRIBUTES_FORMAT = AttributesFormat.keyValues;

    private Integer timesSent;
    private Date lastNotification;
    private List<String> attrs;
    private Boolean onlyChangedAttrs;
    private String attrsFormat = DEFAULT_ATTRIBUTES_FORMAT.name();
    private Map<String, String> http;
    private Date lastFailure;
    private String lastFailureReason;

    public Date getLastNotification() {
        return lastNotification;
    }

    public void setLastNotification(Date lastNotification) {
        this.lastNotification = lastNotification;
    }

    public List<String> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<String> attrs) {
        this.attrs = attrs;
    }

    public Boolean getOnlyChangedAttrs() {
        return onlyChangedAttrs;
    }

    public void setOnlyChangedAttrs(Boolean onlyChangedAttrs) {
        this.onlyChangedAttrs = onlyChangedAttrs;
    }

    public String getAttrsFormat() {
        return attrsFormat;
    }

    public void setAttrsFormat(String attrsFormat) {
        this.attrsFormat = attrsFormat;
    }

    public Map<String, String> getHttp() {
        return http;
    }

    public void setHttp(Map<String, String> http) {
        this.http = http;
    }

    public Date getLastFailure() {
        return lastFailure;
    }

    public void setLastFailure(Date lastFailure) {
        this.lastFailure = lastFailure;
    }

    public String getLastFailureReason() {
        return lastFailureReason;
    }

    public void setLastFailureReason(String lastFailureReason) {
        this.lastFailureReason = lastFailureReason;
    }

    public Notification() {
    }
    
    public Notification(Map<String, String> httpURL) {
        this.http = httpURL;
    }

    public Integer getTimesSent() {
        return timesSent;
    }

    public void setTimesSent(Integer timesSent) {
        this.timesSent = timesSent;
    }
} 