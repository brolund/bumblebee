/**
 * 
 */
package com.agical.bumblebee.junit4.runners.structure.parameterized;

import java.lang.reflect.Method;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import com.agical.bumblebee.junit4.runners.structure.AnnotationAndMethodAssistance;

public class ParameterizedExecutor /*implements ParameterizedVisitor */{
    
    private final RunNotifier runNotifier;
    
    public ParameterizedExecutor(RunNotifier runNotifier) {
        this.runNotifier = runNotifier;
    }
    
    public void beginParameterizedTest(ParameterizedTest parameterizedTest) {
        AnnotationAndMethodAssistance.invokeAnnotatedMethodsOnClassLevel(parameterizedTest.getTestClass(), BeforeClass.class);
    }
    public void parameter(ParameterItem parameterItem) {
        Description description = parameterItem.getDescription();
        runNotifier.fireTestStarted(description);
        try {
            Class<?> testClass = parameterItem.getTestClass();
            Method method = parameterItem.getMethod();
            Object[] parameters = parameterItem.getParameters();
            Object testObject = testClass.getConstructors()[0].newInstance(parameters);
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
    
    public void endParameterizedTest(ParameterizedTest parameterizedTest) {
        AnnotationAndMethodAssistance.invokeAnnotatedMethodsOnClassLevel(parameterizedTest.getTestClass(), AfterClass.class);
    }

//    public void beginParameterizedTestMethod(ParameterizedTestMethod parameterizedTestMethod) {
//    }
//
//    public void endParameterizedTestMethod(ParameterizedTestMethod parameterizedTestMethod) {
//    }
    
}