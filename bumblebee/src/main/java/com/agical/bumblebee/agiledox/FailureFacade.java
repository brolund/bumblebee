package com.agical.bumblebee.agiledox;

import java.io.Serializable;

public class FailureFacade implements Serializable {
    private static final long serialVersionUID = 435649145508205189L;
    private final Throwable throwable;
    private final String message;
    private final String trace;

    public FailureFacade(Throwable throwable, String message, String trace) {
        this.throwable = throwable;
        this.message = message;
        this.trace = trace;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public String getMessage() {
        return message;
    }

    public String getTrace() {
        return trace;
    }

    public String toString() {
        return "Failure: " + message;
    }
    
}
