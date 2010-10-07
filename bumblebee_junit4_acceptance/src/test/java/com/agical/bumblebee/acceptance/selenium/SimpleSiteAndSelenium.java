package com.agical.bumblebee.acceptance.selenium;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.server.SeleniumServer;

import com.agical.bumblebee.acceptance.selenium.site.JettyController;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumLogLevels;

public class SimpleSiteAndSelenium {
    
    private static JettyController jettyController;
    protected static Selenium selenium;
    private static SeleniumServer seleniumServer;

    @BeforeClass
    public static void startJetty() {
        jettyController = JettyController.createFrom(new File(new File("."), "src/test/resources/site"));
        jettyController.start();
    }

    @BeforeClass
    public static void startSelenium() throws Exception {
        seleniumServer = new SeleniumServer();
        seleniumServer.start();
        selenium = new DefaultSelenium("localhost", seleniumServer.getPort(), "*firefox", "http://localhost:8080");
        selenium.start();
    }

    @AfterClass
    public static void stopJetty() {
        jettyController.stop();
    }

    @AfterClass
    public static void stopSelenium() {
        selenium.stop();
        seleniumServer.stop();
    }

    public SimpleSiteAndSelenium() {
        super();
    }
    
}