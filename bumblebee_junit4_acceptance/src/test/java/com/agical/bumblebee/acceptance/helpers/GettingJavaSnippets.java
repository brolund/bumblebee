package com.agical.bumblebee.acceptance.helpers;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class GettingJavaSnippets {
    /*!!
    ** The Java snippet extraction DSL
    The DLS helps extracting data from various sources of information. 
    \"Out-of-the-box\" it currently supports
    getting various parts of source code, but since it is written in Ruby it 
    can be easily extended at 
    any level to customize functionality.
    This section focuses on getting Java-code into your documentation. 
    
    */

    @Test
    public void autoSnippeting() throws Exception {
        String thisIsOutsideTheFirstComment = "and will not be included";
        /*!
        Bumblebee will include as snippets any code in the test methods that resides between 
        comments in a test case (new feature from 1.0.2). The resulting document will more 
        resemble the code you write. 
        
        The whole idea is to get a more natural flow in your tests, where you first 
        write some comments...
        */
        String thenSomeCode = "that automatically gets includes as snippet";
        /*!
        ...and then some more comments... 
        */
        List<String> andThenMoreCode = new ArrayList<String>();
        /*!
        ...and the last comment in the method marks the end of what will be included. In this 
        case it is just an empy comment.
        This method looks like this:
        >>>>
        #{meth}
        <<<<
        Here comes the last part:
        */
        String troublesomeString = "&<>#{}\n";
        /*!*/
        String outsideTheLastComment = "since it is after the last comment";
    }

    @Test
    public void includeTheCurrentlyExecutingMethod() throws Exception {
        /*!
        This is how to include the current method using the DSL:
        >>>>
        #{meth}
        <<<<
        
        =meth= represents the currently executing method.
        
        #{assert.contains 'public'+' void includeTheCurrentlyExecutingMethod', 
        'Can include current method'}
        */
    }
    public void demonstratingUsingMethodMissing() {
        // Demo method
    }
    public void explicitlyGettingMethod() {
        // Demo method
    }
    @Test
    public void includeOtherMethodFromCurrentClass() throws Exception {
        /*!
        It is possible to include other methods from the currently executing class 
        by using the =clazz= object. This is what the output looks like:
        >>>>
        #{clazz.demonstratingUsingMethodMissing}
        <<<<
        ...and this is how it is done:
        >>>>
        #{meth}
        <<<<
        This feature uses the *method_missing* feature in Ruby, i.e. when the 
        =clazz= object gets the call to =demonstratingUsingMethodMissing= 
        and no such method is found, it assumes that the call is intended to 
        return the method object of the specified =clazz= with the name 
        =demonstratingUsingMethodMissing= and returns it. 

        #{assert.contains 'public'+' void demonstratingUsingMethodMissing', 
        'Can include method from clazz object using method_missing'}
        */
    }
    
    @Test
    public void avoidNamingCollisionWhenIncludingMethod() throws Exception {
        /*!
        Sometimes there is a naming collision between the methods on the Ruby 
        class and the ones in the Java class, and the above approach won't work. 
        In those cases you can use the more explicit form =clazz.meth 'methodName'=:
        >>>>
        #{meth}
        <<<<
        The output will be similar to that of the previous example:
        >>>>
        #{clazz.meth('explicitlyGettingMethod')}
        <<<<
        #{assert.contains 'public'+' void explicitlyGettingMethod', 
        'Can include method from clazz with explicit method'}
        */
    }

    @Test
    public void inlineEntireSourceFile() throws Exception {
        /*!m1
        It is possible to include an entire file in the result:
        >>>>
        #{clazz('com.agical.bumblebee.acceptance.helpers.ShortClass')}
        <<<<
        This is how it is done:
        >>>>
        #{meth}
        <<<<
        Note that it is the same =clazz= method, and this time with a parameter telling which 
        class to represent. 
        
        #{assert.contains 'public'+' class ShortClass', 'Can include entire source file'}
        */
    }
    @Test
    public void linkToSourceFile() throws Exception {
        /*!m1
        An alternative is to link to the file:
        
        [[#{clazz('com.agical.bumblebee.acceptance.helpers.ShortClass').source_link}][Link to file]]
        
        This is how it is done:
        >>>>
        #{meth}
        <<<<
        =source_link= will cause Bumblebee to paste the class source into an HTML file and return 
        the link to it.
        
        #{assert.contains 'ShortClass.java', 'Can make link to file'}
        */
    }
    @Test
    public void getCodeFromMethodInOtherClass() throws Exception {
        /*!m1
        Now consider the following method:
        >>>>
        #{meth}
        <<<<
        The =clazz('package.ClassName').methodName= method helps you retrieve methods 
        from other classes:
        >>>>
        #{clazz('com.agical.bumblebee.acceptance.helpers.MethodTester').testingMethod}
        <<<<
        #{assert.contains 'public'+' void testingMethod', 'Can include other methods'}
        */
    }
    @Test
    public void smallerCodeSnippetsFromOtherClassesOrMethods() throws Exception {
        /*!
        By adding Bumblebee comments in other methods or classes than the current you can slice out
        interesting parts without relying on e.g. line numbers and such.
        Consider:
        >>>>
        #{clazz.methodWithComments}
        <<<<
        =#{'#'}{clazz.methodWithComments.from.m1.to.m2}= retrives the code between the comments with the 
        markers (=m1= and =m2= in this case) and the result is:
        >>>>
        #{clazz.methodWithComments.from.m1.to.m2}
        <<<<
        Note that the last variable is not in this snippet.
        Note also that there can be several comments in one file, but the comments can be 
        empty (contain only the marker) to serve as markers
        #{assert.contains 'String '+'includedCode', 'Can include smaller code snippets from other methods'}
        */
        boolean thisVariableWillNotBeInTheFirstSnippet = true; 
    }

    public void methodWithComments() {
        String codeBeforeIncludedPart = "";
        /*!m1*/
        String includedCode = "";
        /*!m2*/
        String codeAfterIncludedPart = "";
    }

    public void methodWithSignature(String s, int i) {
        // Demo method String, int
        System.out.println(s + i);
    }
    public void methodWithSignature(Integer i, String s) {
        // Demo method Integer, String
        System.out.println(s + i);
    }
    public void methodWithSignature() {
        // Demo method without arguments
    }
    public void methodNotOverloadedButWithParameters(Integer i, String s) {
        // Method with arguments that is included without having to specify parameters
        System.out.println(s + i);
    }
    @Test
    public void includeVariableCurrentClass() throws Exception {
        /*!
        It is possible to include a variable declaration in almost the same way as 
        methods:
        >>>>
        #{clazz.variable('runnable')}
        <<<<
        ...and this is how it is done:
        >>>>
        #{meth}
        <<<<
        #{assert.contains 'new'+' Runnable', 'Can include variable'}
        */
    }
    
    private Runnable runnable = new Runnable() {
        public void run() {
            // do stuff
        }
    };

    @Test
    public void includeOverloadedMethod() throws Exception {
        /*!m1
        When the method to be included is available with several signatures you need to provide 
        the signature to select what method to use, to produce something like this:
        >>>>
        #{clazz.methodWithSignature('java.lang.String', 'int')}
        <<<<
        We use the current class here, but it can be done for any class by specifying the desired 
        class as argument to the =clazz= method.
        
        This is what the method that produced this section looks like:
        >>>>
        #{meth}
        <<<<
        #{assert.contains 'Demo'+' method String, int', 
        'Can include methods with overloaded signatures'}
        */
    }
    @Test
    public void uniqueMethodName() throws Exception {
        /*!m1
        If the method you'd like to include has arguments, but isn't overloaded, then you don't 
        need to specify the arguments:
        >>>>
        #{clazz.methodNotOverloadedButWithParameters}
        <<<<
        This is generally something that is nice since specifying all those parameters can be 
        pretty tiring.
        
        This is what the method looks like:
        >>>>
        #{meth}
        <<<<
        #{assert.contains('public'+' void methodNotOverloadedButWithParameters', 
        'Can include methods with signature without specifying its arguments')}
        */
    }
    @Test
    public void getCodeFromInnerClass() throws Exception {
        /*!m1
        Inner classes are more difficult to parse, and cannot be used in the same way as 
        normal classes (at the moment). What can be done is to simply extract the class using a regular 
        expression and some comment markers:
        >>>>
        #{meth}
        <<<<
        For the entire class:
        >>>>
        #{clazz.to_s.match(Regexp.new('//Start'+'tag(.*)//Endtag', Regexp::MULTILINE))[1]}
        <<<<
        #{assert.contains 'public class InnerClass', 'Can include inner classes'}
        */
    }
    
    //Starttag
    public class InnerClass {
        public void someMethod() {
            String doing = "something";
        }
    }
    //Endtag

    
}
