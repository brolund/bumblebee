package com.agical.bumblebee.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.sun.org.apache.regexp.internal.RE;
import com.sun.org.apache.regexp.internal.RESyntaxException;

public abstract class AbstractSourceExtractor implements SourceExtractorInterface {
    
    public static String unindentBlock(String sourceCode, boolean fullIndent, String newIndent) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new StringReader(sourceCode));
            String result = bufferedReader.readLine();
            if (result == null) return "";
            if (result.trim().length() == 0) {
                result = newIndent + bufferedReader.readLine();
            }
            RE re = new RE("(\\s*)\\S*");
            re.match(result);
            String twoindents = re.getParen(1);
            String indent = fullIndent ? twoindents : twoindents.substring(twoindents.length() / 2);
            result = trimIndentFromLine(indent, result) + NewLine.STR;
            String lastLine = "";
            String tmp = "";
            while ((tmp = bufferedReader.readLine()) != null) {
                result += lastLine;
                lastLine = newIndent + trimIndentFromLine(indent, tmp) + NewLine.STR;
            }
            if (lastLine.trim().length() != 0) {
                result += lastLine;
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (RESyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static String trimIndentFromLine(String indent, String result) {
        if (result.startsWith(indent)) {
            String substring = result.substring(indent.length());
            return substring;
        } else if (result.trim().length() == 0) { return indent; }
        throw new RuntimeException(" Line:" + NewLine.STR + "::" + result + "::\n is not correctly indented.");
    }

    public static String extractBumblebeeComment(String method) {
        String METHOD_COMMENT_START = "/*!";
        int compensateForCommentStartTag = METHOD_COMMENT_START.length();
        int soFar = 0;
        String comments = "";
        while (method.indexOf(METHOD_COMMENT_START, soFar) > -1) {
            int beginningOfComment = method.indexOf(METHOD_COMMENT_START, soFar) + compensateForCommentStartTag;
            int endOfComment = method.indexOf("*/", beginningOfComment);
            String comment = method.substring(beginningOfComment, endOfComment);
            
            if (commentIsNotJustAMarker(comment)) {
                comment = comment.substring(comment.indexOf(NewLine.STR));
                comments += comment;
            }
            soFar = endOfComment;
        }
        return unindentBlock(comments, true, "");
    }

    protected static boolean commentIsNotJustAMarker(String comment) {
        return comment.indexOf(NewLine.STR) > -1;
    }

    public static String getCodeBetweenCommentMarkersStatic(String marker1, String marker2, String code) {
        int endOfMarker1Comment = getEndOfComment(code, marker1);
        int beginningOfMarker2Comment = getBeginningOfComment(code, marker2);
        return unindentBlock(code.substring(endOfMarker1Comment, beginningOfMarker2Comment), true, "");
    }

    public static int getBeginningOfComment(String method, String marker) {
        return method.indexOf("/*!" + marker);
    }

    public static int getEndOfComment(String method, String marker) {
        int beginningOfComment = getBeginningOfComment(method, marker);
        int compensateForCommentEndTag = 2;
        return method.substring(beginningOfComment).indexOf("*/") + compensateForCommentEndTag + beginningOfComment;
    }

    public static String getCodeAfterCommentMarkerStatic(String marker1, String code) {
        code = unindentBlock(code, true, "");
        int endOfComment = getEndOfComment(code, marker1);
        String cutoutCode = code.substring(endOfComment);
        return unindentBlock(cutoutCode, false, "");
    }

    public static String escapeTroublesomeCharacters(String code) {
        return code.replace("\"", "\\\"").replace("#{", "#{'#'}{").replace("\\n", "\\\\n");
    }

    public String getLinesBetween(int startLineInclusive, int endLineExclusive, String from) {
        List<String> lines = Arrays.asList(from.split(NewLine.STR));
        List<String> subList = lines.subList(startLineInclusive - 1, endLineExclusive - 1);
        String result = "";
        for (String string : subList) {
            result += string + NewLine.STR;
        }
        return result;
    }

    public String getLinesFrom(int from, String testString) {
        return getLinesBetween(from, testString.split(NewLine.STR).length, testString);
    }

    public String getCommentsFromMethod(Class<?> class1, Method method2) {
        return getCommentsFromMethod(class1.getName(), method2.getName());
    }

    public String getCommentsFromMethod(String className, String methodName) {
        return getCommentsFromMethod(className, methodName, new String[] {});
    }
    
}