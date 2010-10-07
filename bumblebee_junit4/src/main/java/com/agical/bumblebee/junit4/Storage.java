package com.agical.bumblebee.junit4;

import java.io.Serializable;

/**
 *@Deprecated Use com.agical.bumblebee.Storage instead 
 */
public class Storage {
    public static void store(String key, Serializable data) {
        com.agical.bumblebee.Storage.store(key, data);
    }
}
