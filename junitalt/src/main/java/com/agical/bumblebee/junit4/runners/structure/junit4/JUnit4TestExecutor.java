/**
 * 
 */
package com.agical.bumblebee.junit4.runners.structure.junit4;

import java.lang.reflect.Method;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import com.agical.bumblebee.junit4.runners.structure.AnnotationAndMethodAssistance;

public class JUnit4TestExecutor implements JUnit4Visitor {
    
    private final RunNotifier runNotifier;
    
    public JUnit4TestExecutor(RunNotifier runNotifier) {
        this.runNotifier = runNotifier;
    }
    
    public void beginTest(JUnit4Test test) {
        AnnotationAndMethodAssistance.invokeAnnotatedMethodsOnClassLevel(test.getTestClass(), BeforeClass.class);
    }
    public void testMethod(JUnit4TestMethod testMethod) {
        Description description = testMethod.getDescription();
        runNotifier.fireTestStarted(description);
        try {
            Class<?> testClass = testMethod.getTestClass();
            Method method = testMethod.getMethod();
            Object testObject = testClass.newInstance();
            AnnotationAndMethodAssistance.invokeAnnotatedMethodsOnInstanceLevel(testObject, Before.class);
            method.invoke(testObject);
            AnnotationAndMethodAssistance.invokeAnnotatedMethodsOnInstanceLevel(testObject, After.class);
            runNotifier.fireTestFinished(description);
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            runNotifier.fireTestFailure(new Failure(description, cause));
        }
    }
    public void endTest(JUnit4Test test) {
        AnnotationAndMethodAssistance.invokeAnnotatedMethodsOnClassLevel(test.getTestClass(), AfterClass.class);
    }
}