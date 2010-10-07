package com.agical.bumblebee;

public class AbstractCollectorStatus implements CollectorStatus {
    public void verificationFailed(String description, String className, String method, String assertion) {}
    public void verified(String description, String className, String method, String assertion) {}
}
