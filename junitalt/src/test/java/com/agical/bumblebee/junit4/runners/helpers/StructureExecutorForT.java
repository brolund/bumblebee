package com.agical.bumblebee.junit4.runners.helpers;

import org.junit.runner.notification.RunNotifier;

import com.agical.bumblebee.junit4.runners.RunListener;
import com.agical.bumblebee.junit4.runners.StructureExecutor;
import com.agical.bumblebee.junit4.runners.structure.TestStructureRoot;

public class StructureExecutorForT implements StructureExecutor {
    
    private TestStructureRoot root;
    
    public TestStructureRoot getStructure() {
        return root;
    }
    
    public void handle(TestStructureRoot root, RunNotifier runNotifier, RunListener listener) {
        this.root = root;
    }
    
}
