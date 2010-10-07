package com.agical.bumblebee.swing;

import java.awt.Component;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.agical.bumblebee.imaging.Callout;
import com.agical.bumblebee.imaging.SwingImaging;

public class Picter {

    public AppShot getAppShot(Component frame) {
        return new AppShot(
                SwingImaging.usingScreenShot().create(frame), 
                new ArrayList<Callout>(), 
                new ArrayList<Rectangle>(), 
                frame);
    }

}
