package com.agical.bumblebee.acceptance.helpers.extension;

import org.junit.Test;


public class AddRubyExtensions {
    
    @Test
    public void executeRubyCodeDirectlyInComment() throws Exception {
        /*!m1
        Now consider the following method:
        >>>>
        #{meth}
        <<<<

        The comments are string templates that get executed in Ruby. Therefore 
        most Ruby string features can be used within the comment. In this case 
        we output the numbers 0 through 9.

        *#{s = "";10.times {|i| s+=i.to_s};s;}*
        
        This is often rather awkward, and one of the extension suggestions below will 
        probably be a better option when you want to execute more than the Ruby 
        code that comes with Bumblebee.
        #{assert.contains '0123456789', 'Outputs results of Ruby evaluation'}
        */
    }
    


    @Test
    public void addAScriptInTheCommentUsingRequire() throws Exception {
        /*!m1
        #{require('com/agical/bumblebee/acceptance/helpers/extension');''}

        To include a script, just use the Ruby =require= statement.
        >>>>
        #{meth}
        <<<<
        Since the require method returns =true= we have to prevent that from making it to the output
        by adding a =;''= 

        The file is named =extension.rb= and looks like this:
        >>>>
        #{File.new('src/test/resources/com/agical/bumblebee/acceptance/helpers/extension.rb').read}
        <<<<

        The method of the extension script can be invoked like any other method, 
        in this case the output is: =#{extension_method}=
        
        #{assert.contains 'result of extension method', 'Can use extension methods'}
        */
    }
    
    @Test
    public void executeAScriptFromACommentAndIncludeResult() throws Exception {
        /*!m1
        Now we want to run the following Ruby script (i.e. program) 
        and include the result in the documentation:
        >>>>
        #{File.new('src/test/resources/com/agical/bumblebee/acceptance/helpers/evaluate.rb').read}
        <<<<
        We just invoke Ruby's =instance_eval= on the contents of a from a file and get the result:
        
        #{instance_eval(File.new('src/test/resources/com/agical/bumblebee/acceptance/helpers/evaluate.rb').read)}

        This method looks like this: 
        >>>>
        #{meth}
        <<<<
        
        #{assert.contains( 'The result of script evaluation: 4711', 
        'Can evaluate scripts and include results')}
        */
    }
    
    
}
