package net.sf.umlspeed.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiagramElement implements Entity {

    private String entityName = "";
    private Entity entity = null;
    private List links = new ArrayList();
    private Diagram diagram = null;
    private Map renderData = new HashMap();
    
    public String getName() {
        return entityName;
    }
    
    /**
     * @return the entity
     */
    public Entity getEntity() {
        return entity;
    }
    /**
     * @param entity the entity to set
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }
    /**
     * @return the entityName
     */
    public String getEntityName() {
        return entityName;
    }
    /**
     * @param entityName the entityName to set
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
    /**
     * @return the links
     */
    public List getLinks() {
        return links;
    }
    /**
     * @param links the links to set
     */
    public void setLinks(List links) {
        this.links = links;
    }
    /**
     * @return the renderData
     */
    public Map getRenderData() {
        return renderData;
    }
    /**
     * @param renderData the renderData to set
     */
    public void setRenderData(Map renderData) {
        this.renderData = renderData;
    }

    /**
     * @return the diagram
     */
    public Diagram getDiagram() {
        return diagram;
    }

    /**
     * @param diagram the diagram to set
     */
    public void setDiagram(Diagram diagram) {
        this.diagram = diagram;
    }
    
}
