package com.agical.bumblebee.junit4;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import com.agical.bumblebee.junit4.helpers.DummyCase;
import com.agical.bumblebee.junit4.helpers.MockCollector;
import com.agical.bumblebee.parser.NewLine;

public class TestCollectorRunListener {
    private static final Class<DummyCase> CLASS = DummyCase.class;
    private StringBuffer buf;
    private Failure failure;
    private String message = "failure message";
    
    @Before
    public void setup() throws Exception {
        buf = new StringBuffer();
        Description createTestDescription1 = Description.createTestDescription(CLASS, "testMethod1");
        Description createTestDescription2 = Description.createTestDescription(CLASS, "testMethod2");
        Description createTestDescriptionIgnored = Description.createTestDescription(CLASS, "ignoredMethod");
        Description failureDescription = Description.createTestDescription(CLASS, "testFailure");
        failure = new Failure(failureDescription, new RuntimeException(message ));
        CollectorRunListener runListener = new CollectorRunListener(new MockCollector(buf) );
        runListener.testRunStarted(null);
        runListener.testStarted(createTestDescription1);
        runListener.testFinished(createTestDescription1);
        runListener.testIgnored(createTestDescriptionIgnored);
        runListener.testStarted(failureDescription);
        runListener.testFailure(failure);
        runListener.testFinished(failureDescription);
        runListener.testStarted(createTestDescription2);
        runListener.testFinished(createTestDescription2);
        runListener.testRunFinished(new Result());
    }
    
    @Test
    public void verifyResult() throws Exception {
        String expected = "start" + NewLine.STR + 
        		"begin|" +CLASS.getSimpleName() + NewLine.STR + 
        		"beginmethod|testMethod1" + NewLine.STR + 
                "endmethod|testMethod1" + NewLine.STR + 
                "beginmethod|ignoredMethod" + NewLine.STR + 
                "endmethod|ignoredMethod" + NewLine.STR + 
                "beginmethod|testFailure" + NewLine.STR + 
                "endmethodwithfailure|testFailure" + NewLine.STR + 
                "beginmethod|testMethod2" + NewLine.STR + 
                "endmethod|testMethod2" + NewLine.STR + 
        		"end|" +CLASS.getSimpleName() + NewLine.STR + 
        		"done" + NewLine.STR;
                assertEquals(expected, buf.toString());
    }
}
