/**
 * 
 */
package com.agical.bumblebee.junit4.runners.structure.theories;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.experimental.theories.DataPoint;

public class FieldDataPointFactory implements DataPointFactory {
    public void addDataPoints(Class<?> clazz, List<Object> dataPoints) {
        try {
            for (Field field : clazz.getFields()) {
                if (field.getAnnotation(DataPoint.class) != null) {
                    dataPoints.add(field.get(null));
                } else if (field.getAnnotation(DataPoints.class) != null) {
                    MethodDataPointFactory.addDataPointAsArrayOrCollection(dataPoints, field.get(null), field.getType());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Couldn't retrieve data point", e);
        }
    }
}