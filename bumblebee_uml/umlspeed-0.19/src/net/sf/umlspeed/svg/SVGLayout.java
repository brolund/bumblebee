package net.sf.umlspeed.svg;

import java.util.ArrayList;
import java.util.List;

import net.sf.umlspeed.Settings;

public abstract class SVGLayout {

    /** The objects being managed by the layout */
    protected List objects = new ArrayList();
    
    /** The size of the layout */
    protected SVGDimension size = new SVGDimension();
    
    public void add(SVGEntity e) {
        objects.add(e);
    }

    public void remove(SVGEntity e) {
        objects.remove(e);
    }

    public List getObjects() {
        return objects;
    }
    
    public int getWidth() {
        return size.width;
    }
    
    public int getHeight() {
        return size.height;
    }
    
    /** The canvas margin is the gap between the edges of the
     *  layout and the edge of the diagram. The layout needs to
     *  know it so it can offset componenents by at least this
     *  amount when laying them out.
     **/
    protected int canvasmargin = 20;
    
    protected int getCanvasMargin() {
        return canvasmargin;
    }
    
    protected void setCanvasMargin(int canvasmargin) {
        this.canvasmargin = canvasmargin;
    }
    
    /**
     * Looks at how many objects are in the diagram and returns
     * an appropriate pixel margin to leave between them when
     * laying them out - the more objects, the smaller the margin.
     * @return
     */
    protected int getMargin() {
        
        // If an override value has been specified, use that
        // instead.
        if (Settings.marginoverride != -1)
            return Settings.marginoverride;
        
        // Use getObjects instead of member objects variable as
        // some layouts can override how objects are held, but
        // getObjects should always return the correct list.
        List objects = getObjects();
        if ( objects.size() < 3)
            return 100;
        else if ( objects.size() < 5)
            return 70;
        else if ( objects.size() < 10)
            return 50;
        else if ( objects.size() < 20)
            return 20;
        else
            return 10;
        
    }
    /**
     * @return the size
     */
    public SVGDimension getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(SVGDimension size) {
        this.size = size;
    }
    
    /** Renders the entire document as SVG and returns it
     *  as a string.
     * @return The SVG document.
     */
    public abstract String render();
    
    /** For a given cellsize, entitysize and position, calculates
     *  The offset necessary to center the entity within the cell and
     *  returns the new position.
     * @param cellsize The size of the cell
     * @param entitySize The size of the entity
     * @param cellLocation The x,y co-ordinates of the cell location
     * @return A position to center the entity in the cell.
     */
    protected SVGPosition centerInCell(SVGDimension cellsize, SVGDimension entitySize, SVGPosition cellLocation) {
        int x = 0;
        int y = 0;
        x = cellLocation.x + (cellsize.width - entitySize.width) / 2;
        y = cellLocation.y + (cellsize.height - entitySize.height) / 2;
        return new SVGPosition(x, y);
    }

    
    
}
