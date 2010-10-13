package com.agical.bumblebee.acceptance.selenium;

import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({IncludingScreenshotsInTheDocumentation.class /*, DetailingTheScreenShots.class*/})
public class UsingBumblebeeWithSelenium {
	/*!!
	This is a tutorial on how to use Bumblebee together with 
	[[http://seleniumhq.org/][Selenium]] to document your site.
	
	NOTE: Only screenshots work in this version
	Bumblebee will have a simple DSL (Domain Specific Language) to 
	allow you do extract and manipulate screen-shots from the browser,
	thus allowing you to robustly generate documentation for your 
	web GUI.
	The Selenium jars are not included in the =bumblebee-core= to prevent 
	it from getting too big. You need the following:
	>>>>
	#{require '../dependencies';''}
	#{SELENIUM.map {|dep| dep.concat(0x0a)}}
	<<<<
	*/
}
