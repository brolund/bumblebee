package com.agical.bumblebee.junit4;

import org.junit.internal.runners.InitializationError;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;

import com.agical.bumblebee.Collector;

/**
 * @deprecated It is no longer needed to annotate sub-suites with this class, since it is done 
 * automatically by the BumbleBeeSuiteRunner. This class will eventually be removed.
 */
public class BumbleBeeSubSuiteRunner extends AbstractBumblebeeRunner {
    private DescriptionHelper descriptionHelper = new DescriptionHelper();

    public BumbleBeeSubSuiteRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }
    
    @Override
    public void run(final RunNotifier notifier) {
        try {
            Collector collector = getCollector();
            Class<?> classFromDisplayName = descriptionHelper.getSuiteFromDisplayName(getDescription().getDisplayName());
            BumbleBeeSuiteRunner.collectorRunListener.testStarted(getDescription());
            super.run(notifier);
            BumbleBeeSuiteRunner.collectorRunListener.testFinished(getDescription());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private Collector getCollector() {
        Collector collector = BumbleBeeSuiteRunner.getCollector();
        return collector ;
    }
    
    
    
}
