package com.agical.bumblebee.junit4.runners.structure.junit4;


public interface JUnit4Visitor  {
    
    public abstract void beginTest(JUnit4Test test);
    
    public abstract void testMethod(JUnit4TestMethod testMethod);
    
    public abstract void endTest(JUnit4Test test);
    
}