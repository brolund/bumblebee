package com.agical.bumblebee.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.agical.bumblebee.Collector;
import com.agical.bumblebee.StringBufferCollector;
import com.agical.bumblebee.StringBufferCollectorStatus;
import com.agical.bumblebee.collector.CompositeCollector;

public class TestCompositeCollector {
    private static final String NL = NewLine.STR;
    private StringBuffer buffer = new StringBuffer();
    @Test
    public void collectorForwardsCalls() throws Exception {
        Collector collector1 = new StringBufferCollector("nr1_", buffer);
        Collector collector2 = new StringBufferCollector("nr2_", buffer);
        Collector[] collectors = new Collector[] { collector1, collector2 };
        Collector composite = new CompositeCollector(collectors);
        
        composite.setCallback(new StringBufferCollectorStatus(buffer));
        composite.start();
        composite.beginClass(getClass());
        composite.beginMethod(getClass().getMethod("collectorForwardsCalls", (Class<?>[])null));
        composite.endMethod(getClass().getMethod("collectorForwardsCalls", (Class<?>[])null));
        composite.endClass(getClass());
        composite.store("aKey", "aSerializable");
        composite.done();
        assertEquals(
                "nr1_description:setCallback" + NL + 
                "nr2_description:setCallback" + NL + 
                "nr1_start" + NL + 
                "nr2_start" + NL + 
                "nr1_beginClass" + getClass().getSimpleName() + NL + 
                "nr2_beginClass" + getClass().getSimpleName() + NL + 
                "nr1_beginmethod" + ":collectorForwardsCalls" + NL + 
                "nr2_beginmethod" + ":collectorForwardsCalls" + NL + 
                "nr1_endmethod" + ":collectorForwardsCalls" + NL + 
                "nr2_endmethod" + ":collectorForwardsCalls" + NL + 
                "nr1_endClass" + getClass().getSimpleName() + NL + 
                "nr2_endClass" + getClass().getSimpleName() + NL + 
                "nr1_storeaKey:aSerializable" + NL + 
                "nr2_storeaKey:aSerializable" + NL + 
                "nr1_done" + NL + 
                "nr2_done" + NL, buffer.toString());
    }
}
