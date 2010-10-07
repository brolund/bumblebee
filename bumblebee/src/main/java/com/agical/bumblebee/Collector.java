package com.agical.bumblebee;

import java.io.Serializable;
import java.lang.reflect.Method;

public interface Collector {
    void setCallback(CollectorStatus collectorStatus);
    void start();
    void beginClass(Class<? extends Object> executingClass);
    void beginMethod(Method method);
    void beginMethod(Method method, Object target, Object[] arguments);
    void endMethod(Method method);
    void endMethodWithValue(Method method, Object value);
    void endMethodWithException(Method method, Throwable exception);
    void store(String key, Serializable objectToStore);
    void endClass(Class<? extends Object> executingClass);
    public void done();
}