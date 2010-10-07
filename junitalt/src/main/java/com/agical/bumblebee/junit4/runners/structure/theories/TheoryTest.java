package com.agical.bumblebee.junit4.runners.structure.theories;

import org.junit.runner.Description;

import com.agical.bumblebee.junit4.runners.structure.TestStructureItem;

public class TheoryTest extends TestStructureItem {

    private final Class<?> testClass;

    public TheoryTest(Class<?> testClass) {
        this.testClass = testClass;
        setDescription(Description.createSuiteDescription(testClass.getName()));
    }
  
    public void accept(TheoriesVisitor visitor) {
        visitor.beginTheoriesTest(this);
        super.accept(visitor);
        visitor.endTheoriesTest(this);
    }

    public Class<?> getTestClass() {
        return testClass;
    }
}
