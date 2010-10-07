package com.agical.bumblebee.agiledox;

import java.lang.reflect.Method;

public interface Formatter {
    String format(Class<?> executingClass);
    String format(Method method);
    public String format(Throwable throwable);
}
