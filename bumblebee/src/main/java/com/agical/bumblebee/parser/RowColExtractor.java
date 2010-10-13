package com.agical.bumblebee.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * One-based multi-line extractor
 * @author daniel
 */
public class RowColExtractor {
    
    public static String extract(String text, int beginLine, int beginColumnInclusive, int endLine,
            int endColumnExclusive) {
        return extract(new StringReader(text), beginLine, beginColumnInclusive, endLine, endColumnExclusive);
    }
    public static String extract(Reader text, int beginLine, int beginColumnInclusive, int endLine,
            int endColumnExclusive) {
        try {
            String result = "";
            int row = 1;
            BufferedReader bufferedReader = new BufferedReader(text);
            String tmp = null;
            while ((tmp = bufferedReader.readLine()) != null) {
            	tmp = tmp.replace("\t", "    ");
                if (row >= beginLine) {
                	try {
						if (row == beginLine) {
							int start = beginColumnInclusive-1;
							result += tmp.substring(start) + NewLine.STR;
						}
						if (beginLine == endLine) {
						    result = result.substring(0, endColumnExclusive - beginColumnInclusive);
						    break;
						}
						if (row > beginLine && row < endLine) {
						    result += tmp + NewLine.STR;
						}
						if (row == endLine) {
						    result += tmp.substring(0, endColumnExclusive - 1);
						    break;
						}
					} catch (Exception e) {
                		System.err.println("res:"+ result + 
                							"\nline:" + tmp + 
                							"\ncoordinates:" + beginLine + "," + 
	                							endLine + "," + 
	                							beginColumnInclusive + "," + endColumnExclusive);
                		e.printStackTrace();
					}
                }
                row++;
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException("", e);
        }
    }
    
}
