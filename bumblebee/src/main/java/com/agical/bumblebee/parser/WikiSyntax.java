package com.agical.bumblebee.parser;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.StringTokenizer;

import oqube.muse.Publisher;
import oqube.muse.html.HTMLPublisher;

import com.sun.org.apache.regexp.internal.RE;
import com.sun.org.apache.regexp.internal.RESyntaxException;

public class WikiSyntax {
    public String convertToHtml(String string) {
        if(string==null) return "";
        try {
            RE re = new RE( "^>>>>$((\\n|\\r|.)*?)^<<<<$", RE.MATCH_MULTILINE);
            StringBuffer result = new StringBuffer();
            while(re.match(string)) {
                int  start = re.getParenStart(0);
                int  end = re.getParenEnd(0);
                String content = re.getParen(1);
                content = content.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
                result.append( iconvertToHtml(string.substring(0,start)))
                           .append( "<code><pre>" + content +  "</pre></code>");
                string = string.substring(end,string.length());
            }
            result.append(iconvertToHtml(string));
            return result.toString();
        } catch (RESyntaxException e) {
            throw new RuntimeException(string, e);
        }
    }
    private String iconvertToHtml(String string) {
        try {
            Publisher instance = new HTMLPublisher();
            instance.startSession("hej");
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            instance.process("hej", new StringReader(string), printWriter);
            instance.endSession("hej");
            printWriter.close();
            StringTokenizer stringTokenizer = new StringTokenizer( stringWriter.toString(), NewLine.STR, false);
            stringTokenizer.nextElement();
            stringTokenizer.nextElement();
            stringTokenizer.nextElement();
            String token = stringTokenizer.nextToken();
            String result = "";
            while(stringTokenizer.hasMoreElements()) {
                result += token + NewLine.STR;
                token = stringTokenizer.nextToken();
                if( !stringTokenizer.hasMoreElements() ) break;
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (RuntimeException e) {
            throw new RuntimeException(string, e);
        } 
    }

    public String convertToInlineHtml(String string) {
        String result = convertToHtml(string).trim();
        if(result.startsWith("<p>")) result = result.substring(3).trim();
        if(result.endsWith("</p>")) result = result.substring(0,result.length()-4).trim();
        return result;
    }
}
