package com.agical.bumblebee.acceptance;

import org.junit.runner.JUnitCore;

public class Tools {

    public static String createJUnitTestCommand(String resultFile, Class<?> classToRun) {
        String command = "java -cp \""+ System.getProperty("java.class.path") + "\" " + 
            JUnitCore.class.getName() + " " + 
            classToRun.getName() + " > " + resultFile;
        return command;
    }

}
