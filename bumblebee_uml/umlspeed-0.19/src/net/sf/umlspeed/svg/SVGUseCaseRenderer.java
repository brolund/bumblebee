package net.sf.umlspeed.svg;

import net.sf.umlspeed.entities.Class;
import net.sf.umlspeed.entities.DiagramElement;
import net.sf.umlspeed.entities.UseCase;

public class SVGUseCaseRenderer extends SVGEntity {

    private DiagramElement de = null;
    private UseCase c = null;
    
    public SVGUseCaseRenderer(Object o) {
        if (o instanceof DiagramElement) {
            de = (DiagramElement) o;
            c = (UseCase) de.getEntity();
            entity = de;
        }
        else if (o instanceof Class) {
            c = (UseCase) o;
            entity = c;
        }
        else
            throw new IllegalArgumentException("SVGClassRenderer can only accept DiagramElement or Actor");
    }
    
    protected void render() {
        
        StringBuffer s = new StringBuffer();
        s.append("<g>\n");
        
        height = getSmallTextHeight() * 3;
        width = estimateSmallTextWidth(c.getText());
        
        final int SP = 5;
        
        // Draw the drop shadow
        s.append(drawEllipse((width / 2) + SP, (height / 2) + SP, (int) (width / 1.5), height / 2, 1, SVGColours.USECASE_SHADOW, SVGColours.USECASE_SHADOW));
        
        // Draw an ellipse to the same width as the text
        s.append(drawEllipse(width / 2, height / 2, (int) (width / 1.5), height / 2, 1, SVGColours.USECASE_BACKGROUND, SVGColours.USECASE_OUTLINE));
        
        // Render the text
        s.append(drawSmallText(c.getText(), 2, height / 2, -1));
        
        s.append("</g>\n");
        
        svg = s.toString();
    }

}
