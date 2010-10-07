/**
 * 
 */
package com.agical.bumblebee.junit4.runners.structure.theories;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import org.junit.experimental.theories.DataPoint;

public class MethodDataPointFactory implements DataPointFactory {
    public void addDataPoints(Class<?> clazz, List<Object> dataPoints) {
        try {
            for (Method dataPointMethod : clazz.getMethods()) {
                if (dataPointMethod.getAnnotation(DataPoint.class) != null) {
                    dataPoints.add(dataPointMethod.invoke(null));
                } else if (dataPointMethod.getAnnotation(DataPoints.class) != null) {
                    Object dataPoint = dataPointMethod.invoke(null);
                    Class<?> returnType = dataPointMethod.getReturnType();
                    addDataPointAsArrayOrCollection(dataPoints, dataPoint, returnType);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Couldn't retrieve data point", e);
        }
    }

    public static void addDataPointAsArrayOrCollection(List<Object> dataPoints, Object dataPoint, Class<?> returnType) {
        if(returnType.isArray()) {
            for(int i = 0; i < Array.getLength(dataPoint); i++) {
                dataPoints.add(Array.get(dataPoint, i));
            }
        } else {
            dataPoints.addAll((Collection<? extends Object>) dataPoint);
        }
    }
}