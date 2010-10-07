package com.agical.bumblebee.ruby.invocation;

import java.lang.reflect.Method;


public class ReturningInvocationResult extends InvocationResult {
    
    private final Object returnValue;

    public ReturningInvocationResult(long executionTime, Object returnValue, Method method) {
        super(executionTime, method);
        this.returnValue = returnValue;
    }
    
    public Object getReturnValue() {
        return returnValue;
    }
}
