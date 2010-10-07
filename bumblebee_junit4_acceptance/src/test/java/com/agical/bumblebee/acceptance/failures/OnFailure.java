package com.agical.bumblebee.acceptance.failures;

import static org.junit.Assert.fail;

import org.junit.Test;


public class OnFailure {
    @Test
    public void gettingExceptionInformation() throws Exception {
        /*!
        This is a failing method. By failure we mean that it throws an exception. On failure, there are some extra extension points
        to get failure information and alter the report.

        In addition to other methods on the =execution=, when there is a failure the exception is also available. 
        
        This is the result from the Java =Throwable= in this methods failure:
        >>>>
        #{execution.getThrowable().getMessage()}
        #{execution.getMethod().getName()}
        <<<<
        This is how it was done:
        >>>>
        #{meth}
        <<<<
        #{assert.contains 'This'+' is the deliberate failure message', 'Failing method can include failure message'}
        */
        fail( "This is the"+" deliberate failure message");
    }
    
    @Test
    public void executeCodeOnlyWhenAMethodThrowsException() throws Exception {
        /*!
        *** Experimental
        To avoid conditionals when trying to decide when a method fails, there is a hook to use on 
        the execution object, =on_exception=. This method takes a closure that is invoked on failure, 
        in this case the exception message, and the return value of the closure is used in its place:
        >>>>
        #{execution.on_exception {execution.getThrowable().getMessage()}}
        <<<<
        This is how it is done:
        >>>>
        #{meth}
        <<<<
        #{execution.on_completion {'This success'+' message must not be in the result'}}

        #{assert.contains 'Another'+' deliberate failure', 'Can use closure on failure'}
        #{assert.not_contains 'This'+' success message must not be in the result', 'Completion closure is not included'}
        */
        fail("Another"+ " deliberate failure");
    }
    
    @Test
    public void executeOnlyWhenAMethodCompletesNormally() throws Exception {
        /*!
         *** Experimental         
         There is also a hook for normal completion, =on_completion=, that is invoked for 
         non-exceptional completions, in this case returning a plain string:
         >>>>
         #{execution.on_completion {'This'+' success message must be in the result'}}
         <<<<
         This is the code that makes it happend:
         >>>>
         #{meth}
         <<<<
        
         #{execution.on_exception {'This'+' failure message must not be in the result'}}

         #{assert.contains 'This success message'+' must be in the result', 'Completion closure is invoked'}
         #{assert.not_contains 'This failure message'+' must not be in the result', 'Exception closure is not invoked'}
         */
    }
}