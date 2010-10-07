package net.sf.umlspeed.svg;

import net.sf.umlspeed.entities.Actor;
import net.sf.umlspeed.entities.Class;
import net.sf.umlspeed.entities.Component;
import net.sf.umlspeed.entities.DataStore;
import net.sf.umlspeed.entities.DeploymentContent;
import net.sf.umlspeed.entities.DiagramElement;

public class SVGDeploymentContentRenderer extends SVGEntity {

    private DiagramElement de = null;
    private DeploymentContent dc = null;
    private Component c = null;
    
    public SVGDeploymentContentRenderer(Object o) {
        if (o instanceof DiagramElement) {
            de = (DiagramElement) o;
            dc = (DeploymentContent) de.getEntity();
            entity = de;
            c = (Component) DataStore.components.get(dc.getName());
        }
        else if (o instanceof DeploymentContent) {
            dc = (DeploymentContent) o;
            entity = dc;
            c = (Component) DataStore.components.get(dc.getName());
        }
        else
            throw new IllegalArgumentException("SVGDeploymentContentRenderer can only accept DiagramElement or DeploymentContent");
    }
    
    protected void render() {
        
        StringBuffer s = new StringBuffer();
        s.append("<g>\n");
        
        width = estimateSmallTextWidth(c.getText()) + 10;
        height = getSmallTextHeight() * 3;
        
        // Render the box
        s.append(drawRectangle(0, 0, getSize().width, getSize().height, 1, SVGColours.DEPLOYMENTCONTENT_BACKGROUND, SVGColours.DEPLOYMENTCONTENT_OUTLINE));
        
        // Render the component text
        s.append(drawSmallText(c.getText(), 3, getSmallTextHeight() * 2 - (getSmallTextHeight() / 2), -1));
        
        // Render the appropriate icon in the top-right corner
        if (dc.getType() == DeploymentContent.SOFTWARE) 
            s.append(renderSoftware());
        else
            s.append(renderComponent());
        
        s.append("</g>\n");
        svg = s.toString();
    }
    
    // Software is a box with a little folded page corner in the top right
    // corner
    protected String renderSoftware() {
        
        String s = "";
        
        // 8 x 10
        int offset = 5; // distance from edge of component
        int w = 8; // width of page icon
        int h = 10; // height of page icon
        int curl = 4; // amount of curl on page icon
        int x = width - w - offset; // x pos to draw page icon
        int y = offset; // y pos to draw page icon
        
        // top line --
        s += drawLine(x + curl, y, x + w, y, 1, SVGColours.BLACK );
        // bottom line --
        s += drawLine(x, y + h, x + w, y + h, 1, SVGColours.BLACK);
        // right line |
        s += drawLine(x + w, y, x + w, y + h, 1, SVGColours.BLACK);
        // left line |
        s += drawLine(x, y + curl, x, y + h, 1, SVGColours.BLACK);
        // top left diagonal /
        s += drawLine(x, y + curl, x + curl, y, 1, SVGColours.BLACK);
        // triangle of bottom -|
        s += drawLine(x, y + curl, x + curl, y + curl, 1, SVGColours.BLACK);
        s += drawLine(x + curl, y, x + curl, y + curl, 1, SVGColours.BLACK);
        return s;
    }
    
    // Component is the UML component symbol 
    // o==
    // o==
    protected String renderComponent() {
        
        String s = "";
        
        // 8 x 10
        int offset = 5; // distance from edge of component
        int w = 8; // width of box
        int h = 12; // height of box
        int bh = 3; // height of left rectangles
        int bs = 2; // space between left rectangles
        int bo = 3; // amount left rectangles overlap box
        int x = width - w - offset; // x pos to draw page icon
        int y = offset; // y pos to draw icon
        
        // main box
        s += drawRectangle(x, y, w, h, 1, SVGColours.WHITE, SVGColours.BLACK);
        
        // first rectangle
        s += drawRectangle(x - bo, y + bs, bo * 2, bh, 1, SVGColours.WHITE, SVGColours.BLACK);
        
        // second rectangle
        s += drawRectangle(x - bo, y + (bs * 2) + bh, bo * 2, bh, 1, SVGColours.WHITE, SVGColours.BLACK);

        return s;
    }

}
