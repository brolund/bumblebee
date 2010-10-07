package com.agical.bumblebee.junit4.runners.structure.junit4;

import org.junit.runner.Description;

import com.agical.bumblebee.junit4.runners.structure.TestStructureItem;

public class JUnit4Test extends TestStructureItem {

    private final Class<?> testClass;

    public JUnit4Test(Class<?> testClass) {
        this.testClass = testClass;
        setDescription(Description.createSuiteDescription(testClass.getName()));
    }

    public void accept(JUnit4Visitor visitor) {
        visitor.beginTest(this);
        super.accept(visitor);
        visitor.endTest(this);
    }

    public Class<?> getTestClass() {
        return testClass;
    }
}
