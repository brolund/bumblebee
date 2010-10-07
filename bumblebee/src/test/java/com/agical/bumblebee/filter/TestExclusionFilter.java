package com.agical.bumblebee.filter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.agical.bumblebee.Collector;
import com.agical.bumblebee.StringBufferCollector;
import com.agical.bumblebee.StringBufferCollectorStatus;
import com.agical.bumblebee.collector.CompositeCollector;
import com.agical.bumblebee.collector.Exclude;
import com.agical.bumblebee.parser.NewLine;


public class TestExclusionFilter {
    private static final String NL = NewLine.STR;
    private StringBuffer buffer = new StringBuffer();

    @Test
    public void collectorForwardsCalls() throws Exception {
        Collector composite = new CompositeCollector(new Collector[] { new StringBufferCollector("", buffer)});
        FilterCollector filteredCollector = new FilterCollector(composite, new ExcludeAnnotationCriteria(Exclude.class));
        filteredCollector.setCallback(new StringBufferCollectorStatus(buffer));
        filteredCollector.start();
        filteredCollector.beginClass(getClass());
        filteredCollector.beginMethod(getClass().getMethod("collectorForwardsCalls", (Class<?>[])null));
        filteredCollector.endMethod(getClass().getMethod("collectorForwardsCalls", (Class<?>[])null));
        filteredCollector.beginMethod(getClass().getMethod("excludedMethod", (Class<?>[])null));
        filteredCollector.endMethod(getClass().getMethod("excludedMethod", (Class<?>[])null));
        filteredCollector.endClass(getClass());
        filteredCollector.store("aKey", "aSerializable");
        filteredCollector.done();
        assertEquals(
                "description:setCallback" + NL + 
                "start" + NL + 
                "beginClass" + getClass().getSimpleName() + NL + 
                "beginmethod" + ":collectorForwardsCalls" + NL + 
                "endmethod" + ":collectorForwardsCalls" + NL + 
                "endClass" + getClass().getSimpleName() + NL + 
                "storeaKey:aSerializable" + NL + 
                "done" + NL , buffer.toString());
    }
    @Test
    @Exclude
    public void excludedMethod() throws Exception {
        
    }

}
