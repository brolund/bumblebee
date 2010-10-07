package com.agical.bumblebee.ruby.invocation;

public class Execution {
    
    protected final long executionTime;

    public Execution(long executionTime) {
        this.executionTime=executionTime;
    }

    public long getExecutionTime() {
        return executionTime;
    }
    
}