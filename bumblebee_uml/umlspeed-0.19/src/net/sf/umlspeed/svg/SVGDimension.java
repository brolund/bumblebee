package net.sf.umlspeed.svg;

public class SVGDimension {
    
    public int width = 0;
    public int height = 0;
    
    public SVGDimension() {
    }
    
    public SVGDimension(int width, int height)  {
        this.width = width;
        this.height = height;
    }
    
    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }
    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }
    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }
    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }
}
