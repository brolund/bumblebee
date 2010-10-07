package com.agical.bumblebee.selenium;

import java.awt.Rectangle;

import com.agical.bumblebee.imaging.Imaging;
import com.thoughtworks.selenium.Selenium;

public class Picter {

    private Rectangle getBrowserArea(Selenium selenium) {
        int width = Integer.parseInt(selenium.getEval("window.outerWidth"));
        int height = Integer.parseInt(selenium.getEval("window.outerHeight"));
        int x = Integer.parseInt(selenium.getEval("window.screenX"));
        int y = Integer.parseInt(selenium.getEval("window.screenY"));
        return new Rectangle(x, y, width, height);
    }

    public BrowserShot browserShot(Selenium selenium) {
        return new BrowserShot(selenium, Imaging.getScreenPart(getBrowserArea(selenium)));
    }

}
