package net.sf.umlspeed.entities;

import java.util.ArrayList;
import java.util.List;

public class Operation {

    public final static int SCOPE_PUBLIC = 0;
    public final static int SCOPE_PRIVATE = 1;
    public final static int SCOPE_PROTECTED = 2;
    
    private String name = "";
    private int access = SCOPE_PUBLIC;
    private List arguments = new ArrayList();
    private String returnType = "void";
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
    /**
     * @return the arguments
     */
    public List getArguments() {
        return arguments;
    }
    /**
     * @param arguments the arguments to set
     */
    public void setArguments(List arguments) {
        this.arguments = arguments;
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
     * @return the returnType
     */
    public String getReturnType() {
        return returnType;
    }
    /**
     * @param returnType the returnType to set
     */
    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
    
}
