package net.sf.umlspeed.svg;

public class SVGTextRenderer extends SVGEntity {

    private String text = "";
    private int size = 0;
    private boolean bold = false;
    private boolean italic = false;
    
    public SVGTextRenderer(int x, int y, String text, int size, boolean bold, boolean italic) {
        setPosition(x, y);
        this.text = text;
        this.size = size;
        this.bold = bold;
        this.italic = italic;
    }
    
    protected void render() {
        svg = drawText(text, 0, 0, -1, size, bold, italic);
    }

}
