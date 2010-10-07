/**
 * 
 */
package com.agical.bumblebee.junit4;

import java.lang.reflect.Method;

public class DescriptionHelper {

    public Method getMethodFromClassAndName(Class<?> clazz, String name) {
        if( name == null) {
            return null;
        }
        name = cleanOutJUnitParameters(name);
        try {
            Method[] methods = clazz.getMethods();
            Method method = null;
            int matchCount = 0;
            for (int i = 0; i < methods.length; i++) {
                Method method2 = methods[i];
                if(method2.getName().equals(name)) {
                    method = method2;
                    matchCount++;
                }
            }
            if(matchCount>1)  {
                throw new RuntimeException( "Please, do not use method overloading when using Bumblebee, " +
                		"\nin this case " + name + "\nin\n" + clazz.getName());
            }
            if( matchCount==0) {
                throw new RuntimeException( "No method found," +
                        "\nin this case " + name + "\nin\n" + clazz.getName());
            }
            return method;
        } catch (SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String cleanOutJUnitParameters(String name) {
        return name.indexOf("[")>-1?name.substring(0, name.indexOf("[")):name;
    }

    public String getMethodFromDisplayName(String displayName) {
        int indexOfParenthesis = displayName.indexOf('(');
        if(indexOfParenthesis>-1 ) {
            return displayName.substring(0, indexOfParenthesis);
        } else {
            return null;
        }
    }

    public Class<?> getClassFromDisplayName(String displayName) {
        try {
            String name;
            int indexOfParenthesis = displayName.indexOf('(');
            if( indexOfParenthesis > -1 ) {
                name = displayName.substring(indexOfParenthesis+1, displayName.indexOf(')'));
            } else {
                name = displayName;
            }
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return UnknownClass.class;
        }
    }

    public Class<?> getSuiteFromDisplayName(String displayName) {
        try {
            return Class.forName(displayName);
        } catch (ClassNotFoundException e) {
            return UnknownClass.class;
        }
    }
    
    
}