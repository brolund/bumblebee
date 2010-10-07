package com.agical.bumblebee.junit4.runners.structure.suite;

import org.junit.runner.Description;
import org.junit.runners.Suite.SuiteClasses;

import com.agical.bumblebee.junit4.runners.structure.TestStructureItem;
import com.agical.bumblebee.junit4.runners.structure.TestStructureRoot;

public class SuiteHandler {

    public boolean buildStructure(TestStructureItem testStructureItem, Class<?> clazz, Description description) {
        SuiteClasses annotation = clazz.getAnnotation(SuiteClasses.class);
        if(annotation!=null) {
            Class<?>[] value = annotation.value();
            SuiteStructureItem suiteStructureItem = new SuiteStructureItem(clazz);
            description.addChild(suiteStructureItem.getDescription());
            testStructureItem.addChild(suiteStructureItem);
            for (Class<?> suiteSubClass : value) {
                TestStructureRoot.resolve(suiteStructureItem,suiteSubClass);
            }
            return true;
        }
        return false;
    }

}
