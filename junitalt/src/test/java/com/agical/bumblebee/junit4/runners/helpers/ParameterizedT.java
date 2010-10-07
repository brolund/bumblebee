package com.agical.bumblebee.junit4.runners.helpers;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import com.agical.bumblebee.junit4.runners.TestStructureRunner;

@RunWith(TestStructureRunner.class)
public class ParameterizedT {
    private final String data;
    
    public ParameterizedT(String data) {
        this.data = data;
    }
    
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { "data1" }, { "data2" } });
    }
    
    @Test
    public void oneParameterizedTest() throws Exception {
        assertNotNull(data);
    }
    
    @Test
    public void anotherParameterizedTest() throws Exception {
        assertNotNull(data);
    }
    
}
