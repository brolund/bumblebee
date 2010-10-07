package com.agical.bumblebee.collector;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.agical.bumblebee.AbstractCollector;
import com.agical.bumblebee.Collector;
import com.agical.bumblebee.CollectorStatus;

public class CompositeCollector  extends AbstractCollector {
    private final Collector[] collectors;
    public CompositeCollector(Collector[] collectors) {
        this.collectors = collectors;
    }
    public void setCallback(CollectorStatus collectorStatus) {
        for (Collector collector : collectors) {
            collector.setCallback(collectorStatus);
        }
    }
    public void start() {
        for (Collector collector : collectors) {
            collector.start();
        }
    }
    public void beginClass(Class<? extends Object> executingClass) {
        for (Collector collector : collectors) {
            collector.beginClass(executingClass);
        }
    }
    public void beginMethod(Method method) {
        for (Collector collector : collectors) {
            collector.beginMethod(method);
        }
    }
    public void endMethod(Method method) {
        for (Collector collector : collectors) {
            collector.endMethod(method);
        }
    }
    public void endMethodWithException(Method method, Throwable throwable) {
        for (Collector collector : collectors) {
            collector.endMethodWithException(method, throwable);
        }
    }

    public void store(String key, Serializable objectToStore) {
        for (Collector collector : collectors) {
            collector.store(key, objectToStore);
        }
    }
    public void endClass(Class<? extends Object> executingClass) {
        for (Collector collector : collectors) {
            collector.endClass(executingClass);
        }
    }
    public void done() {
        for (Collector collector : collectors) {
            collector.done();
        }
    }
}
