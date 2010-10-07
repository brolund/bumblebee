package net.sf.umlspeed.parser;

import java.util.List;

import net.sf.umlspeed.Settings;
import net.sf.umlspeed.cli.CLI;
import net.sf.umlspeed.entities.DataStore;

/**
 * Base class of all parsers (classes that syntax check and parse a
 * particular token/enclosure from the buffer).
 * As well as being the base class, this is also the top-level Parser
 * that creates the child parsers for each type of entity (see parse method).
 */
public class Parser extends Lexer {
    
    public Parser() { }
    
    public Parser(String filename, String buffer, int currentPosition) {
        super(filename, buffer, currentPosition);
    }
    
    protected void checkEntityNameIsFree(String name) {
        if (DataStore.entities.get(name) != null) {
            parseError("Duplicate entity name '" + name + "'");
        }
    }
    
    protected void arrayToList(String[] arr, List l) {
        for (int i=0; i < arr.length; i++)
            l.add(arr[i].trim());
    }
    
    protected String removeQuotes(String s) {
        if (s.startsWith("\""))
            s = s.substring(1);
        if (s.endsWith("\""))
            s = s.substring(0, s.length()-1);
        return s;
    }
    
    protected String removeBrackets(String s) {
        if (s.startsWith("("))
            s = s.substring(1);
        if (s.endsWith(")"))
            s = s.substring(0, s.length()-1);
        return s;
    }
    
    protected void parseError(String message) {
        
        // Find the line number where the error occurred and
        // the start of the line with the error on it.
        int lineno = 1;
        int lastlinestart = 0;
        for (int i = 0; i < currentPosition; i++) {
            if (buffer.substring(i, i+1).equals("\n")) {
                lineno++;
                lastlinestart = i + 1;
            }
        }
        
        // Output the message and line number/col
        CLI.print("Syntax Error in '" + filename + "' at Line " + lineno + ", Column " + (currentPosition - lastlinestart));
        CLI.print(message);
        
        // Show line of code where error occurred
        int endpoint = buffer.indexOf("\n", currentPosition);
        if (endpoint == -1) endpoint = buffer.length();
        CLI.print("   " + buffer.substring(lastlinestart, endpoint));
        
        Settings.errorFlag = true;
        if (Settings.standalone) System.exit(1);
        
    }
    
    protected void assertToken(String s) {
        if (!currentToken.equals(s)) parseError(s + " expected.");
    }
    
    /**
     * Does the lexing/parsing work. Since this is the top-level base
     * method, the return value of buffer position is irrelevant as we
     * have completed lexing/parsing of the whole buffer on return from here.
     * @return The new buffer position.
     */
    public int parse() {
        
        while (true) {
        
            // Check for errors
            if (Settings.errorFlag) return -1;
            
            // Find next available token
            findNextToken();
            if (currentPosition == -1) break;
            
            // Evaluate and handle the token
            if (currentToken.equals("import")) {
                currentPosition = new ImportParser(filename, buffer, currentPosition).parse();
            }
            else if (currentToken.equals("class")) {
                currentPosition = new ClassParser(filename, buffer, currentPosition).parse();
            }
            else if (currentToken.equals("interface")) {
                currentPosition = new InterfaceParser(filename, buffer, currentPosition).parse();
            }
            else if (currentToken.equals("classdiagram")) {
                currentPosition = new ClassDiagramParser(filename, buffer, currentPosition).parse();
            }
            else if (currentToken.equals("actor")) {
                currentPosition = new ActorParser(filename, buffer, currentPosition).parse();
            }
            else if (currentToken.equals("usecase")) {
                currentPosition = new UseCaseParser(filename, buffer, currentPosition).parse();
            }
            else if (currentToken.equals("deployment")) {
                currentPosition = new DeploymentParser(filename, buffer, currentPosition).parse();
            }
            else if (currentToken.equals("component")) {
                currentPosition = new ComponentParser(filename, buffer, currentPosition).parse();
            }
            else if (currentToken.equals("usecasediagram")) {
                currentPosition = new UseCaseDiagramParser(filename, buffer, currentPosition).parse();
            }
            else if (currentToken.equals("sequencediagram")) {
                currentPosition = new SequenceDiagramParser(filename, buffer, currentPosition).parse();
            }
            else if (currentToken.equals("deploymentdiagram")) {
                currentPosition = new DeploymentDiagramParser(filename, buffer, currentPosition).parse();
            }
            else if (currentToken.equals("namespace")) {
                currentPosition = new NamespaceParser(filename, buffer, currentPosition).parse();
            }
            else {
                parseError("Unrecognised token: " + currentToken); return -1;
            }
        }
        return -1;
    }
}
