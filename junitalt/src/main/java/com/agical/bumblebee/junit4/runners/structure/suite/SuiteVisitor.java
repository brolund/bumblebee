package com.agical.bumblebee.junit4.runners.structure.suite;


public interface SuiteVisitor {
    void beginSuite(SuiteStructureItem suiteStructureItem);
    void endSuite(SuiteStructureItem suiteStructureItem);
}