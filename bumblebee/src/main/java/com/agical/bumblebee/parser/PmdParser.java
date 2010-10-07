package com.agical.bumblebee.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sourceforge.pmd.SourceType;
import net.sourceforge.pmd.ast.ASTCompilationUnit;
import net.sourceforge.pmd.ast.ParseException;
import net.sourceforge.pmd.ast.SimpleNode;
import net.sourceforge.pmd.parsers.Parser;
import net.sourceforge.pmd.sourcetypehandlers.SourceTypeHandler;
import net.sourceforge.pmd.sourcetypehandlers.SourceTypeHandlerBroker;

import org.w3c.dom.Node;

public class PmdParser {
    
    private final String commaSeparatedSourcePaths;

    public PmdParser(String commaSeparatedSourcePaths) {
        super();
        this.commaSeparatedSourcePaths = commaSeparatedSourcePaths;
        roots = Arrays.asList(commaSeparatedSourcePaths.split(","));
    }
    private final Map<String, ASTCompilationUnit> compilationUnits = new HashMap<String, ASTCompilationUnit>();
    private List<String> roots;

    
    public ASTCompilationUnit getAst(Class<?> class1) throws CannotFindSourceException {
        return getAst(getFile(class1));
    }

    public ASTCompilationUnit getAst(File file) throws CannotFindSourceException {
        try {
            ASTCompilationUnit compilationUnit = compilationUnits.get(file);
            if(compilationUnit!=null) return compilationUnit;
                  
            SourceType sourceType = SourceType.JAVA_15;
            SourceTypeHandler sourceTypeHandler = SourceTypeHandlerBroker.getVisitorsFactoryForSourceType(sourceType);
            Parser parser = sourceTypeHandler.getParser();
            parser.setExcludeMarker("NOPMD");
            Reader reader = new FileReader(file);
            ASTCompilationUnit rootNode = (ASTCompilationUnit) parser.parse(reader);
            sourceTypeHandler.getSymbolFacade().start(rootNode);
            compilationUnits.put(file.getAbsolutePath(), rootNode);
            return rootNode;
        } catch (FileNotFoundException e) {
            throw new CannotFindSourceException( "Couldn't find file for " + file, e );
        } catch (ParseException e) {
            throw new RuntimeException( "Couldn't parse file for " + file, e );
        }
    }
    private File getFile(Class<?> clazz) {
        return getFile(clazz.getName());
    }
    public File getFile(String absoluteFilePath) {
        absoluteFilePath = absoluteFilePath.replace('.', '/');
        File file = null;
        for (String root : roots) {
            String pathname = root + "/" + absoluteFilePath + ".java";
            file = new File(pathname);
            if(file.exists()) return file;
        }
        throw new CannotFindSourceException( "Could not find file " + absoluteFilePath + " in any of the roots " + commaSeparatedSourcePaths);
    }

    public static void print(SimpleNode node) {
        System.out.println(xmlToString(node.asXml()));
    }
    public static String xmlToString(Node node) {
        try {
            Source source = new DOMSource(node);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);
            return stringWriter.getBuffer().toString();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
