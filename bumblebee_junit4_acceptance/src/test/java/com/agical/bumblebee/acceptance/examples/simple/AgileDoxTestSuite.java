package com.agical.bumblebee.acceptance.examples.simple;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.agical.bumblebee.agiledox.AgileDoxCollector;
import com.agical.bumblebee.collector.BumblebeeCollectors;
import com.agical.bumblebee.junit4.BumbleBeeSuiteRunner;

@RunWith(BumbleBeeSuiteRunner.class)
@SuiteClasses({TheSimplestTestCase.class})
@BumblebeeCollectors({AgileDoxCollector.class})
public class AgileDoxTestSuite {}
