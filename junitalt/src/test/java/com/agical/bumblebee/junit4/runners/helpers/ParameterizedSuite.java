package com.agical.bumblebee.junit4.runners.helpers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.agical.bumblebee.junit4.runners.TestStructureRunner;

@RunWith(TestStructureRunner.class)
@SuiteClasses( { ParameterizedT.class })
public class ParameterizedSuite {

}
