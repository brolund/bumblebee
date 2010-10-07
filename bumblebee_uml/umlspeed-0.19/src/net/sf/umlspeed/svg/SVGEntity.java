package net.sf.umlspeed.svg;

import java.text.MessageFormat;

import net.sf.umlspeed.entities.Entity;

/**
 * Base class of SVG objects that wrap an entity and can render it.
 */
public abstract class SVGEntity {

    protected int x = 0;

    protected int y = 0;

    protected int width = 0;

    protected int height = 0;

    protected Entity entity = null;

    /** Number of links attached to the 4 connectors on this entity */
    protected int toplinks = 0;

    protected int botlinks = 0;

    protected int leftlinks = 0;

    protected int rightlinks = 0;

    /** The rendered SVG */
    protected String svg = "";

    /**
     * Render to the svg string buffer. It's this method's responsibility to
     * update the width and height values.
     */
    protected abstract void render();

    /** Return the rendered size of this entity */
    public SVGDimension getSize() {
        return new SVGDimension(width, height);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /** Return the x/y position of this entity */
    public SVGPosition getPosition() {
        return new SVGPosition(x, y);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        render();
    }
    
    public void setPosition(SVGPosition p) {
        setPosition(p.x, p.y);
    }

    /**
     * @return the entity
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * @param entity
     *            the entity to set
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public String getSVG() {
        return svg;
    }

    /** Tiny font height */
    public final static int TINY_HEIGHT = 8;
    
    /** Small font height */
    public final static int SMALL_HEIGHT = 12;

    /** Medium font height */
    public final static int MEDIUM_HEIGHT = 16;

    /** Render a line relative to current position */
    protected String drawLine(int x1, int y1, int x2, int y2, int strokewidth,
            String strokecolour) {
        return MessageFormat.format(
                "<line x1=\"{0}\" y1=\"{1}\" x2=\"{2}\" y2=\"{3}\""
                        + " style=\"stroke:{5};stroke-width:{4}\" />\n",
                new Object[] { Integer.toString(this.x + x1),
                        Integer.toString(this.y + y1),
                        Integer.toString(this.x + x2),
                        Integer.toString(this.y + y2),
                        Integer.toString(strokewidth), strokecolour });
    }

    /** Render a dotted line relative to current position */
    protected String drawDottedLine(int x1, int y1, int x2, int y2,
            int strokewidth, String strokecolour) {
        return MessageFormat
                .format(
                        "<line x1=\"{0}\" y1=\"{1}\" x2=\"{2}\" y2=\"{3}\""
                                + " style=\"stroke-dasharray: 9, 5; stroke:{5};stroke-width:{4}\" />\n",
                        new Object[] { Integer.toString(this.x + x1),
                                Integer.toString(this.y + y1),
                                Integer.toString(this.x + x2),
                                Integer.toString(this.y + y2),
                                Integer.toString(strokewidth), strokecolour });
    }

    /** Render a rectangle relative to current position */
    protected String drawRectangle(int x, int y, int width, int height,
            int strokewidth, String fillcolour, String strokecolour) {
        return MessageFormat
                .format(
                        "<rect width=\"{0}\" height=\"{1}\" x=\"{2}\" y=\"{3}\""
                                + " style=\"fill:{4}; stroke-width: {5}; stroke:{6}\" />\n",
                        new Object[] { Integer.toString(width),
                                Integer.toString(height),
                                Integer.toString(this.x + x),
                                Integer.toString(this.y + y), fillcolour,
                                Integer.toString(strokewidth), strokecolour });
    }
    
    /** Render a rectangle relative to current position */
    protected String drawEmptyRectangle(int x, int y, int width, int height,
            int strokewidth, String strokecolour) {
        return MessageFormat
                .format(
                        "<rect width=\"{0}\" height=\"{1}\" x=\"{2}\" y=\"{3}\""
                                + " style=\"fill: none; stroke-width: {4}; stroke:{5}\" />\n",
                        new Object[] { Integer.toString(width),
                                Integer.toString(height),
                                Integer.toString(this.x + x),
                                Integer.toString(this.y + y),
                                Integer.toString(strokewidth), strokecolour });
    }

    /** Render a rectangle relative to current position */
    protected String drawCircle(int x, int y, int radius, int strokewidth,
            String fillcolour, String strokecolour) {
        return MessageFormat
                .format(
                        "<circle cx=\"{0}\" cy=\"{1}\" r=\"{2}\""
                                + " style=\"fill:{3}; stroke-width: {4}; stroke:{5}\" />\n",
                        new Object[] { Integer.toString(this.x + x),
                                Integer.toString(this.y + y),
                                Integer.toString(radius), fillcolour,
                                Integer.toString(strokewidth), strokecolour });
    }

    /** Render a rectangle relative to current position */
    protected String drawEllipse(int x, int y, int xradius, int yradius,
            int strokewidth, String fillcolour, String strokecolour) {
        return MessageFormat
                .format(
                        "<ellipse cx=\"{0}\" cy=\"{1}\" rx=\"{2}\" ry=\"{3}\""
                                + " style=\"fill:{4}; stroke-width: {5}; stroke:{6}\" />\n",
                        new Object[] { Integer.toString(this.x + x),
                                Integer.toString(this.y + y),
                                Integer.toString(xradius),
                                Integer.toString(yradius), fillcolour,
                                Integer.toString(strokewidth), strokecolour });
    }
    
    /** Render a right-pointing equilateral triangle. X/Y is the top left
     *  corner of the triangle.
     * @param x
     * @param y
     * @param size
     * @return
     */
    protected String drawRightEquiTriangle(int x, int y, int size, 
            int strokewidth, String fillcolour, String strokecolour) {
        return MessageFormat
            .format(
                    "<polygon points=\"{0},{1} {2},{3} {4},{5}\""
                        + " style=\"fill:{6}; stroke-width: {7}; stroke:{8}\" />\n",
                   new Object[] {
                       Integer.toString(this.x + x),
                       Integer.toString(this.y + y),
                       Integer.toString(this.x + x),
                       Integer.toString(this.y + y + size),
                       Integer.toString(this.x + x + size),
                       Integer.toString(this.y + y + (size / 2)),
                       fillcolour,
                       Integer.toString(strokewidth),
                       strokecolour
                   });
    }
    
    /** Render a left-pointing equilateral triangle. X/Y is the top right
     *  corner of the triangle.
     * @param x
     * @param y
     * @param size
     * @return
     */
    protected String drawLeftEquiTriangle(int x, int y, int size, 
            int strokewidth, String fillcolour, String strokecolour) {
        return MessageFormat
            .format(
                    "<polygon points=\"{0},{1} {2},{3} {4},{5}\""
                        + " style=\"fill:{6}; stroke-width: {7}; stroke:{8}\" />\n",
                   new Object[] {
                       Integer.toString(this.x + x),
                       Integer.toString(this.y + y),
                       Integer.toString(this.x + x),
                       Integer.toString(this.y + y + size),
                       Integer.toString(this.x + x - size),
                       Integer.toString(this.y + y + (size / 2)),
                       fillcolour,
                       Integer.toString(strokewidth),
                       strokecolour
                   });
    }

    /**
     * Render some text. if width is non-zero, will try to fit text to that
     * width with spacing (but not glyph-stretching/squashing)
     * 
     * @param text
     * @param x
     * @param y
     * @param width
     * @param size
     * @param bold
     * @param italic
     * @return
     */
    protected String drawText(String text, int x, int y, int width, int size,
            boolean bold, boolean italic) {
        text = escapeText(text);
        String textLength = "";
        String style = "";
        if (width > 0)
            textLength = "textLength=\"" + width + "\"";
        style = "font-family: sans-serif; ";
        if (bold)
            style += "font-weight: bold; ";
        if (italic)
            style += "font-style: italic";
        return MessageFormat.format("<text x=\"{0}\" y=\"{1}\" "
                + "font-size=\"{2}\" {3} style=\"{4}\">{5}</text>\n",
                new Object[] { Integer.toString(this.x + x),
                        Integer.toString(this.y + y), Integer.toString(size),
                        textLength, style, text });
    }

    protected String drawTinyText(String text, int x, int y, int width) {
        return drawText(text, x, y, width, TINY_HEIGHT, false, false);
    }
    
    protected String drawSmallText(String text, int x, int y, int width) {
        return drawText(text, x, y, width, SMALL_HEIGHT, false, false);
    }

    protected String drawSmallBoldText(String text, int x, int y, int width) {
        return drawText(text, x, y, width, SMALL_HEIGHT, true, false);
    }

    protected String drawSmallItalicText(String text, int x, int y, int width) {
        return drawText(text, x, y, width, SMALL_HEIGHT, false, true);
    }

    protected String drawSmallBoldItalicText(String text, int x, int y,
            int width) {
        return drawText(text, x, y, width, SMALL_HEIGHT, true, true);
    }

    protected String drawMediumText(String text, int x, int y, int width) {
        return drawText(text, x, y, width, MEDIUM_HEIGHT, false, false);
    }

    protected String drawMediumBoldText(String text, int x, int y, int width) {
        return drawText(text, x, y, width, MEDIUM_HEIGHT, true, false);
    }

    protected String drawMediumItalicText(String text, int x, int y, int width) {
        return drawText(text, x, y, width, MEDIUM_HEIGHT, false, true);
    }

    protected String drawMediumBoldItalicText(String text, int x, int y,
            int width) {
        return drawText(text, x, y, width, MEDIUM_HEIGHT, true, true);
    }

    /**
     * Draws a list of text items, starting at x and y and using the line
     * spacing specified in pixels.
     * 
     * @param items
     * @param x
     * @param y
     * @param linemargin
     * @return
     */
    protected String drawListOfSmallText(String[] items, int x, int y,
            int linemargin) {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < items.length; i++) {
            s.append(drawSmallText(items[i], x, y, -1));
            y += getSmallTextHeight() + linemargin;
        }
        return s.toString();
    }

    /** The height of one line of tiny text */
    public static int getTinyTextHeight() {
        return TINY_HEIGHT + (int) ((double) TINY_HEIGHT * 0.2);
    }
    
    /** The height of one line of small text */
    public static int getSmallTextHeight() {
        return SMALL_HEIGHT + (int) ((double) SMALL_HEIGHT * 0.2);
    }

    /** The height of one line of medium text */
    public static int getMediumTextHeight() {
        return MEDIUM_HEIGHT + (int) ((double) SMALL_HEIGHT * 0.2);
    }

    /**
     * Produces an estimate in pixel size for the width of text at a given
     * fontsize. This is pretty crude, but good enough given that we don't
     * know exactly how the SVG client is going to interpret it.
     * 
     * @param text
     * @param fontsize
     * @return
     */
    protected int estimateTextWidth(String text, int fontsize) {
        final String thinchars = "1lij|tI;:";
        final String thickchars = "<>";
        int THIN = (int) ((double) fontsize * (double) 0.5);
        int THICK = (int) ((double) fontsize * (double) 0.8);
        int NORMAL = (int) ((double) fontsize * (double) 0.7);
        int ts = 0;
        for (int i = 0; i < text.length(); i++) {
            String s = text.substring(i, i+1);
            if (thinchars.indexOf(s) != -1)
                ts += THIN;
            else if (s.equals(s.toUpperCase()) || thickchars.indexOf(s) != -1)
                ts += THICK;
            else
                ts += NORMAL;
        }
        return ts;
    }

    protected int estimateTinyTextWidth(String text) {
        return estimateTextWidth(text, TINY_HEIGHT);
    }
    
    protected int estimateSmallTextWidth(String text) {
        return estimateTextWidth(text, SMALL_HEIGHT);
    }

    protected int estimateMediumTextWidth(String text) {
        return estimateTextWidth(text, MEDIUM_HEIGHT);
    }

    /**
     * Calculates the center position for the text given and the width of the
     * canvas to center on.
     * 
     * @param text
     * @param width
     * @return
     */
    protected int calculateSmallTextCenter(String text, int width) {
        return calculateTextCenter(text, width, SMALL_HEIGHT);
    }

    protected int calculateTinyTextCenter(String text, int width) {
        return calculateTextCenter(text, width, TINY_HEIGHT);
    }
    
    protected int calculateMediumTextCenter(String text, int width) {
        return calculateTextCenter(text, width, MEDIUM_HEIGHT);
    }

    protected int calculateTextCenter(String text, int width, int fontsize) {
        int textwidth = estimateTextWidth(text, fontsize);
        int newx = width / 2 - (textwidth / 2);
        if (newx < 2)
            newx = 2;
        return newx;
    }

    /**
     * Tests if the specified line intersects the interior of this entity.
     * This code (and the following 3 functions) were lifted from the
     * GNU ClassPath awt.geom code for Rectangle2D and Line2D. Thanks
     * guys, you saved me some real fucking about (I hate 2D math).
     * 
     * @param x1
     *            the first x coordinate of line segment
     * @param y1
     *            the first y coordinate of line segment
     * @param x2
     *            the second x coordinate of line segment
     * @param y2
     *            the second y coordinate of line segment
     * @return true if the line intersects this entity
     */
    public boolean intersects(double x1, double y1, double x2, double y2) {
        double xt = (double) this.x;
        double yt = (double) this.y;
        double w = (double) this.width;
        double h = (double) this.height;

        if (w <= 0 || h <= 0)
            return false;

        if (x1 >= xt && x1 <= xt + w && y1 >= yt && y1 <= yt + h)
            return true;
        if (x2 >= xt && x2 <= xt + w && y2 >= yt && y2 <= yt + h)
            return true;

        double x3 = xt + w;
        double y3 = yt + h;

        return (linesIntersect(x1, y1, x2, y2, xt, yt, xt, y3)
                || linesIntersect(x1, y1, x2, y2, xt, y3, x3, y3)
                || linesIntersect(x1, y1, x2, y2, x3, y3, x3, yt) || linesIntersect(
                x1, y1, x2, y2, x3, yt, xt, yt));

    }

    private boolean between(double x1, double y1, double x2, double y2,
            double x3, double y3) {
        if (x1 != x2) {
            return (x1 <= x3 && x3 <= x2) || (x1 >= x3 && x3 >= x2);
        }
        else {
            return (y1 <= y3 && y3 <= y2) || (y1 >= y3 && y3 >= y2);
        }
    }

    private double area2(double x1, double y1, double x2, double y2, double x3,
            double y3) {
        return (x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1);
    }

    private boolean linesIntersect(double x1, double y1, double x2, double y2,
            double x3, double y3, double x4, double y4) {
        double a1, a2, a3, a4;

        // deal with special cases
        if ((a1 = area2(x1, y1, x2, y2, x3, y3)) == 0.0) {
            // check if p3 is between p1 and p2 OR
            // p4 is collinear also AND either between p1 and p2 OR at opposite
            // ends
            if (between(x1, y1, x2, y2, x3, y3)) {
                return true;
            }
            else {
                if (area2(x1, y1, x2, y2, x4, y4) == 0.0) {
                    return between(x3, y3, x4, y4, x1, y1)
                            || between(x3, y3, x4, y4, x2, y2);
                }
                else {
                    return false;
                }
            }
        }
        else if ((a2 = area2(x1, y1, x2, y2, x4, y4)) == 0.0) {
            // check if p4 is between p1 and p2 (we already know p3 is not
            // collinear)
            return between(x1, y1, x2, y2, x4, y4);
        }

        if ((a3 = area2(x3, y3, x4, y4, x1, y1)) == 0.0) {
            // check if p1 is between p3 and p4 OR
            // p2 is collinear also AND either between p1 and p2 OR at opposite
            // ends
            if (between(x3, y3, x4, y4, x1, y1)) {
                return true;
            }
            else {
                if (area2(x3, y3, x4, y4, x2, y2) == 0.0) {
                    return between(x1, y1, x2, y2, x3, y3)
                            || between(x1, y1, x2, y2, x4, y4);
                }
                else {
                    return false;
                }
            }
        }
        else if ((a4 = area2(x3, y3, x4, y4, x2, y2)) == 0.0) {
            // check if p2 is between p3 and p4 (we already know p1 is not
            // collinear)
            return between(x3, y3, x4, y4, x2, y2);
        }
        else { // test for regular intersection
            return ((a1 > 0.0) ^ (a2 > 0.0)) && ((a3 > 0.0) ^ (a4 > 0.0));
        }
    }

    /** Escapes XML text */
    protected String escapeText(String s) {
        s = replace(s, "&", "&amp;");
        s = replace(s, ">", "&gt;");
        s = replace(s, "<", "&lt;");
        s = replace(s, "'", "&apos;");
        s = replace(s, "\"", "&quot;");
        return s;
    }
    
    /** Looks in findin for all occurrences of find and replaces them with replacewith 
     * @param findin The string to find occurrences in
     * @param find The string to find
     * @param replacewith The string to replace found occurrences with
     * @return A string with all occurrences of find replaced.
     */
    protected String replace(String findin, String find, String replacewith) {
        
        StringBuffer sb = new StringBuffer(findin);
        int i = 0;
        try {
            while (i <= sb.length() - find.length()) {
                if (sb.substring(i, i + find.length()).equalsIgnoreCase(find)) {
                    sb.replace(i, i + find.length(), replacewith);
                }
                i++;
            }
        }
        catch (StringIndexOutOfBoundsException e) {
            // We hit the end of the string - do nothing and carry on
        }
        return sb.toString();
    }
}
