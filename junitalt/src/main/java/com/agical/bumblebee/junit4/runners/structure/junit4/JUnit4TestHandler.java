package com.agical.bumblebee.junit4.runners.structure.junit4;

import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.Description;

import com.agical.bumblebee.junit4.runners.structure.AnnotationAndMethodAssistance;
import com.agical.bumblebee.junit4.runners.structure.TestStructureItem;

public class JUnit4TestHandler {

    public boolean buildStructure(TestStructureItem testStructureItem, Class<?> clazz, Description description) {
        if(JUnit4TestHandler.classIsTestCase(clazz)) {
            JUnit4Test unit4Test = new JUnit4Test(clazz);
            testStructureItem.addChild(unit4Test);
            description.addChild(unit4Test.getDescription());
            for (Method method : clazz.getMethods()) {
                if(method.getAnnotation(Test.class)!=null) {
                    JUnit4TestMethod testMethod = new JUnit4TestMethod(clazz, method);
                    unit4Test.addChild(testMethod);
                    unit4Test.getDescription().addChild(testMethod.getDescription());
                }
            }
            return true;
        }
        return false;
    }

    private static boolean classIsTestCase(Class<?> clazz) {
        return AnnotationAndMethodAssistance.hasMethodWithAnnotation(clazz, Test.class);
    }


}
