package net.sf.umlspeed.entities;

import java.util.ArrayList;
import java.util.List;

public class Class implements Entity {
    
    private String name = "";
    private String comment = "";
    private String namespace = "";
    private String[] modifiers = new String[0];
    private List baseClasses = new ArrayList();
    private List operations = new ArrayList();
    private List fields = new ArrayList();
    
    private boolean modifierExists(String mod) {
        for (int i = 0; i < modifiers.length; i++) {
            if (modifiers[i].equals(mod))
                return true;
        }
        return false;
    }
    
    public boolean isPublic() {
        return modifierExists("public");
    }
    
    public boolean isPrivate() {
        return modifierExists("private");
    }
    
    public boolean isFriend() {
        return modifierExists("friend");
    }
    
    public boolean isAbstract() {
        return modifierExists("abstract");
    }
    
    /**
     * @return the baseClasses
     */
    public List getBaseClasses() {
        return baseClasses;
    }
    /**
     * @param baseClasses the baseClasses to set
     */
    public void setBaseClasses(List baseClasses) {
        this.baseClasses = baseClasses;
    }
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
     * @return the fields
     */
    public List getFields() {
        return fields;
    }
    /**
     * @param fields the fields to set
     */
    public void setFields(List fields) {
        this.fields = fields;
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

    /**
     * @return the modifiers
     */
    public String[] getModifiers() {
        return modifiers;
    }

    /**
     * @param modifiers the modifiers to set
     */
    public void setModifiers(String[] modifiers) {
        this.modifiers = modifiers;
    }
    
}
