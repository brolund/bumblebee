package com.agical.bumblebee.junit4.runners.structure.suite;

import org.junit.runner.Description;

import com.agical.bumblebee.junit4.runners.structure.TestStructureItem;


public class SuiteStructureItem extends TestStructureItem {

    public SuiteStructureItem(Class<?> clazz) {
        setDescription(Description.createSuiteDescription(clazz));
    }

    public void accept(SuiteVisitor visitor) {
        visitor.beginSuite(this);
        super.accept(visitor);
        visitor.endSuite(this);
    }

    @Override 
    public String toString() {
        return getDescription().getDisplayName();
    }
}
