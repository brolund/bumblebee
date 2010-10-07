package com.agical.bumblebee.junit4.runners.structure.theories;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.theories.Theory;
import org.junit.runner.Description;

import com.agical.bumblebee.junit4.runners.structure.AnnotationAndMethodAssistance;
import com.agical.bumblebee.junit4.runners.structure.TestStructureItem;
import com.agical.bumblebee.junit4.runners.structure.junit4.JUnit4TestMethod;

public class TheoryHandler {
    
    public boolean buildStructure(TestStructureItem testStructureItem, Class<?> clazz, Description description) {
        if (TheoryHandler.classIsTheory(clazz)) {
            TheoryTest theoryTest = new TheoryTest(clazz);
            testStructureItem.addChild(theoryTest);
            description.addChild(theoryTest.getDescription());
            for (Method method : clazz.getMethods()) {
                if (method.getAnnotation(Test.class) != null) {
                    JUnit4TestMethod testMethod = new JUnit4TestMethod(clazz, method);
                    theoryTest.addChild(testMethod);
                    theoryTest.getDescription().addChild(testMethod.getDescription());
                }
                if (method.getAnnotation(Theory.class) != null) {
                    TheoryTestMethod testMethod = new TheoryTestMethod(method);
                    theoryTest.addChild(testMethod);
                    theoryTest.getDescription().addChild(testMethod.getDescription());
                    List<Object[]> matchingDataPoints = TheoryHandler.getMatchingDataPoints(clazz, method);
                    for (Object[] matchingDataPoint : matchingDataPoints) {
                        TheoryDataPoint theoryDataPoint = new TheoryDataPoint(clazz, method, matchingDataPoint);
                        testMethod.addChild(theoryDataPoint);
                        testMethod.getDescription().addChild(theoryDataPoint.getDescription());
                        
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    private static List<Object[]> getMatchingDataPoints(Class<?> clazz, Method method) {
        List<Object> dataPoints = new ArrayList<Object>();
        new FieldDataPointFactory().addDataPoints(clazz, dataPoints);
        new MethodDataPointFactory().addDataPoints(clazz, dataPoints);
        Class<?>[] parameterTypes = method.getParameterTypes();
        List<List<Object>> matches = new ArrayList<List<Object>>(parameterTypes.length);
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterClass = parameterTypes[i];
            matches.add(new ArrayList<Object>());
            for (Object dataPoint : dataPoints) {
                if (parameterClass.isAssignableFrom(dataPoint.getClass())
                        || AnnotationAndMethodAssistance.parameterIsAssignableToTarget(parameterClass, dataPoint.getClass())) {
                    matches.get(i).add(dataPoint);
                }
            }
        }
        int nrOfCombinations = 1;
        for (List<Object> list : matches) {
            nrOfCombinations *= list.size();
        }
        int[] divs = new int[parameterTypes.length];
        for (int i = 0; i < divs.length; i++) {
            divs[i] = 1;
            for (int j = i + 1; j < divs.length; j++) {
                divs[i] *= matches.get(j).size();
            }
        }
        List<Object[]> values = new ArrayList<Object[]>(nrOfCombinations);
        for (int i = 0; i < nrOfCombinations; i++) {
            Object[] objects = new Object[parameterTypes.length];
            for (int j = 0; j < parameterTypes.length; j++) {
                List<Object> list = matches.get(j);
                objects[j] = list.get((i / divs[j]) % list.size());
            }
            values.add(objects);
        }
        return values;
    }
    
    private static boolean classIsTheory(Class<?> clazz) {
        return AnnotationAndMethodAssistance.hasMethodWithAnnotation(clazz, Theory.class);
    }
    
}
