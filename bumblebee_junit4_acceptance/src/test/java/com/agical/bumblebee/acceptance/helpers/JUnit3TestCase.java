package com.agical.bumblebee.acceptance.helpers;

import junit.framework.TestCase;

public class JUnit3TestCase extends TestCase {
    /*!!
    #{set_header 'Using JUnit3'}
    JUnit3.x testcases can be used standalone or together with JUnit4 testcases.
    */
    public void testIfJUnit3AlsoWorks() throws Exception {
        /*!m1
        #{set_header 'So far, no limitations in that use'}
        Thanks to JUnit4's handling of JUnit3.x test cases they work seamlessly
        with the Suites, hence allowing you 
        to recycle you old test cases.
        >>>>
        #{clazz}
        <<<<
        #{assert.contains 'So far,'+' no limitations in that use', 'JUnit3 is included'}
        */
    }
    
}
