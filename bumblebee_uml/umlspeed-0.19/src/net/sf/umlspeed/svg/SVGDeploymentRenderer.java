package net.sf.umlspeed.svg;

import java.util.ArrayList;
import java.util.Iterator;

import net.sf.umlspeed.entities.Deployment;
import net.sf.umlspeed.entities.DeploymentContent;
import net.sf.umlspeed.entities.DiagramElement;
import net.sf.umlspeed.entities.Interface;

public class SVGDeploymentRenderer extends SVGEntity {

    private DiagramElement de = null;
    private Deployment dep = null;
    
    public SVGDeploymentRenderer(Object o) {
        if (o instanceof DiagramElement) {
            de = (DiagramElement) o;
            dep = (Deployment) de.getEntity();
            entity = de;
        }
        else if (o instanceof Interface) {
            dep = (Deployment) o;
            entity = dep;
        }
        else
            throw new IllegalArgumentException("SVGDeploymentRenderer can only accept DiagramElement or Deployment");
    }
    
    protected void render() {
        
        StringBuffer s = new StringBuffer();
        int textmargin = 5;
        
        // Calculate the height/width of the whole thing
        // Name + Platform
        height = getSmallTextHeight() + (textmargin * 2) + getSmallTextHeight() + textmargin;
        int widestItem = 0;
        widestItem = estimateSmallTextWidth(dep.getDisplayName());
        if (estimateSmallTextWidth(dep.getPlatform()) > widestItem) widestItem = estimateSmallTextWidth(dep.getPlatform());
        
        // Contents
        ArrayList contents = new ArrayList();
        for (int i = 0; i < dep.getContents().size(); i++) {
            DeploymentContent dc = (DeploymentContent) dep.getContents().get(i);
            SVGDeploymentContentRenderer r = new SVGDeploymentContentRenderer(dc);
            r.setPosition(0, 0);
            height += r.getSize().height + (textmargin * 2);
            if (r.getSize().width > widestItem) widestItem = r.getSize().width;
            contents.add(r);
        }
        width = widestItem + (textmargin * 4);
        s.append("<g>");
        
        // Depending on the type, render a node or a database and increase 
        // the height and width accordingly (and start y pos).
        int y = 0;
        int offset = 10;
        if (dep.getType() == Deployment.NODE) {
            // Render the node box
            s.append(drawRectangle(0, 0, width, height, 1, SVGColours.DEPLOYMENT_NODE_BACKGROUND, SVGColours.DEPLOYMENT_NODE_OUTLINE));
            // Render the 3d bits around the edge
            s.append(drawLine(0, height, offset, height + offset, 1, SVGColours.DEPLOYMENT_NODE_OUTLINE));
            s.append(drawLine(offset, height + offset, width + offset, height + offset, 1, SVGColours.DEPLOYMENT_NODE_OUTLINE));
            s.append(drawLine(width, height, width + offset, height + offset, 1, SVGColours.DEPLOYMENT_NODE_OUTLINE));
            s.append(drawLine(width + offset, offset, width + offset, height + offset, 1, SVGColours.DEPLOYMENT_NODE_OUTLINE));
            s.append(drawLine(width, 0, width + offset, offset, 1, SVGColours.DEPLOYMENT_NODE_OUTLINE));
            y = (textmargin * 2);
        }
        else if (dep.getType() == Deployment.DATABASE) {
            // Render the bottom ellipse first
            s.append(drawEllipse(width / 2, height + (offset / 2), width / 2, offset / 2, 1, SVGColours.DEPLOYMENT_NODE_BACKGROUND, SVGColours.DEPLOYMENT_NODE_OUTLINE));
            // Then the box
            s.append(drawRectangle(0, offset / 2, width, height, 1, SVGColours.DEPLOYMENT_NODE_BACKGROUND, SVGColours.DEPLOYMENT_NODE_BACKGROUND));
            // Then the two lines down the side
            s.append(drawLine(0, offset / 2, 0, height + (offset / 2), 1, SVGColours.DEPLOYMENT_NODE_OUTLINE));
            s.append(drawLine(width, offset / 2, width, height + (offset / 2), 1, SVGColours.DEPLOYMENT_NODE_OUTLINE));
            // Then the top ellipse
            s.append(drawEllipse(width / 2, offset / 2, width / 2, offset / 2, 1, SVGColours.DEPLOYMENT_NODE_BACKGROUND, SVGColours.DEPLOYMENT_NODE_OUTLINE));
            y = offset;
            
        }
        
        // Now render the name and platform
        y += getSmallTextHeight();
        s.append(drawSmallBoldText(dep.getDisplayName(), 
                calculateSmallTextCenter(dep.getDisplayName(), width),
                y, -1));
        y += getSmallTextHeight() + textmargin;
        String platform = "<<" + dep.getPlatform() + ">>";
        s.append(drawSmallText(platform, 
                calculateSmallTextCenter(platform, width),
                y, -1));
        
        y += textmargin;
        
        // Now do the components
        for (Iterator it = contents.iterator(); it.hasNext(); ) {
            SVGEntity se = (SVGEntity) it.next();
            se.setPosition( this.x + (this.width / 2 - se.getSize().width / 2), this.y + y);
            s.append(se.getSVG());
            y += se.getSize().height + (textmargin * 2);
        }
        
        s.append("</g>");
        
        // Adjust final height/width
        height += offset;
        if (dep.getType() == Deployment.NODE) width += offset;
        
        svg = s.toString();
    }

}
