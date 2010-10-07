package net.sf.umlspeed.svg;

import net.sf.umlspeed.entities.Class;
import net.sf.umlspeed.entities.DiagramElement;
import net.sf.umlspeed.entities.Entity;

/**
 * This is a catch-all wrapper for entities appearing at the
 * top of sequence diagrams.
 */
public class SVGSequenceEntityRenderer extends SVGEntity {

    private DiagramElement de = null;
    private Entity e = null;
    
    public SVGSequenceEntityRenderer(Object o) {
        if (o instanceof DiagramElement) {
            de = (DiagramElement) o;
            e = (Entity) de.getEntity();
            entity = de;
        }
        else if (o instanceof Class) {
            e = (Entity) o;
            entity = e;
        }
        else
            throw new IllegalArgumentException("SVGSequenceEntityRenderer can only accept DiagramElement or Entity");
    }
    
    protected void render() {
        
        StringBuffer s = new StringBuffer();
        s.append("<g>\n");
        
        int textmargin = 2;
        
        height = getMediumTextHeight() + (textmargin * 4);
        width = estimateMediumTextWidth(":" + e.getName()) + (textmargin * 4);
        
        // Render the box and dropshadow
        // Render the drop shadow rectangle
        s.append(drawRectangle(5, 5, width, height, 0, SVGColours.SEQUENCE_SHADOW, SVGColours.SEQUENCE_SHADOW));
        
        // Render the background rectangle
        s.append(drawRectangle(0, 0, width, height, 
                1, SVGColours.SEQUENCE_BACKGROUND, SVGColours.SEQUENCE_OUTLINE));
        
        // Render the text
        s.append(drawMediumText(":" + e.getName(), textmargin * 2, height - (textmargin * 3), -1));
        
        s.append("</g>\n");
        svg = s.toString();
    }

}
