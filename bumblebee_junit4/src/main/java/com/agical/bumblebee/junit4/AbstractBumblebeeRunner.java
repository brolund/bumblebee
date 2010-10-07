package com.agical.bumblebee.junit4;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.internal.runners.InitializationError;
import org.junit.runner.Description;
import org.junit.runner.Request;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite.SuiteClasses;

public abstract class AbstractBumblebeeRunner extends Runner {
    private static Set<Class<?>> parents = new HashSet<Class<?>>();
    private final List<Runner> fRunners= new ArrayList<Runner>();
    private final String fName;

    public AbstractBumblebeeRunner(Class<?> klass) throws InitializationError {
        super();
        fName = klass.getName();
        addParent(klass);
        for (Class<?> each : JUnit.getAnnotatedTestClasses(klass)) {
            if(each.getAnnotation(RunWith.class)==null&&each.getAnnotation(SuiteClasses.class)!=null) {
                add(new BumbleBeeSubSuiteRunner(each));
            } else {
                Runner childRunner= Request.aClass(each).getRunner();
                if (childRunner != null) {
                    add(childRunner);
                }
            }
        }
        removeParent(klass);
    }
    
    @Override
    public void run(RunNotifier notifier) {
        runChildren(notifier);
    }

    protected void runChildren(RunNotifier notifier) {
        for (Runner each : fRunners)
            each.run(notifier);
    }

    @Override
    public Description getDescription() {
        Description spec= Description.createSuiteDescription(fName);
        for (Runner runner : fRunners)
            spec.addChild(runner.getDescription());
        return spec;
    }

    public List<Runner> getRunners() {
        return fRunners;
    }

    public void addAll(List<? extends Runner> runners) {
        fRunners.addAll(runners);
    }

    public void add(Runner runner) {
        fRunners.add(runner);
    }
    
    protected String getName() {
        return fName;
    }

    private Class<?> addParent(Class<?> parent) throws InitializationError {
        if (!parents.add(parent))
            throw new InitializationError(String.format("class '%s' (possibly indirectly) contains itself as a SuiteClass", parent.getName()));
        return parent;
    }
    
    private void removeParent(Class<?> klass) {
        parents.remove(klass);
    }
    
}
