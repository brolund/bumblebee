package com.agical.bumblebee.junit4.runners.helpers;

import org.junit.runners.Suite.SuiteClasses;

import com.agical.bumblebee.junit4.runners.ExecutorProvider;

@ExecutorProvider(value = StructureExecutorForT.class)
@SuiteClasses( { Success.class, Failure.class })
public class ShallowSuite {

}
