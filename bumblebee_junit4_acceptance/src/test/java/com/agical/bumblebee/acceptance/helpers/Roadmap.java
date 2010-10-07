package com.agical.bumblebee.acceptance.helpers;

import org.junit.Test;


public class Roadmap {
    /*!!
    This section contains the history and the backlog of Bumblebee. 

    Bumblebee is starting to shake in place, and the documented features can be considered rather stable.
    User input will have a big say in the future development, and feedback from usage of Bumblebee 
    is much appreciated in order to make it a better product. 
    I will do my best to add any features requested to Bumblebee, or help you accomplish what you want in 
    some other way.
    
    You can email the Bumblebee Google group at bumblebee-tool@googlegroups.com or join it at  
    [[http://groups.google.com/group/bumblebee-tool][Bumblebee group]] and start a 
    discussion there.
    */
    @Test
    public void releases() throws Exception {
        /*!
        #{File.new('releases.txt').read}
        */
    }
    @Test
    public void backlog() throws Exception {
        /*!
        #{File.new('backlog.txt').read}
        */
    }
    @Test
    public void background() throws Exception {
        /*!
        #{File.new('background.txt').read}
        */
    }
}
