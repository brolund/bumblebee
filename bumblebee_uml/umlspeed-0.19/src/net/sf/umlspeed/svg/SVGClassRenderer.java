package net.sf.umlspeed.svg;

import java.util.Iterator;

import net.sf.umlspeed.entities.Class;
import net.sf.umlspeed.entities.DiagramElement;
import net.sf.umlspeed.entities.Field;
import net.sf.umlspeed.entities.Operation;

public class SVGClassRenderer extends SVGEntity {

    private DiagramElement de = null;
    private Class cl = null;
    
    public SVGClassRenderer(Object o) {
        if (o instanceof DiagramElement) {
            de = (DiagramElement) o;
            cl = (Class) de.getEntity();
            entity = de;
        }
        else if (o instanceof Class) {
            cl = (Class) o;
            entity = cl;
        }
        else
            throw new IllegalArgumentException("SVGClassRenderer can only accept DiagramElement or Class");
    }
    
    protected void render() {
        
        StringBuffer s = new StringBuffer();
        
        // First, split the class into 3 boxes -
        // topbox is the header with the class name and superclass(es)
        // fieldbox is the box containing all the fields
        // opbox is the box containing all the operations
        
        int textmargin = 2; // 2 pixels between lines of text
        
        // Get the height of the boxes
        int topbox = 0;
        int fieldbox = 0;
        int opbox = 0;
        
        // 2 lines of text, one medium, one small with text margin
        topbox = getMediumTextHeight() + getSmallTextHeight() + (textmargin * 2);
        fieldbox = (cl.getFields().size() * (getSmallTextHeight() + textmargin));
        opbox = (cl.getOperations().size() * (getSmallTextHeight() + textmargin));
        // Overall height is the height of the boxes + 5 text margins (the additional
        // height by having 2 ruled lines across).
        height = topbox + fieldbox + opbox + (textmargin * 5);
        
        // Make two string arrays of the fields and operation text
        int widestLineItem = estimateMediumTextWidth(cl.getName());
        String[] fieldOutput = new String[cl.getFields().size()];
        int z = 0;
        for (Iterator i = cl.getFields().iterator(); i.hasNext(); ) {
            Field fl = (Field) i.next();
            String f = null;
            // Access
            if (fl.getAccess() == Field.SCOPE_PRIVATE)
                f = "-";
            else if (fl.getAccess() == Field.SCOPE_PROTECTED)
                f = "#";
            else
                f = "+";
            // Name
            f += fl.getName();
            // Type
            f += ": " + fl.getType();
            fieldOutput[z] = f;
            if (estimateSmallTextWidth(f) > widestLineItem) widestLineItem = estimateSmallTextWidth(f);
            z++;
        }
        
        z = 0;
        String[] operationOutput = new String[cl.getOperations().size()];
        for (Iterator i = cl.getOperations().iterator(); i.hasNext(); ) {
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
        s.append(drawRectangle(5, 5, width, height, 0, SVGColours.CLASS_SHADOW, SVGColours.CLASS_SHADOW));
        
        // Render the background rectangle
        s.append(drawRectangle(0, 0, width, height, 
                1, SVGColours.CLASS_BACKGROUND, SVGColours.CLASS_OUTLINE));
        
        // Do the superclasses (if there are any)
        String bases = "";
        for (Iterator it = cl.getBaseClasses().iterator(); it.hasNext(); ) {
            String base = it.next().toString();
            if (!bases.equals("")) bases += ", ";
            bases += base;
        }
        if (!bases.equals("")) bases = "<<" + bases + ">>";
        int rollingy = textmargin + getSmallTextHeight();
        s.append(drawSmallItalicText(bases, calculateSmallTextCenter(bases, width), rollingy, -1));
        rollingy += getSmallTextHeight() + textmargin;
        
        // Do the title
        int titley = rollingy;
        // If there weren't any superclasses, move the title up by half
        // the height of the superclass line to fill the space
        if (bases.equals("")) {
            titley = rollingy - (getSmallTextHeight() / 2);            
        }
        
        // If the class is abstract, use italics for its title
        if (cl.isAbstract())
            s.append(drawMediumBoldItalicText(cl.getName(), calculateMediumTextCenter(cl.getName(), width), titley, -1));
        else
            s.append(drawMediumBoldText(cl.getName(), calculateMediumTextCenter(cl.getName(), width), titley, -1));
        rollingy += getMediumTextHeight() + textmargin;
        
        // Draw a line across the whole thing
        int liney = rollingy - getSmallTextHeight() + textmargin;
        s.append(drawLine(0, liney, width, liney, 1, SVGColours.CLASS_OUTLINE));
        rollingy += textmargin;
        
        // Do the list of fields
        s.append(drawListOfSmallText(fieldOutput, textmargin, rollingy, textmargin));
        rollingy += fieldbox;
        
        // Draw a line across
        liney = rollingy - getSmallTextHeight() + textmargin;
        s.append(drawLine(0, liney, width, liney, 1, SVGColours.CLASS_OUTLINE));
        rollingy += textmargin;
        
        // And the operations
        s.append(drawListOfSmallText(operationOutput, textmargin, rollingy, textmargin));
        rollingy += opbox;
        
        s.append("</g>");
        svg = s.toString();
    }

}
