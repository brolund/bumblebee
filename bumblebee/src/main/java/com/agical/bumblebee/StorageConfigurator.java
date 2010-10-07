package com.agical.bumblebee;

import java.io.Serializable;

public class StorageConfigurator {
    public static void setStorage(StorageImpl storageImpl) {
        Storage.setStorage(storageImpl);
    }
    public static interface StorageImpl {
        void store(String key, Serializable data);
    }
}
