package com.agical.bumblebee.acceptance.helpers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import com.agical.bumblebee.collector.Exclude;

public class DemonstratingMethodComments {
    /*!!
    One of the key points with Bumblebee is to keep the code close to 
    the comments, and to let the comments become the documentation. 
    With that in mind, the most natural place to keep the comments 
    is of course in the methods. Comments can also reside on a 
    test-class or suite-class level.
    
    This section is a demonstration of basic usage of method 
    comments together with the Ruby execution of the comments.
    #{assert.contains 'Demonstrating method'+' comments', 
    'Headers from de-camelcased class name'}
    */
    private static boolean excludedMethodRan = false;
    private static boolean ignoredMethodRan = false; 
    @Test
    public void probablyTheSimplestComment() throws Exception {
        /*!
        This is the simplest comment possible
        */
    }
    @Test
    public void theSimplestCommentExplained() throws Exception {
        /*!
        This is the code that generated the section above:
        >>>>
        #{clazz.probablyTheSimplestComment}
        <<<<
        It will simply output the comment text as it is, and the section header is the 
        de-camelcased version
        of the method name.
        #{assert.contains 'This is the simplest' +' comment possible', 'Can include comments'}
        */
    }
    @Test
    public void makeAssertionsAboutTheOutput() throws Exception {
        /*!
        It is possible to make assertions about what the resulting document should contain. 
        This is an example from the previous section:
        >>>>
        #{clazz.theSimplestCommentExplained}
        <<<<

        Asserts will be present here and there in the documentation, since the documentation 
        also serves as acceptance tests for Bumblebee.
        
        To avoid having the assertion match its own assertion string (the underlying comments 
        are present as part of the document) the ='+'= is added in the assertion string 
        through-out the document. Assertions are made on the wiki text produced by the comments in the 
        current method, not globally on the document (this has changed between the 0.3 and 0.4 releases).

        To produce a bounded code box, write =>>>>= on a line by itself, followed by one or more lines with snippet text (or calls), followed by 
        =<<<<= on a line by itself.
        
        **** A note on mixing testing and documentation
        Some people argue that testing and documenting are separate responsibilities. 
        However, splitting them up would result in a 
        lot of duplication, thus breaking the *DRY* (don't repeat yourself) principle. 

        The *SRP* (single responsibility principle) is often defined as 
        *a module should only have one reason to change*, and by that definition 
        sharing documentation and acceptance testing could be done in the same module. 
        Since the the correlation is strong between the 
        documented features and the tested acceptance features, and splitting them up 
        would result in breaking the DRY principle, 
        tests and documentation is in one place in Bumblebee.
        */
    }

   
    @Test
    public void nameThatWillBeOverridden() throws Exception {
        /*!
        By default, Bumblebee will generate a section header by de-camelcasing the 
        method name or the simple name of the 
        class (when there is no method, e.g. for test suites).
        To override this for occasional headlines, just set the header. 
        #{set_header 'Override default header'}

        For this section the title would have been 'Name that will be  overridden' 
        but instead we set it to 'Override default header'.
        This is how you override the method name:
        >>>>
        #{meth}
        <<<<
        #{assert.not_contains 'Name'+' that will be overridden', 'Can replace headlines'}
        */
    }
    
    @Test
    @Ignore
    public void ignoredMethod() throws Exception {
        /*!
        #{raise Exception.new('I must not be executed')}
        And the test must not be run.
        */
        ignoredMethodRan = true;
    }

    @Test
    @Exclude
    public void excludedMethod() throws Exception {
        /*!
        #{raise Exception.new('I must not be executed')}
        But the test must be run.
        */
        excludedMethodRan = true;
    }
    
    @Test
    public void excludingMethodsFromDocumentation() throws Exception {
        /*!
        Sometimes you don't want all of your tests to be part of your documentation, but 
        you still want the tests to run.
        In those cases you can use the =@Exclude= annotation, like in:
        >>>>
        #{clazz.excludedMethod}
        <<<<
        This method won't make it into the documentation, but it will be run by JUnit. 
        
        If you want the test to be ignored altogether, just use the JUnit =@Ignore= annotation
        >>>>
        #{clazz.ignoredMethod}
        <<<<
        */
        assertTrue(excludedMethodRan);
        assertFalse(ignoredMethodRan);
    }
    
}
