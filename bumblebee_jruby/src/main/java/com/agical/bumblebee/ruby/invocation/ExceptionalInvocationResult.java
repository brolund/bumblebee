package com.agical.bumblebee.ruby.invocation;

import java.lang.reflect.Method;

public class ExceptionalInvocationResult extends InvocationResult {

    private final Throwable throwable;

    public ExceptionalInvocationResult(long executionTime, Throwable throwable, Method method) {
        super(executionTime, method);
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
