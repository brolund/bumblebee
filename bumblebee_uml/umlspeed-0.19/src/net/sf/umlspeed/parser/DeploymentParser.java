package net.sf.umlspeed.parser;

import net.sf.umlspeed.Settings;
import net.sf.umlspeed.cli.CLI;
import net.sf.umlspeed.entities.DataStore;
import net.sf.umlspeed.entities.Deployment;
import net.sf.umlspeed.entities.DeploymentContent;

/**
 * Handles lexing/parsing of deployment objects, eg:
 *  deployment banner_feedservice {
 *       name = "Feed Server";
 *       platform = "Microsoft Windows 2000";
 *       type = node; // Node or database
 *       contents {
 *               component feedservice;
 *               software deltacopy;
 *       }
 * }
      
 * Spawns a new Lexer, buffers in the file and parses its entities
 * into our set.
 */
public class DeploymentParser extends Parser {
    
    private Deployment de = new Deployment();
    
    public DeploymentParser(String filename, String buffer, int currentPosition) {
        super(filename, buffer, currentPosition);
    }
    
    /**
     * Does the lexing/parsing work. Returns the new position in the buffer
     * after all lexing/parsing has been done for this token.
     * @return The new buffer position.
     */
    public int parse() {
        
        // Get next argument. This should be the deployment name
        findNextToken();
        de.setName(currentToken);
        CLI.print("DeploymentParser: " + de.getName(), 1);
        checkEntityNameIsFree(currentToken);
        
        // Should be start of enclosure
        findNextToken();
        assertToken("{"); if (Settings.errorFlag) return -1;
        
        // Loop round looking for inner deployment enclosures - name, 
        // platform, type and contents
        while (true) {
            
            findNextToken();
            if (currentToken.equals("name")) {
                parseName();
            }
            else if (currentToken.equals("platform")) {
                parsePlatform();
            }
            else if (currentToken.equals("type")) {
                parseType();
            }
            else if (currentToken.equals("contents")) {
                parseContents();
            }
            else if (currentToken.equals("}")) {
                // That's the end of the deployment enclosure, break out and finish.
                CLI.print("DeploymentParser: end of deployment.", 2);
                DataStore.deployments.put(de.getName(), de);
                DataStore.entities.put(de.getName(), de);
                break;
            }
            else {
                parseError("'" + currentToken + "' is not valid here, expected name, platform, type or contents.");
                return -1;
            }
            
            if (Settings.errorFlag) return -1;
            
        }
        return currentPosition;
    }
    
    /** Parse the name */
    private void parseName() {
        findNextToken();
        assertToken("=");
        findNextToken();
        de.setDisplayName(removeQuotes(currentToken));
        findNextToken();
        assertToken(";");
    }
    
    /** Parse the platform */
    private void parsePlatform() {
        findNextToken();
        assertToken("=");
        findNextToken();
        de.setPlatform(removeQuotes(currentToken));
        findNextToken();
        assertToken(";");
    }
    
    /** Parse the type */
    private void parseType() {
        findNextToken();
        assertToken("=");
        findNextToken();
        if (currentToken.equals("node"))
            de.setType(Deployment.NODE);
        else if (currentToken.equals("database"))
            de.setType(Deployment.DATABASE);
        else {
            parseError("Deployment type should be database or node.");
        }
        findNextToken();
        assertToken(";");
    }
    
    /**
     * Parses a contents { } enclosure.
     *
     */
    private void parseContents() {
        
        findNextToken();
        assertToken("{");
        
        while (true) {
            findNextToken();
            if (currentToken.equals("}"))
                // End of the enclosure
                break;
            
            DeploymentContent dc = new DeploymentContent();
            
            // Should be component | software
            if (currentToken.equals("component"))
                dc.setType(DeploymentContent.COMPONENT);
            else if (currentToken.equals("software"))
                dc.setType(DeploymentContent.SOFTWARE);
            else {
                parseError("Deployment content should be one of component or software.");
                return;
            }
             
            // Should be a component name
            findNextToken();
            if (!DataStore.components.containsKey(currentToken)) {
                parseError("'" + currentToken + "' is not a valid component, or has not been defined yet.");
                return;
            }
            dc.setName(currentToken);
            
            // Add to list
            de.getContents().add(dc);
            CLI.print("DeploymentParser: Added content item '" + currentToken + "' of type " + 
                    ( dc.getType() == DeploymentContent.COMPONENT ? "component" : "software"), 2);
            
            findNextToken();
            assertToken(";");
        }
    }
}
