/**

*/
package com.agical.bumblebee.junit4.helpers;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.agical.bumblebee.AbstractCollector;
import com.agical.bumblebee.parser.NewLine;

public class MockCollector extends AbstractCollector {
    private StringBuffer buf;
    public MockCollector(StringBuffer buf) {
        this.buf = buf;
    }

    public void start() {
        buf.append("start" + NewLine.STR);
    }

    public void beginClass(Class<? extends Object> executingClass) {
        buf.append("begin|" + executingClass.getSimpleName()).append(NewLine.STR);
    }

    public void beginMethod(Method method) {
        buf.append("beginmethod|" + method.getName()).append(NewLine.STR);
    }
    public void store(String key, Serializable objectToStore) {
        buf.append("store|" + key + "=>" + objectToStore.toString()).append(NewLine.STR);
    }
    public void endMethod(Method method) {
        buf.append("endmethod|" + method.getName()).append(NewLine.STR);
    }
    public void endMethodWithException(Method method, Throwable throwable) {
        buf.append("endmethodwithfailure|" + method.getName()).append(NewLine.STR);
    }
    
    public void endClass(Class<? extends Object> executingClass) {
        buf.append("end|" + executingClass.getSimpleName()).append(NewLine.STR);
    }

    public void done() {
        buf.append("done" + NewLine.STR);
    }

    
}