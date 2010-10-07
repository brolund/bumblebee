package com.agical.bumblebee;

public interface CollectorStatus {
    void verificationFailed( String description, String className, String method, String assertion );
    void verified( String description, String className, String method, String assertion );
}
