package com.agical.bumblebee.junit4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({TestCollectorRunListener.class, TestDescriptionHelper.class})
public class TestAll {

}
