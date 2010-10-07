package net.sf.umlspeed.svg;

import net.sf.umlspeed.cli.CLI;
import net.sf.umlspeed.entities.DiagramLink;

/**
 * Renders the swimlanes on sequence diagrams
 */
public class SVGSequenceMessageRenderer extends SVGEntity {

    private String message = "";
    private int x2 = 0;
    private int diagramLinkType = 0;
    private int orientation = LEFT_TO_RIGHT;
    private final static int LEFT_TO_RIGHT = 0;
    private final static int RIGHT_TO_LEFT = 1;
    private final static int SELF = 2;
    
    /** 
     * 
     * @param x X co-ordinate on canvas to start
     * @param y Y co-ordinate on canvas to start
     * @param x2 X co-ordinate on canvas to finish
     * @param message
     * @param diagramLinkType
     */
    public SVGSequenceMessageRenderer(int x, int y, int x2, String message, int diagramLinkType) {
        
        if (x2 == x) {
            orientation = SELF;
            this.x2 = x2;
            setPosition(x, y); 
            CLI.print("SVGSequenceMessageRenderer: " + message + " - SELF", 2);
        }
        else if (x2 < x) {
            // We swap the X co-ordinates if we're going left to right.
            orientation = RIGHT_TO_LEFT;
            this.x2 = x;
            setPosition(x2, y);
            CLI.print("SVGSequenceMessageRenderer: " + message + " - RIGHT_TO_LEFT", 2);
        }
        else {
            orientation = LEFT_TO_RIGHT;
            this.x2 = x2;
            setPosition(x, y);
            CLI.print("SVGSequenceMessageRenderer: " + message + " - LEFT_TO_RIGHT", 2);
        }
        this.message = message;
        this.diagramLinkType = diagramLinkType;
        setSize(this.x2 - this.x, getTinyTextHeight() * 2);
    }
    
    protected void render() {
        StringBuffer s = new StringBuffer();
        s.append("<g>\n");
        
        // Render the message line
        int y = height - (getTinyTextHeight() / 2);
        final int TS = 4; // Triangle size for message heads
        switch (diagramLinkType) {
            case DiagramLink.LINK_CREATES:
            case DiagramLink.LINK_INVOKES:
                if (orientation == LEFT_TO_RIGHT) {
                    s.append(drawLine(0, y, width - 1, y, 1, SVGColours.BLACK)); // Link line
                    s.append(drawRightEquiTriangle(width - TS - 1, y - (TS/2), TS, 1, SVGColours.BLACK, SVGColours.BLACK));
                }
                else if (orientation == RIGHT_TO_LEFT) {
                    s.append(drawLine(0, y, width - 1, y, 1, SVGColours.BLACK)); // Link line
                    s.append(drawLeftEquiTriangle(TS + 1, y - (TS/2), TS, 1, SVGColours.BLACK, SVGColours.BLACK));
                }                
                else if (orientation == SELF) {
                    s.append(drawEmptyRectangle((TS*2) * -1, y, TS*4, TS*2, 1, SVGColours.BLACK));
                    s.append(drawRightEquiTriangle(width - TS - 1, y - (TS/2), TS, 1, SVGColours.BLACK, SVGColours.BLACK));
                }
                break;
            case DiagramLink.LINK_INVOKES_ASYNC:
                if (orientation == LEFT_TO_RIGHT) {
                    s.append(drawLine(0, y, width - 1, y, 1, SVGColours.BLACK));
                    s.append(drawLine(width, y, width - TS - 1, y - (TS/2), 1, SVGColours.BLACK));
                }
                else if (orientation == RIGHT_TO_LEFT) {
                    s.append(drawLine(0, y, width - 1, y, 1, SVGColours.BLACK));
                    s.append(drawLine(0, y, TS + 1, y - (TS/2), 1, SVGColours.BLACK));
                }
                else if (orientation == SELF) {
                    s.append(drawEmptyRectangle((TS*2) * -1, y, TS*4, TS*2, 1, SVGColours.BLACK));
                    s.append(drawLine(width, y, width - TS - 1, y - (TS/2), 1, SVGColours.BLACK));
                }
                break;
        }
        
        // Render the message text centrally across the line
        int x = calculateTinyTextCenter(message, width);
        s.append(drawTinyText(message, x, y-4, -1));
        
        s.append("</g>\n");
        svg = s.toString();
    }

}
