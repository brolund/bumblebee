package com.agical.bumblebee.collector;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.agical.bumblebee.Collector;

@Retention(RetentionPolicy.RUNTIME)
public @interface BumblebeeCollectors {
    Class<? extends Collector>[] value();
}
