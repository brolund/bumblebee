package net.sf.umlspeed.svg;

/**
 * Renders the swimlanes on sequence diagrams
 */
public class SVGSequenceSwimlaneRenderer extends SVGEntity {

    
    public SVGSequenceSwimlaneRenderer(int x, int y, int height) {
        setPosition(x, y);
        setSize(1, height);
    }
    
    protected void render() {
        StringBuffer s = new StringBuffer();
        s.append("<g>\n");
        s.append(drawDottedLine(0, 0, 0, height, 1, SVGColours.SEQUENCE_OUTLINE));
        s.append("</g>\n");
        svg = s.toString();
    }

}
