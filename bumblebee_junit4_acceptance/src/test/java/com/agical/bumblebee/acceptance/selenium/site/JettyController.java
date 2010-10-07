package com.agical.bumblebee.acceptance.selenium.site;

import java.io.File;

import org.mortbay.http.HttpContext;
import org.mortbay.http.HttpServer;
import org.mortbay.util.InetAddrPort;
import org.openqa.selenium.server.FsResourceLocator;
import org.openqa.selenium.server.StaticContentHandler;

public class JettyController {

    private final File dir;
    private HttpServer httpServer;

    public static JettyController createFrom(File dir) {
        return new JettyController(dir);
    }
    
    public JettyController(File dir) {
        this.dir = dir;
    }

    public void start() {
        try {
            httpServer = new HttpServer();
            httpServer.addListener(new InetAddrPort(8080));
            HttpContext context = httpServer.addContext("/");
            StaticContentHandler staticContentHandler = new StaticContentHandler("http:s//localhost:45628");
            staticContentHandler.addStaticContent(new FsResourceLocator(dir));
            context.addHandler(staticContentHandler); 
            httpServer.start();
        } catch (Exception e) {
            throw new RuntimeException( "", e );
        }
    }

    public void stop() {
        try {
            httpServer.stop();
        } catch (Exception e) {
            throw new RuntimeException( "", e );
        }
    }
    
}
