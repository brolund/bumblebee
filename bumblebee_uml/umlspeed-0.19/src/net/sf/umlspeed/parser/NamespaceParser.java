package net.sf.umlspeed.parser;

import net.sf.umlspeed.Settings;
import net.sf.umlspeed.cli.CLI;
import net.sf.umlspeed.entities.DataStore;

/**
 * Handles lexing/parsing of namespaces, eg:
 *      namespace org.rawsontetley;
 *      
 */
public class NamespaceParser extends Parser {
    
    public NamespaceParser(String filename, String buffer, int currentPosition) {
        super(filename, buffer, currentPosition);
    }
    
    /**
     * Does the parsing work. Returns the new position in the buffer
     * after all lexing/parsing has been done for this token.
     * @return The new buffer position.
     */
    public int parse() {
        
        // Get next argument. This should be the namespace.
        findNextToken();
        DataStore.namespace = currentToken;
        CLI.print("NamespaceParser: " + currentToken, 1);
        
        // Expecting a terminator;
        findNextToken();
        assertToken(";"); if (Settings.errorFlag) return -1;
        
        return currentPosition;
    }
    
}
