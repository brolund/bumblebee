package net.sf.umlspeed.svg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import net.sf.umlspeed.Settings;
import net.sf.umlspeed.cli.CLI;
import net.sf.umlspeed.entities.Actor;
import net.sf.umlspeed.entities.Class;
import net.sf.umlspeed.entities.DataStore;
import net.sf.umlspeed.entities.Deployment;
import net.sf.umlspeed.entities.Diagram;
import net.sf.umlspeed.entities.DiagramElement;
import net.sf.umlspeed.entities.DiagramLink;
import net.sf.umlspeed.entities.Entity;
import net.sf.umlspeed.entities.Interface;
import net.sf.umlspeed.entities.SequenceDiagram;
import net.sf.umlspeed.entities.UseCase;
import net.sf.umlspeed.util.Util;

/**
 * Responsible for rendering entities into an SVG document.
 */
public class SVGRenderer {

    private SVGLayout layout = null;
    private StringBuffer svg = new StringBuffer();
    private int height = 0;
    private int width = 0;
    private int canvasmargin = 20;
    private Diagram currentDiagram = null;
    
    /** 
     * Renders all diagram entities as SVG documents
     */
    public void renderAllDiagrams() {
        
        CLI.print("SVGRenderer: Rendering all diagrams", 2);
        
        Vector diagrams = new Vector();
        
        // Render each diagram and add its name to a list
        for (Iterator it = DataStore.orderedDiagrams.iterator(); it.hasNext(); ) {
            String diagramEntity = it.next().toString();
            render(diagramEntity);
            diagrams.add(diagramEntity);
        }
        
        if (diagrams.size() == 0) {
            CLI.print("SVGRenderer: SVG Output specified, but no diagrams in input file.");
            Settings.errorFlag = true;
            if (Settings.standalone)
                System.exit(1);
            return;
        }
        
        // Build the table of contents
        createToc(diagrams);
        
        // Loop through the list of output diagrams and create an
        // HTML document.
        CLI.print("SVGRenderer: Building umlspeed.html", 2);
        StringBuffer html = new StringBuffer();
        html.append(getHTMLHeader());
        for (int i = 0; i < diagrams.size(); i++) {
            String d = diagrams.get(i).toString();
            String f= d + ".svg";
            String c = ((Diagram) DataStore.diagrams.get(d)).getComment();
            String doc = ((Diagram) DataStore.diagrams.get(d)).getDocumentation();
            
            String b = getHTMLBody();
            b = Util.replace(b, "$FILE", f);
            b = Util.replace(b, "$NAME", d);
            b = Util.replace(b, "$COMMENT", c);
            b = Util.replace(b, "$DOC", doc);
            b = replaceKeys(b);
            html.append(b); 
        }
        html.append(getHTMLFooter());
        Settings.html = html;
        
        // Flush to umlspeed.html if output is on
        if (Settings.outputHTML) {
            try {
                File f = new File(Settings.getOutputDir() + "umlspeed.html");
                FileOutputStream o = new FileOutputStream(f);
                o.write(html.toString().getBytes());
                o.flush();
                o.close();
                CLI.print("SVGRenderer: Wrote umlspeed.html", 1);
            }
            catch (Exception ex) {
                ex.printStackTrace();
                if (Settings.standalone)
                    System.exit(1);
                return;
            }
        }
        
    }
    
    private String htmlHeader = null;
    private String htmlBody = null;
    private String htmlFooter = null;
    
    public void loadTemplate() {
        CLI.print("SVGRenderer: Getting HTML template", 2);
        if (Settings.htmlTemplate == null) {
            CLI.print("SVGRenderer: No HTML template given, falling back to default.", 2);
            return;
        }
        String buffer = "";
        try {
            FileInputStream in = new FileInputStream(Settings.htmlTemplate);
            int sz = in.available();
            byte b[]= new byte[sz];
            in.read(b);
            buffer = new String(b) + " ";
            in.close();
        }
        catch (Exception e) {
            CLI.print("ERROR: Failed reading HTML template");
            CLI.print("   " + e.getMessage());
            e.printStackTrace();
        }
        
        // Split our buffer into the header, body and footer portions
        // The file should use $=== to demark the end of the header
        // and start of the body, and end of the body and start of the
        // footer.
        int startbody = buffer.indexOf("$===");
        if (startbody == -1) CLI.print("ERROR: HTML Template is missing $=== token to end header and start body.");
        int endbody = buffer.indexOf("$===", startbody + 4);
        if (endbody == -1) CLI.print("ERROR: HTML Template is missing $=== token to end body and start footer.");
        htmlHeader = buffer.substring(0, startbody);
        htmlBody = buffer.substring(startbody + 4, endbody);
        htmlFooter = buffer.substring(endbody + 4);
    }
    
    /**
     * Returns the HTML header
     * @return
     */ 
    public String getHTMLHeader() {
        loadTemplate();
        if (htmlHeader != null) return replaceKeys(htmlHeader);
        return replaceKeys("<html><head><title>UMLSpeed SVG Output</title></head><body><center>$TOC");
    }
    
    /**
     * Returns the HTML body to use. Some variables should be supplied here:
     *    $FILE (the SVG filename), $NAME (the diagram name), $COMMENT (the diagram comment)
     * @return
     */
    public String getHTMLBody() {
        if (htmlBody != null) return htmlBody;
        return "<p><a href=\"$FILE\">$COMMENT</a></p>\n";
    }
    
    /**
     * Returns the HTML footer
     * @return
     */
    public String getHTMLFooter() {
        if (htmlFooter != null) return replaceKeys(htmlFooter);
        return "</center></body></html>";
    }
    
    public String replaceKeys(String s) {
        String name = Settings.projectName;
        String version = Settings.projectVersion;
        String publishDate = Settings.publishDate;
        if (name == null || name.equals("")) name = "[n/a]";
        if (version == null || version.equals("")) version = "[n/a]";
        if (publishDate == null || publishDate.equals("")) publishDate = new Date().toString();
        s = Util.replace(s, "$PROJECTNAME", name);
        s = Util.replace(s, "$PROJECTVERSION", version);
        s = Util.replace(s, "$PUBLISHDATE", publishDate);
        s = Util.replace(s, "$TOC", toc);
        return s;
    }
    
    /** The HTML table of contents */
    private String toc = "";
    
    /**
     * Create the HTML table of contents 
     */
    public void createToc(List diagrams) {
        StringBuffer sb = new StringBuffer();
        sb.append("<ul>\n");
        for (int i = 0; i < diagrams.size(); i++) {
            String d = diagrams.get(i).toString();
            String c = ((Diagram) DataStore.diagrams.get(d)).getComment();
            sb.append("<li><a href=\"#" + d + "\">" + c  + "</a></li>\n");
        }
        sb.append("</ul>\n");
        toc = sb.toString();
    }
    
    public void renderError(String message) {
        CLI.print("ERROR: === " + message);
        if (Settings.standalone)
            System.exit(1);
        return;
    }
    
    /** SVG Document Header */
    private String renderHeader() {
        StringBuffer s = new StringBuffer();
        s.append("<?xml version=\"1.0\" standalone=\"no\"?>\n");
        s.append("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.0//EN\" \n");
        s.append("\"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg11.dtd\" >\n");
        s.append("<svg version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" ");
        s.append("width=\"" + width + "\" height=\"" + height + "\"");
        s.append(">\n\n");
        s.append("<!-- Generated by UMLSpeed " + CLI.VERSION + " -->\n\n");
        return s.toString();
    }
    
    /** SVG Document Footer */
    private String renderFooter() {
        return "</svg>";
    }
    
    /** 
     * Renders the given entity into a single SVG document.
     * This is generally for use with diagram descendants, but if another 
     * entity type is passed, the single entity will be rendered.
     */
    public void render(String entityName) {
        
        CLI.print("SVGRenderer: Rendering " + entityName, 1);
        svg = new StringBuffer();
        
        Entity e = (Entity) DataStore.entities.get(entityName);
        if (e == null)
            renderError("Entity '" + entityName + "' does not exist.");
        
        // Keep a reference to the current diagram and render it
        if (e instanceof Diagram) {
            currentDiagram = (Diagram) e;
            renderDiagram(currentDiagram);
            SVGTextRenderer se = new SVGTextRenderer(
                    canvasmargin, 
                    height - canvasmargin, 
                    ((Diagram) e).getComment(), 
                    SVGEntity.MEDIUM_HEIGHT, 
                    false, true);
            se.render();
            svg.append(se.getSVG());
        }
        else {
            // It's just an entity of some type, render it
            renderSingleEntity(e);
        }
        
        // Finish the SVG document
        svg.append(renderFooter());
        
        // Flush to disk
        try {
            File f = new File(Settings.getOutputDir() + entityName + ".svg");
            FileOutputStream o = new FileOutputStream(f);
            o.write(svg.toString().getBytes());
            o.flush();
            o.close();
            CLI.print("SVGRender: Wrote " + entityName + ".svg", 1);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            if (Settings.standalone)
                System.exit(1);
            return;
        }
        
        // If output to png is specified, use ImageMagick to convert it
        if (Settings.outputPNG) {
            try {
                String exec = "convert " + 
                    Settings.getOutputDir() + entityName + ".svg" + " " +
                    Settings.getOutputDir() + entityName + ".png";
                CLI.print("Executing: " + exec, 1);
                Runtime.getRuntime().exec(exec).waitFor();
            }
            catch (Exception ex) {
                CLI.print("ERROR: Failed converting SVG to PNG");
                CLI.print("   " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        
    }
    
    /** Renders a diagram */
    public void renderDiagram(Diagram d) {
        
        handleDiagramLayout(d);

        // Render the layout as our body
        String body = layout.render();
        
        // Get overall document bounds
        width = layout.getWidth();
        height = layout.getHeight();
        
        svg.append(renderHeader());
        svg.append(body);
        
        // Render links between linked elements for non-sequence diagrams
        // (the sequence layout handles messages/links by itself).
        if (!(d instanceof SequenceDiagram))
            renderDiagramLinks();
        
    }
    
    /** 
     * Uses a diagram layout to find all the DiagramElement 
     * objects in it and render their links to other elements.
     */
    public void renderDiagramLinks() { 
        // Render the links between diagram elements
        // This works since all the SVGEntity objects held in the layout
        // know their positions now and we can accurately draw the links
        // between them.
        List o = layout.getObjects();
        CLI.print("SVGRenderer: Scanning links for " + o.size() + " visual objects.", 3);
        for (Iterator it = o.iterator(); it.hasNext(); ) {
            SVGEntity e1 = (SVGEntity) it.next();
            // For this entity, get its links to other entities
            if (e1.getEntity() instanceof DiagramElement) {
                CLI.print("SVGRenderer: Evaluating links for element '" + e1.getEntity().getName() + "'", 3);
                List links = ((DiagramElement) e1.getEntity()).getLinks();
                CLI.print("SVGRenderer: Element " + e1.getEntity().getName() + " has " + links.size() + " links.", 3);
                for (Iterator itt = links.iterator(); itt.hasNext(); ) {
                    DiagramLink l = (DiagramLink) itt.next();
                    // Get the target entity
                    DiagramElement te = l.getTargetEntity();
                    CLI.print("SVGRenderer: " + e1.getEntity().getName() + " -> " + te.getEntityName(), 3);
                    // Find the target entity's SVGEntity
                    for (Iterator ittt = o.iterator(); ittt.hasNext(); ) {
                        SVGEntity e2 = (SVGEntity) ittt.next();
                        if (e2.getEntity().getName().equals(te.getEntityName())) {
                            
                            // We've got the link and the other entity now, 
                            // render it.
                            svg.append(
                                new SVGLinkRenderer(e1, e2, layout.getObjects(), l.getLinkType(), l.getLinkComment()).renderLink()
                                );
                            break;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * For any given diagram, creates the correct layout and 
     * adds all the diagram elements to it.
     * @param d
     */
    public void handleDiagramLayout(Diagram d) {
        
        // Create our layout manager.
        
        // Satellite layout
        if (d.getLayout().equals("satellite")) {
            layout = new SVGSatelliteLayout();
            SVGSatelliteLayout sat = (SVGSatelliteLayout) layout;
            Entity fe = null;
            if (d.getLayoutArgs().length == 0) {
                // No satellite class specified, take the first
                fe = (Entity) d.getElements().values().toArray()[0];
            }
            else {
                fe = (Entity) d.getElements().get(d.getLayoutArgs()[0]);
            }
            sat.setSatelliteEntity(wrapEntity(fe));
        }
        
        // Grid layout
        if (d.getLayout().equals("grid")) {
            int noCols = 1;
            try {
                noCols = Integer.parseInt(d.getLayoutArgs()[0]);
            }
            catch (NumberFormatException e) {
                CLI.print("SVGRenderer: (" + d.getName() + ") Grid layout arguments should start with number of columns.");
                if (Settings.standalone)
                    System.exit(1);
                return;
            }
            layout = new SVGGridLayout(noCols, d.getLayoutArgs());
        }
        
        // Hierarchy layout
        if (d.getLayout().equals("hierarchy")) {
            layout = new SVGHierarchyLayout();
        }
        
        // Usecase layout
        if (d.getLayout().equals("usecase")) {
            //layout = new SVGUseCaseLayout();
            layout = new SVGHierarchyLayout(SVGHierarchyLayout.LEFT_TO_RIGHT);
        }
        
        // Sequence layout (implicit from sequencediagram)
        if (d.getLayout().equals("sequence")) {
            layout = new SVGSequenceLayout();
        }
        
        // Set the canvas margin on the layout
        layout.setCanvasMargin(canvasmargin);
        
        // Loop through and add the diagram elements
        for (Iterator it = d.getOrderedElements().iterator(); it.hasNext(); ) {
            Entity e = (Entity) it.next();
            if (e != null) layout.add(wrapEntity(e));
        }
    }
    
    /**
     * For a given UMLSpeed entity, wraps it in the appropriate
     * SVG Rendering class and returns it.
     * @param e
     * @return
     */
    public SVGEntity wrapEntity(Entity e) {
        
        Entity etest = e;
        
        // If it's a diagram element, find out what entity is being
        // encapsulated
        if (e instanceof DiagramElement) 
            etest = ((DiagramElement) e).getEntity();
        
        // Sequence diagrams have their own wrapper for entities since
        // they work a bit differently from the other diagrams
        if (currentDiagram instanceof SequenceDiagram) {
            return new SVGSequenceEntityRenderer(e);
        }
        
        // Check for different types of entity and wrap them
        if (etest instanceof Class)
            return new SVGClassRenderer(e);
        else if (etest instanceof Interface)
            return new SVGInterfaceRenderer(e);
        else if (etest instanceof Actor)
            return new SVGActorRenderer(e);
        else if (etest instanceof UseCase)
            return new SVGUseCaseRenderer(e);
        else if (etest instanceof Deployment)
            return new SVGDeploymentRenderer(e);
        
        // Failed, can't wrap
        throw new IllegalArgumentException("Can't wrap entity " + e.getName() + ", class type is " + e.getClass().getName());
    }
    
    
    /** Renders a single entity without a layout manager */
    public void renderSingleEntity(Entity en) {
        SVGEntity e = wrapEntity(en);
        e.setPosition(0, 0);
        e.render();
        width = e.getSize().width;
        height = e.getSize().height;
        svg.append(renderHeader());
        svg.append(e.getSVG());
    }
    
}
