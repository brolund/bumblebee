package com.agical.bumblebee.ruby.invocation;

import java.lang.reflect.Method;

public class MethodCall {

    private final Object target;
    private final Method method;
    private final Object[] arguments;

    public MethodCall(Object target, Method method, Object[] arguments) {
        this.target = target;
        this.method = method;
        this.arguments = arguments;
    }

    public Object getTarget() {
        return target;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArguments() {
        return arguments;
    }
    
    
}
