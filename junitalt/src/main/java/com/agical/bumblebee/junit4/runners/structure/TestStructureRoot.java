package com.agical.bumblebee.junit4.runners.structure;

import org.junit.runner.Description;

import com.agical.bumblebee.junit4.runners.structure.junit4.JUnit4TestHandler;
import com.agical.bumblebee.junit4.runners.structure.parameterized.ParameterizedHandler;
import com.agical.bumblebee.junit4.runners.structure.suite.SuiteHandler;
import com.agical.bumblebee.junit4.runners.structure.theories.TheoryHandler;

public class TestStructureRoot extends TestStructureItem {
    
    public TestStructureRoot() {
        setDescription(Description.createSuiteDescription("root"));
    }
    
    public void resolve(Class<?> rootClass) {
        resolve(this, rootClass);
    }
    
    public static void resolve(TestStructureItem testStructureItem, Class<?> clazz) {
        Description description = testStructureItem.getDescription();
        if (new ParameterizedHandler().buildStructure(testStructureItem, clazz, description)) return;
        if (new TheoryHandler().buildStructure(testStructureItem, clazz, description)) return;
        if (new JUnit4TestHandler().buildStructure(testStructureItem, clazz, description)) return;
        if (new SuiteHandler().buildStructure(testStructureItem, clazz, description)) return;
        throw new RuntimeException("Couldn't find any hint on how to handle " + clazz.getName());
    }
    
    public int testCount() {
        return 1; // Arguable in face of parameterization and theories
    }
}
