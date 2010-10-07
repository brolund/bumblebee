package com.agical.bumblebee.acceptance.examples.simple;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.agical.bumblebee.collector.BumblebeeCollectors;
import com.agical.bumblebee.junit4.BumbleBeeSuiteRunner;
import com.agical.bumblebee.ruby.RubyCollector;

@RunWith(BumbleBeeSuiteRunner.class)
@SuiteClasses({TheSimplestTestCase.class})
@BumblebeeCollectors({RubyCollector.class})
public class RubyCollectorTestSuite {
    /*!!
    #{configuration.target_file='target/site/simple-setup.html';''}
    */
}
