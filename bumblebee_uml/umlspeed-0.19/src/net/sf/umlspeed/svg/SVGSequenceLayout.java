package net.sf.umlspeed.svg;

import java.util.Iterator;

import net.sf.umlspeed.entities.DiagramElement;
import net.sf.umlspeed.entities.DiagramLink;
import net.sf.umlspeed.entities.Entity;
import net.sf.umlspeed.entities.SequenceDiagram;

/**
 * Handles the layout for a sequence diagram.
 */
public class SVGSequenceLayout extends SVGLayout {
    
    public String render() {
        
        StringBuffer svg = new StringBuffer();
        
        // Get a reference to the diagram itself
        SVGEntity firstItem = (SVGEntity) objects.get(0);
        SequenceDiagram dg = (SequenceDiagram) ((DiagramElement) firstItem.getEntity()).getDiagram(); 
        
        // We arrange the objects into a row at the top
        // with the satellite entity in the middle. A quick pass through
        // first figures out the biggets item we're dealing with
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
        
        // The canvasmargin is the gap we allow between objects and the
        // edge of the canvas
        int canvasmargin = getCanvasMargin();
        
        // Margin is the gap (horizontal) between the nearest
        // diagram element. The fewer the elements, the larger the margin
        // should be for clarity.
        int margin = getMargin();
        
        // Calculate how many method calls we have, so we can figure out
        // the overall height of the swimlanes.
        int nummessages = dg.getSequence().size();
        int messagemargin = 10; // The vertical gap between messages
        int messageheight = SVGEntity.getTinyTextHeight() * 2 + messagemargin;
        int swimlaneheight = nummessages * messageheight + messagemargin;
        
        // Start the render
        int x = canvasmargin;
        int y = canvasmargin;
        
        for (int i = 0; i < objects.size(); i++) {
            
            // Render the entity at the top
            SVGEntity e = (SVGEntity) objects.get(i);
            e.setPosition(x, y);
            svg.append(e.getSVG());
            
            // Render the swimlane for the entity
            int swimlanex = x + (e.getSize().width / 2);
            SVGSequenceSwimlaneRenderer sw = new SVGSequenceSwimlaneRenderer(swimlanex, y + e.getSize().height, swimlaneheight);
            sw.render();
            svg.append(sw.getSVG());
            
            x += biggestwidth + margin;
            
        }
        
        // Render the links and methods
        y = canvasmargin + biggestheight + messagemargin;
        for (int i = 0; i < dg.getSequence().size(); i++) {

            // Get this link
            DiagramLink dl = (DiagramLink) dg.getSequence().get(i);
            
            // Find the diagram element for the source and target
            SVGEntity e = findSVGEntity(dl.getSourceEntity());
            SVGEntity target = findSVGEntity(dl.getTargetEntity());
            
            // Render the message
            x = e.getPosition().x + (e.getSize().width / 2);
            int x2 = target.x + (target.width / 2);
            SVGSequenceMessageRenderer sm = new SVGSequenceMessageRenderer(x, y, x2, dl.getLinkComment(), dl.getLinkType());
            sm.render();
            svg.append(sm.getSVG());
            
            y += messageheight;
            
        }
        
        // Calculate total canvas size of layout
        size.height = (canvasmargin * 3) + biggestheight + swimlaneheight;
        size.width = (canvasmargin * 2) + ((biggestwidth + margin) * objects.size());
        
        return svg.toString();
    }
    
    /** 
     *  Finds the SVGEntity that has e as it's entity or null if it
     *  doesn't exist.
     **/
    private SVGEntity findSVGEntity(Entity e) {
        for (int i = 0; i < objects.size(); i++) {
            SVGEntity en = (SVGEntity) objects.get(i);
            if (en.getEntity().getName().equals(e.getName())) {
                return en;
            }
        }
        return null;
    }

}
