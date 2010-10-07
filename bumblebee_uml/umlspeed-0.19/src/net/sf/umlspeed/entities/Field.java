package net.sf.umlspeed.entities;

public class Field {

    public final static int SCOPE_PUBLIC = 0;
    public final static int SCOPE_PRIVATE = 1;
    public final static int SCOPE_PROTECTED = 2;
    
    private String name = "";
    private String type = "";
    private int access = SCOPE_PRIVATE;
    private String defaultValue = "";
    
    /**
     * @return the defaultValue
     */
    public String getDefaultValue() {
        return defaultValue;
    }
    /**
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
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
     * @return the type
     */
    public String getType() {
        return type;
    }
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * @return the access
     */
    public int getAccess() {
        return access;
    }
    /**
     * @param access the access to set
     */
    public void setAccess(int access) {
        this.access = access;
    }
    
    
    
}
