package com.agical.bumblebee.swing;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import com.agical.bumblebee.imaging.Callout;
import com.agical.bumblebee.imaging.Shot;

public class AppShot extends Shot{

    private final Component outerComponent;

    public AppShot(BufferedImage image, List<Callout> callouts, List<Rectangle> highlights, Component outerComponent) {
        super(image, callouts, highlights);
        this.outerComponent = outerComponent;
    }

    public AppShot highlight(Component component) {
        return (AppShot) highlight(bounds(component));
    }

    public Rectangle bounds(Component component) {
        Rectangle bounds = component.getBounds();
        Point locationOnScreen = component.getLocationOnScreen();
        Point outerLocationOnScreen = outerComponent.getLocationOnScreen();
        bounds.x = locationOnScreen.x-outerLocationOnScreen.x;
        bounds.y = locationOnScreen.y-outerLocationOnScreen.y;
        return bounds;
    }

    public Shot crop(Component form) {
        return crop(bounds(form));
    }

    public AppShot callout(Component component, String text, String annotationReference) {
        return (AppShot)callout(bounds(component), text, annotationReference);
    }


}
