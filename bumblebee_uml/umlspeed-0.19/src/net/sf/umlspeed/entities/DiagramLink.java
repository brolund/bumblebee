package net.sf.umlspeed.entities;


public class DiagramLink implements Entity {
    
    private DiagramElement targetEntity = null;
    private DiagramElement sourceEntity = null;
    private int linkType = 0;
    private String name = "";
    private String linkComment = "";
    
    public final static int LINK_EXTENDS = 0;
    public final static int LINK_IMPLEMENTS = 1;
    public final static int LINK_ONETOONE = 2;
    public final static int LINK_ONETOMANY = 3;
    public final static int LINK_MANYTOONE = 4;
    public final static int LINK_MANYTOMANY = 5;
    public final static int LINK_USES = 6;
    public final static int LINK_INCLUDES = 7;
    public final static int LINK_DEPENDS = 8;
    public final static int LINK_CREATES = 9;
    public final static int LINK_INVOKES = 10;
    public final static int LINK_INVOKES_ASYNC = 11;
    public final static int LINK_MESSAGE = 11;
    
    /**
     * @return the linkType
     */
    public int getLinkType() {
        return linkType;
    }
    /**
     * @param linkType the linkType to set
     */
    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }
    /**
     * @return the targetEntity
     */
    public DiagramElement getTargetEntity() {
        return targetEntity;
    }
    /**
     * @param targetEntity the targetEntity to set
     */
    public void setTargetEntity(DiagramElement targetEntity) {
        this.targetEntity = targetEntity;
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
     * @return the linkComment
     */
    public String getLinkComment() {
        return linkComment;
    }
    /**
     * @param linkComment the linkComment to set
     */
    public void setLinkComment(String linkComment) {
        this.linkComment = linkComment;
    }
    /**
     * @return the sourceEntity
     */
    public DiagramElement getSourceEntity() {
        return sourceEntity;
    }
    /**
     * @param sourceEntity the sourceEntity to set
     */
    public void setSourceEntity(DiagramElement sourceEntity) {
        this.sourceEntity = sourceEntity;
    }
    
    
}
