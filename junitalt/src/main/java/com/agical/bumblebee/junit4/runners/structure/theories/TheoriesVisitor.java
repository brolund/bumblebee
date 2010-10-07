package com.agical.bumblebee.junit4.runners.structure.theories;


public interface TheoriesVisitor  {
    
    public abstract void beginTheoryTestMethod(TheoryTestMethod theoryTestMethod);
    
    public abstract void beginTheoriesTest(TheoryTest theoryTest);
    
    public abstract void dataPoint(TheoryDataPoint theoryDataPoint);
    
    public abstract void endTheoriesTest(TheoryTest theoryTest);
    
    public abstract void endTheoryTestMethod(TheoryTestMethod theoryTestMethod);
    
}