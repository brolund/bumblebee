package com.agical.bumblebee.parser;

import java.lang.reflect.Method;

public interface SourceExtractorInterface {
    
    String getCodeFromMethod(String className, String methodName, String... parameterNames);
    
    String getCommentsFromMethod(Class<?> class1, Method method2);
    
    String getCommentsFromMethod(String className, String methodName);
    
    String getCommentsFromMethod(String className, String methodName, String... parameterNames);
    
    String getCodeBetweenMarkers(String className, String methodName, String marker1, String marker2);
    
    String getCodeBetweenCommentMarkers(String marker1, String marker2, String code);
    
    String getCodeAfterMarkers(String className, String methodName, String marker1)
            throws UnderlyingBugException;
    
    String getCodeAfterCommentMarker(String marker1, String code);
    
    String getClassComment(Class<?> clazz);
    
    String getClassComment(String className);
    
    String getClassSource(String name);
    
    String getAutoSnippetingComment(String className, String methodName);
    
    String getAutoSnippetingComment(String className, String methodName, String... parameterNames);
    
    String getFieldDeclaration(String className, String variableName);

    String getLinesBetween(int startLineInclusive, int endLineExclusive, String from);
    
}