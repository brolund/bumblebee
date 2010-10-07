package net.sf.umlspeed.parser;

import net.sf.umlspeed.Settings;
import net.sf.umlspeed.cli.CLI;
import net.sf.umlspeed.entities.DataStore;
import net.sf.umlspeed.entities.Field;
import net.sf.umlspeed.entities.Interface;
import net.sf.umlspeed.entities.Operation;
import net.sf.umlspeed.util.Util;

/**
 * Handles lexing/parsing of interface objects, eg:
 * 
 *      interface myinterface (interface1) {
 *          comment { "My interface" }
 *          operations {
 *              +getType(): String;
 *              +getName(anarg: String): String;
 *              #doSomething(): void;
 *              -privateSomething(): void;
 *          }
 *      }
 *      
 * Spawns a new Lexer, buffers in the file and parses its entities
 * into our set.
 */
public class InterfaceParser extends Parser {
    
    private Interface iface = new Interface();
    
    public InterfaceParser(String filename, String buffer, int currentPosition) {
        super(filename, buffer, currentPosition);
    }
    
    /**
     * Does the parsing work. Returns the new position in the buffer
     * after all lexing/parsing has been done for this token.
     * @return The new buffer position.
     */
    public int parse() {
        
        // Get next argument. This should be the class name
        findNextToken();
        iface.setName(currentToken);
        iface.setNamespace(DataStore.namespace);
        DataStore.addEntityToNameSpace(DataStore.namespace, iface);
        CLI.print("InterfaceParser: " + iface.getName(), 1);
        checkEntityNameIsFree(currentToken);
        
        // Get next argument, this should be a list of classes 
        // this one inherits from, or the opening brace to start
        // the enclosure
        findNextToken();
        if (!currentToken.startsWith("(") && !currentToken.startsWith("{")) {
            parseError("( or { expected.");
            return -1;
        }
        
        // If we have parent interfaces classes
        if (!currentToken.equals("{")) {
            if (!currentToken.equals("()")) {
                currentToken = removeBrackets(currentToken);
                // Break up the list of interfaces and assign them
                String[] base = Util.split(currentToken, ",");
                arrayToList(base, iface.getInterfaces());
                CLI.print("InterfaceParser: interface " + iface.getName() + " has " + base.length + " parent interfaces (" + currentToken + ")", 2);
            }
            else {
                CLI.print("InterfaceParser: interface " + iface.getName() + " has no parent interfaces.", 2);
            }
               
            // Grab the next token - should be the opening enclosure
            findNextToken();
            assertToken("{"); if (Settings.errorFlag) return -1;
        }
        else {
            CLI.print("InterfaceParser: interface " + iface.getName() + " has no parent interfaces.", 2);
        }
        
        // Loop round looking for inner interface enclosures - comment and operations:
        while (true) {
            
            findNextToken();
            if (currentToken.equals("comment")) {
                parseComment();
            }
            else if (currentToken.equals("operations")) {
                parseOperations();
            }
            else if (currentToken.equals("}")) {
                // That's the end of the interface enclosure, break out and finish.
                CLI.print("InterfaceParser: end of interface.", 2);
                DataStore.interfaces.put(iface.getName(), iface);
                DataStore.entities.put(iface.getName(), iface);
                break;
            }
            else {
                parseError("'" + currentToken + "' is not valid here, expected comment or operations.");
                return -1;
            }
            if (Settings.errorFlag) return -1;
        }
        return currentPosition;
    }
    
    /** Parse the comment enclosure */
    private void parseComment() {
        findNextToken();
        assertToken("=");
        findNextToken();
        iface.setComment(removeQuotes(currentToken));
        findNextToken();
        assertToken(";");
    }
    
    private void parseOperations() {
        
        findNextToken();
        assertToken("{");
        
        while (true) {
            findNextToken();
            if (currentToken.equals("}"))
                // End of the enclosure
                break;
            
            // Should have the fieldname and access modifier prefix
            Operation o = new Operation();
            if (currentToken.startsWith("#"))
                o.setAccess(Operation.SCOPE_PROTECTED);
            else if (currentToken.startsWith("-"))
                o.setAccess(Operation.SCOPE_PRIVATE);
            else if (currentToken.startsWith("+"))
                o.setAccess(Operation.SCOPE_PUBLIC);
            else
                parseError("Operation scope not specified, should be one of +, #, or -");
            o.setName(currentToken.substring(1));
            
            // argument list
            findNextToken();
            if (!currentToken.startsWith("(")) parseError("(arglist) expected for operation.");
            String arglist = currentToken;
            if (!currentToken.equals("()")) {
                String[] args = Util.split(removeBrackets(currentToken), ",");
                for (int i = 0; i < args.length; i++) {
                    String[] nv = Util.split(args[i], ":");
                    if (nv.length < 2) parseError("Syntax error with argument: " + args[i]);
                    // Each arg should have a name and a type
                    Field f = new Field();
                    f.setName(nv[0].trim());
                    f.setType(nv[1].trim());
                    o.getArguments().add(f);
                }
            }
            
            // : separator for type
            findNextToken();
            assertToken(":");
            
            // Get type
            findNextToken();
            o.setReturnType(currentToken);
            
            // Debug
            String modifier = "public";
            if (o.getAccess() == Operation.SCOPE_PRIVATE) modifier = "private";
            if (o.getAccess() == Operation.SCOPE_PROTECTED) modifier = "protected";
            CLI.print("InterfaceParser: " + modifier + " operation '" + o.getName() + "' with args " + arglist + ", returns " + o.getReturnType(), 2);

            // Add to collection of operations for class
            iface.getOperations().add(o);
            
            findNextToken();
            assertToken(";");
        }
        
    }
    
}
