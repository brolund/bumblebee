package com.agical.bumblebee.acceptance;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.agical.bumblebee.acceptance.defaults.DefaultTesting;
import com.agical.bumblebee.acceptance.examples.simple.AgileDoxTestSuite;
import com.agical.bumblebee.acceptance.examples.simple.RubyCollectorTestSuite;

@RunWith(Suite.class)
@SuiteClasses({
    DefaultTesting.class,
    RubyCollectorTestSuite.class, 
    AgileDoxTestSuite.class, 
    BumblebeeDocumentation.class})
public class AllTest {
}
