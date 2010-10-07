package com.agical.bumblebee.acceptance.helpers.experimental;

import static com.agical.bumblebee.junit4.Storage.store;
import static org.junit.Assume.assumeThat;

import java.util.ArrayList;

import org.hamcrest.core.IsEqual;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class UsingTheories {
    @DataPoint public static final String aString = "string 1";
    @DataPoint public static final String anotherString = "string 2";
    
    private static ArrayList<String> data = new ArrayList<String>();
    private static ArrayList<String> multiData = new ArrayList<String>();
    
    @Theory
    public void aTheory(String theStringToTest) throws Exception {
        /*!
        It is also possible to use JUnit theories, an experimental feature in JUnit 4.4. 
        In difference from using the Parameterized runner, each test will only be reported 
        by JUnit once, so you need to store the data from each run in the test:
        >>>>
        #{clazz}
        <<<<
        This is a simple single argument theory. These are the data points submitted.
        #{theory_data.collect {|datapoint| datapoint + ' | '}}
        */
         data.add(theStringToTest);
         store("theory_data", data);
    }
    
    @Theory
    public void anAssumingMultiArgumentTheory(String first, String second) throws Exception {
        assumeThat(first, new IsEqual<String>(aString));
        /*!
        You can use =assumeThat(...)= to filter which tests to run to the end, 
        and hence what data is stored:
        >>>>
        #{meth}
        <<<<
        In this case we use *assumeThat* to let the test continue for data sets 
        where the first argument is =\"string 1\"=:
        #{theory_data.collect {|datapoint| datapoint + ' | '}}
        */
        multiData.add(first + "-" + second);
        store("theory_data", multiData);
    }
    
}