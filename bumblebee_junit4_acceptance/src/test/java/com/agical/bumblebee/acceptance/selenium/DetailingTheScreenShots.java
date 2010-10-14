package com.agical.bumblebee.acceptance.selenium;

import org.junit.Ignore;
import org.junit.Test;

import com.agical.bumblebee.selenium.Picter;


public class DetailingTheScreenShots extends SimpleSiteAndSelenium {
    /*!!
    Bumblebee also allow you to put more detailed descriptions in your snapshots.  
    */
    @Test
    public void usingCallouts() throws Exception {
        selenium.open("index.html");
        Picter picter = new Picter();
        /*!
        By adding call-outs to a picture, you can explain several coherent parts in the 
        same image, or a flow of events. 
        */
        picter.browserShot(selenium)
            .callout("xpath=//h1", "The header", "headerRef")
            .callout("bodytable", "The body table", "bodytableRef")
            .callout("home", "The link to the first page", "homeRef")
            .callout("something", "The link to something", "somethingRef")
            .callout("nothing", "The link to nothing", "nothingRef")
            .callout("xpath=//p", "The text", "textRef")
            .crop("bodytable")
            .writeAs("annotated");
        /*!
        #{annotated}
        
        #{headerRef}

        #{bodytableRef}

        #{homeRef}

        #{somethingRef}

        #{nothingRef}

        #{textRef}

        Each call-out will be added as a numbered balloon just outside of the image,
        with a line to the element referenced. The call-out will be available to use in
        the text, e.g:
        
        Reference||Output||Description
        #{'#'}{headerRef.icon} | #{headerRef.icon} | The icon in the image
        #{'#'}{headerRef.text} | #{headerRef.text} | The text
        #{'#'}{headerRef.nr} | #{headerRef.nr} | The number of the text
        #{'#'}{headerRef} | #{headerRef} | The icon and text in one
        
        This method looks like this:
        >>>>
        #{meth}
        <<<<
        */
    }
}