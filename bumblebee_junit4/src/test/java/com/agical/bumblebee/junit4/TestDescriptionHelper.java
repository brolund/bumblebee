package com.agical.bumblebee.junit4;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import org.junit.Test;


public class TestDescriptionHelper {

    @Test
    public void testThatParameterizedTestNamesAreParsedCorrectly() throws Exception {
        DescriptionHelper descriptionHelper = new DescriptionHelper();
        Method thisMethod = getClass().getMethod("testThatParameterizedTestNamesAreParsedCorrectly", (Class<?>[])null);
        assertEquals(thisMethod, descriptionHelper.getMethodFromClassAndName(getClass(), "testThatParameterizedTestNamesAreParsedCorrectly[0]"));
        assertEquals(thisMethod, descriptionHelper.getMethodFromClassAndName(getClass(), "testThatParameterizedTestNamesAreParsedCorrectly"));
    }
}
