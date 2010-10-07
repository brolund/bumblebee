package com.agical.bumblebee.junit4.runners.structure.theories;

import java.lang.reflect.Method;

import org.junit.runner.Description;

import com.agical.bumblebee.junit4.runners.structure.TestStructureItem;

public class TheoryDataPoint extends TestStructureItem {

    private final Class<?> clazz;
    private final Method method;
    private final Object[] matchingDataPoint;

    public TheoryDataPoint(Class<?> clazz, Method method, Object[] matchingDataPoint) {
        this.clazz = clazz;
        this.method = method;
        this.matchingDataPoint = matchingDataPoint;
        String dataPointDescription = createDataPointDescription(matchingDataPoint);
        setDescription(Description.createTestDescription(clazz, dataPointDescription));
    }
    
    private static String createDataPointDescription(Object[] matchingDataPoint) {
        String s = "[";
        String comma = "";
        for (Object object : matchingDataPoint) {
            s+=comma + object;
            comma = ",";
        }
        return s+"]";
    }

    public void accept(TheoriesVisitor visitor) {
        visitor.dataPoint(this);
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getMatchingDataPoint() {
        return matchingDataPoint;
    }
    
    
}
