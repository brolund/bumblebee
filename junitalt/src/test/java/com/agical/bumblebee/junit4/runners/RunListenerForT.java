package com.agical.bumblebee.junit4.runners;

public class RunListenerForT implements RunListener {
    
    private StringBuffer calls = new StringBuffer();

    public String getCalls() {
        return calls.toString();
    }
    
    

}
