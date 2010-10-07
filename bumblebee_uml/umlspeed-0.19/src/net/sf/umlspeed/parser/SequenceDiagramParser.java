package net.sf.umlspeed.parser;

import java.util.Iterator;

import net.sf.umlspeed.Settings;
import net.sf.umlspeed.cli.CLI;
import net.sf.umlspeed.entities.Class;
import net.sf.umlspeed.entities.DataStore;
import net.sf.umlspeed.entities.DiagramElement;
import net.sf.umlspeed.entities.DiagramLink;
import net.sf.umlspeed.entities.Entity;
import net.sf.umlspeed.entities.Field;
import net.sf.umlspeed.entities.Operation;
import net.sf.umlspeed.entities.SequenceDiagram;

/**
 * Handles lexing/parsing of sequence diagrams, eg:
 * 
 *      sequencediagram mydiagram {
 *          comment = "My diagram";
 *          entities {
 *              thing1 creates thing2;
 *              thing2 invokes thing3 method1;
 *              thing3 invokesasync thing4 method2;
 *              thing3 invokesasync thing5 "Descriptive text instead";
 *          }
 *      }
 *      
 */
public class SequenceDiagramParser extends DiagramParser {
    
    private SequenceDiagram dg = new SequenceDiagram();
    
    public SequenceDiagramParser(String filename, String buffer, int currentPosition) {
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
        CLI.print("SequenceDiagramParser: " + dg.getName(), 1);
        checkEntityNameIsFree(currentToken);
        
        // Get next argument, this should be the opening brace to start
        // the enclosure
        findNextToken();
        assertToken("{"); if (Settings.errorFlag) return -1;
        
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
            else if (currentToken.equals("visuals")) {
                parseVisuals(dg); if (Settings.errorFlag) return -1;
            }
            else if (currentToken.equals("}")) {
                // That's the end of the entities enclosure, break out and finish.
                CLI.print("SequenceDiagramParser: end of diagram.", 2);
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
        if (el != null) CLI.print("SequenceDiagramParser: findElement: Got existing '" + name + "' entity.", 3);
        if (el == null) {
            el = new DiagramElement();
            el.setEntityName(name);
            el.setEntity(entity);
            el.setDiagram(dg);
            dg.getElements().put(name, el);
            dg.getOrderedElements().add(el);
            CLI.print("SequenceDiagramParser: findElement: Created new '" + name + "' entity.", 3);
        }
        return el;
    }
    
    /** Parse the comment enclosure */
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
            if (e == null) {
                parseError("Entity '" + currentToken + "' does not exist, or has not been parsed yet.\nPut your diagram below the entity declaration.");
                return;
            }
            
            // Either find an existing element if we have one, or create one
            DiagramElement el = findElement(currentToken, e);
            CLI.print("SequenceDiagramParser: left join entity " + currentToken, 2);

            // Relationship 
            findNextToken();
                            
            // Create a link object
            DiagramLink dl = new DiagramLink();
            if (currentToken.equals("creates"))
                dl.setLinkType(DiagramLink.LINK_CREATES);
            else if (currentToken.equals("invokes"))
                dl.setLinkType(DiagramLink.LINK_INVOKES);
            else if (currentToken.equals("invokesasync"))
                dl.setLinkType(DiagramLink.LINK_INVOKES_ASYNC);
            else {
                parseError("Invalid relationship '" + currentToken + "'");
                return;
            }
            CLI.print("SequenceDiagramParser: " + el.getEntityName() + " " + currentToken, 2);
            
            // Target entity
            findNextToken();
            
            e = (Entity) DataStore.entities.get(currentToken);
            if (e == null) {
                parseError("Entity '" + currentToken + "' does not exist, or has not been parsed yet.\nPut your diagram below the entity declaration.");
                return;
            }
            CLI.print("SequenceDiagramParser: right join entity " + currentToken, 2);
            
            // Find it in our existing diagram elements if we have it, or
            // create it if not
            DiagramElement el2 = findElement(currentToken, e);
            
            // If it's a create message, we don't need to do anything 
            // else
            String linkString = "";
            if (dl.getLinkType() == DiagramLink.LINK_CREATES) {
                linkString = "<<creates>>";
            }
            else {
                
                // Get the message - this can either be a method name on the 
                // target entity, or just a string. If it's a method, we get
                // the signature and output that as the string.
                findNextToken();
                
                linkString = currentToken;
                
                // If we have a class as the target entity, check to see if our
                // relationship string is an operation on it - if so, make the
                // linkString the full signature
                if (el2.getEntity() instanceof net.sf.umlspeed.entities.Class) {
                    // Find the method
                    for (Iterator it = ((Class) el2.getEntity()).getOperations().iterator(); it.hasNext(); ) {
                        Operation o = (Operation) it.next();
                        if (o.getName().equals(currentToken)) {
                            // We've got the method, expand it out into the linkString
                            linkString += "(";
                            String args = "";
                            for (Iterator itt = o.getArguments().iterator(); itt.hasNext(); ) {
                                Field a = (Field) itt.next();
                                if (!args.equals("")) args += ", ";
                                args += a.getName() + ": " + a.getType();
                            }
                            linkString += args + "): " + o.getReturnType();
                            break;
                        }
                    }
                }
            }
            linkString = removeQuotes(linkString);
            dl.setLinkComment(linkString);
            
            // Make the link from the first element to the second one
            dl.setTargetEntity(el2);
            
            // Make the link from the second element to the first one
            dl.setSourceEntity(el);
            
            // Assign it to the first element
            el.getLinks().add(dl);
            
            // Add the link to our sequence
            dg.getSequence().add(dl);
            
            CLI.print("SequenceDiagramParser: Link Creation (" + el.getLinks().size() + ")", 2);
            
            // We should have a terminator next, ready for the next element
            findNextToken();
            assertToken(";");
            
        }
        
    }
    
}
