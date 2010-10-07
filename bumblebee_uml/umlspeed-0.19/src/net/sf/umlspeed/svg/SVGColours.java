package net.sf.umlspeed.svg;

public class SVGColours {
    
    public final static String BLACK = "rgb(0,0,0)";
    public final static String WHITE = "rgb(255,255,255)";
    public final static String PALE_YELLOW = "rgb(255,253,96)";
    public final static String GREY = "rgb(131,131,131)";
    public final static String PALE_GREY = "rgb(225,225,225)";
    public final static String PALE_BLUE = "rgb(197,237,244)";
    
    // Colours for entities
    public static String ACTOR_LINE = BLACK;
    public static String ACTOR_HEAD = PALE_YELLOW;
    
    public static String CLASS_SHADOW = GREY;
    public static String CLASS_BACKGROUND = PALE_YELLOW;
    public static String CLASS_OUTLINE = BLACK;
    
    public static String INTERFACE_SHADOW = GREY;
    public static String INTERFACE_BACKGROUND = PALE_YELLOW;
    public static String INTERFACE_OUTLINE = BLACK;
 
    public static String PACKAGE_SHADOW = GREY;
    public static String PACKAGE_LABEL_BACKGROUND = PALE_BLUE;
    public static String PACKAGE_BACKGROUND = PALE_GREY;
    public static String PACKAGE_OUTLINE = BLACK;

    public static String SEQUENCE_SHADOW = GREY;
    public static String SEQUENCE_BACKGROUND = PALE_YELLOW;
    public static String SEQUENCE_OUTLINE = BLACK;
    
    public static String USECASE_SHADOW = GREY;
    public static String USECASE_BACKGROUND = PALE_YELLOW;
    public static String USECASE_OUTLINE = BLACK;
    
    public static String DEPLOYMENTCONTENT_OUTLINE = BLACK;
    public static String DEPLOYMENTCONTENT_BACKGROUND = WHITE;
    public static String DEPLOYMENT_NODE_BACKGROUND = PALE_YELLOW;
    public static String DEPLOYMENT_NODE_OUTLINE = BLACK;
 
    
    
    
    public static void reset() {
        
        ACTOR_LINE = BLACK;
        ACTOR_HEAD = PALE_YELLOW;
        
        CLASS_SHADOW = GREY;
        CLASS_BACKGROUND = PALE_YELLOW;
        CLASS_OUTLINE = BLACK;
        
        INTERFACE_SHADOW = GREY;
        INTERFACE_BACKGROUND = PALE_YELLOW;
        INTERFACE_OUTLINE = BLACK;
     
        PACKAGE_SHADOW = GREY;
        PACKAGE_LABEL_BACKGROUND = PALE_BLUE;
        PACKAGE_BACKGROUND = PALE_GREY;
        PACKAGE_OUTLINE = BLACK;

        SEQUENCE_BACKGROUND = PALE_YELLOW;
        SEQUENCE_OUTLINE = BLACK;
        
        USECASE_SHADOW = GREY;
        USECASE_BACKGROUND = PALE_YELLOW;
        USECASE_OUTLINE = BLACK;
        
    }
    
}
