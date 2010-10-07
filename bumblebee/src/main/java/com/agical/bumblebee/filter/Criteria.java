/**
 * 
 */
package com.agical.bumblebee.filter;

import java.lang.reflect.Method;

public interface Criteria {
    boolean accept(Method method);
}