package com.agical.bumblebee;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Extend this class when implementing a Collector
 */
public class AbstractCollector implements Collector {
    public void setCallback(CollectorStatus collectorStatus) {}
    public void start() {}
    public void beginClass(Class<? extends Object> executingClass) {}
    public void beginMethod(Method method) {}
    public void beginMethod(Method method, Object target, Object[] arguments) {
        beginMethod(method);
    }
    public void store(String key, Serializable objectToStore) {}
    public void endMethod(Method method) {}
    public void endMethodWithException(Method method, Throwable exception) {
        endMethod(method);
    }
    public void endMethodWithValue(Method method, Object value) {
        endMethod(method);
    }
    public void endClass(Class<? extends Object> executingClass) {}
    public void done() {}
}
