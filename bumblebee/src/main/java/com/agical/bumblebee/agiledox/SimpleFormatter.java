package com.agical.bumblebee.agiledox;

import java.lang.reflect.Method;

public class SimpleFormatter implements Formatter {
    public String format(Class<?> executingClass) {
        return executingClass.getSimpleName();
    }
    public String format(Method method) {
        return method == null ? "NULL???" : method.getName();
    }
    public String format(Throwable throwable) {
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        String result = "";
        for (int i = 0; i < stackTrace.length; i++) {
            StackTraceElement stackTraceElement = stackTrace[i];
            result += stackTraceElement.toString() + "\n";
        }
        return result;
    }

}
