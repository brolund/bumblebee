package com.agical.bumblebee.ruby.invocation;

public class ClassExecution extends Execution {

    private final Class<?> executingClass;

    public ClassExecution(long executionTime, Class<?> executingClass) {
        super(executionTime);
        this.executingClass = executingClass;
    }

    public Class<?> getExecutingClass() {
        return executingClass;
    }
}
