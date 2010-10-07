package com.agical.bumblebee.junit4.runners.structure.theories;

import java.lang.reflect.Method;

import org.junit.runner.Description;

import com.agical.bumblebee.junit4.runners.structure.TestStructureItem;

public class TheoryTestMethod extends TestStructureItem {

    public TheoryTestMethod(Method method) {
        setDescription(Description.createSuiteDescription(method.getName()));
    }

    public void accept(TheoriesVisitor visitor) {
        visitor.beginTheoryTestMethod(this);
        super.accept(visitor);
        visitor.endTheoryTestMethod(this);
    }
}
