package net.sf.umlspeed.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Diagram implements Entity {

    protected Map elements = new HashMap();
    /** 
     *  The elements, but in order that we saw them. This is important
     *  for how some diagrams are drawn.
     */
    protected Vector orderedElements = new Vector();
    protected String name = "";
    protected String comment = "";
    protected String documentation = "";
    protected String layout = "hierarchy";
    protected String[] layoutArgs = null;

    /**
     * @return the elements
     */
    public Map getElements() {
        return elements;
    }

    /**
     * @param elements the elements to set
     */
    public void setElements(Map elements) {
        this.elements = elements;
    }

    /**
     * @return the comments
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comments the comments to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the layout
     */
    public String getLayout() {
        return layout;
    }

    /**
     * @param layout the layout to set
     */
    public void setLayout(String layout) {
        this.layout = layout;
    }

    /**
     * @return the layoutArgs
     */
    public String[] getLayoutArgs() {
        return layoutArgs;
    }

    /**
     * @param layoutArgs the layoutArgs to set
     */
    public void setLayoutArgs(String[] layoutArgs) {
        this.layoutArgs = layoutArgs;
    }

    /**
     * @return the orderedElements
     */
    public Vector getOrderedElements() {
        return orderedElements;
    }

    /**
     * @param orderedElements the orderedElements to set
     */
    public void setOrderedElements(Vector orderedElements) {
        this.orderedElements = orderedElements;
    }

    /**
     * @return the documentation
     */
    public String getDocumentation() {
        return documentation;
    }

    /**
     * @param documentation the documentation to set
     */
    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }
    
}
