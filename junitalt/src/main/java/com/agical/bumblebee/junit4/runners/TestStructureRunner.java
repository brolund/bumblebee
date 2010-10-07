package com.agical.bumblebee.junit4.runners;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

import com.agical.bumblebee.junit4.runners.structure.ConfigurableStructureExecutor;
import com.agical.bumblebee.junit4.runners.structure.TestStructureRoot;

/**
 * See the Bumblebee documentation for details on how to use this Runner.
 */
public class TestStructureRunner extends Runner  {
    
    private TestStructureRoot root;
    private Description description;
    private final StructureExecutor executor;
    private final RunListener listener;

    public TestStructureRunner(Class<?> testCase) throws Exception {
        this(testCase, getExecutor(testCase), getListener(testCase));
    }

    private static RunListener getListener(Class<?> testCase) {
        return null;
    }

    private static StructureExecutor getExecutor(Class<?> testCase) throws InstantiationException, IllegalAccessException {
        StructureExecutor executor;
        ExecutorProvider annotation = testCase.getAnnotation(ExecutorProvider.class);
        if(annotation==null) {
            executor = new ConfigurableStructureExecutor();
        } else {
            Class<? extends StructureExecutor> value = annotation.value();
            executor = value.newInstance();
        }
        return executor;
    }

    // Use for testing to bypass annotation reading
    public TestStructureRunner(Class<?> testCase, StructureExecutor executor, RunListener listener) throws Exception {
        this.executor = executor;
        this.listener = listener;
        root = new TestStructureRoot();
        root.resolve(testCase);
    }    
    
    @Override 
    public Description getDescription() {
        if(description==null) {
            description = root.getDescription();
        }
        return description.getChildren().get(0);
    }
    
    @Override 
    public void run(RunNotifier runNotifier) {
        executor.handle(root, runNotifier, listener);
    }

    @Override 
    public int testCount() {
        return root.testCount();
    }
}
