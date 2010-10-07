package com.agical.bumblebee.junit4.runners.structure.parameterized;

import org.junit.runner.Description;

import com.agical.bumblebee.junit4.runners.structure.TestStructureItem;

public class ParameterizedTest extends TestStructureItem {

    private  final Class<?> clazz;

    public ParameterizedTest(Class<?> clazz) {
        this.clazz =  clazz;
        setDescription(Description.createSuiteDescription(clazz.getName()));
    }

    public void accept(ParameterizedVisitor visitor) {
        visitor.beginParameterizedTest(this);
        super.accept(visitor);
        visitor.endParameterizedTest(this);
    }

    public Class<?> getTestClass() {
        return clazz;
    }

    
}
