package com.agical.bumblebee.junit4.runners.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SetupAndTearDownParameterized {
    public static final StringBuffer calls = new StringBuffer();
    private final String data;
    
    @Parameters
    public static Collection<Object[]> data() {
        List<Object[]> list = new ArrayList<Object[]>();
        list.add(new Object[] { "data" });
        return list;
    }
    
    public SetupAndTearDownParameterized(String data) {
        this.data = data;
    }
    
    @BeforeClass
    public static void beforeClass() {
        calls.append("beforeClass ");
    }
    @Before
    public void beforeTest() {
        calls.append("beforeTest ");
    }
    
    @Test
    public void test() throws Exception {
        calls.append("test ");
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
