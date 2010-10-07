package com.agical.bumblebee.acceptance.failures;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.agical.bumblebee.collector.BumblebeeCollectors;
import com.agical.bumblebee.junit4.BumbleBeeSuiteRunner;
import com.agical.bumblebee.ruby.RubyCollector;

@RunWith(BumbleBeeSuiteRunner.class)
@SuiteClasses({OnFailure.class})
@BumblebeeCollectors({RubyCollector.class})
public class FailingSuite {
    /*!!
    #{configuration.target_file='target/site/failure.html';''}
    #{configuration.stylesheet='src/site/css/stylesheet.css';''}
    #{configuration.copyright='Agical AB';''}
    #{configuration.inception_year=2007;''}
    This is examples of a failing suite. It cannot be invoked within the normal suite 
    (since it would fail the build), hence it is called upon and linked in with the main 
    document.
    */
}
