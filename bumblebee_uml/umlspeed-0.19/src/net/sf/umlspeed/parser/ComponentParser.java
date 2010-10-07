package net.sf.umlspeed.parser;

import net.sf.umlspeed.Settings;
import net.sf.umlspeed.cli.CLI;
import net.sf.umlspeed.entities.Component;
import net.sf.umlspeed.entities.DataStore;

/**
 * Handles lexing/parsing of components, eg:
 *      component user = "User";
 *      
 */
public class ComponentParser extends Parser {
    
    public ComponentParser(String filename, String buffer, int currentPosition) {
        super(filename, buffer, currentPosition);
    }
    
    /**
     * Does the parsing work. Returns the new position in the buffer
     * after all lexing/parsing has been done for this token.
     * @return The new buffer position.
     */
    public int parse() {
        
        // Get next argument. This should be the component internal name
        findNextToken();
        Component c = new Component();
        c.setName(currentToken);
        CLI.print("ComponentParser: " + c.getName(), 1);
        
        // We're expecting an equals sign and some text next
        findNextToken();
        assertToken("="); if (Settings.errorFlag) return -1;
        
        // The text
        findNextToken();
        c.setText(removeQuotes(currentToken));
        
        // terminator
        findNextToken();
        assertToken(";"); if (Settings.errorFlag) return -1;

        DataStore.entities.put(c.getName(), c);
        DataStore.components.put(c.getName(), c);
        
        return currentPosition;
    }
    
}
