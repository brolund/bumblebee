package com.agical.bumblebee.junit4.runners;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExecutorProvider {
    Class<? extends StructureExecutor> value();
}
