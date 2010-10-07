package net.sf.umlspeed.entities;

public class DeploymentContent implements Entity {

    public final static int COMPONENT = 0;
    public final static int SOFTWARE = 1;
    
    protected String name = "";
    protected int type = COMPONENT;
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
    public int getType() {
        return type;
    }
    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

}
