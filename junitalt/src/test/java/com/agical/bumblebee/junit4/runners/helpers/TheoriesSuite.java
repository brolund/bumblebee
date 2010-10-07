package com.agical.bumblebee.junit4.runners.helpers;

import org.junit.runners.Suite.SuiteClasses;

import com.agical.bumblebee.junit4.runners.ExecutorProvider;

@ExecutorProvider(StructureExecutorForT.class)
@SuiteClasses( { TheoriesT.class })
public class TheoriesSuite {

}
