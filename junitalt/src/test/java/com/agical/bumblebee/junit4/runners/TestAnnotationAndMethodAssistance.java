package com.agical.bumblebee.junit4.runners;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Method;

import org.junit.Test;

import com.agical.bumblebee.junit4.runners.structure.AnnotationAndMethodAssistance;
import com.agical.bumblebee.junit4.runners.structure.suite.SuiteStructureItem;
import com.agical.bumblebee.junit4.runners.structure.suite.SuiteVisitor;

public class TestAnnotationAndMethodAssistance {
    
    @Test
    public void findsMethodsOfSameOrigin() throws Exception {
        Method method = SuiteStructureItem.class.getMethod("accept", SuiteVisitor.class);
        SuiteStructureItem suiteStructureItem = new SuiteStructureItem(Object.class);
        Method duckTypeMethod = AnnotationAndMethodAssistance.getDuckTypeMethod("accept",
                suiteStructureItem, new Object[] { new TestingSuiteVisitor()} );
        assertEquals(method, duckTypeMethod);
    }
    
    @Test
    public void handlesPrimitivesAndDifferentOrigin() throws Exception {
        Method method = PrimitivesInterface.class.getMethod("for_int", Integer.TYPE);
        Primitives target = new Primitives();
        Object[] parameters = new Object[] {1};
        Class<?>[] parameterTypes = new Class<?>[] { Integer.TYPE };
        Method duckTypeMethod = AnnotationAndMethodAssistance.getDuckTypeMethod("for_int",
                target, parameters);
        assertNotNull(duckTypeMethod);
        assertEquals(method.getName(), duckTypeMethod.getName());
        assertEquals(method.getParameterTypes(), duckTypeMethod.getParameterTypes());
    }
    
    
    private class TestingSuiteVisitor implements SuiteVisitor {
        public void beginSuite(SuiteStructureItem suiteStructureItem) {}
        public void endSuite(SuiteStructureItem suiteStructureItem) {}
    }
    private class Primitives {
        public void for_boolean(boolean param) {
        }
        public void for_byte(byte param) {
        }
        public void for_char(char param) {
        }
        public void for_double(double param) {
        }
        public void for_float(float param) {
        }
        public void for_int(int param) {
        }
        public void for_long(long param) {
        }
        public void for_short(short param) {
        }
    }
}
