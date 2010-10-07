/**
 * 
 */
package com.agical.bumblebee.junit4.runners.structure.theories;

import java.lang.reflect.Method;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import com.agical.bumblebee.junit4.runners.structure.AnnotationAndMethodAssistance;

public class TheoriesExecutor implements TheoriesVisitor {
    
    private final RunNotifier runNotifier;
    
    public TheoriesExecutor(RunNotifier runNotifier) {
        this.runNotifier = runNotifier;
    }
    
    public void beginTheoriesTest(TheoryTest theoryTest) {
        AnnotationAndMethodAssistance.invokeAnnotatedMethodsOnClassLevel(theoryTest.getTestClass(), BeforeClass.class);
    }
    public void dataPoint(TheoryDataPoint theoryDataPoint) {
        Description description = theoryDataPoint.getDescription();
        runNotifier.fireTestStarted(description);
        try {
            Class<?> testClass = theoryDataPoint.getClazz();
            Method method = theoryDataPoint.getMethod();
            Object[] objects = theoryDataPoint.getMatchingDataPoint();
            Object testObject = testClass.newInstance();
            AnnotationAndMethodAssistance.invokeAnnotatedMethodsOnInstanceLevel(testObject, Before.class);
            method.invoke(testObject, objects);
            AnnotationAndMethodAssistance.invokeAnnotatedMethodsOnInstanceLevel(testObject, After.class);
            runNotifier.fireTestFinished(description);
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            if (cause.getClass().getName().contains("AssumptionViolatedException")) {
                runNotifier.fireTestIgnored(description);
            } else {
                runNotifier.fireTestFailure(new Failure(description, cause));
            }
        }
    }
    
    public void endTheoriesTest(TheoryTest theoryTest) {
        AnnotationAndMethodAssistance.invokeAnnotatedMethodsOnClassLevel(theoryTest.getTestClass(), AfterClass.class);
    }

    public void beginTheoryTestMethod(TheoryTestMethod theoryTestMethod) {
    }

    public void endTheoryTestMethod(TheoryTestMethod theoryTestMethod) {
    }
    
}