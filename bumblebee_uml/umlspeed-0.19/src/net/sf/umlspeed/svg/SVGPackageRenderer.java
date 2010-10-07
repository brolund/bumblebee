package net.sf.umlspeed.svg;

/**
 * Renders a package
 */
public class SVGPackageRenderer extends SVGEntity {

    private String name = "";
    
    public SVGPackageRenderer(String name, int x, int y, int w, int h) {
        this.name = name;
        setPosition(x, y);
        setSize(w, h);
    }
    
    protected void render() {
        StringBuffer s = new StringBuffer();
        s.append("<g>\n");
        
        int textmargin = 4;
        int boxoffset = 5;
        
        SVGDimension labelbox = new SVGDimension(estimateSmallTextWidth(name) + (textmargin * 2), getSmallTextHeight() + (textmargin * 2));
        SVGDimension packagebox = new SVGDimension(getSize().width, getSize().height - labelbox.height);
        SVGPosition packagepos = new SVGPosition(0, labelbox.height + 1);
        
        // Render the package label box shadow
        s.append(drawRectangle(boxoffset, boxoffset, labelbox.width, labelbox.height, 1, SVGColours.PACKAGE_SHADOW, SVGColours.PACKAGE_SHADOW));
        
        // Render the box shadow
        s.append(drawRectangle(packagepos.x + boxoffset, packagepos.y + boxoffset, packagebox.width, packagebox.height, 1, SVGColours.PACKAGE_SHADOW, SVGColours.PACKAGE_SHADOW));
        
        // Render the label box
        s.append(drawRectangle(0, 0, labelbox.width, labelbox.height, 1, SVGColours.PACKAGE_LABEL_BACKGROUND, SVGColours.PACKAGE_OUTLINE));
        s.append(drawSmallBoldItalicText(name, textmargin, textmargin + getSmallTextHeight(), -1));
        
        // Render the package box
        s.append(drawRectangle(packagepos.x, packagepos.y, packagebox.width, packagebox.height, 1, SVGColours.PACKAGE_BACKGROUND, SVGColours.PACKAGE_OUTLINE));
        
        s.append("</g>\n");
        svg = s.toString();
    }

}
