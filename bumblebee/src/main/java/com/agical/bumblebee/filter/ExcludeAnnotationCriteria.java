/**
 * 
 */
package com.agical.bumblebee.filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ExcludeAnnotationCriteria implements Criteria {
    private Class<? extends Annotation>[] annotationClasses;
    public ExcludeAnnotationCriteria(Class<? extends Annotation>... annotationClasses) {
        this.annotationClasses = annotationClasses;
    }
    public boolean accept(Method method) {
        for (Class<? extends Annotation> annotationClass: annotationClasses) {
            if(method.getAnnotation(annotationClass) != null) return false;
        }
        return true;
    }
}