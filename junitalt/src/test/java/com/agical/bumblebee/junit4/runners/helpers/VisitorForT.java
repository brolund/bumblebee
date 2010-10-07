/**
 * 
 */
package com.agical.bumblebee.junit4.runners.helpers;

import java.util.ArrayList;
import java.util.List;

import com.agical.bumblebee.junit4.runners.structure.TestStructureItem;
import com.agical.bumblebee.junit4.runners.structure.junit4.JUnit4Test;
import com.agical.bumblebee.junit4.runners.structure.junit4.JUnit4TestMethod;
import com.agical.bumblebee.junit4.runners.structure.junit4.JUnit4Visitor;
import com.agical.bumblebee.junit4.runners.structure.parameterized.ParameterItem;
import com.agical.bumblebee.junit4.runners.structure.parameterized.ParameterizedTest;
import com.agical.bumblebee.junit4.runners.structure.parameterized.ParameterizedTestMethod;
import com.agical.bumblebee.junit4.runners.structure.parameterized.ParameterizedVisitor;
import com.agical.bumblebee.junit4.runners.structure.suite.SuiteStructureItem;
import com.agical.bumblebee.junit4.runners.structure.suite.SuiteVisitor;
import com.agical.bumblebee.junit4.runners.structure.theories.TheoriesVisitor;
import com.agical.bumblebee.junit4.runners.structure.theories.TheoryDataPoint;
import com.agical.bumblebee.junit4.runners.structure.theories.TheoryTest;
import com.agical.bumblebee.junit4.runners.structure.theories.TheoryTestMethod;

public class VisitorForT implements SuiteVisitor, JUnit4Visitor, ParameterizedVisitor, TheoriesVisitor {
    public final List<Pair<String, TestStructureItem>> calls = new ArrayList<Pair<String, TestStructureItem>>();
    
    private void addMessage(String key, TestStructureItem value) {
        calls.add(new Pair<String, TestStructureItem>(key, value));
    }
    
    public void beginSuite(SuiteStructureItem suiteStructureItem) {
        addMessage("beginSuite", suiteStructureItem);
    }
    
    public void beginTest(JUnit4Test test) {
        addMessage("beginTest", test);
    }
    
    public void testMethod(JUnit4TestMethod testMethod) {
        addMessage("testMethod", testMethod);
    }
    
    public void endTest(JUnit4Test test) {
        addMessage("endTest", test);
    }
    
    public void endSuite(SuiteStructureItem suiteStructureItem) {
        addMessage("endSuite", suiteStructureItem);
    }
    
    public void beginParameterizedTest(ParameterizedTest parameterizedTest) {
        addMessage("beginParameterizedTest", parameterizedTest);
    }
    
    public void endParameterizedTest(ParameterizedTest parameterizedTest) {
        addMessage("endParameterizedTest", parameterizedTest);
    }
    
    public void beginTheoriesTest(TheoryTest theoryTest) {
        addMessage("beginTheoriesTest", theoryTest);
    }
    
    public void endTheoriesTest(TheoryTest theoryTest) {
        addMessage("endTheoriesTest", theoryTest);
    }
    
    public void beginTheoryTestMethod(TheoryTestMethod theoryTestMethod) {
        addMessage("beginTheoryTestMethod", theoryTestMethod);
    }
    
    public void endTheoryTestMethod(TheoryTestMethod theoryTestMethod) {
        addMessage("endTheoryTestMethod", theoryTestMethod);
    }
    
    public void dataPoint(TheoryDataPoint theoryDataPoint) {
        addMessage("theoryDataPoint", theoryDataPoint);
    }
    
    public void beginParameterizedTestMethod(ParameterizedTestMethod parameterizedTestMethod) {
        addMessage("beginParameterizedTestMethod", parameterizedTestMethod);
    }
    
    public void endParameterizedTestMethod(ParameterizedTestMethod parameterizedTestMethod) {
        addMessage("endParameterizedTestMethod", parameterizedTestMethod);
    }
    
    public void parameter(ParameterItem parameterItem) {
        addMessage("parameter", parameterItem);
    }
    
}