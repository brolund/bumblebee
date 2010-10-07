package net.sf.umlspeed.parser;

import java.io.File;

import net.sf.umlspeed.Settings;
import net.sf.umlspeed.cli.CLI;
import net.sf.umlspeed.entities.DataStore;

/**
 * Handles lexing/parsing of import statements, eg:
 *      import "newfile.ums";
 *      import "otherfile.ums";
 *      
 * Spawns a new Lexer, buffers in the file and parses its entities
 * into our set.
 */
public class ImportParser extends Parser {
    
    public ImportParser(String filename, String buffer, int currentPosition) {
        super(filename, buffer, currentPosition);
    }
    
    /**
     * Does the parsing work. Returns the new position in the buffer
     * after all lexing/parsing has been done for this token.
     * @return The new buffer position.
     */
    public int parse() {
        
        // Get next argument. This should be the file to include
        findNextToken();
        currentToken = removeQuotes(currentToken);
        
        // Look for the new file relative to the path we're already on
        String currentPath = filename.substring(0, filename.lastIndexOf(File.separator) + 1);
        String newFile = currentPath + currentToken;
        
        // Does this file exist in our already imported files?
        if (DataStore.filesimported.containsKey(newFile)) {
            CLI.print("ImportParser: Already imported file '" + newFile + "', ignoring.", 2);
        }
        else {
            
            // Check that the file exists
            File f = new File(newFile);
            if (!f.exists()) {
                parseError("File '" + currentToken + "' does not exist.");
                return -1;
            }
       
            // Create a new parser and run our imported file
            CLI.print("ImportParser: '" + f.getAbsolutePath() + "'", 1);
            Parser p = new Parser();
            p.bufferFile(f);
            p.parse();
            CLI.print("ImportParser: Finished file '" + f.getAbsolutePath() + "'", 2);
            
            // Add it to our list of imported files
            DataStore.filesimported.put(newFile, new Object());
        }
        
        // Look for the terminator
        findNextToken();
        assertToken(";"); if (Settings.errorFlag) return -1;
        
        return currentPosition;
    }
    
}
