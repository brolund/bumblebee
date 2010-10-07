package com.agical.bumblebee.acceptance;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.agical.bumblebee.acceptance.helpers.DefaultRubyWikiHtmlCollector;
import com.agical.bumblebee.acceptance.selenium.UsingBumblebeeWithSelenium;
import com.agical.bumblebee.collector.BumblebeeCollectors;
import com.agical.bumblebee.junit4.BumbleBeeSuiteRunner;

@RunWith(BumbleBeeSuiteRunner.class)
@SuiteClasses({UsingBumblebeeWithSelenium.class})
@BumblebeeCollectors({DefaultRubyWikiHtmlCollector.class})
public class MicroBumblebee {
    /*!!
    #{configuration.target_file='target/site/micro.html';''}
    */
}
