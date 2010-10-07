package com.agical.bumblebee.junit4.runners;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.agical.bumblebee.junit4.runners.helpers.LotsOfTheories;
import com.agical.bumblebee.junit4.runners.helpers.ParameterizedT;
import com.agical.bumblebee.junit4.runners.helpers.SetupAndTearDownSuite;
import com.agical.bumblebee.junit4.runners.helpers.TheoriesT;

@RunWith(TestStructureRunner.class)
@SuiteClasses( { Descriptions.class, ParameterizedT.class, TheoriesT.class, LotsOfTheories.class,
        SetupAndTearDownSuite.class })
public class TestingSuite {

}
