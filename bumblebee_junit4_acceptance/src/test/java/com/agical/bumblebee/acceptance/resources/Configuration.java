package com.agical.bumblebee.acceptance.resources;

import org.junit.Test;


public class Configuration {
    @Test
    public void gettingBumblebee() throws Exception {
        /*!
        The latest version of Bumblebee is available at 
        [[http://www.agical.com/bumblebee/download][the Bumblebee download area]] 
        at [[http://www.agical.com][Agical]].
        
        There is a =bumblebee-all= jar including Bumblebee and all dependencies. 
        The =bumblebee-all-no-junit= jar contains everything except
        the JUnit jar. The =bumblebee-all= zip file contains the condensed 
        version of the entire Bumblebee source tree, including all the dependencies.
       
        The entire codebase is available as open source at 
        [[https://code.launchpad.net/bumblebee/][Bumblebee Launchpad Bazaar DVCS]].
        */ 
    }
    @Test
    public void settingBumblebeeUp() throws Exception {
        /*!
        Create a JUnit4 test suite like this:
        >>>>
        #{clazz('com.agical.bumblebee.acceptance.examples.simple.RubyCollectorTestSuite')}
        <<<<
        
        In this example there are some things that are not as the standard JUnit4 test-suite setup:
        
            1. =@RunWith= is configured with the Bumblebee runner
            1. =@BumblebeeCollectors= has to be configured with the desired Bumblebee collectors (more on collectors below).
        
        =@SuiteClasses= is configured as for any other suite.
        
        Create the JUnit test case and run the suite. If everything is correctly setup you will get the documentation in: 
        [[simple-setup.html][target/site/simple-setup.html]]. And you are up and running!
        */ 
    }
    
    @Test
    public void theRubyCollector() throws Exception {
        /*!
        The =RubyCollector= used in the example allows you to:
        
            1. Add text to the document by adding comments to your code
            1. Include code snippets easily
            1. Format the text easily using the muse wiki-syntax
            1. Use serializable values from execution in your documentation
            1. Include whole or parts of texts from files
        
        ...and generally use Ruby or Java thanks to the JRuby interpretation of the comments.
        */
    }

    @Test
    public void configuringSources() throws Exception {
         /*!
        By default, it is assumed that the sources are structured according to the 
        [[http://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html][Standard Directory Structure]]
        if you want to include code snippets. 
        If that doesn't suit your needs just make your own subclass of the =RubyCollector= and change 
        or add source folders to fit your needs. Use the constructor that takes a =File[]=.
        
        You will have to do this if you want to include sources from other projects than the current.
        
         Bumblebee will also assume that it is run in the root of the project. If not, you will also have to 
         specify for Bumblebee where your sources are. 
         
        */
    }
    @Test
    public void documentTargetNameAndDirectory() throws Exception {
        /*!
        The default target for the documentation is =#{configuration.default_target_file}=, but if you want to change that 
        you can put a line like the following in the Bumblebee comment of your root suite: 
        
        >>>>
        #{clazz('com.agical.bumblebee.acceptance.BumblebeeDocumentation').to_s.match(Regexp.new('^(.*)[.]target_file=(.*)$'))}
        <<<<
        */
    }
    @Test
    public void stylesheet() throws Exception {
        /*!
        Bumblebee will provide a default stylesheet, but if you want to have a special stylesheet, add a line like this:
        >>>>
        #{'#'}{configuration.stylesheet='src/site/css/mystylesheet.css'}
        <<<<
        It will be copied to the same folder as the =target_file= and linked to 
        in the document.
        
        Bumblebee will always write the [[bumblebee-default-stylesheet.css][bumblebee-default-stylesheet.css]] to the 
        target folder, and you can include it and extend it from your CSS like this:
        >>>>
        #{File.new('src/site/css/html-stylesheet.css').read.strip}
        <<<<
        #{assert.contains '.*\>\>\>\>.*@import.*\<\<\<\<.*', 'Includes file'}
        */ 
    }
    
    @Test
    public void imagesAndOtherResources() throws Exception {
        /*!
        To copy more resources to e.g. the output folder, use the Bumblebee copy utility:
        #{file_command='copy(\'src/site/dummyresource.txt\', \'target/tmp/dummyresource.txt\')';''}
        >>>>
        #{'#{'+file_command+'}'}
        <<<<
        #{instance_eval file_command;''}
        It will create the output directory if it doesn't exist.
        
        The Ruby API can of course be used for more advances tasks.
        
        #{assert.is_true(File.exists?('target/tmp/dummyresource.txt'), 'Can copy files in Bumblebee')}
        */
    }

    @Test
    public void usingANestedSuiteStructure() throws Exception {
        /*!
        If you have a nested suite structure, subsuites need not specify any
        runner, just the =SuiteClasses= annotation. Bumblebee will assume 
        them to be Bumblebee subsuites since they reside within a Bumblebee suite. 
        
        This is the (somewhat obvious) way to do it:
        >>>>
        #{clazz('com.agical.bumblebee.acceptance.helpers.extension.ExtendingBumblebee')}
        <<<<
        
        Previously, sub suites were configured with the =@RunWith(BumbleBeeSubSuiteRunner.class)=. 
        Keeping that configuration won't render any errors at the moment, but the class will eventually be removed.
        */
    }
    
    @Test
    public void usingMaven() throws Exception {
        /*!
        If you are a Maven2 user, add =http://www.agical.com/maven2= to your repository list and the following dependency to your =pom.xml=:
        >>>>
        <dependency>
            <groupId>com.agical.bumblebee</groupId>
            <artifactId>bumblebee-all</artifactId>
            <version>#{configuration.version_nr}</version>
            <scope>test</scope>
        </dependency>
        <<<<
        */     
    }

    @Test
    public void theAgileDoxCollector() throws Exception {
        /*!
        The =AgileDoxCollector= is a simple Bumblebee implementation that just prints out the de-camelcased names of the suites, 
        test classes and tests methods in an HTML list item hierarchy. If used as is, it produces the HTML-file 
        =[[agile-dox.html][target/site/agile-dox.html]]=, and it can be configured to write the file anywhere on your file system by extending it and 
        calling the superclass constructor with the desired output file. The =AgileDoxCollector= cannot retrieve any comments, in 
        order to do so you must use the =RubyCollector=. 
        */ 
    }
    
}
