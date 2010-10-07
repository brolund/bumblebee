package com.agical.bumblebee.filter;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.agical.bumblebee.Collector;
import com.agical.bumblebee.CollectorStatus;

public class DoNothingCollector implements Collector {
    public void beginClass(Class<? extends Object> executingClass) {
    }
    public void beginMethod(Method method) {
    }
    public void beginMethod(Method method, Object target, Object[] arguments) {
    }
    public void done() {
    }
    public void endClass(Class<? extends Object> executingClass) {
    }
    public void endMethod(Method method) {
    }
    public void endMethodWithException(Method method, Throwable exception) {
    }
    public void endMethodWithValue(Method method, Object value) {
    }
    public void setCallback(CollectorStatus collectorStatus) {
    }
    public void start() {
    }
    public void store(String key, Serializable objectToStore) {
    }
}
