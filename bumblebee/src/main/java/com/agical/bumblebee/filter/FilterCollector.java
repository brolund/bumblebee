package com.agical.bumblebee.filter;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.agical.bumblebee.Collector;
import com.agical.bumblebee.CollectorStatus;

public class FilterCollector implements Collector {
    
    private Collector delegate;
    private Collector currentCollector;
    private Collector doNothingCollector = new DoNothingCollector();
    private int stackDepth = 0;
    private int lowestExcludeLevel = -1;
    private final Criteria criteria;

    public FilterCollector(Collector delegate, Criteria criteria) {
        this.delegate = delegate;
        this.currentCollector = delegate;
        this.criteria = criteria;
    }
    
    public void beginClass(Class<? extends Object> executingClass) {
        increaseStackDepth();
        currentCollector.beginClass(executingClass);
    }
    
    public void beginMethod(Method method) {
        increaseStackDepth();
        turnFilteringOnIfExcluded(method);
        currentCollector.beginMethod(method);
    }
    
    public void beginMethod(Method method, Object target, Object[] arguments) {
        increaseStackDepth();
        turnFilteringOnIfExcluded(method);
        currentCollector.beginMethod(method, target, arguments);
    }

    public void done() {
        currentCollector.done();
        decreaseStackDepth();
    }
    
    public void endClass(Class<? extends Object> executingClass) {
        currentCollector.endClass(executingClass);
        decreaseStackDepth();
    }
    public void endMethod(Method method) {
        currentCollector.endMethod(method);
        turnFilteringOnIfEndOfExclusion(method);
        decreaseStackDepth();
    }

    public void endMethodWithException(Method method, Throwable exception) {
        currentCollector.endMethodWithException(method, exception);
        turnFilteringOnIfEndOfExclusion(method);
        decreaseStackDepth();
    }
    
    public void endMethodWithValue(Method method, Object value) {
        currentCollector.endMethodWithValue(method, value);
        turnFilteringOnIfEndOfExclusion(method);
        decreaseStackDepth();
    }

    public void setCallback(CollectorStatus collectorStatus) {
        currentCollector.setCallback(collectorStatus);
    }
    
    public void start() {
        increaseStackDepth();
        currentCollector.start();
    }
    
    public void store(String key, Serializable objectToStore) {
        currentCollector.store(key, objectToStore);
    }

    private int increaseStackDepth() {
        return stackDepth++;
    }
    private int decreaseStackDepth() {
        return stackDepth--;
    }
    
    private void turnFilteringOnIfExcluded(Method method) {
        if(currentCollector==delegate && !criteria.accept(method)) {
            lowestExcludeLevel = stackDepth;
            currentCollector = doNothingCollector;
        }
    }
    
    private void turnFilteringOnIfEndOfExclusion(Method method) {
        if(currentCollector==doNothingCollector && lowestExcludeLevel==stackDepth) {
            lowestExcludeLevel = -1;
            currentCollector = delegate;
        }
    }
    
    
}

