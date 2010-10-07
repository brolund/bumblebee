package com.agical.bumblebee.agiledox;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Method;

import com.agical.bumblebee.AbstractCollector;
import com.agical.bumblebee.parser.NewLine;

public class AgileDoxCollector extends AbstractCollector {
    private final PrintWriter printWriter;
    private final Formatter formatter;
    private FailureFacade failureFacade;
    
    private static PrintWriter getPrintWriter() {
        try {
            File file = new File(new File("."), "target/site/agile-dox.html");
            file.getParentFile().mkdirs();
            return new PrintWriter(new FileWriter(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AgileDoxCollector() {
        this(getPrintWriter(), new DeCamelCasingFormatter());
    }
    public AgileDoxCollector(PrintWriter printWriter) {
        this(printWriter, new DeCamelCasingFormatter());
    }
    public AgileDoxCollector(PrintWriter printWriter, Formatter formatter) {
        this.printWriter = printWriter;
        this.formatter = formatter;
    }
    public void start() {
        app("<html><body>" + NewLine.STR + "<ul>" + NewLine.STR);
    }
    public void beginClass(Class<? extends Object> executingClass) {
        app("<li>" + formatter.format(executingClass) + "<br/>" + NewLine.STR + "<ul>" + NewLine.STR);
    }
    public void store(String key, Serializable objectToStore) {
    }
    public void endMethod(Method method) {
        app((failureFacade!=null?"<li style=\"background: #BB0000; color: white;\">Failure: ":"<li>") + formatter.format(method) + "</li>" + NewLine.STR);
        failureFacade=null;
    }
    public void endMethodWithException(Method method,Throwable throwable) {
        String stackTrace = new SimpleFormatter().format(throwable).replace("\n", "<br/>");
        app("<li><div style=\"background: #BB0000; color: white;\">Failure: " + formatter.format(method) + "<br/>" + stackTrace + "</div></li>" + NewLine.STR);
        failureFacade=null;
    }
    public void endClass(Class<? extends Object> executingClass) {
        app("</ul>" + NewLine.STR + "</li>" + NewLine.STR);
    }
    public void done() {
        app("</ul>" + NewLine.STR + "</body></html>" + NewLine.STR);
        printWriter.close();
    }
    private void app(String str) {
        printWriter.append(str);
    }
}