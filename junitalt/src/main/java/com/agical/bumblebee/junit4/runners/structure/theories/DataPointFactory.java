/**
 * 
 */
package com.agical.bumblebee.junit4.runners.structure.theories;

import java.util.List;

public interface DataPointFactory {
    void addDataPoints(Class<?> clazz, List<Object> dataPoints);
}