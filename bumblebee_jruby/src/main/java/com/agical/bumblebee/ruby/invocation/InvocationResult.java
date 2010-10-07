package com.agical.bumblebee.ruby.invocation;

import java.lang.reflect.Method;

public class InvocationResult extends Execution {
    private final Method method;

    public InvocationResult(long executionTime, Method method) {
        super(executionTime);
        this.method = method;
    }
    
    public Method getMethod() {
        return method;
    }
}
