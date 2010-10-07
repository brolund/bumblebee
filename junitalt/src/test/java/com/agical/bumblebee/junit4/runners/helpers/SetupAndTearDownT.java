package com.agical.bumblebee.junit4.runners.helpers;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SetupAndTearDownT {
    public static final StringBuffer calls = new StringBuffer();
    @BeforeClass
    public static void beforeClass() {
        calls.append("beforeClass ");
    }
    @Before
    public void beforeTest() {
        calls.append("beforeTest ");
    }
    
    @Test
    public void test1() throws Exception {
        calls.append("test1 ");
    }
    
    @Test
    public void test2() throws Exception {
        calls.append("test2 ");
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
