package com.agical.bumblebee.selenium;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agical.bumblebee.imaging.Callout;
import com.agical.bumblebee.imaging.Shot;
import com.thoughtworks.selenium.Selenium;

/**
 * This class represents a full browser shot, which is a special case of  the 
 * Shot that can crop out HTML elements from the image.
 */
public class BrowserShot extends Shot {
    private static class NavigationSpace {
        int top;
        int bottom;
        public NavigationSpace(int top, int bottom) {
            this.top = top;
            this.bottom = bottom;
        }
    }
    private final Selenium selenium;
    private final Map<String, NavigationSpace> windowIdToNavigationSpace = new HashMap<String, NavigationSpace>();
    public BrowserShot(Selenium selenium, BufferedImage browserShot) {
        this(selenium, browserShot, new ArrayList<Callout>(), new ArrayList<Rectangle>());
    }
    
    protected BrowserShot(Selenium selenium, BufferedImage browserShot, List<Callout> callouts, List<Rectangle> highlights) {
        super(browserShot, callouts, highlights);
        this.selenium = selenium;
    }

    /**
     * Crops a screen-shot to the HTML element defined by the 
     * seleniumLocator. This method will also add any callouts
     * registered, hence this is not a pure crop method 
     * defined by the {@link annotate} method
     * @param seleniumLocator
     * @return the cropped image
     */
    public Shot crop(final String seleniumLocator) {
        return crop(getRectanglesForElements(seleniumLocator)[0]);
    }

    private Rectangle[] getRectanglesForElements(final String... seleniumLocators) {
        Rectangle[] rectangles = new Rectangle[seleniumLocators.length];
        int count = 0;
        for (String seleniumLocator : seleniumLocators) {
            rectangles[count] = new Rectangle(selenium.getElementPositionLeft(seleniumLocator).intValue(), selenium
                    .getElementPositionTop(seleniumLocator).intValue()
                    + getNavigationSpace(selenium).top, selenium.getElementWidth(seleniumLocator).intValue(), selenium
                    .getElementHeight(seleniumLocator).intValue());
            count++;
        }
        return rectangles;
    }
    
    private NavigationSpace getNavigationSpace(Selenium selenium) {
        String windowId = selenium.getEval("'' + window.location;");
        NavigationSpace navigationSpace = windowIdToNavigationSpace.get(windowId);
        if(navigationSpace==null) {
            String navigationAndFooterHeights = selenium
                    .getEval("win = window.open('url', 'windowname', 'height=200,width=200,status=0,toolbar=0,menubar=0,resizable=0,scrollbars=0'); \r\n"
                            + "bottomBarHeight = win.outerHeight-win.innerHeight;\r\n"
                            + "topNavigationHeight = window.outerHeight-window.innerHeight-bottomBarHeight;\r\n"
                            + "win.close();\r\n" + "topNavigationHeight+','+bottomBarHeight;\r\n" + "");
            String[] spaces = navigationAndFooterHeights.split(",");
            navigationSpace = new NavigationSpace(Integer.parseInt(spaces[0]), Integer.parseInt(spaces[1]));
            windowIdToNavigationSpace.put(windowId, navigationSpace);
            System.out.println("Added: " + windowId);
        }
        return navigationSpace;
    }
    
    
    public BrowserShot highlight(String... seleniumLocators) {
        highlight(getRectanglesForElements(seleniumLocators));
        return this;
    }

    public BrowserShot callout(String seleniumLocator, String text, String annotationReference) {
        Rectangle location = getRectanglesForElements(seleniumLocator)[0];
        callout(location, text, annotationReference);
        return this;
    }

    public BrowserShot callout(Rectangle location, String text, String annotationReference) {
        return (BrowserShot) super.callout(location, text, annotationReference);
    }
    
    public BrowserShot highlight(Rectangle... rectanglesForElements) {
        return (BrowserShot) super.highlight(rectanglesForElements);
    }
}
