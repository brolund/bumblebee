/**
 * 
 */
package com.agical.bumblebee.junit4.runners.structure;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;

public abstract class TestStructureItem {
    
    private final List<TestStructureItem> children = new ArrayList<TestStructureItem>();
    private Description description;
    
    public void accept(Object visitor) {
        for (TestStructureItem testStructureItem : getChildren()) {
            invoke(testStructureItem, "accept", new Class[] { visitor.getClass() }, new Object[] { visitor });
        }
    }
    public void invoke(TestStructureItem testStructureItem, String methodName, Class<?>[] parameterTypes,
            Object[] parameters) {
        try {
            Method targetMethod = AnnotationAndMethodAssistance.getDuckTypeMethod(methodName, testStructureItem, parameters);
            targetMethod.invoke(testStructureItem, parameters);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't find '" + methodName + "' method on '" + testStructureItem.getClass().getName() + "'", e);
        }
    }
    protected void setDescription(Description description) {
        this.description = description;
    }
    public Description getDescription() {
        return description;
    }
    public void addChild(TestStructureItem testStructureItem) {
        getChildren().add(testStructureItem);
    }
    public List<TestStructureItem> getChildren() {
        return children;
    }
}