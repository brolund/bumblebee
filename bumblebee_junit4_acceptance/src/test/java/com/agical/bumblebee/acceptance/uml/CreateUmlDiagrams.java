package com.agical.bumblebee.acceptance.uml;

import org.junit.Test;

import com.agical.bumblebee.junit4.Storage;

public class CreateUmlDiagrams {
    /*!!
    Bumblebee has some functionality for creating UML diagrams, demonstrated below.
    The interface is intended to be as lightweight, yet helpful, as possible.
    
    On a technical note, Bumblebee integrates Umlspeed for creating UML diagrams.
    Umlspeed creates UML diagrams in the SVG format from a proprietary textual description. 
    The SVG is transformed into a PNG using Apache Batik. The implementation of Umlspeed 
    is a bit awkward to use, hence the choice of Umlspeed might be changed in the future, but the 
    Bumblebee interface is likely to remain.
    
    The jars for Umlspeed and Batik are not included in the =bumblebee-core= jar. You need to 
    add the following;
    >>>>
    #{require '../dependencies';''}
    #{UMLSPEED_NAME}
    #{BATIK.map {|dep| dep.concat(0x0a)}}
    <<<<
    Umlspeed is not in any maven repository, but it is included in the =bumblebee-all-x.y.z.zip= 
    file.
    */
    
    @Test
    public void drawAClassDiagramOfClassesInAMethod() throws Exception {
        /*!
        Suppose you have some code like this in a method, for instance a test method: 
        */
        SuperClass objectUnderTest = new CentralObject();
        /*!
        Now you want to create a UML diagram like this:
        #{uml.classdiagram.focused_on('com.agical.bumblebee.acceptance.uml.CentralObject')}
        This is how you do it:
        >>>>
        #{meth}
        <<<<
        */
    }
    @Test
    public void drawAClassDiagramOfSpecificClasses() throws Exception {
        /*!
        If you want to include some specific classes in a class diagram, do like this:
        >>>>
        #{meth}
        <<<<
        and the result is this:  
        #{uml.classdiagram.for_all(classesForDiagram)}
        The focus will be on the first class in the provided array.
        */
        Storage.store("classesForDiagram", new Class<?>[] {
                CentralObject.class, 
                DependentObject.class, 
                SuperClass.class,
                CreateUmlDiagrams.class});
    }
}
