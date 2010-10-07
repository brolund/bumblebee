package com.agical.bumblebee.acceptance.helpers.experimental;

import org.junit.Test;

public class ExcludingContent {

    @Test
    public void excludeTestFromReport() throws Exception {
         /*!
         This is how to exclude a test from the resulting document:
         >>>>
         #{clazz.thisIsTheExcludedTest}
         <<<<
         You need to exclude it from both the =wiki2doc= and the =htmlindex= traverser 
         in order to make it disappear totally from the document.
         
         This is also an example of how to make asserts on the resulting document.
         The result in the JUnit report is not very nice at the moment, but at least 
         there is an error.
         
         */

    }

    @Test
    public void thisIsTheExcludedTest() throws Exception {
        /*!
        #{exclude}
        This comment won't be in the resulting document.
        
        #{configuration.traversers.wiki2doc.assert(self, 'Test can be excluded from the resulting document') {|doc|
            !doc.include?('This'+' is the excluded test')
        }}
        */

    }

}
