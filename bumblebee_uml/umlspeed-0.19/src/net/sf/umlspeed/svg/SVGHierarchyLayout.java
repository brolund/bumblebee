package net.sf.umlspeed.svg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sf.umlspeed.cli.CLI;
import net.sf.umlspeed.entities.DiagramElement;
import net.sf.umlspeed.entities.DiagramLink;

/**
 * An automatic layout that places objects into a hierarchy.
 * A number of row collections are created, containing all the
 * items that should appear on that row.
 * 
 * 1. Objects are ordered so that objects with the most links
 *    away appear first.
 *    
 * 2. The object tree is traversed and links followed. Spaces
 *    are inserted after objects that have links according to
 *    the number of links (to make sure related objects appear
 *    close together). A new row is started for every depth
 *    of link traversed.
 *    
 * 3. The rows are looped through in reverse order so that
 *    the most linked to items appear at the bottom.
 * 
 * We use the widest row as the width for all rows, and the
 * items are put in cells the size of the widest entity.
 * 
 * Eg:
 * 
 *   Entity 2     Entity 3    Entity 5    Entity 6
 *       |        /      \        |       /   
 *   Entity 1                 Entity 4
 * 
 */
public class SVGHierarchyLayout extends SVGLayout {

    private Vector rows = new Vector();
    private Map usedEntities = new HashMap();
    
    public final static int BOTTOM_TO_TOP = 0;
    public final static int TOP_TO_BOTTOM = 1;
    public final static int LEFT_TO_RIGHT = 2;
    public final static int RIGHT_TO_LEFT = 3;
    
    private int orientation = BOTTOM_TO_TOP;
    
    public SVGHierarchyLayout() {
    }
    
    public SVGHierarchyLayout(int orientation) {
        this.orientation = orientation;
    }
    
    /**
     * Does the layout of the individual rows.
     */
    private void layoutRows() {
        int row = 0;
        HierarchyRow r = new HierarchyRow();
        rows.add(r);
        for (Iterator it = getObjects().iterator(); it.hasNext(); ) {
            SVGEntity e = (SVGEntity) it.next();
            DiagramElement de = (DiagramElement) e.getEntity();
            // Add it to the row if we haven't already added it somewhere else
            if (!usedEntities.containsKey(e.getEntity().getName())) {
                r.entities.add(e);
                usedEntities.put(e.getEntity().getName(), e);
            }
            // If it has child links, add them on a new row
            // and add a null item to the current row for each one
            if (de.getLinks().size() > 0) {
                for (int i = 0; i < de.getLinks().size()-1; i++) { r.entities.add(null); }
                traverse(de.getLinks(), row);
            }
        }
        stripRow(r);
    }
    
    private void traverse(List elements, int row) {
        row++;
        HierarchyRow r = null;
        if (rows.size() < (row+1)) {
            r = new HierarchyRow();
            rows.add(r);
        }
        else {
            r = (HierarchyRow) rows.get(row);
        }
        for (Iterator it = elements.iterator(); it.hasNext(); ) {
            DiagramElement en = ((DiagramLink) it.next()).getTargetEntity();
            SVGEntity e = getSVGEntity(en);
            if (e != null) {
                if (!usedEntities.containsKey(e.getEntity().getName())) {
                    r.entities.add(e);
                    usedEntities.put(e.getEntity().getName(), e);
                }
                if (en.getLinks().size() > 0) {
                    for (int i = 0; i < en.getLinks().size()-1; i++) { r.entities.add(null); }
                    traverse(en.getLinks(), row);
                }
            }
        }
        stripRow(r);
    }
    
    /** Removes any null objects from the end of the row */
    private void stripRow(HierarchyRow r) {
        for (int i = r.entities.size() - 1; i >= 0; i--) {
            if (r.entities.get(i) == null)
                r.entities.remove(i);
            else
                break;
        }
    }
    
    /** Returns the SVGEntity wrapper for a diagram element */
    private SVGEntity getSVGEntity(DiagramElement e) {
        for (int i = 0; i < objects.size(); i++) {
            SVGEntity en = (SVGEntity) objects.get(i);
            if (en.getEntity().getName().equals(e.getName()))
                return en;
        }
        return null;
    }
    
    private int getNumberOfLinks(SVGEntity e) {
        return ((DiagramElement) e.getEntity()).getLinks().size();
    }
    
    /** Reorders the objects so that the entities
     *  with the most links AWAY appear first (uses a simple
     *  bubblesort).
     */
    private void reorderLinksAway() {
        Object[] newList = objects.toArray();
        boolean swapped = true;
        while (swapped) {
            swapped = false;
            for (int i = 0; i < newList.length-1; i++) {
                SVGEntity o1 = (SVGEntity) newList[i];
                SVGEntity o2 = (SVGEntity) newList[i+1];
                if (
                    ((DiagramElement) o1.getEntity()).getLinks().size() <
                    ((DiagramElement) o2.getEntity()).getLinks().size()
                    ) {
                    swapped = true;
                    newList[i] = o2;
                    newList[i+1] = o1;
                }
            }
        }
        objects = new ArrayList();
        CLI.print("SVGHierarchyLayout: Resorted objects for layout ======", 3);
        for (int i = 0; i < newList.length; i++) {
            objects.add(newList[i]);
            CLI.print("SVGHierarchyLayout: " + ((SVGEntity) newList[i]).getEntity().getName() + " (" + getNumberOfLinks((SVGEntity) newList[i]) + " links)", 3);
        }
    }
    
    public String render() {
        
        // Reorder objects with most links first
        reorderLinksAway();
        
        // Get our collection of rows together
        layoutRows();
        
        switch (orientation) {
            case (BOTTOM_TO_TOP): return renderBottomToTop();
            case (TOP_TO_BOTTOM): return renderTopToBottom();
            case (LEFT_TO_RIGHT): return renderLeftToRight();
            case (RIGHT_TO_LEFT): return renderRightToLeft();
        }
        CLI.print("Invalid orientation specified for hierarchy layout.");
        System.exit(1);
        return "";
    }
        
        
    private String renderBottomToTop() {
        
        StringBuffer svg = new StringBuffer();
        
        int margin = getMargin();
        int canvasmargin = getCanvasMargin();
        int widestitem = 0;
        int nocols = 0;
        
        // Calculate the overall width and height of the layout
        size.height = (canvasmargin * 2);
        size.width = 0;
        CLI.print("SVGHierarchyLayout: Hierarchy Rows (" + rows.size() + ") ============== ", 2);
        for (int i = 0; i < rows.size(); i++) {
            HierarchyRow r = (HierarchyRow) rows.get(i);
            if (r.getWidestItem() > widestitem) widestitem = r.getWidestItem();
            if (r.entities.size() > nocols) nocols = r.entities.size();
            size.height += r.getHeight() + margin;
            CLI.print("SVGHierarchyLayout: " + r.toString(), 2);
        }
        size.width = (canvasmargin * 2) + (nocols * (widestitem + getMargin()));
        
        // Draw each row
        int x, y = canvasmargin;
        for (int i = rows.size()-1; i >= 0; i--) {
            
            HierarchyRow r = (HierarchyRow) rows.get(i);
            
            x = canvasmargin;
            
            // Draw each item on the row
            for (int z = 0; z < r.entities.size(); z++) {
                SVGEntity e = (SVGEntity) r.entities.get(z);
                if (e != null) {
                    e.setPosition(centerInCell(
                        new SVGDimension(widestitem, r.getHeight()),
                        e.getSize(), 
                        new SVGPosition(x, y)));
                    svg.append(e.getSVG());
                }
                // Move along for the next item
                x += margin + widestitem;
            }
            
            // Drop to the next row
            y += margin + r.getHeight();
            
        }
        
        return svg.toString();
    }
    
    private String renderTopToBottom() {
        
        StringBuffer svg = new StringBuffer();
        
        int margin = getMargin();
        int canvasmargin = getCanvasMargin();
        int widestitem = 0;
        int nocols = 0;
        
        // Calculate the overall width and height of the layout
        size.height = (canvasmargin * 2);
        size.width = 0;
        CLI.print("SVGHierarchyLayout: Hierarchy Rows ================= ", 2);
        for (int i = 0; i < rows.size(); i++) {
            HierarchyRow r = (HierarchyRow) rows.get(i);
            if (r.getWidestItem() > widestitem) widestitem = r.getWidestItem();
            if (r.entities.size() > nocols) nocols = r.entities.size();
            size.height += r.getHeight() + margin;
            CLI.print("SVGHierarchyLayout: " + r.toString(), 2);
        }
        size.width = (canvasmargin * 2) + (nocols * (widestitem + getMargin()));
        
        // Draw each row
        int x, y = canvasmargin;
        for (int i = 0; i < rows.size(); i++) {
            
            HierarchyRow r = (HierarchyRow) rows.get(i);
            
            x = canvasmargin;
            
            // Draw each item on the row
            for (int z = 0; z < r.entities.size(); z++) {
                SVGEntity e = (SVGEntity) r.entities.get(z);
                if (e != null) {
                    e.setPosition(centerInCell(
                        new SVGDimension(widestitem, r.getHeight()),
                        e.getSize(), 
                        new SVGPosition(x, y)));
                    svg.append(e.getSVG());
                }
                // Move along for the next item
                x += margin + widestitem;
            }
            
            // Drop to the next row
            y += margin + r.getHeight();
            
        }
        
        return svg.toString();
    }
    
    private String renderLeftToRight() {
        
        StringBuffer svg = new StringBuffer();
        
        int margin = getMargin();
        int canvasmargin = getCanvasMargin();
        int highestitem = 0;
        int widestitem = 0;
        int norows = 0;
        int nocols = 0;
        
        // Calculate the overall width and height of the layout
        size.height = 0;
        size.width = (canvasmargin * 2);
        CLI.print("SVGHierarchyLayout: Hierarchy Rows ================= ", 2);
        for (int i = 0; i < rows.size(); i++) {
            HierarchyRow r = (HierarchyRow) rows.get(i);
            if (r.getHeight() > highestitem) highestitem = r.getHeight();
            if (r.getWidestItem() > widestitem) widestitem = r.getWidestItem();
            if (r.entities.size() > norows) norows = r.entities.size();
            CLI.print("SVGHierarchyLayout: " + r.toString(), 2);
        }
        nocols = rows.size();
        size.width = (canvasmargin * 2) + (nocols * (widestitem + getMargin()));
        size.height = (canvasmargin * 2) + (norows * (highestitem + getMargin()));
        
        // Draw each row (each row becomes a column instead in left to right)
        int y, x = canvasmargin;
        for (int i = 0; i < rows.size(); i++) {
            
            HierarchyRow r = (HierarchyRow) rows.get(i);
            
            y = canvasmargin;
            
            // Draw each item in the column
            for (int z = 0; z < r.entities.size(); z++) {
                SVGEntity e = (SVGEntity) r.entities.get(z);
                if (e != null) {
                    e.setPosition(centerInCell(
                        new SVGDimension(widestitem, highestitem),
                        e.getSize(), 
                        new SVGPosition(x, y)));
                    svg.append(e.getSVG());
                }
                // Drop down for the next item
                y += margin + highestitem;
            }
            
            // Move to the next column
            x += margin + widestitem;
            
        }
        
        return svg.toString();
    }
    
    private String renderRightToLeft() {
        
        StringBuffer svg = new StringBuffer();
        
        int margin = getMargin();
        int canvasmargin = getCanvasMargin();
        int highestitem = 0;
        int widestitem = 0;
        int norows = 0;
        int nocols = 0;
        
        // Calculate the overall width and height of the layout
        size.height = 0;
        size.width = (canvasmargin * 2);
        CLI.print("SVGHierarchyLayout: Hierarchy Rows ================= ", 2);
        for (int i = 0; i < rows.size(); i++) {
            HierarchyRow r = (HierarchyRow) rows.get(i);
            if (r.getHeight() > highestitem) highestitem = r.getHeight();
            if (r.getWidestItem() > widestitem) widestitem = r.getWidestItem();
            if (r.entities.size() > norows) norows = r.entities.size();
            CLI.print("SVGHierarchyLayout: " + r.toString(), 2);
        }
        nocols = rows.size();
        size.width = (canvasmargin * 2) + (nocols * (widestitem + getMargin()));
        size.height = (canvasmargin * 2) + (norows * (highestitem + getMargin()));
        
        // Draw each row (each row becomes a column instead in right to left)
        int y, x = canvasmargin;
        for (int i = rows.size()-1; i >= 0; i--) {
            
            HierarchyRow r = (HierarchyRow) rows.get(i);
            
            y = canvasmargin;
            
            // Draw each item in the column
            for (int z = 0; z < r.entities.size(); z++) {
                SVGEntity e = (SVGEntity) r.entities.get(z);
                if (e != null) {
                    e.setPosition(centerInCell(
                        new SVGDimension(widestitem, highestitem),
                        e.getSize(), 
                        new SVGPosition(x, y)));
                    svg.append(e.getSVG());
                }
                // Drop down for the next item
                y += margin + highestitem;
            }
            
            // Move to the next column
            x += margin + widestitem;
            
        }
        
        return svg.toString();
    }
    
    /**
     * Represents a row of the hierarchy
     * @author robin
     */
    public class HierarchyRow {
        
        public Vector entities = new Vector();
        private int height = 0;
        private int width = 0;
        private int widestitem = 0;
        
        private void getSizes() {
            width = 0;
            for (int i = 0; i < entities.size(); i++) {
                SVGEntity e = (SVGEntity) entities.get(i);
                if (e != null) {
                    e.setPosition(0, 0);
                    if (e.getSize().width > widestitem) widestitem = e.getSize().width;
                    if (e.getSize().height > height) height = e.getSize().height;
                }
            }
            width = (widestitem + getMargin()) * entities.size();
        }
        
        public int getWidestItem() {
            if (widestitem == 0) getSizes();
            return widestitem;
        }
        
        public int getWidth() {
            if (width == 0) getSizes();
            return width;
        }
        
        public int getHeight() {
            if (height == 0) getSizes();
            return height;
        }
        
        public boolean entityExists(SVGEntity e) {
            return entities.contains(e);
        }
        
        /** Output a comma separated list of entity names on the row */
        public String toString() {
            String o = "";
            for (int i = 0; i < entities.size(); i++) {
                SVGEntity e = (SVGEntity) entities.get(i);
                if (!o.equals("")) o += ", ";
                if (e != null)
                    o += e.getEntity().getName();
                else
                    o += "(blank)";
            }
            o += " (w: " + getWidth() + ", h: " + getHeight() + ")";
            return o;
        }
    }

}

