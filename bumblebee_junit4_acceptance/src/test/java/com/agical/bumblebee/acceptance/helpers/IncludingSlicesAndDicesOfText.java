package com.agical.bumblebee.acceptance.helpers;

import org.junit.Test;

public class IncludingSlicesAndDicesOfText {
    /*!!
    Bumblebee tries to utilize Rubys excellent string manipulation 
    functionality.
    */
    @Test
    public void includeArbitraryFile() throws Exception {
        /*!m1
        This is how to include the contents of an arbitrary file 
        into the documentation, here as a snippet:
        >>>>
        #{File.new('src/test/resources/demofile.txt').read}
        <<<<

        >>>>
        #{meth}
        <<<<

        The filename can be relative to the execution directory 
        or an absolute path (not recommended).
        #{assert.contains('This'+' is a Bumblebee example file.', 
        'Can include arbitrary files')}
        */
    }
    
    @Test
    public void includeLineRanges() throws Exception {
        /*!m1
        This is how you include certain lines:
        >>>>
        #{meth}
        <<<<
        The first argument to =lines= is the starting line (one-based), and 
        the second argument is how many lines 
        to include.
        
        This is what it looks like for a file:
        >>>>
        #{File.new('src/test/resources/multiline.txt').read.lines(1,3)}
        <<<<
        
        =lines= is a method added onto the Ruby =String= object, and can be used 
        on Strings through-out.
        
        #{assert.contains 'Line 1'+' of multiline.txt', 
        'Can include specific line range from content' }
        */
    }
    
    @Test
    public void includeTextBasedOnRegularExpression() throws Exception {
        /*!m1
        Ruby has support for regular expressions, and they can be used as-is in Bumblebee:
        >>>>
        #{meth}
        <<<<
        The output looks like this:
        >>>>
        #{File.new('src/test/resources/multiline.txt').read.match('Line 9(.*)')}
        <<<<

        #{assert.contains 'Line'+' 9 of multiline.txt', 
        'Can use regexp to include parts of content'}
        */
    }
}
