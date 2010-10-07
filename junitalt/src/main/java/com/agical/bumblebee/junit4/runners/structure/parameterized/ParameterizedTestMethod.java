package com.agical.bumblebee.junit4.runners.structure.parameterized;

import java.lang.reflect.Method;

import org.junit.runner.Description;

import com.agical.bumblebee.junit4.runners.structure.TestStructureItem;



public class ParameterizedTestMethod extends TestStructureItem {

    public ParameterizedTestMethod(Class<?> clazz, Method method) {
        setDescription(Description.createSuiteDescription( method.getName() + "(" + clazz.getName() + ")"));
    }

    public void accept(ParameterizedVisitor visitor) {
        visitor.beginParameterizedTestMethod(this);
        super.accept(visitor);
        visitor.endParameterizedTestMethod(this);
    }
    
}
