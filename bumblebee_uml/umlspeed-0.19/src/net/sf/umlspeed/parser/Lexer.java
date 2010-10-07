package net.sf.umlspeed.parser;

import java.io.File;
import java.io.FileInputStream;

import net.sf.umlspeed.Settings;
import net.sf.umlspeed.cli.CLI;

/**
 * Base class of all parsers (classes that syntax check and parse a
 * particular token/enclosure from the buffer).
 * This is a slightly unusual model for lexical analysis as traditional
 * lexers feed the parsers, but it makes more sense in an OO environment
 * to have the parsers pull tokens from the lexer.
 */
public abstract class Lexer {
    
    protected String buffer = "";
    protected int currentPosition = 0;
    protected String currentToken = "";
    protected String filename = "";
    
    public Lexer() {}
    
    public Lexer(String filename, String buffer, int currentPosition) {
        this.filename = filename;
        this.buffer = buffer;
        this.currentPosition = currentPosition;
    }
    
    /** Loads the input file into a text buffer for working with */
    public void bufferFile(File f) {
        try {
            filename = f.getAbsolutePath();
            FileInputStream in = new FileInputStream(f);
            int sz = in.available();
            byte b[]= new byte[sz];
            in.read(b);
            buffer = new String(b) + " ";
        }
        catch (Exception e) {
            CLI.print("Failed reading file: " + e.getMessage());
            if (Settings.standalone)
                System.exit(1);
        }
    }
    
    /**
     * Scans the buffer and returns the next token. A token is
     * simply the next word, ignoring whitespace and comments.
     * In addition, tokens terminate at characters defined in
     * isTerminator().
     */
    protected void findNextToken() {
        CLI.print("findNextToken(): Pos " + currentPosition + " of " + buffer.length(), 3);
        for (int i = currentPosition; i < buffer.length(); i++) {
            char cc = buffer.substring(i, i+1).toCharArray()[0];
            
            if (!isWhiteSpace(cc)) {
                
                // Got the start, now find the end of the token
                
                if (((i+2) < buffer.length()) && buffer.substring(i, i+2).equals("//")) {
                    // The token is a single line comment, completely 
                    // skip it and call ourselves again to move to
                    // the next token
                    CLI.print("findNextToken(): Got single line comment", 3);
                    currentPosition = buffer.indexOf("\n", i) + 1;
                    findNextToken();
                    return;
                }
                else if (((i+2) < buffer.length()) && buffer.substring(i, i+2).equals("/*")) {
                    // The token is a multi-line comment, completely
                    // skip it and call ourselves again to move to
                    // the next token
                    CLI.print("findNextToken(): Got multi-line comment", 3);
                    currentPosition = buffer.indexOf("*/", i) + 2;
                    findNextToken();
                    return;
                }
                else if (cc == '(') {
                    // The token is enclosed in brackets, return it up to the 
                    // closing bracket
                    int endbracket = buffer.indexOf(")", i + 1) + 1;
                    currentToken = buffer.substring(i, endbracket);
                    currentPosition = endbracket;
                    CLI.print("findNextToken(): Got parenthesis-enclosed token '" + currentToken + "'", 3);
                    return;
                }
                else if (cc == '"') {
                    // The token is quoted, return it up to the final
                    // quoted character
                    int endquote = buffer.indexOf("\"", i + 1) + 1;
                    currentToken = buffer.substring(i, endquote);
                    currentPosition = endquote;
                    CLI.print("findNextToken(): Got quoted token '" + currentToken + "'", 3);
                    return;
                }
                else {
                    // The token is a regular character, no spaces allowed
                    // so grab upto the next bit of whitespace or terminator.
                    for (int z = i + 1; z < buffer.length(); z++) {
                        char nc = buffer.substring(z, z+1).toCharArray()[0];
                        if (isWhiteSpace(nc) || isTerminator(nc)) {
                            currentToken = buffer.substring(i, z);
                            currentPosition = z;
                            CLI.print("findNextToken(): Got regular token '" + currentToken + "'", 3);
                            return;
                        }
                    }
                }
            }
        }
        currentPosition = -1;
        currentToken = "";
    }
    
    protected boolean isWhiteSpace(char c) {
        return c == '\n' || c == '\r' || c == '\t' || c == ' ';
    }
    
    protected boolean isTerminator(char c) {
        return c == ';' || c == '}' || c == ':' || c == '(';
    }
}
