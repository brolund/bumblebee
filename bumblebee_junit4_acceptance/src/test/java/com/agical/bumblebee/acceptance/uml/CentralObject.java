package com.agical.bumblebee.acceptance.uml;

public class CentralObject extends SuperClass {

    private DependentObject dependentObject;

    public DependentObject getDependentObject() {
        return dependentObject;
    }

    public void setDependentObject(DependentObject dependentObject) {
        this.dependentObject = dependentObject;
    }
    
}
