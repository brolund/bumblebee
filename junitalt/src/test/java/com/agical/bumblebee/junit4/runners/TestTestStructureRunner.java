package com.agical.bumblebee.junit4.runners;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.notification.RunNotifier;

import com.agical.bumblebee.junit4.runners.helpers.DeepSuite;
import com.agical.bumblebee.junit4.runners.helpers.Failure;
import com.agical.bumblebee.junit4.runners.helpers.ParameterizedSuite;
import com.agical.bumblebee.junit4.runners.helpers.ParameterizedT;
import com.agical.bumblebee.junit4.runners.helpers.SetupAndTearDownParameterized;
import com.agical.bumblebee.junit4.runners.helpers.SetupAndTearDownSuite;
import com.agical.bumblebee.junit4.runners.helpers.SetupAndTearDownT;
import com.agical.bumblebee.junit4.runners.helpers.SetupAndTearDownTheories;
import com.agical.bumblebee.junit4.runners.helpers.ShallowSuite;
import com.agical.bumblebee.junit4.runners.helpers.StructureExecutorForT;
import com.agical.bumblebee.junit4.runners.helpers.Success;
import com.agical.bumblebee.junit4.runners.helpers.SuiteWithoutExecutor;
import com.agical.bumblebee.junit4.runners.helpers.TheoriesSuite;
import com.agical.bumblebee.junit4.runners.helpers.TheoriesT;
import com.agical.bumblebee.junit4.runners.helpers.VisitorForT;
import com.agical.bumblebee.junit4.runners.structure.TestStructureItem;
import com.agical.bumblebee.junit4.runners.structure.junit4.JUnit4Test;
import com.agical.bumblebee.junit4.runners.structure.junit4.JUnit4TestMethod;
import com.agical.bumblebee.junit4.runners.structure.parameterized.ParameterizedTestMethod;
import com.agical.bumblebee.junit4.runners.structure.suite.SuiteStructureItem;
import com.agical.bumblebee.junit4.runners.structure.theories.TheoryTestMethod;

public class TestTestStructureRunner {
    
    @Before
    public void clean() throws Exception {
        SetupAndTearDownT.calls.delete(0, SetupAndTearDownT.calls.length());
        SetupAndTearDownTheories.calls.delete(0, SetupAndTearDownTheories.calls.length());
        SetupAndTearDownParameterized.calls.delete(0, SetupAndTearDownParameterized.calls.length());
    }
    
    @Test
    public void handlesClassWithSuiteClassesAnnotationAsSuite() throws Exception {
        VisitorForT visitor = executeClass(ShallowSuite.class);
        int callNr = 0;
        displayNameIsClass(visitor, "beginSuite", SuiteStructureItem.class, callNr, ShallowSuite.class);
        callNr++;
        displayNameIsClass(visitor, "beginTest", JUnit4Test.class, callNr, Success.class);
        callNr++;
        displayNameIsMethod(visitor, "testMethod", JUnit4TestMethod.class, callNr, Success.class, "success");
        callNr++;
        displayNameIsClass(visitor, "endTest", JUnit4Test.class, callNr, Success.class);
        callNr++;
        displayNameIsClass(visitor, "beginTest", JUnit4Test.class, callNr, Failure.class);
        callNr++;
        displayNameIsMethod(visitor, "testMethod", JUnit4TestMethod.class, callNr, Failure.class, "failure");
        callNr++;
        displayNameIsClass(visitor, "endTest", JUnit4Test.class, callNr, Failure.class);
        callNr++;
        displayNameIsClass(visitor, "endSuite", SuiteStructureItem.class, callNr, ShallowSuite.class);
    }
    
    private void displayNameIsClass(VisitorForT visitor, String callName, Class<?> structureClass, int callNr,
            Class<?> suiteOrTestClass) {
        displayName(visitor, callName, structureClass, callNr, suiteOrTestClass.getName());
    }
    private void displayNameIsMethod(VisitorForT visitor, String callName, Class<?> structureClass, int callNr,
            Class<?> testClass, String displayName) {
        displayName(visitor, callName, structureClass, callNr, displayName + "(" + testClass.getName() + ")");
    }
    
    private void displayName(VisitorForT visitor, String callName, Class<?> structureClass, int callNr,
            String displayName) {
        assertEquals(callName, visitor.calls.get(callNr).key);
        assertEquals(structureClass, visitor.calls.get(callNr).value.getClass());
        assertEquals(displayName, getDisplayName(visitor, callNr));
    }
    
    @Test
    public void handlesDeepSuiteStructure() throws Exception {
        VisitorForT visitor = executeClass(DeepSuite.class);
        int callNr = 0;
        displayNameIsClass(visitor, "beginSuite", SuiteStructureItem.class, callNr, DeepSuite.class);
        callNr++;
        displayNameIsClass(visitor, "beginSuite", SuiteStructureItem.class, callNr, ShallowSuite.class);
        callNr++;
        displayNameIsClass(visitor, "beginTest", JUnit4Test.class, callNr, Success.class);
        callNr++;
        displayNameIsMethod(visitor, "testMethod", JUnit4TestMethod.class, callNr, Success.class, "success");
        callNr++;
        displayNameIsClass(visitor, "endTest", JUnit4Test.class, callNr, Success.class);
        callNr++;
        displayNameIsClass(visitor, "beginTest", JUnit4Test.class, callNr, Failure.class);
        callNr++;
        displayNameIsMethod(visitor, "testMethod", JUnit4TestMethod.class, callNr, Failure.class, "failure");
        callNr++;
        displayNameIsClass(visitor, "endTest", JUnit4Test.class, callNr, Failure.class);
        callNr++;
        displayNameIsClass(visitor, "endSuite", SuiteStructureItem.class, callNr, ShallowSuite.class);
        callNr++;
        displayNameIsClass(visitor, "endSuite", SuiteStructureItem.class, callNr, DeepSuite.class);
    }
    
    @Test
    public void whenExecutorIsAbscentADefaultIsUsed() throws Exception {
        executeClass(SuiteWithoutExecutor.class);
    }
    
    @Test
    public void handlesParameterizedTest() throws Exception {
        VisitorForT visitor = executeClass(ParameterizedSuite.class);
        int callNr = 0;
        assertEquals("beginSuite", visitor.calls.get(callNr).key);
        assertEquals(ParameterizedSuite.class.getName(), getDisplayName(visitor, callNr));
        callNr++;
        assertEquals("beginParameterizedTest", visitor.calls.get(callNr).key);
        assertEquals(ParameterizedT.class.getName(), getDisplayName(visitor, callNr));
        callNr++;
        assertEquals("oneParameterizedTest(" + ParameterizedT.class.getName() + ")", getDisplayName(visitor, callNr));
        assertEquals(ParameterizedTestMethod.class.getName(), visitor.calls.get(callNr).value.getClass().getName());
        callNr++;
        assertEquals("parameter", visitor.calls.get(callNr).key);
        assertEquals("oneParameterizedTest[data1](" + ParameterizedT.class.getName() + ")", getDisplayName(visitor,
                callNr));
        callNr++;
        assertEquals("parameter", visitor.calls.get(callNr).key);
        assertEquals("oneParameterizedTest[data2](" + ParameterizedT.class.getName() + ")", getDisplayName(visitor,
                callNr));
        callNr++;
        assertEquals("oneParameterizedTest(" + ParameterizedT.class.getName() + ")", getDisplayName(visitor, callNr));
        assertEquals(ParameterizedTestMethod.class.getName(), visitor.calls.get(callNr).value.getClass().getName());
        callNr++;
        assertEquals("anotherParameterizedTest(" + ParameterizedT.class.getName() + ")",
                getDisplayName(visitor, callNr));
        assertEquals(ParameterizedTestMethod.class.getName(), visitor.calls.get(callNr).value.getClass().getName());
        callNr++;
        assertEquals("parameter", visitor.calls.get(callNr).key);
        assertEquals("anotherParameterizedTest[data1](" + ParameterizedT.class.getName() + ")", getDisplayName(visitor,
                callNr));
        callNr++;
        assertEquals("parameter", visitor.calls.get(callNr).key);
        assertEquals("anotherParameterizedTest[data2](" + ParameterizedT.class.getName() + ")", getDisplayName(visitor,
                callNr));
        callNr++;
        assertEquals("anotherParameterizedTest(" + ParameterizedT.class.getName() + ")",
                getDisplayName(visitor, callNr));
        assertEquals(ParameterizedTestMethod.class.getName(), visitor.calls.get(callNr).value.getClass().getName());
        callNr++;
        assertEquals("endParameterizedTest", visitor.calls.get(callNr).key);
        assertEquals(ParameterizedT.class.getName(), getDisplayName(visitor, callNr));
        callNr++;
        assertEquals("endSuite", visitor.calls.get(callNr).key);
        assertEquals(SuiteStructureItem.class, visitor.calls.get(callNr).value.getClass());
    }
    
    @Test
    public void handlesTheories() throws Exception {
        VisitorForT visitor = executeClass(TheoriesSuite.class);
        int callNr = 0;
        assertEquals("beginSuite", visitor.calls.get(callNr).key);
        assertEquals(TheoriesSuite.class.getName(), getDisplayName(visitor, callNr));
        callNr++;
        assertEquals("beginTheoriesTest", visitor.calls.get(callNr).key);
        assertEquals(TheoriesT.class.getName(), getDisplayName(visitor, callNr));
        callNr++;
        assertEquals(TheoryTestMethod.class.getName(), visitor.calls.get(callNr).value.getClass().getName());
        assertEquals("aTheory", getDisplayName(visitor, callNr));
        callNr++;
        assertEquals("theoryDataPoint", visitor.calls.get(callNr).key);
        assertEquals("[string 1](" + TheoriesT.class.getName() + ")", getDisplayName(visitor, callNr));
        callNr++;
        assertEquals("theoryDataPoint", visitor.calls.get(callNr).key);
        assertEquals("[string 2](" + TheoriesT.class.getName() + ")", getDisplayName(visitor, callNr));
        callNr++;
        assertEquals(TheoryTestMethod.class.getName(), visitor.calls.get(callNr).value.getClass().getName());
        assertEquals("aTheory", getDisplayName(visitor, callNr));
        callNr++;
        assertEquals(TheoryTestMethod.class.getName(), visitor.calls.get(callNr).value.getClass().getName());
        assertEquals("anAssumingMultiArgumentTheory", getDisplayName(visitor, callNr));
        callNr++;
        assertEquals("theoryDataPoint", visitor.calls.get(callNr).key);
        assertEquals("[string 1,string 1](" + TheoriesT.class.getName() + ")", getDisplayName(visitor, callNr));
        callNr++;
        assertEquals("theoryDataPoint", visitor.calls.get(callNr).key);
        assertEquals("[string 1,string 2](" + TheoriesT.class.getName() + ")", getDisplayName(visitor, callNr));
        callNr++;
        assertEquals("theoryDataPoint", visitor.calls.get(callNr).key);
        assertEquals("[string 2,string 1](" + TheoriesT.class.getName() + ")", getDisplayName(visitor, callNr));
        callNr++;
        assertEquals("theoryDataPoint", visitor.calls.get(callNr).key);
        assertEquals("[string 2,string 2](" + TheoriesT.class.getName() + ")", getDisplayName(visitor, callNr));
        callNr++;
        assertEquals(TheoryTestMethod.class.getName(), visitor.calls.get(callNr).value.getClass().getName());
        assertEquals("anAssumingMultiArgumentTheory", getDisplayName(visitor, callNr));
        callNr++;
        assertEquals(TheoryTestMethod.class.getName(), visitor.calls.get(callNr).value.getClass().getName());
        assertEquals("dataPointsTest", getDisplayName(visitor, callNr));
        callNr++;
        assertEquals("theoryDataPoint", visitor.calls.get(callNr).key);
        assertEquals("[666](" + TheoriesT.class.getName() + ")", getDisplayName(visitor, callNr));
        callNr++;
        assertEquals("theoryDataPoint", visitor.calls.get(callNr).key);
        assertEquals("[667](" + TheoriesT.class.getName() + ")", getDisplayName(visitor, callNr));
        callNr++;
        assertEquals(TheoryTestMethod.class.getName(), visitor.calls.get(callNr).value.getClass().getName());
        assertEquals("dataPointsTest", getDisplayName(visitor, callNr));
        callNr++;
        assertEquals(TheoryTestMethod.class.getName(), visitor.calls.get(callNr).value.getClass().getName());
        assertEquals("dataPointsTestForField", getDisplayName(visitor, callNr));
        callNr++;
        assertEquals("theoryDataPoint", visitor.calls.get(callNr).key);
        assertEquals("[668](" + TheoriesT.class.getName() + ")", getDisplayName(visitor, callNr));
        callNr++;
        assertEquals("theoryDataPoint", visitor.calls.get(callNr).key);
        assertEquals("[669](" + TheoriesT.class.getName() + ")", getDisplayName(visitor, callNr));
        callNr++;
        assertEquals(TheoryTestMethod.class.getName(), visitor.calls.get(callNr).value.getClass().getName());
        assertEquals("dataPointsTestForField", getDisplayName(visitor, callNr));
        callNr++;
        assertEquals("endTheoriesTest", visitor.calls.get(callNr).key);
        assertEquals(TheoriesT.class.getName(), getDisplayName(visitor, callNr));
        callNr++;
        assertEquals("endSuite", visitor.calls.get(callNr).key);
        assertEquals(SuiteStructureItem.class, visitor.calls.get(callNr).value.getClass());
    }
    
    @Test
    public void verifySetupAndTearDownMethods() throws Exception {
        TestStructureRunner runner = new TestStructureRunner(SetupAndTearDownSuite.class);
        RunNotifier runNotifier = new RunNotifier();
        runner.run(runNotifier);
        assertEquals("beforeClass beforeTest test1 afterTest beforeTest test2 afterTest afterClass ",
                SetupAndTearDownT.calls.toString());
        assertEquals("beforeClass beforeTest theory afterTest afterClass ", SetupAndTearDownTheories.calls.toString());
        assertEquals("beforeClass beforeTest test afterTest afterClass ", SetupAndTearDownParameterized.calls
                .toString());
    }
    
    private String getDisplayName(VisitorForT visitor, int callNr) {
        return visitor.calls.get(callNr).value.getDescription().getDisplayName();
    }
    
    private VisitorForT executeClass(Class<?> testSuite) throws Exception {
        StructureExecutorForT executor = new StructureExecutorForT();
        RunListener listener = new VoidRunListener();
        TestStructureRunner runner = new TestStructureRunner(testSuite, executor, listener);
        RunNotifier runNotifier = new RunNotifier();
        runner.run(runNotifier);
        TestStructureItem root = executor.getStructure();
        VisitorForT visitor = new VisitorForT();
        root.accept(visitor);
        return visitor;
    }
    
}
