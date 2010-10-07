package com.agical.bumblebee.junit4;

import static org.junit.Assert.fail;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import junit.framework.AssertionFailedError;

import org.junit.Ignore;
import org.junit.internal.runners.InitializationError;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;

import com.agical.bumblebee.AbstractCollector;
import com.agical.bumblebee.AbstractCollectorStatus;
import com.agical.bumblebee.Collector;
import com.agical.bumblebee.CollectorStatus;
import com.agical.bumblebee.StorageConfigurator;
import com.agical.bumblebee.StorageConfigurator.StorageImpl;
import com.agical.bumblebee.collector.BumblebeeCollectors;
import com.agical.bumblebee.collector.CompositeCollector;
import com.agical.bumblebee.collector.Exclude;
import com.agical.bumblebee.filter.ExcludeAnnotationCriteria;
import com.agical.bumblebee.filter.FilterCollector;

public class BumbleBeeSuiteRunner extends AbstractBumblebeeRunner {
    public static class BumblebeeRunNotifier extends RunNotifier {
        private final RunNotifier delegate;
        public BumblebeeRunNotifier(RunNotifier notifier) {
            this.delegate = notifier;
        }
        public void addFirstListener(RunListener listener) {
            delegate.addFirstListener(listener);
        }
        public void addListener(RunListener listener) {
            delegate.addListener(listener);
        }
        public void fireTestFailure(Failure failure) {
//            Collection<Annotation> annotations = failure.getDescription().getAnnotations();
//            for (Annotation annotation : annotations) {
//                if(annotation.annotationType().equals(Exclude.class)) {
//                    delegate.fireTestIgnored(failure.getDescription());
//                    return;
//                }
//            }
            delegate.fireTestFailure(failure);
        }
        public void fireTestFinished(Description description) {
            delegate.fireTestFinished(description);
        }
        public void fireTestIgnored(Description description) {
            delegate.fireTestIgnored(description);
        }
        public void fireTestRunFinished(Result result) {
            delegate.fireTestRunFinished(result);
        }
        public void fireTestRunStarted(Description description) {
            delegate.fireTestRunStarted(description);
        }
        public void fireTestStarted(Description description) throws StoppedByUserException {
            delegate.fireTestStarted(description);
        }
        public void pleaseStop() {
            delegate.pleaseStop();
        }
        public void removeListener(RunListener listener) {
            delegate.removeListener(listener);
        }
    }

    private static Collector collector;
    private DescriptionHelper descriptionHelper = new DescriptionHelper();
    static CollectorRunListener collectorRunListener;
    
    public BumbleBeeSuiteRunner(Class<?> klass) throws InitializationError {
        super(klass);
        StorageConfigurator.setStorage(new StorageImpl() {
            public void store(String key, Serializable data) {
                BumbleBeeSuiteRunner.getCollector().store(key, data);
            }
        });
    }

    @Override
    public Description getDescription() {
        return super.getDescription();
    }
    
    @Override
    public void run(final RunNotifier notifier) {
        collectorRunListener = null;
        final List<String[]> result = new ArrayList<String[]>();
        try {
            CollectorStatus collectorStatus;
                String displayName = getDescription().getDisplayName();
                final Class<?> classFromDisplayName = descriptionHelper.getSuiteFromDisplayName(displayName);
                collector = getCollectorFromAnnotation(classFromDisplayName);
                collectorStatus = new AbstractCollectorStatus() {
                    public void verificationFailed(String description, String className, String method, String assertion) {
                        result.add(new String[] { description, className, method, assertion, "failed" });
                    }
                    public void verified(String description, String className, String method, String assertion) {
                        result.add(new String[] { description, className, method, assertion, "succeded" });
                    }
                };
            collector.setCallback(collectorStatus);
            collectorRunListener = new CollectorRunListener(collector);
            collectorRunListener.testRunStarted(null);
            collectorRunListener.testStarted(getDescription());
            notifier.addListener(collectorRunListener);
            BumblebeeRunNotifier bumblebeeRunNotifier = new BumblebeeRunNotifier(notifier);
            super.run(bumblebeeRunNotifier);
            collectorRunListener.testFinished(getDescription());
            collectorRunListener.testRunFinished(null);
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            notifier.removeListener(collectorRunListener);
            collector = null;
        }
        for (String[] error : result) {
            if (error[4].equals("failed")) {
                Description createTestDescription = Description.createTestDescription(getClassForName(error[1]),
                        error[2] == null ? "" : error[2], (Annotation[])null);
                notifier.fireTestStarted(createTestDescription);
                notifier.fireTestFailure(new Failure(createTestDescription, new AssertionFailedError(error[0])));
            } else {
                Description createTestDescription = Description.createTestDescription(getClassForName(error[1]),
                        error[3], (Annotation[])null);
                notifier.fireTestStarted(createTestDescription);
                notifier.fireTestFinished(createTestDescription);
            }
        }
        
    }
    private Class<?> getClassForName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static Collector getCollectorFromAnnotation(Class<?> testClass) {
        try {
            BumblebeeCollectors annotation = testClass.getAnnotation(BumblebeeCollectors.class);
            Class<? extends Collector>[] classes = annotation.value();
            Collector[] collectors = new Collector[classes.length];
            for (int i = 0; i < classes.length; i++) {
                collectors[i] = classes[i].newInstance();
            }
            return new FilterCollector(new CompositeCollector(collectors), new ExcludeAnnotationCriteria(Exclude.class, Ignore.class));
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static Collector getCollector() {
        return collector == null ? new AbstractCollector() : collector;
    }
    
    public void assertionsFailed() {

    }

}
