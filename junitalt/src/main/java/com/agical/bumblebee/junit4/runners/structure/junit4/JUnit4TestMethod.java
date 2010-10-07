package com.agical.bumblebee.junit4.runners.structure.junit4;

import java.lang.reflect.Method;

import org.junit.runner.Description;

import com.agical.bumblebee.junit4.runners.structure.TestStructureItem;


public class JUnit4TestMethod extends TestStructureItem {
    
    private Class<?> testClass;
    private Method method;

    public JUnit4TestMethod(Class<?> testClass, Method method) {
        this.testClass = testClass;
        this.method = method;
        setDescription(Description.createTestDescription(testClass, method.getName()));
    }

    public void accept(JUnit4Visitor visitor) {
        visitor.testMethod(this);
    }

    public Class<?> getTestClass() {
        return testClass;
    }

    public Method getMethod() {
        return method;
    }
    
}
