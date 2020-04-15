package com.microsoft.samples.iot.fiware.ngsiv2;

public class ContextBrokerException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ContextBrokerException() {
    }

    public ContextBrokerException(String message) {
        super(message);
    }

    public ContextBrokerException(Throwable cause) {
        super(cause);
    }

    public ContextBrokerException(String message, Throwable cause) {
        super(message, cause);
    }

    
}