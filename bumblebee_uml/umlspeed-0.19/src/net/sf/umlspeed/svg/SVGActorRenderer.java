package net.sf.umlspeed.svg;

import net.sf.umlspeed.entities.Actor;
import net.sf.umlspeed.entities.Class;
import net.sf.umlspeed.entities.DiagramElement;

public class SVGActorRenderer extends SVGEntity {

    private DiagramElement de = null;
    private Actor a = null;
    
    public SVGActorRenderer(Object o) {
        if (o instanceof DiagramElement) {
            de = (DiagramElement) o;
            a = (Actor) de.getEntity();
            entity = de;
        }
        else if (o instanceof Actor) {
            a = (Actor) o;
            entity = a;
        }
        else
            throw new IllegalArgumentException("SVGActorRenderer can only accept DiagramElement or Actor");
    }
    
    protected void render() {
        
        StringBuffer s = new StringBuffer();
        s.append("<g>\n");
        
        int vmargin = 7;
        height = getSmallTextHeight() + 100 + vmargin;
        width = estimateSmallTextWidth(a.getText());
        if (width < 34) width = 34;
        
        // Draw our stick man - 100 units high
        s.append(drawCircle(width / 2, 21, 13, 1, SVGColours.ACTOR_HEAD, SVGColours.ACTOR_LINE)); // Head
        s.append(drawLine(width /2, 33, width / 2, 66, 1, SVGColours.ACTOR_LINE)); // Body
        s.append(drawLine(width /2, 66, (width / 2) - 17, 100, 1, SVGColours.ACTOR_LINE)); // Left leg
        s.append(drawLine(width /2, 66, (width / 2) + 17, 100, 1, SVGColours.ACTOR_LINE)); // Right leg
        s.append(drawLine(width /2 - 17, 40, width /2 + 17, 40, 1, SVGColours.ACTOR_LINE)); // Arms
        
        // Render the text
        s.append(drawSmallText(a.getText(), 2, height - vmargin, -1));
        
        s.append("</g>\n");
        svg = s.toString();
    }

}
