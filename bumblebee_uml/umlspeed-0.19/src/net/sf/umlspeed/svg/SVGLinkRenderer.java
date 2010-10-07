package net.sf.umlspeed.svg;

import java.util.Iterator;
import java.util.List;

import net.sf.umlspeed.cli.CLI;
import net.sf.umlspeed.entities.DiagramLink;

/** 
 * Responsible for rendering links between diagram elements.
 */
public class SVGLinkRenderer extends SVGEntity {
    
    private SVGEntity e1 = null;
    private SVGEntity e2 = null;
    
    // Objects in the layout we are drawing links on (for intersect tests)
    private List layoutObjects = null;
    
    private int x1 = 0;
    private int y1 = 0;
    private int x2 = 0;
    private int y2 = 0;
    
    private int orientation = 0;
    private final static int BOTTOM = 2;
    private final static int TOP = 3;
    private final static int LEFT = 0;
    private final static int RIGHT = 1;
    
    private String linkMessage = "";
    private String linkComment = "";
    
    /** a link type as defined in DiagramLink */
    private int linktype = 0;

    public SVGLinkRenderer (SVGEntity e1, SVGEntity e2, List layoutObjects, int linktype, String linkMessage) {
        this.e1 = e1;
        this.e2 = e2;
        this.layoutObjects = layoutObjects;
        this.linktype = linktype;
        this.linkMessage = linkMessage;
    }
    
    public void render() {}
    
    /** Renders the link as SVG output */
    public String renderLink() {
        
        linkComment = "\n<!-- " + e1.getEntity().getName() + " to " + e2.getEntity().getName() + " -->\n";
        
        calculateShortestNonIntersecting();
        CLI.print("SVGLinkRenderer: Best fit line bounds = (" + x1 + "," + y1 + "," + x2 + "," + y2 + ")", 2);
        
        switch (linktype) {
            case DiagramLink.LINK_EXTENDS: return linkComment + renderExtends();
            case DiagramLink.LINK_IMPLEMENTS: return linkComment + renderImplements();
            case DiagramLink.LINK_MANYTOMANY: return linkComment + renderManyToMany();
            case DiagramLink.LINK_MANYTOONE: return linkComment + renderManyToOne();
            case DiagramLink.LINK_ONETOMANY: return linkComment + renderOneToMany();
            case DiagramLink.LINK_ONETOONE: return linkComment + renderOneToOne();
            case DiagramLink.LINK_USES: return linkComment + renderUses();
            case DiagramLink.LINK_INCLUDES: return linkComment + renderIncludes();
            case DiagramLink.LINK_DEPENDS: return linkComment + renderDepends();
            case DiagramLink.LINK_MESSAGE: return linkComment + renderMessage();
        }
        return "<!-- Missing Linktype " + linktype + " -->";
    }
    
    private class Line {
        public int x1 = 0;
        public int y1 = 0;
        public int x2 = 0;
        public int y2 = 0;
        public int orientation = 0;
    }
    
    /**
     * Calculates the shortest distance between two entities without
     * intersecting another entity.
     * It does this by calculating all paths from the 4 connecting 
     * nodes on the source entity to the 4 nodes on the target
     * entity.
     */
    private void calculateShortestNonIntersecting() {
        
        Line shortestPath = null;
        Line l = null;
        
        // Evaluate the paths
        for (int e1o = 0; e1o < 4; e1o++) {
            for (int e2o = 0; e2o < 4; e2o++) {
                
                l = new Line();
                
                // Source entity
                switch (e1o) {
                    case BOTTOM: l.x1 = e1.x + (e1.width / 2);
                                 l.y1 = e1.y + (e1.height) + 1;
                                 break;
                    case TOP:    l.x1 = e1.x + (e1.width / 2);
                                 l.y1 = e1.y - 1;
                                 break;
                    case LEFT:   l.x1 = e1.x - 1;
                                 l.y1 = e1.y + (e1.height / 2);
                                 break;
                    case RIGHT:  l.x1 = e1.x + e1.width + 1;
                                 l.y1 = e1.y + (e1.height / 2);
                                 break;
                }
                
                // Target entity
                switch (e2o) {
                    case BOTTOM: l.x2 = e2.x + (e2.width / 2);
                                 l.y2 = e2.y + (e2.height) + 1;
                                 l.orientation = BOTTOM;
                                 break;
                    case TOP:    l.x2 = e2.x + (e2.width / 2);
                                 l.y2 = e2.y - 1;
                                 l.orientation = TOP;
                                 break;
                    case LEFT:   l.x2 = e2.x - 1;
                                 l.y2 = e2.y + (e2.height / 2);
                                 l.orientation = LEFT;
                                 break;
                    case RIGHT:  l.x2 = e2.x + e2.width + 1;
                                 l.y2 = e2.y + (e2.height / 2);
                                 l.orientation = RIGHT;
                                 break;
                }
                
                // Does this line intersect an entity? If not, compare it to our
                // shortest path, if it's shorter, keep that one.
                if (!intersectsEntity(l)) {
                    if (shortestPath == null) {
                        shortestPath = l;
                    }
                    else {
                        shortestPath = shorterPath(l, shortestPath);
                    }
                }
            }
        }
        
        // Fall back to the last path tried if there are no intersecting
        // paths.
        if (shortestPath == null) shortestPath = l;
        
        // Copy the shortest path
        x1 = shortestPath.x1;
        x2 = shortestPath.x2;
        y1 = shortestPath.y1;
        y2 = shortestPath.y2;
        orientation = shortestPath.orientation;
    }
    
    /**
     * Calculate the shorter of two lines
     * @param l1
     * @param l2
     * @return
     */
    private Line shorterPath(Line l1, Line l2) {
        int d1 = Math.abs(l1.x1 - l1.x2) + Math.abs(l1.y1 - l1.y2);
        int d2 = Math.abs(l2.x1 - l2.x2) + Math.abs(l2.y1 - l2.y2);
        if (d1 > d2)
            return l2;
        else
            return l1;
    }
    
    /** Tests intersection for all entities in the layout and returns true if our
     *  line intersects any one of them. */
    private boolean intersectsEntity(Line l) {
        for (Iterator it = layoutObjects.iterator(); it.hasNext(); ) {
            SVGEntity e = (SVGEntity) it.next();
            if (e.intersects(l.x1, l.y1, l.x2, l.y2))
                return true;
        }
        CLI.print("SVGLinkRenderer: Non-intersecting link from " + e1.getEntity().getName() + " to " + e2.getEntity().getName() + " orientation " + orientation, 3);
        return false;
    }
    
    /**
     * Adjusts the target endpoint of the linkline according to how many
     * links are already at that node of the target.
     */
    private void adjustLinkLine() {
        
        final int SP = 20; // Gap between links to the same node on the same entity
        
        // Adjust target position line based on the
        // number of links at that node so arrows don't
        // get drawn over each other.
        switch (orientation) {
            case TOP:    x2 += e2.toplinks * SP;
                         e2.toplinks++;
                         break;
            case BOTTOM: x2 += e2.botlinks * SP;
                        e2.botlinks++;
                        break;
            case LEFT:  y2 += e2.leftlinks * SP;
                        e2.leftlinks++;
                        break;
            case RIGHT: y2 += e2.rightlinks * SP;
                        e2.rightlinks++;
                        break;
        }
    }
    
    /**
     * Render an extends link. This one is an empty triangle with
     * a solid line.
     * @return
     */
    public String renderExtends() {
        String s = "<g>";
        final int T_SZ = 10; // Triangle size
        adjustLinkLine(); // Arrow heads need to move for other heads
        switch (orientation) {
            case TOP:     s += drawLine(x2-T_SZ, y2-T_SZ, x2+T_SZ, y2-T_SZ, 1, SVGColours.BLACK); // Top horizontal
                                    s += drawLine(x2-T_SZ, y2-T_SZ, x2, y2, 1, SVGColours.BLACK); // Left diagonal to point
                                    s += drawLine(x2+T_SZ, y2-T_SZ, x2, y2, 1, SVGColours.BLACK); // Right diagonal to point
                                    s += drawLine(x1, y1, x2, y2-T_SZ, 1, SVGColours.BLACK); // Link from first entity to triangle
                                    break;
            case BOTTOM:     s += drawLine(x2-T_SZ, y2+T_SZ, x2+T_SZ, y2+T_SZ, 1, SVGColours.BLACK); // Bottom horizontal
                                    s += drawLine(x2-T_SZ, y2+T_SZ, x2, y2, 1, SVGColours.BLACK); // Left diagonal to point
                                    s += drawLine(x2+T_SZ, y2+T_SZ, x2, y2, 1, SVGColours.BLACK); // Right diagonal to point
                                    s += drawLine(x1, y1, x2, y2+T_SZ, 1, SVGColours.BLACK); // Link from first entity to triangle
                                    break;
            case LEFT:     s += drawLine(x2-T_SZ, y2-T_SZ, x2-T_SZ, y2+T_SZ, 1, SVGColours.BLACK); // Leftmost vertical
                                    s += drawLine(x2-T_SZ, y2-T_SZ, x2, y2, 1, SVGColours.BLACK); // Top diagonal to point
                                    s += drawLine(x2-T_SZ, y2+T_SZ, x2, y2, 1, SVGColours.BLACK); // Bottom diagonal to point
                                    s += drawLine(x1, y1, x2-T_SZ, y2, 1, SVGColours.BLACK); // Link from first entity to triangle
                                    break;
            case RIGHT:     s += drawLine(x2+T_SZ, y2-T_SZ, x2+T_SZ, y2+T_SZ, 1, SVGColours.BLACK); // Rightmost vertical
                                    s += drawLine(x2+T_SZ, y2-T_SZ, x2, y2, 1, SVGColours.BLACK); // Top diagonal to point
                                    s += drawLine(x2+T_SZ, y2+T_SZ, x2, y2, 1, SVGColours.BLACK); // Bottom diagonal to point
                                    s += drawLine(x1, y1, x2+T_SZ, y2, 1, SVGColours.BLACK); // Link from first entity to triangle
                                    break;
        }
        return s + "</g>";
    }
    
    /** Renders an implements link. This is a solid triangle
     *  with a dotted line.
     * @return
     */
    public String renderImplements() {
        String s = "<g>";
        final int T_SZ = 10; // Triangle size
        adjustLinkLine(); // Arrow heads need to move for other heads
        switch (orientation) {
            case TOP:     s += drawLine(x2-T_SZ, y2-T_SZ, x2+T_SZ, y2-T_SZ, 1, SVGColours.BLACK); // Top horizontal
                                    s += drawLine(x2-T_SZ, y2-T_SZ, x2, y2, 1, SVGColours.BLACK); // Left diagonal to point
                                    s += drawLine(x2+T_SZ, y2-T_SZ, x2, y2, 1, SVGColours.BLACK); // Right diagonal to point
                                    s += drawDottedLine(x1, y1, x2, y2-T_SZ, 1, SVGColours.BLACK); // Link from first entity to triangle
                                    break;
            case BOTTOM:     s += drawLine(x2-T_SZ, y2+T_SZ, x2+T_SZ, y2+T_SZ, 1, SVGColours.BLACK); // Bottom horizontal
                                    s += drawLine(x2-T_SZ, y2+T_SZ, x2, y2, 1, SVGColours.BLACK); // Left diagonal to point
                                    s += drawLine(x2+T_SZ, y2+T_SZ, x2, y2, 1, SVGColours.BLACK); // Right diagonal to point
                                    s += drawDottedLine(x1, y1, x2, y2+T_SZ, 1, SVGColours.BLACK); // Link from first entity to triangle
                                    break;
            case LEFT:     s += drawLine(x2-T_SZ, y2-T_SZ, x2-T_SZ, y2+T_SZ, 1, SVGColours.BLACK); // Leftmost vertical
                                    s += drawLine(x2-T_SZ, y2-T_SZ, x2, y2, 1, SVGColours.BLACK); // Top diagonal to point
                                    s += drawLine(x2-T_SZ, y2+T_SZ, x2, y2, 1, SVGColours.BLACK); // Bottom diagonal to point
                                    s += drawDottedLine(x1, y1, x2-T_SZ, y2, 1, SVGColours.BLACK); // Link from first entity to triangle
                                    break;
            case RIGHT:     s += drawLine(x2+T_SZ, y2-T_SZ, x2+T_SZ, y2+T_SZ, 1, SVGColours.BLACK); // Rightmost vertical
                                    s += drawLine(x2+T_SZ, y2-T_SZ, x2, y2, 1, SVGColours.BLACK); // Top diagonal to point
                                    s += drawLine(x2+T_SZ, y2+T_SZ, x2, y2, 1, SVGColours.BLACK); // Bottom diagonal to point
                                    s += drawDottedLine(x1, y1, x2+T_SZ, y2, 1, SVGColours.BLACK); // Link from first entity to triangle
                                    break;
        }
        return s + "</g>";
    }
    
    /** Renders an depends link. This is a dotted line/arrow
     * @return
     */
    public String renderDepends() {
        String s = "<g>";
        final int T_SZ = 10; // Triangle size
        adjustLinkLine(); // Arrow heads need to move for other heads
        switch (orientation) {
            case TOP:     s += drawDottedLine(x2-T_SZ, y2-T_SZ, x2+T_SZ, y2-T_SZ, 1, SVGColours.BLACK); // Top horizontal
                                    s += drawDottedLine(x2-T_SZ, y2-T_SZ, x2, y2, 1, SVGColours.BLACK); // Left diagonal to point
                                    s += drawDottedLine(x2+T_SZ, y2-T_SZ, x2, y2, 1, SVGColours.BLACK); // Right diagonal to point
                                    s += drawDottedLine(x1, y1, x2, y2-T_SZ, 1, SVGColours.BLACK); // Link from first entity to triangle
                                    break;
            case BOTTOM:     s += drawDottedLine(x2-T_SZ, y2+T_SZ, x2+T_SZ, y2+T_SZ, 1, SVGColours.BLACK); // Bottom horizontal
                                    s += drawDottedLine(x2-T_SZ, y2+T_SZ, x2, y2, 1, SVGColours.BLACK); // Left diagonal to point
                                    s += drawDottedLine(x2+T_SZ, y2+T_SZ, x2, y2, 1, SVGColours.BLACK); // Right diagonal to point
                                    s += drawDottedLine(x1, y1, x2, y2+T_SZ, 1, SVGColours.BLACK); // Link from first entity to triangle
                                    break;
            case LEFT:     s += drawDottedLine(x2-T_SZ, y2-T_SZ, x2-T_SZ, y2+T_SZ, 1, SVGColours.BLACK); // Leftmost vertical
                                    s += drawDottedLine(x2-T_SZ, y2-T_SZ, x2, y2, 1, SVGColours.BLACK); // Top diagonal to point
                                    s += drawDottedLine(x2-T_SZ, y2+T_SZ, x2, y2, 1, SVGColours.BLACK); // Bottom diagonal to point
                                    s += drawDottedLine(x1, y1, x2-T_SZ, y2, 1, SVGColours.BLACK); // Link from first entity to triangle
                                    break;
            case RIGHT:     s += drawDottedLine(x2+T_SZ, y2-T_SZ, x2+T_SZ, y2+T_SZ, 1, SVGColours.BLACK); // Rightmost vertical
                                    s += drawDottedLine(x2+T_SZ, y2-T_SZ, x2, y2, 1, SVGColours.BLACK); // Top diagonal to point
                                    s += drawDottedLine(x2+T_SZ, y2+T_SZ, x2, y2, 1, SVGColours.BLACK); // Bottom diagonal to point
                                    s += drawDottedLine(x1, y1, x2+T_SZ, y2, 1, SVGColours.BLACK); // Link from first entity to triangle
                                    break;
        }
        return s + "</g>";
    }
    
    /**
     * Many to many. This renders a solid line with a "0.." at
     * each end.
     * @return
     */
    public String renderManyToMany() {
        return renderXtoX("0..", "0..");
    }
    
    /** Many to one. This renders a solid line with a "0.." at
      * e1 and a 1 at e2 */
    public String renderManyToOne() {
        return renderXtoX("0..", "1.");
    }

    public String renderOneToMany() {
        return renderXtoX("1.", "0..");
    }

    public String renderOneToOne() {
        return renderXtoX("1.", "1.");
    }
    
    public String renderMessage() {
        String s = "<g>";
        // The link line
        s += drawLine(x1, y1, x2, y2, 1, SVGColours.BLACK);
        s += renderHalfwayMessage(linkMessage, true);
        s += "</g>";
        return s;
    }
    
    // Renders some text halfway along the link line 
    public String renderHalfwayMessage(String message, boolean bold) {
        String s = "";
        // Find a point half way along the line
        int hx = 0;
        int hxdiff = Math.abs(x2 - x1) / 2;
        int hy = 0;
        int hydiff = Math.abs(y2 - y1) / 2;
        if (x1 < x2) 
            hx = x1 + hxdiff;
        else
            hx = x2 + hxdiff;
        if (y1 < y2)
            hy = y1 + hydiff;
        else
            hy = y2 + hydiff;
        hx -= estimateSmallTextWidth(message) / 2;
        if (bold) 
            s += drawSmallBoldText("<<" + message + ">>", hx, hy, -1);
        else 
            s += drawSmallText("<<" + message + ">>", hx, hy, -1);
        return s;
    }
    
    private String renderXtoX(String e1Text, String e2Text) {
        String s = "<g>";
        final int SP = getSmallTextHeight(); // Indent from line
        s += drawLine(x1, y1, x2, y2, 1, SVGColours.BLACK);
        adjustLinkLine(); // Arrow heads need to move for other heads
        switch (orientation) {
        
            case TOP: s += drawSmallBoldText(e1Text, x1 + SP, y1 + SP, -1);
                                s += drawSmallBoldText(e2Text, x2 - SP, y2 - SP, -1);
                                break;
            case BOTTOM: s += drawSmallBoldText(e1Text, x1 - SP, y1 - SP, -1);
                                s += drawSmallBoldText(e2Text, x2 + SP, y2 + SP, -1);
                                break;
                                
            case LEFT: s += drawSmallBoldText(e1Text, x1 + SP, y1 + SP, -1);
                                s += drawSmallBoldText(e2Text, x2 - SP, y2 - SP, -1);
                                break;
            case RIGHT: s += drawSmallBoldText(e1Text, x1 - SP, y1 - SP, -1);
                                s += drawSmallBoldText(e2Text, x2 + SP, y2 + SP, -1);
                                break;
        }                       
        return s + "</g>";
    }
    
    /** Use case "uses" relationship */
    private String renderUses() {
        return drawLine(x1, y1, x2, y2, 1, SVGColours.BLACK);
    }
    
    /** Use case "includes" relationship */
    private String renderIncludes() {
        String s = "<g>";
        s += drawDottedLine(x1, y1, x2, y2, 1, SVGColours.BLACK);
        s += renderHalfwayMessage("includes", true);
        return s + "</g>";
    }
    
}
