package com.agical.bumblebee.acceptance.helpers.experimental;

import static com.agical.bumblebee.junit4.Storage.store;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ParameterizedTests {
    /*!!
    Parameterized tests can also be used. JUnit will execute the methods once for each parameter, 
    and Bumblebee will get the corresponding calls.
    A good recommendation for large series of tests is probably to have the header to contain the parameter:
    >>>>
    #{clazz.oneParameterizedTest}
    <<<<
    As we see, you can store the actual data used in the test to display it in the report.
    */
    private final String data;

    public ParameterizedTests( String data ) {
        this.data = data;
    }
    
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {{"data1"},{"data2"}});
    }
    
    @Test
    public void oneParameterizedTest() throws Exception {
        /*!
        #{set_header(data + ' First test')}
        */
        store("data", data);
    }

    @Test
    public void anotherParameterizedTest() throws Exception {
        /*!
        #{set_header(data + ' Second test')}
        */
        store("data", data);
    }

}
