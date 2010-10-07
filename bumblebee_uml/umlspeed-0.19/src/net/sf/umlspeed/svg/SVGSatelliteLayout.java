package net.sf.umlspeed.svg;

import java.util.Iterator;

/**
 * Manages the layout of SVG objects in a satellite layout around
 * one object.
 */
public class SVGSatelliteLayout extends SVGLayout {

    private SVGEntity satellite = null;
    
    public void add(SVGEntity e) {
        // Don't do anything for the satellite entity
        if (e.getEntity().getName().equals(satellite.getEntity().getName())) 
                return;
        objects.add(e);
    }
    
    public void setSatelliteEntity(SVGEntity e) {
        satellite = e;
        objects.add(satellite);
    }
    
    public String render() {
        
        StringBuffer svg = new StringBuffer();
        
        // We arrange the objects into two rows at the top and bottom
        // with the satellite entity in the middle. A quick pass through
        // first figures out the largest item we're dealing with
        int biggestwidth = 0;
        int biggestheight = 0;
        for (Iterator it = objects.iterator(); it.hasNext(); ) {
            SVGEntity e = (SVGEntity) it.next();
            e.setPosition(0, 0);
            if (e.getSize().width > biggestwidth)
                biggestwidth = e.getSize().width;
            if (e.getSize().height > biggestheight)
                biggestheight = e.getSize().height;
        }
        
        // Render the satellite object as well so we can get its size
        satellite.setPosition(0, 0);
        
        // The canvasmargin is the gap we allow between objects and the
        // edge of the canvas
        int canvasmargin = getCanvasMargin();
        
        // Margin is the gap (horizontal and vertical) between the nearest
        // diagram element. The fewer the elements, the larger the margin
        // should be for clarity.
        int margin = getMargin();
        
        // Figure out the top and bottom lines, along with the overall
        // canvas size.
        int topRowY = canvasmargin;
        int bottomRowY = canvasmargin + biggestheight + satellite.getSize().height + (margin * 2);
        int itemsPerRow = ((objects.size()-1) / 2);
        if (itemsPerRow == 0) itemsPerRow = 1;
        size.width = (canvasmargin * 2) + ((biggestwidth + margin) * (itemsPerRow+1));
        size.height = (canvasmargin * 2) + (biggestheight * 2) + (margin * 3) + satellite.getSize().height; 
        
        // Render the satellite in its final position
        int satx = (size.width / 2) - (satellite.getSize().width / 2);
        int saty = biggestheight + canvasmargin + margin;
        satellite.setPosition(satx, saty);
        svg.append(satellite.getSVG());
        
        // Render all the other objects
        int currentItem = 0;
        int y = topRowY;
        int x = canvasmargin;
        for (Iterator it = objects.iterator(); it.hasNext(); ) {
            
            SVGEntity e = (SVGEntity) it.next();
            
            // Ignore the satellite entity - we've already rendered it
            if (!e.getEntity().getName().equals(satellite.getEntity().getName())) {
                e.setPosition(centerInCell(new SVGDimension(biggestwidth, biggestheight), e.getSize(), new SVGPosition(x, y)));
                svg.append(e.getSVG());
                
                x += biggestwidth + margin;
                currentItem++;
                if (currentItem > itemsPerRow) {
                    y = bottomRowY;
                    x = canvasmargin;
                    currentItem = -1;
                }
            }
        }
        
        return svg.toString();
    }

}
