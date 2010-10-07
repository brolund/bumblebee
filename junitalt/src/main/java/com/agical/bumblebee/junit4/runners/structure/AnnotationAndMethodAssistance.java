package com.agical.bumblebee.junit4.runners.structure;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AnnotationAndMethodAssistance {

    private static List<Method> getMethodsWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        Method[] methods = clazz.getMethods();
        List<Method> returnedMethods = new ArrayList<Method>();
        for (Method method : methods) {
            if (method.getAnnotation(annotation) != null) {
                returnedMethods.add(method);
            }
        }
        return returnedMethods;
    }

    public static void invokeAnnotatedMethodsOnClassLevel(Class<?> testClass, Class<? extends Annotation> annotation) {
        for (Method method : getMethodsWithAnnotation(testClass, annotation)) {
            try {
                method.invoke(null);
            } catch (Exception e) {
                throw new RuntimeException("Could not invoke method with AfterClass annotation", e);
            }
        }
    }

    public static void invokeAnnotatedMethodsOnInstanceLevel(Object testObject, Class<? extends Annotation> annotation)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        for (Method method : getMethodsWithAnnotation(testObject.getClass(), annotation)) {
            method.invoke(testObject);
        }
    }

    public static boolean hasMethodWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        for (Method method : clazz.getMethods()) {
            if(method.getAnnotation(annotationClass)!=null) {
                return true;
            }
        }
        return false;
    }

    public static Method getDuckTypeMethod(String methodName, Object target, Object[] parameters) throws NoSuchMethodException {
        Method[] declaredMethods = target.getClass().getDeclaredMethods();
        Method targetMethod = null;
        for (int i = 0; i < declaredMethods.length; i++) {
            Method method = declaredMethods[i];
            if(method.getName().equals(methodName) ) {
                boolean allAssignable = true;
                for (int j = 0; j < parameters.length; j++) {
                    Object param = parameters[j];
                    Class<?> paramType = param.getClass();
                    Class<?> targetParameterType = method.getParameterTypes()[j];
                    if(!parameterIsAssignableToTarget(targetParameterType, paramType)) {
                        allAssignable = false;
                        break;
                    }
                }
                if(allAssignable) {
                    targetMethod = method;
                    System.out.println("----------------------------------------------------");
                    break;
                }
            }
        }
        if(targetMethod!=null) return targetMethod;
        throw new NoSuchMethodException("Could not find method matching: " + target + "." + methodName + "(" + getParameterString(parameters) + ")");
    }

    private static String getParameterString(Object[] parameterTypes) {
        String result = "";
        String comma = "";
        for (int i = 0; i < parameterTypes.length; i++) {
            result += comma + parameterTypes[i];
            comma = ",";
        }
        return result;
    }

    public static boolean parameterIsAssignableToTarget(Class<?> targetParameter, Class<?> parameterToMatch) {
        if(targetParameter.isAssignableFrom(parameterToMatch)) return true;
        
        if(!targetParameter.isPrimitive()) return false;
    
        if(targetParameter.equals(Boolean.TYPE)&&parameterToMatch.equals(Boolean.class)) return true;
        if(targetParameter.equals(Character.TYPE)&&parameterToMatch.equals(Character.class)) return true;
        if(targetParameter.equals(Byte.TYPE)&&parameterToMatch.equals(Byte.class)) return true;
        if(targetParameter.equals(Short.TYPE)&&parameterToMatch.equals(Short.class)) return true;
        if(targetParameter.equals(Integer.TYPE)&&parameterToMatch.equals(Integer.class)) return true;
        if(targetParameter.equals(Long.TYPE)&&parameterToMatch.equals(Long.class)) return true;
        if(targetParameter.equals(Float.TYPE)&&parameterToMatch.equals(Float.class)) return true;
        if(targetParameter.equals(Double.TYPE)&&parameterToMatch.equals(Double.class)) return true;
    
        return false;
    }
    
}
