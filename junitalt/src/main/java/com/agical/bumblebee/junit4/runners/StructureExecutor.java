package com.agical.bumblebee.junit4.runners;

import org.junit.runner.notification.RunNotifier;

import com.agical.bumblebee.junit4.runners.structure.TestStructureRoot;

public interface StructureExecutor {
    void handle(TestStructureRoot root, RunNotifier runNotifier, RunListener listener);
}
