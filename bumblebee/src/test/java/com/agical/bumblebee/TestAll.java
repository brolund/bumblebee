package com.agical.bumblebee;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.agical.bumblebee.agiledox.TestAgileDoxCollector;
import com.agical.bumblebee.agiledox.TestDeCamelCasingFormatter;
import com.agical.bumblebee.parser.TestCompositeCollector;
import com.agical.bumblebee.parser.TestPmdSourceExtractor;
import com.agical.bumblebee.parser.TestWikiSyntax;

@RunWith(Suite.class)
@SuiteClasses({
            TestCompositeCollector.class, 
            TestPmdSourceExtractor.class, 
            TestWikiSyntax.class,
            TestAgileDoxCollector.class,
            TestDeCamelCasingFormatter.class})
public class TestAll {

}
