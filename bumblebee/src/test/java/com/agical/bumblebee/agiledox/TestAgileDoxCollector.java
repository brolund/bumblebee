package com.agical.bumblebee.agiledox;

import static org.junit.Assert.assertEquals;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.agical.bumblebee.parser.NewLine;

public class TestAgileDoxCollector {
    private static final Class<DummyCase> TESTCASE_CLASS = DummyCase.class;
    private static final Class<DummySuite> SUITE_CLASS1 = DummySuite.class;
    private static final Class<DummySuite2> SUITE_CLASS2 = DummySuite2.class;
    private static final Class<DummySuite3> SUITE_CLASS3 = DummySuite3.class;
    private static final Method TEST_METHOD_1 = getMethod(TESTCASE_CLASS, "testMethod1");
    private static final Method TEST_METHOD_2 = getMethod(TESTCASE_CLASS, "testMethod2");

    private StringWriter stringWriter = new StringWriter();
    private PrintWriter printWriter = new PrintWriter(stringWriter);
    private AgileDoxCollector collector;

    @Before
    public void before() {
        collector = new AgileDoxCollector(printWriter, new SimpleFormatter());
    }
    @Test
    public void fullNestetSuite() throws Exception {
        collector.start();
            collector.beginClass(SUITE_CLASS1);
                collector.beginClass(TESTCASE_CLASS);
                collector.beginMethod(TEST_METHOD_1);
                collector.endMethod(TEST_METHOD_1);
                collector.beginMethod(TEST_METHOD_2);
                collector.endMethod(TEST_METHOD_2);
                collector.endClass(TESTCASE_CLASS);
                collector.beginClass(SUITE_CLASS1);
                    collector.beginClass(TESTCASE_CLASS);
                    collector.beginMethod(TEST_METHOD_1);
                    collector.endMethod(TEST_METHOD_1);
                    collector.beginMethod(TEST_METHOD_2);
                    collector.endMethod(TEST_METHOD_2);
                    collector.endClass(TESTCASE_CLASS);
                collector.endClass(SUITE_CLASS1);
            collector.endClass(SUITE_CLASS1);
        collector.done();
        String string = 
            "<html><body>" + NewLine.STR + 
                "<ul>" + NewLine.STR +
                    "<li>" + SUITE_CLASS1.getSimpleName() + "<br/>" + NewLine.STR +
                        "<ul>" + NewLine.STR +
                            "<li>" + TESTCASE_CLASS.getSimpleName() + "<br/>" + NewLine.STR +
                        		"<ul>" + NewLine.STR +
                        		    "<li>" + TEST_METHOD_1.getName() + "</li>" + NewLine.STR +
                                    "<li>" + TEST_METHOD_2.getName() + "</li>" + NewLine.STR +
                        		"</ul>" + NewLine.STR +
                        	"</li>" + NewLine.STR +
                            "<li>" + SUITE_CLASS1.getSimpleName() + "<br/>" + NewLine.STR +
                                "<ul>" + NewLine.STR +
                                    "<li>" + TESTCASE_CLASS.getSimpleName() + "<br/>" + NewLine.STR +
                                        "<ul>" + NewLine.STR +
                                            "<li>" + TEST_METHOD_1.getName() + "</li>" + NewLine.STR +
                                            "<li>" + TEST_METHOD_2.getName() + "</li>" + NewLine.STR +
                                        "</ul>" + NewLine.STR +
                                    "</li>" + NewLine.STR +
                                "</ul>" + NewLine.STR +
                            "</li>" + NewLine.STR +
                        	"</ul>" + NewLine.STR +
                    "</li>" + NewLine.STR +
                "</ul>" + NewLine.STR +
            "</body></html>" + NewLine.STR;
        assertEquals(string, stringWriter.toString());
    }
    @Test
    public void suiteContainsSuiteDirectly() throws Exception {
        collector.start();
            collector.beginClass(SUITE_CLASS1);
                collector.beginClass(SUITE_CLASS2);
                    collector.beginClass(TESTCASE_CLASS);
                    collector.beginMethod(TEST_METHOD_1);
                    collector.endMethod(TEST_METHOD_1);
                    collector.beginMethod(TEST_METHOD_2);
                    collector.endMethod(TEST_METHOD_2);
                    collector.endClass(TESTCASE_CLASS);
                collector.endClass(SUITE_CLASS2);
                collector.beginClass(SUITE_CLASS3);
                    collector.beginClass(TESTCASE_CLASS);
                    collector.beginMethod(TEST_METHOD_1);
                    collector.endMethod(TEST_METHOD_1);
                    collector.beginMethod(TEST_METHOD_2);
                    collector.endMethod(TEST_METHOD_2);
                    collector.endClass(TESTCASE_CLASS);
            collector.endClass(SUITE_CLASS3);
            collector.endClass(SUITE_CLASS1);
        collector.done();
        String string = 
            "<html><body>" + NewLine.STR + 
                "<ul>" + NewLine.STR +
                    "<li>" + SUITE_CLASS1.getSimpleName() + "<br/>" + NewLine.STR +
                        "<ul>" + NewLine.STR +
                            "<li>" + SUITE_CLASS2.getSimpleName() + "<br/>" + NewLine.STR +
                                "<ul>" + NewLine.STR +
                                    "<li>" + TESTCASE_CLASS.getSimpleName() + "<br/>" + NewLine.STR +
                                        "<ul>" + NewLine.STR +
                                            "<li>" + TEST_METHOD_1.getName() + "</li>" + NewLine.STR +
                                            "<li>" + TEST_METHOD_2.getName() + "</li>" + NewLine.STR +
                                        "</ul>" + NewLine.STR +
                                    "</li>" + NewLine.STR +
                                "</ul>" + NewLine.STR +
                            "</li>" + NewLine.STR +
                            "<li>" + SUITE_CLASS3.getSimpleName() + "<br/>" + NewLine.STR +
                                "<ul>" + NewLine.STR +
                                    "<li>" + TESTCASE_CLASS.getSimpleName() + "<br/>" + NewLine.STR +
                                        "<ul>" + NewLine.STR +
                                            "<li>" + TEST_METHOD_1.getName() + "</li>" + NewLine.STR +
                                            "<li>" + TEST_METHOD_2.getName() + "</li>" + NewLine.STR +
                                        "</ul>" + NewLine.STR +
                                    "</li>" + NewLine.STR +
                                "</ul>" + NewLine.STR +
                            "</li>" + NewLine.STR +
                            "</ul>" + NewLine.STR +
                    "</li>" + NewLine.STR +
                "</ul>" + NewLine.STR +
            "</body></html>" + NewLine.STR;
        assertEquals(string, stringWriter.toString());
    }
    @Test
    public void onlyTestCases() throws Exception {
        collector.start();
        collector.beginClass(TESTCASE_CLASS);
        collector.beginMethod(TEST_METHOD_1);
        collector.endMethod(TEST_METHOD_1);
        collector.beginMethod(TEST_METHOD_2);
        collector.endMethod(TEST_METHOD_2);
        collector.endClass(TESTCASE_CLASS);
        collector.done();
        String string = 
            "<html><body>" + NewLine.STR + 
                "<ul>" + NewLine.STR +
                    "<li>" + TESTCASE_CLASS.getSimpleName() + "<br/>" + NewLine.STR +
                        "<ul>" + NewLine.STR +
                            "<li>" + TEST_METHOD_1.getName() + "</li>" + NewLine.STR +
                            "<li>" + TEST_METHOD_2.getName() + "</li>" + NewLine.STR +
                        "</ul>" + NewLine.STR +
                    "</li>" + NewLine.STR +
                "</ul>" + NewLine.STR +
            "</body></html>" + NewLine.STR;
        assertEquals(string, stringWriter.toString());
    }

    @Test
    public void reportsFailure() throws Exception {
        RuntimeException runtimeException = new RuntimeException("message");
        String stackTrace = new SimpleFormatter().format(runtimeException).replace("\n", "<br/>");
        collector.start();
        collector.beginClass(TESTCASE_CLASS);
        collector.beginMethod(TEST_METHOD_1);
        collector.endMethodWithException(TEST_METHOD_1, runtimeException);
        collector.endClass(TESTCASE_CLASS);
        collector.done();
        String string = 
            "<html><body>" + NewLine.STR + 
                "<ul>" + NewLine.STR +
                    "<li>" + TESTCASE_CLASS.getSimpleName() + "<br/>" + NewLine.STR +
                        "<ul>" + NewLine.STR +
                            "<li><div style=\"background: #BB0000; color: white;\">Failure: " + TEST_METHOD_1.getName() + "<br/>" + stackTrace + "</div></li>" + NewLine.STR +
                        "</ul>" + NewLine.STR +
                    "</li>" + NewLine.STR +
                "</ul>" + NewLine.STR +
            "</body></html>" + NewLine.STR;
        assertEquals(string, stringWriter.toString());
    }

    private static Method getMethod(Class<DummyCase> clazz, String methodName){
        try {
            return clazz.getDeclaredMethod(methodName, new Class[0]);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
