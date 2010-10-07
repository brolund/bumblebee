package net.sf.umlspeed.entities;

public class UseCase implements Entity {

    private String name = "";
    private String text = "";
    
    public String getName() {
        return name;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}
