package com.agical.bumblebee.agiledox;

import java.lang.reflect.Method;

public class DeCamelCasingFormatter implements Formatter {
    public String format(Class<?> executingClass) {
        return executingClass==null?"CLASS IS NULL":deCamelCase(executingClass.getSimpleName());
    }
    public String format(Method method) {
        return method==null?"METHOD IS NULL":deCamelCase(method.getName());
    }
    public String deCamelCase(String name) {
        String result = (name.charAt(0)+"").toUpperCase();
        for(int i = 1; i < name.length(); i++ ) {
            char ch = name.charAt(i);
            if( Character.isUpperCase(ch) )  {
                result += (" " + ch).toLowerCase();
            } else {
                result += ch;
            }
        }
        return result;
    }
    public String format(Throwable throwable) {
        return null;
    }
}
