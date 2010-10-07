package com.agical.bumblebee.junit4.runners.helpers;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class SetupAndTearDownTheories {
    public static final StringBuffer calls = new StringBuffer();
    @BeforeClass
    public static void beforeClass() {
        calls.append("beforeClass ");
    }
    @Before
    public void beforeTest() {
        calls.append("beforeTest ");
    }
    
    @DataPoint
    public static final String data = "a string";
    @Theory
    public void test1(String data) throws Exception {
        calls.append("theory ");
    }
    
    @After
    public void afterTest() {
        calls.append("afterTest ");
    }
    @AfterClass
    public static void afterClass() {
        calls.append("afterClass ");
    }
    
}
