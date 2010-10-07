package net.sf.umlspeed.entities;

import java.util.ArrayList;
import java.util.List;

public class Interface implements Entity {
    
    private String name = "";
    private String comment = "";
    private String namespace = "";
    private List interfaces = new ArrayList();
    private List operations = new ArrayList();
    
    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }
    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
    /**
     * @return the interfaces
     */
    public List getInterfaces() {
        return interfaces;
    }
    /**
     * @param interfaces the interfaces to set
     */
    public void setInterfaces(List interfaces) {
        this.interfaces = interfaces;
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
     * @return the operations
     */
    public List getOperations() {
        return operations;
    }
    /**
     * @param operations the operations to set
     */
    public void setOperations(List operations) {
        this.operations = operations;
    }
    /**
     * @return the namespace
     */
    public String getNamespace() {
        return namespace;
    }
    /**
     * @param namespace the namespace to set
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    
}
