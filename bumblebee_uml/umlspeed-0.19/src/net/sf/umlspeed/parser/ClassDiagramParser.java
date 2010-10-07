package net.sf.umlspeed.parser;

import net.sf.umlspeed.Settings;
import net.sf.umlspeed.cli.CLI;
import net.sf.umlspeed.entities.ClassDiagram;
import net.sf.umlspeed.entities.DataStore;
import net.sf.umlspeed.entities.DiagramElement;
import net.sf.umlspeed.entities.DiagramLink;
import net.sf.umlspeed.entities.Entity;

/**
 * Handles lexing/parsing of class diagrams, eg:
 * 
 *      classdiagram mydiagram {
 *          comment = "My diagram";
 *          entities {
 *              thing1 extends thing2;
 *              thing1 implements thing3;
 *              thing3 onetomany thing1;
 *          }
 *      }
 *      
 */
public class ClassDiagramParser extends DiagramParser {
    
    private ClassDiagram dg = new ClassDiagram();
    
    public ClassDiagramParser(String filename, String buffer, int currentPosition) {
        super(filename, buffer, currentPosition);
    }
    
    /**
     * Does the lexing/parsing work. Returns the new position in the buffer
     * after all lexing/parsing has been done for this token.
     * @return The new buffer position.
     */
    public int parse() {
        
        // Get next argument. This should be the diagram name
        findNextToken();
        dg.setName(currentToken);
        CLI.print("ClassDiagramParser: " + dg.getName(), 1);
        checkEntityNameIsFree(currentToken);
        
        // Get next argument, this should be the opening brace to start
        // the enclosure
        findNextToken();
        assertToken("{");  if (Settings.errorFlag) return -1;
        
        // Loop round looking for inner diagram enclosures - comment and entities:
        while (true) {
            
            findNextToken();
            if (currentToken.equals("comment")) {
                parseComment(); if (Settings.errorFlag) return -1;
            }
            else if (currentToken.equals("documentation")) {
                parseDocumentation(); if (Settings.errorFlag) return -1;
            }
            else if (currentToken.equals("entities")) {
                parseEntities(); if (Settings.errorFlag) return -1;
            }
            else if (currentToken.equals("layout")) {
                parseLayout(dg); if (Settings.errorFlag) return -1;
            }
            else if (currentToken.equals("visuals")) {
                parseVisuals(dg); if (Settings.errorFlag) return -1;
            }
            else if (currentToken.equals("}")) {
                // That's the end of the entities enclosure, break out and finish.
                CLI.print("ClassDiagramParser: end of diagram.", 2);
                DataStore.diagrams.put(dg.getName(), dg);
                DataStore.entities.put(dg.getName(), dg);
                DataStore.orderedDiagrams.add(dg.getName());
                break;
            }
            else {
                parseError("'" + currentToken + "' is not valid here, expected comment, layout or entities.");
                return -1;
            }
            
            if (Settings.errorFlag) return -1;
            
        }
        return currentPosition;
    }
    
    /**
     * Searches the diagram elements, looking for the entity named.
     * If the entity doesn't exist in the diagram elements, we create
     * it and return the new object back to the caller.
     * Working this way, we build up a map of unique objects that
     * only appear once on the diagram and can recursively link to
     * each other.
     * 
     * @param name The name of the entity
     * @param entity The entity object
     */
    private DiagramElement findElement(String name, Entity entity) {
        DiagramElement el = (DiagramElement) dg.getElements().get(name);
        if (el != null) CLI.print("ClassDiagramParser: findElement: Got existing '" + name + "' entity.", 3);
        if (el == null) {
            el = new DiagramElement();
            el.setEntityName(name);
            el.setEntity(entity);
            el.setDiagram(dg);
            dg.getElements().put(name, el);
            dg.getOrderedElements().add(el);
            CLI.print("ClassDiagramParser: findElement: Created new '" + name + "' entity.", 3);
        }
        return el;
    }
    
    /** Parse the comment */
    private void parseComment() {
        findNextToken();
        assertToken("=");
        findNextToken();
        dg.setComment(removeQuotes(currentToken));
        findNextToken();
        assertToken(";");
    }
    
    /** Parse the documentation */
    private void parseDocumentation() {
        findNextToken();
        assertToken("=");
        findNextToken();
        dg.setDocumentation(removeQuotes(currentToken));
        findNextToken();
        assertToken(";");
    }
    
    private void parseEntities() {
        
        findNextToken();
        assertToken("{");
        
        while (true) {
            
            findNextToken();
            if (currentToken.equals("}"))
                // End of the enclosure
                break;
            
            // Should be an entity name
            Entity e = (Entity) DataStore.entities.get(currentToken);
            if (e == null)
                parseError("Entity '" + currentToken + "' does not exist, or has not been parsed yet.\nPut your diagram below the entity declaration.");
            
            // Either find an existing element if we have one, or create one
            DiagramElement el = findElement(currentToken, e);
            CLI.print("ClassDiagramParser: left join entity " + currentToken, 2);

            // Relationship and second entity or terminator
            findNextToken();
            if (!currentToken.equals(";")) {
                
                // Create a link object
                DiagramLink dl = new DiagramLink();
                if (currentToken.equals("extends"))
                    dl.setLinkType(DiagramLink.LINK_EXTENDS);
                else if (currentToken.equals("implements"))
                    dl.setLinkType(DiagramLink.LINK_IMPLEMENTS);
                else if (currentToken.equals("onetoone"))
                    dl.setLinkType(DiagramLink.LINK_ONETOONE);
                else if (currentToken.equals("onetomany"))
                    dl.setLinkType(DiagramLink.LINK_ONETOMANY);
                else if (currentToken.equals("manytomany"))
                    dl.setLinkType(DiagramLink.LINK_MANYTOMANY);
                else if (currentToken.equals("manytoone"))
                    dl.setLinkType(DiagramLink.LINK_MANYTOONE);
                else if (currentToken.equals("depends"))
                    dl.setLinkType(DiagramLink.LINK_DEPENDS);
                else {
                    parseError("Invalid relationship '" + currentToken + "'");
                }
                CLI.print("ClassDiagramParser: " + el.getEntityName() + " " + currentToken, 2);
                
                // Target entity
                findNextToken();
                e = (Entity) DataStore.entities.get(currentToken);
                if (e == null)
                    parseError("Entity '" + currentToken + "' does not exist, or has not been parsed yet.\nPut your diagram below the entity declaration.");
                CLI.print("ClassDiagramParser: right join entity " + currentToken, 2);
                
                // Find it in our existing diagram elements if we have it, or
                // create it if not
                DiagramElement el2 = findElement(currentToken, e);
                
                // Make the link from the first element to the second one
                dl.setTargetEntity(el2);
                
                // And back again
                dl.setSourceEntity(el);
                
                // Assign it to the first element
                el.getLinks().add(dl);
                CLI.print("ClassDiagramParser: Link Creation (" + el.getLinks().size() + ")", 2);
                
                // We should have a terminator next, ready for the next element
                findNextToken();
                assertToken(";");
            }
            
        }
        
    }
    
}
