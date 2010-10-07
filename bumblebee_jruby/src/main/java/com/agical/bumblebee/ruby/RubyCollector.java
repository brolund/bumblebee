package com.agical.bumblebee.ruby;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import org.jruby.Ruby;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;

import com.agical.bumblebee.AbstractCollector;
import com.agical.bumblebee.CollectorStatus;
import com.agical.bumblebee.parser.NewLine;
import com.agical.bumblebee.ruby.invocation.ClassExecution;
import com.agical.bumblebee.ruby.invocation.ExceptionalInvocationResult;
import com.agical.bumblebee.ruby.invocation.Execution;
import com.agical.bumblebee.ruby.invocation.InvocationResult;
import com.agical.bumblebee.ruby.invocation.MethodCall;

public class RubyCollector extends AbstractCollector {
    private List<URL> extensions = new ArrayList<URL>();
    private final List<File> sourceRoots;
    private CollectorStatus collectorStatus;
    private IRubyObject bumblebeeStructure;
    private Ruby runtime;
    private Stack<Long> startTimes = new Stack<Long>();
    /**
     * This one of my constructors should be used when you know what you are doing. It will totally 
     * bootstrap my JRuby execution, and if the right hooks aren't there, I will fail miserably. 
     * @param sourceRoots I will look into these directories to find Ruby scripts and Java 
     * source files.
     * @param scripts These are the scripts I will use to boot strap myself.  
     */
    public RubyCollector(File[] sourceRoots, URL[] scripts) {
        this.sourceRoots = printWarningIfSourceRootIsMissing(sourceRoots);
        this.extensions.addAll(Arrays.asList(scripts));
    }
    /**
     * Here I provide you some slack in that I select for myself how I want to 
     * start things up. As a service, I also load all <code>bootstrap.rb</code> 
     * files I can find in the root of the classpath.
     * @param sourceRoots Ruby scripts and Java source files
     */
    public RubyCollector(File[] sourceRoots) {
        this(sourceRoots, getDefaultBootstrapExtensions());
    }
    /**
     * If you use this constructor I assume you have structured your source roots 
     * Maven-style.
     */
    public RubyCollector() {
        this(getMavenStyleSourceRoots(), getDefaultBootstrapExtensions());
    }
    
    public static File[] getMavenStyleSourceRoots() {
        List<File> files = new ArrayList<File>();
        addFileIfItExists(files, "src/main/java");
        addFileIfItExists(files, "src/test/java");
        addFileIfItExists(files, "src/main/resources");
        addFileIfItExists(files, "src/test/resources");
        return files.toArray(new File[] {});
    }
    private static void addFileIfItExists(List<File> files, String pathname) {
        File src = new File(pathname);
        if (src.exists()) files.add(src);
    }
    public static URL[] getDefaultBootstrapExtensions() {
        try {
            ClassLoader classLoader = RubyCollector.class.getClassLoader();
            ArrayList<URL> bootstrapScripts = Collections.list(classLoader.getResources("bootstrap.rb"));
            bootstrapScripts.add(0, classLoader.getResource("bumblebee_bootstrap.rb"));
            return bootstrapScripts.toArray(new URL[] {});
        } catch (IOException e) {
            throw new RuntimeException("Couldn't read bootstrap script url(s) from classpath.", e);
        }
    }
    
    public void setCallback(CollectorStatus collectorStatus) {
        this.collectorStatus = collectorStatus;
    }
    public void start() {
        try {
            runtime = JavaEmbedUtils.initialize(createListOfFileNames(sourceRoots));
            String script = "";
            script += getScripts() + NewLine.STR;
            script += "Bumblebee::BumblebeeStructure.new " + NewLine.STR;
            bumblebeeStructure = runtime.evalScriptlet(script);
            JavaEmbedUtils.invokeMethod(runtime, bumblebeeStructure, "status_callback=",
                    new Object[] { collectorStatus }, Object.class);
            JavaEmbedUtils.invokeMethod(runtime, bumblebeeStructure, "start_script", new Object[] {}, Object.class);
            startTimes.push(System.currentTimeMillis());
        } catch (IOException e) {
            throw new RuntimeException("Sourceroots should already have been checked to exist. Strange...", e);
        }
    }
    private static List<String> createListOfFileNames(List<File> sourceRoots) {
        List<String> roots = new ArrayList<String>();
        for (int i = 0; i < sourceRoots.size(); i++) {
            roots.add(sourceRoots.get(i).getAbsolutePath());
        }
        return roots;
    }
    public void beginClass(Class<? extends Object> executingClass) {
        JavaEmbedUtils.invokeMethod(runtime, bumblebeeStructure, "begin_structure", new Object[] { executingClass },
                Object.class);
        startTimes.push(System.currentTimeMillis());
    }
    public void beginMethod(Method method) {
        MethodCall invocation = new MethodCall(null, method, null);
        JavaEmbedUtils.invokeMethod(runtime, bumblebeeStructure, "begin_method", new Object[] { invocation },
                Object.class);
        startTimes.push(System.currentTimeMillis());
    }
    public void store(String key, Serializable objectToStore) {
        JavaEmbedUtils.invokeMethod(runtime, bumblebeeStructure, "store_data", new Object[] { key, objectToStore },
                Object.class);
    }
    public void endMethod(Method method) {
        Long time = System.currentTimeMillis() - startTimes.pop();
        Execution invocationResult = new InvocationResult(time, method);
        JavaEmbedUtils.invokeMethod(runtime, bumblebeeStructure, "end_method", new Object[] { invocationResult },
                Object.class);
    }
    public void endMethodWithException(Method method, Throwable exception) {
        Long time = System.currentTimeMillis() - startTimes.pop();
        ExceptionalInvocationResult exceptionalInvocationResult = new ExceptionalInvocationResult(time, exception,
                method);
        JavaEmbedUtils.invokeMethod(runtime, bumblebeeStructure, "end_method_with_exception",
                new Object[] { exceptionalInvocationResult }, Object.class);
    }
    public void endClass(Class<? extends Object> executingClass) {
        Long time = System.currentTimeMillis() - startTimes.pop();
        ClassExecution execution = new ClassExecution(time, executingClass);
        JavaEmbedUtils.invokeMethod(runtime, bumblebeeStructure, "end_structure", new Object[] { execution },
                Object.class);
    }
    public void done() {
        Long time = System.currentTimeMillis() - startTimes.pop();
        JavaEmbedUtils.invokeMethod(runtime, bumblebeeStructure, "execute_structure", new Object[] { time },
                Object.class);
    }
    
    private List<File> printWarningIfSourceRootIsMissing(File[] sourceRoots) {
        List<File> files = new ArrayList<File>();
        for (File file : sourceRoots) {
            if (file.exists()) {
                files.add(file);
            } else {
                System.err.println("** WARNING! Configured but non-existing source root: " + file.getAbsolutePath());
            }
        }
        return files;
    }
    private String getScripts() throws IOException {
        String result = "";
        for (URL url : extensions) {
            result += getResource(url);
        }
        return result;
    }
    private String getResource(URL url) throws IOException {
        InputStream openStream = url.openStream();
        if (openStream == null) { throw new RuntimeException("Could not find " + url.toExternalForm()); }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openStream));
        try {
            String row = null;
            String result = "";
            while ((row = bufferedReader.readLine()) != null) {
                result += row + NewLine.STR;
            }
            return result;
        } finally {
            bufferedReader.close();
        }
    }
    
}
