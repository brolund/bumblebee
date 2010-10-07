package com.agical.bumblebee.uml;

import java.util.List;
import java.util.Map;

public class UmlClass {
    public void meth() {
    }
    public void parameterizedMethod(String parameter) {
    }
    public OtherObject returnValue(int i, OtherObject indata) {
        return new OtherObject();
    }
    void packageMethod() {}
    protected void protectedMethod() {}
    private void privateMethod() {}
    public List<String> generics(Map<OtherObject, UmlClass> map) {
        return null;
    } 
    public List<String> nestedGenerics(Map<OtherObject, List<String>> map) {
        return null;
    } 
    
}
