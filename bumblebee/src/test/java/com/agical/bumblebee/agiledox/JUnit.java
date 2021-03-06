package com.agical.bumblebee.agiledox;

import org.junit.internal.runners.InitializationError;
import org.junit.runners.Suite.SuiteClasses;

class JUnit {

    public static Class<?>[] getAnnotatedTestClasses(Class<?> klass) throws InitializationError {
        SuiteClasses annotation= klass.getAnnotation(SuiteClasses.class);
        if (annotation == null)
            throw new InitializationError(String.format("class '%s' must have a SuiteClasses annotation", klass.getName()));
        return annotation.value();
    }

}
