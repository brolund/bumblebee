package com.agical.bumblebee.acceptance.selenium;

import java.awt.Rectangle;

import org.junit.Ignore;
import org.junit.Test;

import com.agical.bumblebee.selenium.Picter;



public class IncludingScreenshotsInTheDocumentation extends SimpleSiteAndSelenium {
    @Test
    public void gettingASnapshotOfATestedSite() throws Exception {
        /*!
        First, you need to start the site you are testing. I hope you know how to do that. 
        
        For this example we use a simple, static site that runs in JettyController. 
        It has one main page and two sub pages.
        
        Secondly, you need to setup Selenium:
        >>>>
        #{clazz('com.agical.bumblebee.acceptance.selenium.SimpleSiteAndSelenium').startSelenium}
        <<<<
        Then, feed the =selenium= variable to the DSL root-class =Picter=. This integration 
        will use selenium to extract portions of a page, write it to file and create a link for 
        you to use in the text. 
        Do whatever you want to do with selenium, in this case we open the index.html, and then 
        start working with the DSL.
        */
        selenium.open("index.html");
        Picter picter = new Picter();
        picter.browserShot(selenium).writeAs("linkReference");
        /*!
        This will retrieve the full browser window in an image on disk. 
        You can link it with =#{'#'}{linkReference}=, i.e. the same name that you provided
        the method. This is the result:

        #{linkReference} 
        
        Note that the link reference cannot collide with any (J)Ruby keyword, nor can it collide with 
        any Bumblebee configuration parameter or snippet DSL names, e.g. =clazz= or =meth=.
        If you get strange results, change the name, preferrably to something more explicit.
        
        The method that produced this looks like this:
        >>>>
        #{meth}
        <<<<
        */
    }

    @Test
    public void intelligentCropping() throws Exception {
        /*!
        As you probably saw, the image in the first example was huge. Most of the time 
        you are only interested in a portion of the page, i.e. you want to crop the page:
        */
        selenium.open("index.html");
        Picter picter = new Picter();
        picter.browserShot(selenium).crop(new Rectangle(40, 40, 200, 200)).writeAs("staticallyCropped");
        /*!
        This is the result:
        
        #{staticallyCropped}
        
        As you can see, just cropping an image with absolute coordinates is pretty tricky. It is also not very tolerant to 
        changes in the site layout and design. We would like those coordinates to be extracted from the elements in the
        page instead:
        */
        picter.browserShot(selenium).crop("menu").writeAs("croppedFromPageCoordinates");
        /*!
        #{croppedFromPageCoordinates}
        
        You can use any [[http://seleniumhq.org/projects/core/reference.html][Selenium locators]] in the =crop= method: 
        */
        picter.browserShot(selenium).crop("xpath=//h1").writeAs("headerFromXPath");
        /*!
        ...resulting in:
        
        #{headerFromXPath}
        
        Note that using the Selenium locators can only be used on the =BrowserShot= class, since they rely on
        the positionings in the browser.
        */
    }  
    
    @Test
    public void highlightingAndCropping() throws Exception {
        selenium.open("index.html");
        Picter picter = new Picter();
        /*!
        Sometimes you want to highlight parts of the picture and crop a larger part:
        */
        picter.browserShot(selenium)
            .highlight("menu", "xpath=//h1")
            .highlight("xpath=//p")
            .crop("bodytable")
            .writeAs("highlighted");
        /*!
        =highlight(...)= will take one or several Selenium locators and highlight them in the image:
        
        #{highlighted}
        
        The =highlight(...)= method will return a =BrowserShot= object, hence it is still possible to 
        manipulate it based on Selenium locators, e.g. cropping out *bodytable*. 
        */
    }
}