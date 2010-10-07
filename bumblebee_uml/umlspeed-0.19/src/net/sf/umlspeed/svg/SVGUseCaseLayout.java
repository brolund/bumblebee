package net.sf.umlspeed.svg;

import java.util.Iterator;

import net.sf.umlspeed.Settings;
import net.sf.umlspeed.cli.CLI;
import net.sf.umlspeed.entities.Actor;
import net.sf.umlspeed.entities.DiagramElement;
import net.sf.umlspeed.entities.UseCase;

/**
 * Manages the layout of SVG objects in 3 vertical columns 
 * according to entity type. Actors on the left and right, 
 * use cases in the middle. Actor size does not affect the
 * size of use case cells.
 */
public class SVGUseCaseLayout extends SVGLayout {

    public String render() {
        
        StringBuffer svg = new StringBuffer();
        
        // A quick pass through first figures out the largest item 
        // of each type we're dealing with:
        int ucbiggestwidth = 0;
        int ucbiggestheight = 0;
        int acbiggestwidth = 0;
        int acbiggestheight = 0;
        int numactors = 0;
        int numusecases = 0;
        for (Iterator it = objects.iterator(); it.hasNext(); ) {
            SVGEntity e = (SVGEntity) it.next();
            e.setPosition(0, 0);
            if (((DiagramElement) e.getEntity()).getEntity() instanceof Actor) {
                numactors++;
                if (e.getSize().width > acbiggestwidth)
                    acbiggestwidth = e.getSize().width;
                if (e.getSize().height > acbiggestheight)
                    acbiggestheight = e.getSize().height;
            }
            else {
                numusecases++;
                if (e.getSize().width > ucbiggestwidth)
                    ucbiggestwidth = e.getSize().width;
                if (e.getSize().height > ucbiggestheight)
                    ucbiggestheight = e.getSize().height;
            }
        }
        
        // The canvasmargin is the gap we allow between objects and the
        // edge of the canvas
        int canvasmargin = getCanvasMargin();
        
        // Margin is the gap (horizontal and vertical) between the nearest
        // diagram element. The fewer the elements, the larger the margin
        // should be for clarity.
        int margin = getMargin();
        
        int leftcol = canvasmargin;
        int midcol = canvasmargin + acbiggestwidth + margin;
        int rightcol = canvasmargin + acbiggestwidth + (margin * 2) + ucbiggestwidth;
        size.width = rightcol + acbiggestwidth + canvasmargin;
        
        // We start the left and right columns as vertically centered as we can
        int vcenter = (canvasmargin + ((ucbiggestheight + margin) * numusecases) / 2);
        // Reduce center point by half the height of the number of actors in one
        // column.
        vcenter = vcenter - (((acbiggestheight * numactors) / 2) / 2);
        
        int ly = vcenter;
        int ry = vcenter;
        
        int my = canvasmargin;
        
        // The actor column we're sending to
        int acol = 0;
        
        // Render the objects
        int x = 0;
        int y = 0;
        size.height = 0;
        
        for (Iterator it = objects.iterator(); it.hasNext(); ) {
            
            SVGEntity e = (SVGEntity) it.next();
            
            // If we're an Actor, go to the left or right column depending
            // on which one we did last.
            if (((DiagramElement) e.getEntity()).getEntity() instanceof Actor) {
                if (acol == 0) {
                    x = leftcol;
                    y = ly;
                    e.setPosition(centerInCell(new SVGDimension(acbiggestwidth, acbiggestheight), e.getSize(), new SVGPosition(x, y)));
                    svg.append(e.getSVG());
                    ly += margin + acbiggestheight;
                    acol = 1;
                    if (ly > size.height) size.height = ly + canvasmargin;
                }
                else {
                    x = rightcol;
                    y = ry;
                    e.setPosition(centerInCell(new SVGDimension(acbiggestwidth, acbiggestheight), e.getSize(), new SVGPosition(x, y)));
                    svg.append(e.getSVG());
                    ry += margin + acbiggestheight;
                    acol = 0;
                    if (ry > size.height) size.height = ry + canvasmargin;
                }
            }
            else if (((DiagramElement) e.getEntity()).getEntity() instanceof UseCase) {
                x = midcol;
                y = my;
                e.setPosition(centerInCell(new SVGDimension(ucbiggestwidth, ucbiggestheight), e.getSize(), new SVGPosition(x, y)));
                svg.append(e.getSVG());                
                my += margin + ucbiggestheight;
                if (my > size.height) size.height = my + canvasmargin;
            }
            else {
                CLI.print("SVGUseCaseLayout: UseCase layout can only have Actor or UseCase entities.");
                if (Settings.standalone)
                    System.exit(1);
                return "";
            }
        }
        
        return svg.toString();
    }

}
