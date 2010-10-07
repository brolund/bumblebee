/**
 * 
 */
package com.agical.bumblebee.imaging;

import java.awt.Rectangle;
import java.io.Serializable;

public class Callout implements Serializable {
    private static final long serialVersionUID = -4397766567052129943L;

    public Callout() {
        super();
    }
    private int nr;
    private String text;
    private String icon;
    private Rectangle location;

    public int getNr() {
        return nr;
    }
    public String getText() {
        return text;
    }
    public String getIcon() {
        return icon;
    }
    public String toString() {
        return icon + " " + text;
    }
    public void setNr(int nr) {
        this.nr = nr;
    }
    public void setText(String text) {
        this.text = text;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public void setLocation(Rectangle location) {
        this.location = location;
    }
    public Rectangle getLocation() {
        return location;
    }
}