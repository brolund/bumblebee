package com.agical.bumblebee.junit4.helpers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.agical.bumblebee.AbstractCollector;
import com.agical.bumblebee.collector.BumblebeeCollectors;

@RunWith(Suite.class)
@SuiteClasses({DummyCase.class, DummySuite2.class})
@BumblebeeCollectors(AbstractCollector.class)
public class DummySuite {}
