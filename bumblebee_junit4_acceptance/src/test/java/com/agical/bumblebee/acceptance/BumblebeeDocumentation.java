package com.agical.bumblebee.acceptance;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.agical.bumblebee.acceptance.helpers.DefaultRubyWikiHtmlCollector;
import com.agical.bumblebee.acceptance.helpers.DemonstratingMethodComments;
import com.agical.bumblebee.acceptance.helpers.GettingJavaSnippets;
import com.agical.bumblebee.acceptance.helpers.IncludingSlicesAndDicesOfText;
import com.agical.bumblebee.acceptance.helpers.JUnit3TestCase;
import com.agical.bumblebee.acceptance.helpers.Roadmap;
import com.agical.bumblebee.acceptance.helpers.UsingImages;
import com.agical.bumblebee.acceptance.helpers.UsingRuntimeData;
import com.agical.bumblebee.acceptance.helpers.UsingWikiSyntax;
import com.agical.bumblebee.acceptance.helpers.experimental.Experimental;
import com.agical.bumblebee.acceptance.helpers.extension.ExtendingBumblebee;
import com.agical.bumblebee.acceptance.resources.Configuration;
import com.agical.bumblebee.acceptance.selenium.UsingBumblebeeWithSelenium;
import com.agical.bumblebee.acceptance.swing.UsingBumblebeeWithSwing;
import com.agical.bumblebee.acceptance.uml.CreateUmlDiagrams;
import com.agical.bumblebee.agiledox.AgileDoxCollector;
import com.agical.bumblebee.collector.BumblebeeCollectors;
import com.agical.bumblebee.junit4.BumbleBeeSuiteRunner;

@RunWith(BumbleBeeSuiteRunner.class)
@SuiteClasses({
    Configuration.class, 
    DemonstratingMethodComments.class,
    GettingJavaSnippets.class,
    IncludingSlicesAndDicesOfText.class, 
    UsingWikiSyntax.class,
    UsingRuntimeData.class, 
    UsingImages.class,
    CreateUmlDiagrams.class,
    JUnit3TestCase.class,
    UsingBumblebeeWithSwing.class, 
    UsingBumblebeeWithSelenium.class, 
    ExtendingBumblebee.class, 
    Experimental.class, 
    Roadmap.class})
@BumblebeeCollectors({DefaultRubyWikiHtmlCollector.class,AgileDoxCollector.class})
public class BumblebeeDocumentation {
    /*!!
    #{configuration.version_nr=File.new('../dependencies.rb').read.match('BUMBLEBEE_VERSION = \'(.*)\'')[1];''}
    #{configuration.default_target_file=configuration.target_file;''}
    #{configuration.target_file='target/site/bumblebee_doc.html';''}
    #{configuration.copyright='Agical AB';''}
    #{configuration.inception_year=2007;''}
    #{configuration.document_title='The Bumblebee Documentation';''}
    #{set_header 'Bumblebee ' + configuration.version_nr + ' documentation';''}

    Bumblebee is a framework that builds on top of JUnit to create *documentation by example*.
    It is [[https://code.launchpad.net/bumblebee/][open source]] and 
    [[http://www.apache.org/licenses/LICENSE-2.0][Apache 2]] licensed.  

    *** A quick example
    Bumblebee takes code like this:
    >>>>
    public class ASimpleExample {
        @Test
        public void createTimeReporter() throws Exception {
            /*!
            To create a time reporter object, simply invoke the *default constructor*
            #{'*'}/
            TimeReporter timeReporter = new TimeReporter();
            /*!#{'*'}/
        }
    }
    <<<<
    and turns it into documentation like this:
    
    -------
    
    ** A simple example
    
    *** Create time reporter
    To create a time reporter object, simply invoke the *default constructor*
    >>>>
    TimeReporter timeReporter = new TimeReporter();
    <<<<
    
    -------
    
    Bumblebee is especially suited for creating documents where code snippets or runtime data are necessary. 
    Some of the features and benefits: 
        - Bumblebee utilizes a test structure as a base for the structure of the documentation, where suites of suites of test cases can serve as a base for the chapters, and the test cases and the tests can serve as sections or paragraphs
        - The text that goes into the documentation comes mainly from the comments of the test suites and test cases
        - The comments can be formatted using Wiki-style syntax 
        - Code snippets and file extracts are easily included using a domain specific language (DSL), written in Ruby
        - Runtime data can be saved and included in the resulting document
        - The comments are Ruby string templates executed in JRuby; anything can be included that can be retrieved from Ruby or Java 
        - It is easy to create simple UML class diagrams
        - The documentation is refactoring-friendly since the documentation is generated when the tests run, every time 

    The general idea is to retrieve as much human-friendly documentation out 
    of your code base as possible, with 
        - minimal effort
        - minimal intrusion
        - maximal expressive power
        - maximal executional power
        - maximal flexibility
        - maximal extensibility
    
    The documentation is an abstracted view of the system, generated from the details. This differs Bumblebee 
    from many other approaches to code-integrated documentation that tries to create details from an abstract
    view which is difficult, at best. This document, the Bumblebee documentation, is created from the 
    acceptance test suite of Bumblebee. 
    
    Bumblebee can also be used to document Web GUIs (preferably using [[http://selenium-rc.openqa.org/][Selenium]]) and 
    Swing GUIs, please see examples posted at the [[http://groups.google.com/group/bumblebee-tool][Bumblebee Google Group]].
    
    *** This release: #{configuration.version_nr}
    See the [[#com.agical.bumblebee.acceptance.helpers.Roadmap][Roadmap]] section for current and coming 
    features, as well as some information about the Bumblebee project.

    *** Getting support
    There is a [[http://groups.google.com/group/bumblebee-tool][Bumblebee Google Group]] where you can post questions
    or see what others have had issues with, and how their issues were resolved.
    
    *** About this document
    Since the purpose of the documentation is to demonstrate how to use the different features of Bumblebee, most 
    sections will have a structure where the method that is the base for the sections is presented as code, and it is 
    the same method that is actually rendering that section. This can be a bit awkward/meta/recursive, but you 
    will get the hang of it.
    */
}
