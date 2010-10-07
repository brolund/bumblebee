/**
 * 
 */
package com.agical.bumblebee;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.agical.bumblebee.parser.NewLine;

/** 
 * This class is for testing purposes
 */
public class StringBufferCollector extends AbstractCollector {
    String NL = NewLine.STR;
	String prefix;
    private final StringBuffer buffer;
    public StringBufferCollector(String prefix, StringBuffer buffer) {
        this.prefix = prefix;
        this.buffer = buffer;
    }
    public void setCallback(CollectorStatus collectorStatus) {
        collectorStatus.verificationFailed(prefix + "description", getClass().getName(), "setCallback", null);
    }

    public void start() {
        buffer.append(prefix).append("start").append(NL);
    }
    public void beginClass(Class<? extends Object> executingClass) {
        buffer.append(prefix).append("beginClass").append(executingClass.getSimpleName()).append(NL);
    }
    public void beginMethod(Method method) {
        buffer.append(prefix).append("beginmethod:").append(method.getName()).append(NL);
    }
    public void store(String key, Serializable objectToStore) {
        buffer.append(prefix).append("store").append(key).append(":").append(objectToStore).append(NL);
    }
    public void endMethod(Method method) {
        buffer.append(prefix).append("endmethod:").append(method.getName()).append(NL);
    }
    public void endClass(Class<? extends Object> executingClass) {
        buffer.append(prefix).append("endClass").append(executingClass.getSimpleName()).append(NL);
    }
    public void done() {
        buffer.append(prefix).append("done").append(NL);
    }
}