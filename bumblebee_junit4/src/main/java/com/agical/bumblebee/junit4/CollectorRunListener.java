package com.agical.bumblebee.junit4;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import com.agical.bumblebee.Collector;

public class CollectorRunListener extends RunListener {
    private DescriptionHelper descriptionHelper = new DescriptionHelper();
    private JUnit4CallbacksToCollectorTranslator jUnit4CallbacksToCollectorTranslator;
    private Failure failure;
    
    public CollectorRunListener(Collector collector) {
        jUnit4CallbacksToCollectorTranslator = new JUnit4CallbacksToCollectorTranslator( collector);
    }

    @Override
    public void testRunStarted(Description description) throws Exception {
//        log(description);
        jUnit4CallbacksToCollectorTranslator.start();
    }

    private void log(Description description) {
//        System.out.println(description);
    }

    @Override
    public void testStarted(Description description) throws Exception {
        log(description);
        beginStructureFromDescription(description);
    }

    @Override
    public void testFinished(Description description) throws Exception {
        log(description);
        if(failure!=null) {
            endStructureFromDescription(description, failure);
            failure = null;
        } else {
            endStructureFromDescription(description);
        }
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        log(failure.getDescription());
        this.failure = failure;
    }

    @Override
    public void testIgnored(Description description) throws Exception {
        log(description);
        beginStructureFromDescription(description);
        endStructureFromDescription(description);
    }
    
    @Override
    public void testRunFinished(Result result) throws Exception {
        jUnit4CallbacksToCollectorTranslator.done();
    }
    
    private void beginStructureFromDescription(Description description) {
        String displayName = description.getDisplayName();
        Class<?> classFromDisplayName = descriptionHelper.getClassFromDisplayName(displayName);
        String methodNameFromDisplayName = descriptionHelper.getMethodFromDisplayName(displayName);
        jUnit4CallbacksToCollectorTranslator.beginClass(classFromDisplayName, descriptionHelper.getMethodFromClassAndName(classFromDisplayName, methodNameFromDisplayName));
    }
    
    private void endStructureFromDescription(Description description) {
        String displayName = description.getDisplayName();
        Class<?> classFromDisplayName = descriptionHelper.getClassFromDisplayName(displayName);
        String methodNameFromDisplayName = descriptionHelper.getMethodFromDisplayName(displayName);
        jUnit4CallbacksToCollectorTranslator.endClass(classFromDisplayName, descriptionHelper.getMethodFromClassAndName(classFromDisplayName, methodNameFromDisplayName));
    }
    private void endStructureFromDescription(Description description, Failure failure) {
        String displayName = description.getDisplayName();
        Class<?> classFromDisplayName = descriptionHelper.getClassFromDisplayName(displayName);
        String methodNameFromDisplayName = descriptionHelper.getMethodFromDisplayName(displayName);
        jUnit4CallbacksToCollectorTranslator.endClass(classFromDisplayName, descriptionHelper.getMethodFromClassAndName(classFromDisplayName, methodNameFromDisplayName), failure);
    }

}