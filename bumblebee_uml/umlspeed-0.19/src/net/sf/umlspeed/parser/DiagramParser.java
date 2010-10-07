package net.sf.umlspeed.parser;

import net.sf.umlspeed.Settings;
import net.sf.umlspeed.cli.CLI;
import net.sf.umlspeed.entities.Diagram;
import net.sf.umlspeed.util.Util;

/** Superclass of diagram parsers */
public class DiagramParser extends Parser {

    public DiagramParser(String filename, String buffer, int currentPosition) {
        super(filename, buffer, currentPosition);
    }
    
    protected void parseVisuals(Diagram dg) {
        
        findNextToken();
        assertToken("{");

        while (true) {
            
            findNextToken();
            
            if (currentToken.equals("}"))
                // End of the enclosure
                break;
            
            // entity_margin
            if (currentToken.equals("entity_margin")) {
                findNextToken();
                assertToken("=");
                findNextToken();
                try {
                    Settings.marginoverride = Integer.parseInt(currentToken);
                }
                catch (NumberFormatException e) {
                    parseError("entity_margin must be a valid integer. '" + currentToken + "' is not valid.");
                    return;
                }                
                findNextToken();
                assertToken(";");
            }
            else {
                parseError("Expected entity_margin or }");
                return;
            }
        }
        
    }
    
    protected void parseLayout(Diagram dg) {
        
        findNextToken();
        assertToken("=");
        
        findNextToken();
        dg.setLayout(removeQuotes(currentToken));
        
        // Verify we have a valid layout
        if (!currentToken.equals("satellite") &&
            !currentToken.equals("usecase") &&
            !currentToken.equals("grid") &&
            !currentToken.equals("hierarchy"))
            parseError("'" + currentToken + "' is not a valid diagram layout.");
        CLI.print("DiagramParser: Using " + currentToken + " layout", 2);
        
        findNextToken();
        
        // Should have a bracketed list of arguments or a terminator
        if (!currentToken.startsWith("(") && !currentToken.equals(";"))
            parseError("Expected layout argument list or ;");
        
        if (currentToken.startsWith("(")) {
            dg.setLayoutArgs(Util.split(removeBrackets(currentToken), ","));
            for (int i = 0; i < dg.getLayoutArgs().length; i++)
                dg.getLayoutArgs()[i] = dg.getLayoutArgs()[i].replace('"', ' ').trim();
            
            CLI.print("DiagramParser: Layout arguments " + currentToken, 2);
            
            findNextToken();
            assertToken(";");
        }
        else {
            // If the layout manager is satellite or grid and we got here,
            // no argument list was given.
            if (dg.getLayout().equals("grid") || dg.getLayout().equals("satellite"))
                parseError("satellite and grid layout require layout arguments.");
        }
    }
    
}
