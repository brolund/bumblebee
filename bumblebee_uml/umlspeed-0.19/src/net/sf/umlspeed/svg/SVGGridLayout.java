package net.sf.umlspeed.svg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sf.umlspeed.Settings;
import net.sf.umlspeed.cli.CLI;

/**
 * Manages the layout of SVG objects in a grid of
 * X columns, with the entities specified in order
 * by the layout arguments.
 */
public class SVGGridLayout extends SVGLayout {

    // Map for object storage so that we can pull entities by their
    // names from the layout specification.
    private Map mobjects = new HashMap();
    private int noColumns = 0;
    private String[] layoutArgs = null;
    
    public SVGGridLayout(int noColumns, String[] layoutArgs) {
        this.noColumns = noColumns;
        this.layoutArgs = layoutArgs;
    }
    
    public void add(SVGEntity e) {
        mobjects.put(e.getEntity().getName(), e);
    }

    public void remove(SVGEntity e) {
        mobjects.remove(e);
    }
    
    public List getObjects() {
        ArrayList l = new ArrayList();
        for (Iterator it = mobjects.values().iterator(); it.hasNext(); )
            l.add(it.next());
        return l;
    }
    
    /** Figures out whether all components on the diagram have
     *  been used in the layout. If not, prints an error naming the
     *  components not used.
     * @return false if not all components have been used
     */
    public boolean checkAllComponentsUsed() {
        Vector objectsNotUsed = new Vector();
        for (Iterator it = mobjects.keySet().iterator(); it.hasNext(); ) {
            String ename = it.next().toString();
            boolean inLayout = false;
            for (int i = 1; i < layoutArgs.length; i++) {
                if (layoutArgs[i].equals(ename)) {
                    inLayout = true;
                    break;
                }
            }
            if (!inLayout)
                objectsNotUsed.add(ename);
        }
        if (objectsNotUsed.size() > 0) {
            Settings.errorFlag = true;
            CLI.print("GridLayout: Error - not all components have been used:");
            for (int i = 0; i < objectsNotUsed.size(); i++)
                CLI.print(objectsNotUsed.get(i).toString());
            return false;
        }
        return true;
    }
    
    public String render() {
        
        StringBuffer svg = new StringBuffer();
        if (!checkAllComponentsUsed()) {
            return "";
        }
        
        // We arrange the objects into noColumns columns and as many
        // rows as we need to display the layout args
        int noRows = (layoutArgs.length-1) / noColumns;
        
        // Make a quick pass through the objects to figure out
        // the largest item we're dealing with.
        int biggestwidth = 0;
        int biggestheight = 0;
        for (Iterator it = mobjects.values().iterator(); it.hasNext(); ) {
            SVGEntity e = (SVGEntity) it.next();
            e.setPosition(0, 0);
            if (e.getSize().width > biggestwidth)
                biggestwidth = e.getSize().width;
            if (e.getSize().height > biggestheight)
                biggestheight = e.getSize().height;
        }
        
        // The canvasmargin is the gap we allow between objects and the
        // edge of the canvas
        int canvasmargin = getCanvasMargin();
        
        // Margin is the gap (horizontal and vertical) between the nearest
        // diagram element. The fewer the elements, the larger the margin
        // should be for clarity.
        int margin = getMargin();
        
        // Do we have any packages? If so, extend the cell size so we know
        // there won't be an overlap between the biggest class and the package.
        // We can render the packages right away so they're at the bottom of
        // the z-order.
        boolean hasPackages = false; 
        for (int i = 1; i < layoutArgs.length; i++) {
            if (layoutArgs[i].startsWith("pkg:")) {
                
                if (!hasPackages) {
                    hasPackages = true;
                    // Extend the cell size - use a margin for either side, plus the
                    // height of the rendered text for the package name
                    int pkgmargin = 10;
                    biggestheight += SVGEntity.getSmallTextHeight() + (pkgmargin * 2);
                    biggestwidth += (pkgmargin * 2);
                }
                
                // Get the package arguments for where to put it
                String[] pkgArgs = split(layoutArgs[i], ":");
                if (pkgArgs.length != 6) {
                    CLI.print("pkg declaration in gridlayout requires 5 arguments - name, x, y, width, height.");
                }
                
                String name = pkgArgs[1];
                int ax = Integer.parseInt(pkgArgs[2]);
                int ay = Integer.parseInt(pkgArgs[3]);
                int aw = Integer.parseInt(pkgArgs[4]);
                int ah = Integer.parseInt(pkgArgs[5]);
                
                // Calculate real x and y positions from the cells
                int x = (canvasmargin + ((biggestwidth + margin) * ax));
                int y = (canvasmargin + ((biggestheight + margin) * ay));
                int w = ((biggestwidth + margin) * aw) - (margin / 2);
                int h = ((biggestheight + margin) * ah) - (margin / 2);
                
                // Render the package
                SVGPackageRenderer r = new SVGPackageRenderer(name, x, y, w, h);
                r.render();
                svg.append(r.getSVG());
            }
        }
        
        // Render the objects
        int x = canvasmargin;
        int y = canvasmargin;
        int curcol = 1;
        for (int i = 1; i < layoutArgs.length; i++) {
            
            if (!layoutArgs[i].startsWith("pkg")) {
                if (!layoutArgs[i].equals("*")) {
                    SVGEntity e = (SVGEntity) mobjects.get(layoutArgs[i]);
                    
                    // If we have no entity, someone's made a booboo and not included
                    // it in the entities list.
                    if (e == null) {
                        CLI.print("SVGGridLayout: Layout specifies entity '" + 
                             layoutArgs[i] + "' but it doesn't exist in the entities list.");
                        if (Settings.standalone)
                            System.exit(1);
                        return "";
                    }
                    e.setPosition(centerInCell(new SVGDimension(biggestwidth, biggestheight), e.getSize(), new SVGPosition(x, y)));
                    svg.append(e.getSVG());
                }
                
                curcol++;
                if (curcol > noColumns) {
                    curcol = 1;
                    x = canvasmargin;
                    y += margin + biggestheight;
                }
                else {
                    x += margin + biggestwidth;
                }
            }
        }
        
        // Set overall width/height (might want them later)
        size.width = (canvasmargin * 2) + (noColumns * (biggestwidth + margin));
        size.height = (canvasmargin * 2) + (noRows * (biggestheight + margin));
        
        return svg.toString();
    }

    /**
     * Splits a string by a particular char and returns an array of 
     * strings. If there are no occurrences of the split char, the
     * original string is returned in an array of 1 item.
     * @param splitstring The string to be split
     * @param splitchar The char to split on
     * @return An array of strings
     */
    protected String[] split(String splitstring, String splitchar) {
       
        splitstring = splitstring.trim();
        
        // If there is only one element, just return that
        if (splitstring.indexOf(splitchar) == -1) {
            String[] rets = new String[1];
            rets[0] = splitstring;
            return rets;
        }
        
        // Find how many there are
        int tot = 0;
        int lpos = splitstring.indexOf(splitchar);
        while (lpos != -1) {
            tot++;
            lpos = splitstring.indexOf(splitchar, lpos + 1);
        }
        tot++;
        
        // Create our new array
        String[] rets = new String[tot];
        tot = 0;
        lpos = 0;
        int spos = splitstring.indexOf(splitchar);
        while (spos != -1) {
            // Add into the array
            rets[tot] = splitstring.substring(lpos, spos);
            tot++;
            lpos = spos + 1;
            spos = splitstring.indexOf(splitchar, lpos);
        }
        
        // Include last word
        rets[tot] = splitstring.substring(lpos, splitstring.length());
        
        // Return it
        return rets;
    }
    
}
