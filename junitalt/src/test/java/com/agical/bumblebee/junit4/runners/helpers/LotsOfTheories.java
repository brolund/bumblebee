package com.agical.bumblebee.junit4.runners.helpers;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeThat;

import java.util.List;

import org.hamcrest.core.IsEqual;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.agical.bumblebee.junit4.runners.TestStructureRunner;
import com.agical.bumblebee.junit4.runners.structure.theories.DataPoints;

@RunWith(TestStructureRunner.class)
public class LotsOfTheories {
    @DataPoint
    public static final String aString = "string 1";
    @DataPoint
    public static String getAnotherString() {
        return "string 2";
    }
    @DataPoints
    public static int[] getIntegers() {
        return new int[] { 666, 667 };
    }
    @DataPoints
    public static final long[] longs = new long[] { 668, 669 };
    
    @DataPoints
    public static final Object[] objects = new Object[] { "jasdlakjs", new Object(), 2, 'h' };
    
    @Theory
    public void aTheory(String theStringToTest) throws Exception {}
    
    @Theory
    public void aTheory(List<?> theStringToTest) throws Exception {}
    
    @Theory
    public void anAssumingMultiArgumentTheory(String first, String second) throws Exception {
        assumeThat(first, new IsEqual<String>(aString));
        assertThat(first, new IsEqual<String>(aString));
    }
    
    @Theory
    public void dataPointsTest(int anInt) throws Exception {}
    
    @Theory
    public void dataPointsTest(int anInt, String s) throws Exception {}
    
    @Theory
    public void dataPointsTestForField(long aLong) throws Exception {
        assertTrue(aLong == 668 || aLong == 669);
    }
    
}