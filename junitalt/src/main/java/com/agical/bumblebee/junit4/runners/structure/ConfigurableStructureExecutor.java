package com.agical.bumblebee.junit4.runners.structure;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.notification.RunNotifier;

import com.agical.bumblebee.junit4.runners.RunListener;
import com.agical.bumblebee.junit4.runners.StructureExecutor;
import com.agical.bumblebee.junit4.runners.structure.junit4.JUnit4TestExecutor;
import com.agical.bumblebee.junit4.runners.structure.junit4.JUnit4Visitor;
import com.agical.bumblebee.junit4.runners.structure.parameterized.ParameterizedExecutor;
import com.agical.bumblebee.junit4.runners.structure.parameterized.ParameterizedVisitor;
import com.agical.bumblebee.junit4.runners.structure.suite.SuiteVisitor;
import com.agical.bumblebee.junit4.runners.structure.theories.TheoriesExecutor;
import com.agical.bumblebee.junit4.runners.structure.theories.TheoriesVisitor;

public class ConfigurableStructureExecutor implements StructureExecutor {

    public class DuckTypingInvocationHandler implements InvocationHandler {
        private final List<Object> targetCandidates;
        public DuckTypingInvocationHandler(List<Object> targetCandidates) {
            this.targetCandidates = targetCandidates;
        }
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            for (Object executor : targetCandidates) {
                try {
                    Method duckTypingMethod = 
                        AnnotationAndMethodAssistance.getDuckTypeMethod(method.getName(), executor, args);
                    return duckTypingMethod.invoke(executor, args);
                } catch (IllegalArgumentException e) {
                    throw e;
                } catch (InvocationTargetException e) {
                    throw e;
                } catch (IllegalAccessException e) {
                    throw e;
                } catch (NoSuchMethodException e) {
                    continue;
                } catch (SecurityException e) {
                    throw e;
                } catch (Exception e) {
                    throw e;
                }
            }
            return null;
        }
    }

    public void handle(TestStructureRoot root, RunNotifier runNotifier, RunListener listener) {
        List<Object> listeners = new ArrayList<Object>();
        listeners.add(listener);
        InvocationHandler listenerHandler = new DuckTypingInvocationHandler(listeners);
        Object duckyListener = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[] { SuiteVisitor.class,
                JUnit4Visitor.class, ParameterizedVisitor.class, TheoriesVisitor.class }, listenerHandler);

        final JUnit4TestExecutor jUnit4TestExecutor = new JUnit4TestExecutor(runNotifier);
        final TheoriesExecutor theoriesExecutor = new TheoriesExecutor(runNotifier);
        final ParameterizedExecutor parameterizedExecutor = new ParameterizedExecutor(runNotifier);
        final List<Object> executors = new ArrayList<Object>();
        executors.add(jUnit4TestExecutor);
        executors.add(theoriesExecutor);
        executors.add(parameterizedExecutor);
        InvocationHandler executionHandler = new DuckTypingInvocationHandler(executors);
        Object visitor = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[] { SuiteVisitor.class,
                JUnit4Visitor.class, ParameterizedVisitor.class, TheoriesVisitor.class }, executionHandler);
        root.accept(visitor);
    }

}
