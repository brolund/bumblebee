package com.agical.bumblebee.junit4.runners.structure.parameterized;

import java.lang.reflect.Method;

import org.junit.runner.Description;

import com.agical.bumblebee.junit4.runners.structure.TestStructureItem;

public class ParameterItem extends TestStructureItem {

    private Object[] parameters;
    private Method method;
    private Class<?> testClass;

    public ParameterItem(Object[] parameters, Method method, Class<?> testClass) {
        super();
        this.parameters = parameters;
        this.method = method;
        this.testClass = testClass;
        String parametersDisplayName = getParametersDisplayName(parameters,testClass);
        setDescription(Description.createTestDescription(testClass, method.getName() + parametersDisplayName));
    }

    private static String getParametersDisplayName(Object[] objects, Class<?> clazz) {
        String s = "[";
        String comma = "";
        for (Object object : objects) {
            s+=comma + object;
            comma = ",";
        }
        return s+"]";
    }
    
    public void accept(ParameterizedVisitor visitor) {
        visitor.parameter(this);
    }

    public Class<?> getTestClass() {
        return testClass;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getParameters() {
        return parameters;
    }
    
    

}
