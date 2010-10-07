package com.agical.bumblebee.junit4.runners.structure.parameterized;

import java.lang.reflect.Method;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.Parameterized.Parameters;

import com.agical.bumblebee.junit4.runners.structure.AnnotationAndMethodAssistance;
import com.agical.bumblebee.junit4.runners.structure.TestStructureItem;

public class ParameterizedHandler {

    public boolean buildStructure(TestStructureItem parent, Class<?> clazz, Description description) {
        if(classIsParameterizedTestCase(clazz)) {
            ParameterizedTest parameterizedTest = new ParameterizedTest(clazz);
            parent.addChild(parameterizedTest);
            description.addChild(parameterizedTest.getDescription());
            Collection<Object[]> data = null;
            for (Method method : clazz.getMethods()) {
                if(method.getAnnotation(Parameters.class)!=null) {
                    try {
                        data = (Collection<Object[]>) method.invoke(null);
                        break;
                    } catch (Exception e) {
                        throw new RuntimeException("Couldn't setup " + clazz.getName(), e);
                    }
                }
            }
            for (Method method : clazz.getMethods()) {
                if(method.getAnnotation(Test.class)!=null) {
                    ParameterizedTestMethod testMethod = new ParameterizedTestMethod(clazz, method);
                    parameterizedTest.addChild(testMethod);
                    parameterizedTest.getDescription().addChild(testMethod.getDescription());
                    int parameterNr = 0;
                    for (Object[] objects : data) {
                        ParameterItem parameters = new ParameterItem(objects, method, clazz);
                        testMethod.addChild(parameters);
                        testMethod.getDescription().addChild(parameters.getDescription());
                        parameterNr++;
                    }
                }
            }
            return true;
        }
        return false;
    }


    private static boolean classIsParameterizedTestCase(Class<?> clazz) {
        return AnnotationAndMethodAssistance.hasMethodWithAnnotation(clazz, Parameters.class);
    }

}
