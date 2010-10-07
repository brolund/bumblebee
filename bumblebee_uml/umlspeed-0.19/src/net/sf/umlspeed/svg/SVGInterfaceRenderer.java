package net.sf.umlspeed.svg;

import java.util.Iterator;

import net.sf.umlspeed.entities.DiagramElement;
import net.sf.umlspeed.entities.Field;
import net.sf.umlspeed.entities.Interface;
import net.sf.umlspeed.entities.Operation;

public class SVGInterfaceRenderer extends SVGEntity {

    private DiagramElement de = null;
    private Interface iface = null;
    
    public SVGInterfaceRenderer(Object o) {
        if (o instanceof DiagramElement) {
            de = (DiagramElement) o;
            iface = (Interface) de.getEntity();
            entity = de;
        }
        else if (o instanceof Interface) {
            iface = (Interface) o;
            entity = iface;
        }
        else
            throw new IllegalArgumentException("SVGInterfaceRenderer can only accept DiagramElement or Interface");
    }
    
    protected void render() {
        
        StringBuffer s = new StringBuffer();
        
        // First, split the class into 2 boxes -
        // topbox is the header with the class name and superclass(es)
        // opbox is the box containing all the operations
        
        int textmargin = 2; // 2 pixels between lines of text
        
        // Get the height of the boxes
        int topbox = 0;
        int opbox = 0;
        
        // 2 lines of text, one medium, one small with text margin
        topbox = getMediumTextHeight() + getSmallTextHeight() + (textmargin * 2);
        opbox = (iface.getOperations().size() * (getSmallTextHeight() + textmargin));
        // Overall height is the height of the boxes + 3 text margins (the additional
        // height by having 1 ruled line across).
        height = topbox + opbox + (textmargin * 3);
        
        // Make an array of the operation text
        int widestLineItem = estimateMediumTextWidth(iface.getName());
        int z = 0;
        String[] operationOutput = new String[iface.getOperations().size()];
        for (Iterator i = iface.getOperations().iterator(); i.hasNext(); ) {
            Operation op = (Operation) i.next();
            String o = null;
            // Access
            if (op.getAccess() == Operation.SCOPE_PRIVATE)
                o = "-";
            else if (op.getAccess() == Operation.SCOPE_PROTECTED)
                o = "#";
            else
                o = "+";
            o += op.getName();
            // Function args
            String args = "";
            for (Iterator it = op.getArguments().iterator(); it.hasNext(); ) {
                Field f = (Field) it.next();
                if (!args.equals("")) args += ", ";
                args += f.getName() + ": " + f.getType();
            }
            o += "(" + args + ")";
            // Return Type
            o += ": " + op.getReturnType();
            operationOutput[z] = o;
            if (estimateSmallTextWidth(o) > widestLineItem) widestLineItem = estimateSmallTextWidth(o);
            z++;
        }
        
        // Set box width
        width = widestLineItem + (textmargin * 2);
        
        // Our class is a grouped object
        s.append("<g>");
        
        // Render the drop shadow rectangle
        s.append(drawRectangle(5, 5, width, height, 0, SVGColours.INTERFACE_SHADOW, SVGColours.INTERFACE_SHADOW));
        
        // Render the background rectangle
        s.append(drawRectangle(0, 0, width, height, 
                1, SVGColours.INTERFACE_BACKGROUND, SVGColours.INTERFACE_OUTLINE));
        
        // Do the parent interfaces (if there are any)
        String bases = "";
        for (Iterator it = iface.getInterfaces().iterator(); it.hasNext(); ) {
            String base = it.next().toString();
            if (!bases.equals("")) bases += ", ";
            bases += base;
        }
        if (!bases.equals("")) 
            bases = "<<" + bases + ">>";
        else
            bases = "<<interface>>";
        
        int rollingy = textmargin + getSmallTextHeight();
        s.append(drawSmallItalicText(bases, calculateSmallTextCenter(bases, width), rollingy, -1));
        rollingy += getSmallTextHeight() + textmargin;
        
        // Do the title
        int titley = rollingy;
        
        s.append(drawMediumBoldItalicText(iface.getName(), calculateMediumTextCenter(iface.getName(), width), titley, -1));
        rollingy += getMediumTextHeight() + textmargin;
        
        // Draw a line across the whole thing
        int liney = rollingy - getSmallTextHeight() + textmargin;
        s.append(drawLine(0, liney, width, liney, 1, SVGColours.INTERFACE_OUTLINE));
        rollingy += textmargin;
                
        // Do the operations
        s.append(drawListOfSmallText(operationOutput, textmargin, rollingy, textmargin));
        rollingy += opbox;
        
        s.append("</g>");
        svg = s.toString();
    }

}
