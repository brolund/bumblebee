package com.agical.bumblebee.agiledox;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class TestDeCamelCasingFormatter {
    private Formatter formatter = new DeCamelCasingFormatter();
    @Test
    public void formatClass() throws Exception {
        assertEquals("Test de camel casing formatter", formatter.format(getClass()));
    }
    @Test
    public void formatMethod() throws Exception {
        assertEquals("Format method", formatter.format(getClass().getMethod("formatMethod", new Class[0])));
    }
}
