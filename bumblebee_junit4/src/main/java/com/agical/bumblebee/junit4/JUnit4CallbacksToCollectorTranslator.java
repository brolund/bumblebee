package com.agical.bumblebee.junit4;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Stack;

import org.junit.runner.notification.Failure;

import com.agical.bumblebee.Collector;

public class JUnit4CallbacksToCollectorTranslator {
    private final Collector col;
    private Stack<StackItem> stack = new Stack<StackItem>();

    public JUnit4CallbacksToCollectorTranslator(Collector col) {
        this.col = col;
    }
    
    public void start() {
        stack.push(new RootStackItem());
        col.start();
    }
    public void beginClass(Class<?> executingClass, Method method) {
        try {
            stack.peek().onBegin(executingClass, method);
        } catch (Exception e) {
            System.out.println(executingClass + "." + method);
            throw new RuntimeException( "", e );
            
        }
    }
    public void endClass(Class<?> executingClass, Method method) {
        stack.peek().onEnd(executingClass, method);
    }
    public void endClass(Class<?> executingClass, Method method, Failure failure) {
        stack.peek().onEnd(executingClass, method, failure);
    }

    public void done() {
        while (!stack.isEmpty()) stack.pop().finish();
        col.done();
    }
    private interface StackItem {
        void start(Class<?> executingClass, Method method);
        void onBegin(Class<?> executingClass, Method method);
        void onEnd(Class<?> executingClass, Method method);
        void onEnd(Class<?> executingClass, Method method, Failure failure);
        void finish();
    }
    private class RootStackItem implements StackItem {
        public void finish() {}
        public void onBegin(Class<?> executingClass, Method method) {
            if (method == null) {
                stack.push(new SuiteStackItem(executingClass)).start(executingClass, method);
            } else {
                ClassStackItem classStackItem = new ClassStackItem(executingClass);
                classStackItem.start(executingClass, method);
                stack.push(classStackItem);
            }
        }
        public void onEnd(Class<? extends Object> executingClass, Method method) {}
        public void onEnd(Class<? extends Object> executingClass, Method method, Failure failure) {}
        public void start(Class<?> executingClass, Method method) {}
    }
    private class ClassStackItem implements StackItem {
        private final Class<?> executingClass;
        public ClassStackItem(Class<?> executingClass) {
            this.executingClass = executingClass;
        }
        public void start(Class<?> executingClass, Method method) {
            col.beginClass(executingClass);
            col.beginMethod(method);
        }
        public void onBegin(Class<?> executingClass, Method method) {
            if (this.executingClass.equals(executingClass)) {
                col.beginMethod(method);
            } else {
                if (method == null) {
                    stack.pop().finish();
                    SuiteStackItem classStackItem = new SuiteStackItem(executingClass);
                    classStackItem.start(executingClass, method);
                    stack.push(classStackItem);
                } else {
                    stack.pop().finish();
                    ClassStackItem classStackItem = new ClassStackItem(executingClass);
                    classStackItem.start(executingClass, method);
                    stack.push(classStackItem);
                }
            }
        };
        public void onEnd(Class<? extends Object> executingClass, Method method) {
            if (method == null) {
                onSuitePopBothClassAndParentSuite();
            } else {
                col.endMethod(method);
            }
        }
        public void onEnd(Class<? extends Object> executingClass, Method method, Failure failure) {
            if (method == null) {
                onSuitePopBothClassAndParentSuite();
            } else {
                col.endMethodWithException(method, failure.getException());
            }
        }
        
        private void onSuitePopBothClassAndParentSuite() {
            stack.pop().finish();
            stack.pop().finish();
        }
        public void finish() {
            col.endClass(executingClass);
        }
    }

    private class SuiteStackItem implements StackItem {
        private final Class<?> executingClass;
        public SuiteStackItem(Class<?> executingClass) {
            this.executingClass = executingClass;
        }
        public void start(Class<?> executingClass, Method method) {
            col.beginClass(executingClass);
        }
        public void onBegin(Class<?> executingClass, Method method) {
            if (method == null) {
                stack.push(new SuiteStackItem(executingClass)).start(executingClass, method);
            } else {
                stack.push(new ClassStackItem(executingClass)).start(executingClass, method);
            }
        }
        public void onEnd(Class<? extends Object> executingClass, Method method) {
            stack.pop().finish();
        }
        public void onEnd(Class<? extends Object> executingClass, Method method, Failure failure) {
            onEnd(executingClass, method);
        }
        public void finish() {
            col.endClass(executingClass);
        }
    }
    public void store(String key, Serializable value) {
        col.store(key, value);
    }



}
