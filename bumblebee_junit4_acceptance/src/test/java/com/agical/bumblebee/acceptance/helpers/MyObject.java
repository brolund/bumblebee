package com.agical.bumblebee.acceptance.helpers;

import java.io.Serializable;

public class MyObject implements Serializable {
    private static final long serialVersionUID = -9047583716063916445L;
    private String string;

    public MyObject(String string) {
        this.string = string;
    }

    public String getContainedData() {
        return string;
    }
    
    public String toString() {
        return "Result of toString method from MyObject";
    }
}
