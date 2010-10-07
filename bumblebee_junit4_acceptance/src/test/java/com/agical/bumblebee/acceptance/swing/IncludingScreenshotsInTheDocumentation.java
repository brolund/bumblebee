package com.agical.bumblebee.acceptance.swing;

import java.awt.Rectangle;

import org.junit.After;
import org.junit.Test;

import com.agical.bumblebee.acceptance.swing.application.Application;
import com.agical.bumblebee.swing.Picter;

public class IncludingScreenshotsInTheDocumentation {
    /*!!
    Having pictures in the documentation makes it so much nicer to read. 
    On the other hand, updating the pictures every time you change something
    is a true pain. 
    
    By using Bumblebee to generate the documentation you will 
    get away from that pain. Some goodies in the DSL will also help you do things 
    you wouldn't even consider if you used manual screen shots. 
    
    Let's stop talking and start walking.
    */

    private Application application;
    
    @After
    public void stopApplication() {
        application.stop();
    }

    @Test
    public void gettingAShotOfYourApplication() throws Exception {
        /*!
        First, lets create a Swing application:
        */
        application = new Application();
        /*!
        Feed that frame (or actually any =javax.swing.Component=) to the =Picter= shot method:
        */
        Picter picter = new Picter();
        picter
            .getAppShot(application.getFrame())
            .writeAs("fullApp");
        /*!
        This is the shot written:
        
        #{fullApp}
        
        This whole section output is created from a test method that looks like this: 
        >>>>
        #{meth}
        <<<<
        Note that the link reference cannot collide with any (J)Ruby keyword, nor can it collide with 
        any Bumblebee configuration parameter or snippet DSL names, e.g. =clazz= or =meth=.
        If you get strange results, change the name, preferrably to something more explicit.
        
        The application used in these examples looks 
        [[#{clazz('com.agical.bumblebee.acceptance.swing.application.Application').source_link}][like this]].
        */
    }

    @Test
    public void highlightingPartsOfTheShot() throws Exception {
        application = new Application();
        Picter picter = new Picter();
        /*!
        Oftentimes you want to highlight a part of the screen. This is how 
        you do it:
        */
        picter
            .getAppShot(application.getFrame())
            .highlight(new Rectangle(30, 40, 100, 120))
            .writeAs("highlightedApp");
        /*!
        This is the result:
        
        #{highlightedApp}
        
        Hmm, that's impressive, but not too useful. We should probably use the coordinates from 
        the Swing components to highlight cohesive parts. Here we use the reference to the 
        form component of the application.
        */
        picter
            .getAppShot(application.getFrame())
            .highlight(application.getForm())
            .writeAs("highlightedFromComponent");
        /*!
        #{highlightedFromComponent}
        
        Now, that looks better! 
        
        Here, we used the strategy of explicitly exposing relevant 
        components in the application. Another strategy is to traverse the Swing/AWT component
        hierarchy from a root component until you find the component of interest. 
        */
    }
    
    @Test
    public void cropping() throws Exception {
        application = new Application();
        Picter picter = new Picter();
        /*!
        Every now and then you want to crop out and show only a part of the 
        application. Here's how you do that
        */
        picter
            .getAppShot(application.getFrame())
            .crop(application.getForm())
            .writeAs("croppedForm");
        /*!
        This is the result:
        
        #{croppedForm}
        */
    }
    
    @Test
    public void callouts() throws Exception {
        application = new Application();
        Picter picter = new Picter();
        /*!
        When you want to add detailed information in a screenshot, you can 
        add callouts to describe a flow or just different parts in the picture.
        */
        picter
            .getAppShot(application.getFrame())
            .callout(application.getNameLabel(), "The Name field", "nameField")
            .callout(application.getEmailLabel(), "The Email field", "emailField")
            .callout(application.getPhoneLabel(), "The Phone field", "phoneField")
            .crop(application.getForm())
            .writeAs("calloutForm");
        /*!
        This is the result:
        
        #{calloutForm}
        
        #{nameField}
        
        #{emailField}
        
        #{phoneField}
        
        You can get the details from each reference:
        Reference||Output||Description
        #{'#'}{nameField.icon} | #{nameField.icon} | The icon in the image
        #{'#'}{nameField.text} | #{nameField.text} | The text
        #{'#'}{nameField.nr} | #{nameField.nr} | The number of the text
        #{'#'}{nameField} | #{nameField} | The icon and text in one

        This method looks like this:
        >>>>
        #{meth}
        <<<<
        */
    }
    
    
}
