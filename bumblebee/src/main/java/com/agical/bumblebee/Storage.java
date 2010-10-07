package com.agical.bumblebee;

import java.io.Serializable;

import com.agical.bumblebee.StorageConfigurator.StorageImpl;

public class Storage {
    private static StorageImpl storageImpl = new StorageImpl() {
        public void store(String key, Serializable data) {}
    };

    public static void store(String key, Serializable data) {
        storageImpl.store(key, data);
    }

    static void setStorage(StorageImpl storageImpl) {
        Storage.storageImpl = storageImpl;
        
    }
}
