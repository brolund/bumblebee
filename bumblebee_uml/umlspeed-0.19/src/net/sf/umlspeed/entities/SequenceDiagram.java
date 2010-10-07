package net.sf.umlspeed.entities;

import java.util.Vector;

public class SequenceDiagram extends Diagram {
    
    /** The rendering order for links, contains a list
     *  of DiagramElements with links
     */
    private Vector sequence = new Vector();
    
    /* Sequence diagrams can only ever have one layout */
    public String getLayout() {
        return "sequence";
    }

    /**
     * @return the sequence
     */
    public Vector getSequence() {
        return sequence;
    }

    /**
     * @param sequence the sequence to set
     */
    public void setSequence(Vector sequence) {
        this.sequence = sequence;
    }
    
}
