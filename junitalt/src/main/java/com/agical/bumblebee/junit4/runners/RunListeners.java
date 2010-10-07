package com.agical.bumblebee.junit4.runners;

public @interface RunListeners {

    Class<? extends RunListener>[] value();

}
