package com.agical.bumblebee.acceptance.helpers.experimental;

import static com.agical.bumblebee.junit4.Storage.store;

import org.junit.Test;

import com.agical.bumblebee.acceptance.Tools;
import com.agical.bumblebee.acceptance.failures.FailingSuite;

public class InvocationInformation {
    /*!!
    Invocation information is information regarding the method 
    execution time and what method was called, and whether it 
    returned normally or with an exception.
    
    For the JUnit case this is sufficient, but if you would like to 
    use Bumblebee in a more generic situation, there are some more 
    data for those cases.
    */

    @Test
    public void getExecutionMetaInformation() throws Exception {
        /*!
        #{current_execution=execution.getMethod();''}
        #{parent_class=parent.execution.getExecutingClass();''}
        #{grand_parent_class=parent.parent.execution.getExecutingClass();''}
        #{great_grand_parent_class=parent.parent.parent.execution.getExecutingClass();''}

        This example shows how to retrieve different kinds of meta information
        around the execution of a certain method.
        
        Member || time[ms]
        #{execution.getMethod().getName()} | #{execution.getExecutionTime()}
        #{parent_class.getSimpleName()} | #{parent.execution.getExecutionTime()}
        #{grand_parent_class.getSimpleName()}| #{parent.parent.execution.getExecutionTime()}
        #{great_grand_parent_class.getSimpleName()}| #{parent.parent.parent.execution.getExecutionTime()}
        
        How it is done is shown here:
        >>>>
        #{meth}
        <<<<
        Here we make use of a new feature, to add arbitrary parameters to a 
        node (=current_execution= etc). 
        
        The =execution= variable is holding the current execution information. 
        On a class/suite there is the java =Class= and the local execution time. 
        For the methods there is the java =Method= object and the execution time, 
        as shown above.
        
        */
        Thread.sleep(20);
    }
    
    @Test
    public void gettingFailureInformation() throws Exception {
        /*!
        #{exec run_test_command;''}
        When a test fails there is additional information on the  =execution=
        variable. 

        An example of a failing suite can be found [[failure.html][here]]
        */
        
        String command = Tools.createJUnitTestCommand("target/site/failure_result.txt", FailingSuite.class);
        store("run_test_command", command);
    }

}
