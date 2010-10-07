package com.agical.bumblebee.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestRowColExtractor {
    
    private String text = "abcdefg" + NewLine.STR + "1234567" + NewLine.STR + ",.-<*";
    
    @Test
    public void extractByRowAndColumn() throws Exception {
        assertEquals("a", RowColExtractor.extract(text, 1, 1, 1, 2));
    }
    
    @Test
    public void extractMultipleRows() throws Exception {
        assertEquals("g" + NewLine.STR + "1", RowColExtractor.extract(text, 1, 7, 2, 2));
    }
    
    @Test
    public void extractEmptyReturnsEmpty() throws Exception {
        assertEquals("", RowColExtractor.extract("", 1, 1, 1, 1));
    }
    
}
