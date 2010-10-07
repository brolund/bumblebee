package com.agical.bumblebee.parser;

public class QDoxBugClass {
    /*!!
    Hej
    */
    final public static String C1 = "C1", C2 = "C2";
    final public static String[] ALL = { C1, C2 };

    public void mended() {
        /*!
        A comment
        */
        System.out.println("Bug fixed?");
    }
}