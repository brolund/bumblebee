package com.agical.bumblebee.junit4.runners;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.Description;

import com.agical.bumblebee.junit4.runners.helpers.DeepSuite;
import com.agical.bumblebee.junit4.runners.helpers.Failure;
import com.agical.bumblebee.junit4.runners.helpers.ShallowSuite;
import com.agical.bumblebee.junit4.runners.helpers.Success;
import com.agical.bumblebee.junit4.runners.structure.TestStructureRoot;

public class Descriptions {
    @Test
    public void deepSuite() throws Exception {
        TestStructureRoot root = new TestStructureRoot();
        root.resolve(DeepSuite.class);
        
        Description rootDescription = root.getDescription();
        
        assertEquals("root", rootDescription.getDisplayName());
        
        ArrayList<Description> rootChildren = rootDescription.getChildren();
        Description deepSuiteDescription = rootChildren.get(0);
        assertEquals(DeepSuite.class.getName(), deepSuiteDescription.getDisplayName());
        
        ArrayList<Description> deepSuiteChildren = deepSuiteDescription.getChildren();
        Description shallowSuiteDescription = deepSuiteChildren.get(0);
        assertEquals(ShallowSuite.class.getName(), shallowSuiteDescription.getDisplayName());
        
        ArrayList<Description> shallowSuiteChildren = shallowSuiteDescription.getChildren();
        Description successfulTestDescription = shallowSuiteChildren.get(0);
        assertEquals(Success.class.getName(), successfulTestDescription.getDisplayName());
        
        ArrayList<Description> successfulTestChildren = successfulTestDescription.getChildren();
        Description successDescription = successfulTestChildren.get(0);
        assertEquals("success(" + Success.class.getName() + ")", successDescription.getDisplayName());
        
        Description failingTestDescription = shallowSuiteChildren.get(1);
        assertEquals(Failure.class.getName(), failingTestDescription.getDisplayName());
        ArrayList<Description> failingTestChildren = failingTestDescription.getChildren();
        Description failureDescription = failingTestChildren.get(0);
        assertEquals("failure(" + Failure.class.getName() + ")", failureDescription.getDisplayName());
    }
}
