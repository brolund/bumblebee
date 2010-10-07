package com.agical.bumblebee.acceptance.helpers;

import static com.agical.bumblebee.junit4.Storage.store;

import org.junit.Test;


public class UsingRuntimeData {

    @Test
    public void passingRuntimeDataToUseInDocumentation() throws Exception {
        /*!m1
        It is possible to pass runtime variables to use in the method comments. The data is 
        inlined in the text when called. This is the an example where direct parameter 
        access is used, i.e. the =toString()= method is called rendering: 

        *#{myKey2}*
    
        This method looks like this:
        >>>>
        #{meth}
        <<<<
        =store= is a static method on the class =Storage=, and it is a good candidate for a 
        static import.
          
        #{assert.contains 'Result'+' of toString method from MyObject', 
            'Can include runtime variables'}
         */
        store("myKey2", new MyObject("The text"));
    }
        
    @Test
    public void rulesAndLimitationsForKeysAndValues() throws Exception {
        /*!m1
        The data stored must implement =java.io.Serializable=. This is largely a measure to enable storing 
        data on disk if needed without breaking existing code.
        
        To use direct access to the stored data, keys must conform to the 
        [[http://www.ruby-doc.org/core/classes/Symbol.html][Ruby symbol syntax]], 
        and also be accessed in such way.
        
        If you would like to store data with other key strings, e.g.  =\'#{get_value('non-symbol'+' key:')}\'= you can 
        use the accessor method =#{'#'}{get_value(key)}=.
        #{assert.contains 'non-symbol'+' key:', 'Can use get_value to retrieve stored data'}
        */
        String keyAndValue  = "non-symbol key:";
        store( keyAndValue, keyAndValue);
    }
    @Test
    public void callingMethodsOnStoredObjects() throws Exception {
        /*!m1
        The nice thing with JRuby is that you can call the method on the 
        Java objects you store from within your Ruby code. This method looks like this:
        >>>>
        #{meth}
        <<<<
        and we can get the =getContainedData()= from the =MyObject=:  *#{myKey2.getContainedData()}*
        
        MyObject looks like this:
        >>>>
        #{clazz('com.agical.bumblebee.acceptance.helpers.MyObject')}
        <<<<

        JRuby has the feature that it converts Java-style method names to Ruby-style, and in this case we
        could have called 
        
        =contained_data= 
        
        just as well as 
        
        =getContainedData()= 
        
        However, since it 
        can be rather confusing mixing Ruby and Java code, it is recommended to call included 
        Java objects by their Java names.
        
        #{assert.contains 'The'+' text', 'Can call methods on runtime variables'}
         */
       store("myKey2", new MyObject("The" + " text"));
    }
    
}
