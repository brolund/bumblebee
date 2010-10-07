package com.agical.bumblebee.junit4.runners.structure.parameterized;


public interface ParameterizedVisitor  {
    
    public abstract void beginParameterizedTest(ParameterizedTest parameterizedTest);
    
    public abstract void beginParameterizedTestMethod(ParameterizedTestMethod parameterizedTestMethod);
    
    public abstract void parameter(ParameterItem parameterItem);
    
    public abstract void endParameterizedTestMethod(ParameterizedTestMethod parameterizedTestMethod);
    
    public abstract void endParameterizedTest(ParameterizedTest parameterizedTest);
    
}