package com.agical.bumblebee;

import com.agical.bumblebee.parser.NewLine;

/**
 * This class is for testing purposes
 */
public class StringBufferCollectorStatus extends AbstractCollectorStatus {
    StringBuffer buffer;
    public StringBufferCollectorStatus(StringBuffer buffer) {
        this.buffer = buffer;
    }
    public void verificationFailed(String description, String className, String method, String assertion) {
        buffer.append(description + ":" + method  +NewLine.STR);
    }
}