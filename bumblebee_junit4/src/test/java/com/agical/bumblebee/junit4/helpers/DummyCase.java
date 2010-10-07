package com.agical.bumblebee.junit4.helpers;

import org.junit.Ignore;
import org.junit.Test;




public class DummyCase {
    @Test
    public void testMethod1() throws Exception {
    }
    
    @Test
    public void testMethod2() throws Exception {
    }
    @Test
    public void testFailure() throws Exception {
    }
    
    @Test
    @Ignore
    public void ignoredMethod() throws Exception {
    
    }
}
