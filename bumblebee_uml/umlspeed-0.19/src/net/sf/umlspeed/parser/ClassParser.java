package net.sf.umlspeed.parser;

import net.sf.umlspeed.Settings;
import net.sf.umlspeed.cli.CLI;
import net.sf.umlspeed.entities.Class;
import net.sf.umlspeed.entities.DataStore;
import net.sf.umlspeed.entities.Field;
import net.sf.umlspeed.entities.Operation;
import net.sf.umlspeed.util.Util;

/**
 * Handles lexing/parsing of class objects, eg:
 * 
 *      class myclass (baseclass1, baseclass2) {
 *          comment { "My class" }
 *          fields {
 *              -type: String;
 *              -name: String;
 *          }
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
public class ClassParser extends Parser {
    
    private Class ce = new Class();
    
    public ClassParser(String filename, String buffer, int currentPosition) {
        super(filename, buffer, currentPosition);
    }
    
    /**
     * Does the lexing/parsing work. Returns the new position in the buffer
     * after all lexing/parsing has been done for this token.
     * @return The new buffer position.
     */
    public int parse() {
        
        // Get next argument. This should be the class name
        findNextToken();
        ce.setName(currentToken);
        ce.setNamespace(DataStore.namespace);
        DataStore.addEntityToNameSpace(DataStore.namespace, ce);
        CLI.print("ClassParser: " + ce.getName(), 1);
        checkEntityNameIsFree(currentToken);
        
        // Get next argument, this should be a list of classes 
        // this one inherits from, or the opening brace to start
        // the enclosure
        findNextToken();
        if (!currentToken.startsWith("(") && !currentToken.startsWith("{")) {
            parseError("( or { expected.");
            return -1;
        }
        
        // If we have base classes
        if (!currentToken.equals("{")) {
            if (!currentToken.equals("()")) {
                currentToken = removeBrackets(currentToken);
                // Break up the list of classes and assign them
                String[] base = Util.split(currentToken, ",");
                arrayToList(base, ce.getBaseClasses());
                CLI.print("ClassParser: class " + ce.getName() + " has " + base.length + " base classes (" + currentToken + ")", 2);
            }
            else {
                CLI.print("ClassParser: class " + ce.getName() + " has no base classes.", 2);
            }
               
            // Grab the next token - should be the opening enclosure
            findNextToken();
            assertToken("{");  if (Settings.errorFlag) return -1;
        }
        else {
            CLI.print("ClassParser: class " + ce.getName() + " has no base classes.", 2);
        }
        
        // Loop round looking for inner class enclosures - comment, fields
        // and operations:
        while (true) {
            
            findNextToken();
            if (currentToken.equals("modifiers")) {
                parseModifiers();
            }
            else if (currentToken.equals("comment")) {
                parseComment();
            }
            else if (currentToken.equals("fields")) {
                parseFields();
            }
            else if (currentToken.equals("operations")) {
                parseOperations();
            }
            else if (currentToken.equals("}")) {
                // That's the end of the class enclosure, break out and finish.
                CLI.print("ClassParser: end of class.", 2);
                DataStore.classes.put(ce.getName(), ce);
                DataStore.entities.put(ce.getName(), ce);
                break;
            }
            else {
                parseError("'" + currentToken + "' is not valid here, expected comment, fields or operations.");
                return -1;
            }
            
            if (Settings.errorFlag) return -1;
            
        }
        return currentPosition;
    }
    
    /** Parse the comment */
    private void parseComment() {
        findNextToken();
        assertToken("=");
        findNextToken();
        ce.setComment(removeQuotes(currentToken));
        findNextToken();
        assertToken(";");
    }
    
    /**
     * Parses a field { } enclosure.
     *
     */
    private void parseFields() {
        
        findNextToken();
        assertToken("{");
        
        while (true) {
            findNextToken();
            if (currentToken.equals("}"))
                // End of the enclosure
                break;
            
            // Should have the fieldname and access modifier prefix
            Field f = new Field();
            if (currentToken.startsWith("#"))
                f.setAccess(Field.SCOPE_PROTECTED);
            else if (currentToken.startsWith("-"))
                f.setAccess(Field.SCOPE_PRIVATE);
            else if (currentToken.startsWith("+"))
                f.setAccess(Field.SCOPE_PUBLIC);
            else
                parseError("Field scope not specified, should be one of +, #, or -");
            f.setName(currentToken.substring(1).trim());
            
            // : separator for type
            findNextToken();
            assertToken(":");
            
            // Get type
            findNextToken();
            f.setType(currentToken.trim());
            
            // Debug
            String modifier = "public";
            if (f.getAccess() == Field.SCOPE_PRIVATE) modifier = "private";
            if (f.getAccess() == Field.SCOPE_PROTECTED) modifier = "protected";
            CLI.print("ClassParser: " + modifier + " field '" + f.getName() + "' (" + f.getType() + ")", 2);

            // Add to collection of fields for class
            ce.getFields().add(f);
            
            findNextToken();
            assertToken(";");
        }
    }
    
    private void parseModifiers() {
        findNextToken();
        assertToken("=");
        findNextToken();
        
        if (!currentToken.startsWith("("))
            parseError("Expected class modifier list.");
        
        ce.setModifiers(Util.split(removeBrackets(currentToken), ","));
        for (int i = 0; i < ce.getModifiers().length; i++) {
            ce.getModifiers()[i] = ce.getModifiers()[i].trim();
        }
        
        CLI.print("ClassParser: modifier list: " + currentToken, 2);
        
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
            CLI.print("ClassParser: " + modifier + " operation '" + o.getName() + "' with args " + arglist + ", returns " + o.getReturnType(), 2);

            // Add to collection of operations for class
            ce.getOperations().add(o);
            
            findNextToken();
            assertToken(";");
        }
        
    }
    
}
