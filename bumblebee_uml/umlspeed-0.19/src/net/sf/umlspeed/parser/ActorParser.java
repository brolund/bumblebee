package net.sf.umlspeed.parser;

import net.sf.umlspeed.Settings;
import net.sf.umlspeed.cli.CLI;
import net.sf.umlspeed.entities.Actor;
import net.sf.umlspeed.entities.DataStore;

/**
 * Handles lexing/parsing of usecase actors, eg:
 *      actor user = "User";
 *      
 */
public class ActorParser extends Parser {
    
    public ActorParser(String filename, String buffer, int currentPosition) {
        super(filename, buffer, currentPosition);
    }
    
    /**
     * Does the parsing work. Returns the new position in the buffer
     * after all lexing/parsing has been done for this token.
     * @return The new buffer position.
     */
    public int parse() {
        
        // Get next argument. This should be the actor internal name
        findNextToken();
        Actor a = new Actor();
        a.setName(currentToken);
        CLI.print("ActorParser: " + a.getName(), 1);
        
        // We're expecting an equals sign and some text next
        findNextToken();
        assertToken("="); if (Settings.errorFlag) return -1;
        
        // The text
        findNextToken();
        a.setText(removeQuotes(currentToken));
        
        // terminator
        findNextToken();
        assertToken(";"); if (Settings.errorFlag) return -1;

        DataStore.entities.put(a.getName(), a);
        DataStore.actors.put(a.getName(), a);
        
        return currentPosition;
    }
    
}
