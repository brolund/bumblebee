package com.agical.bumblebee.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pmd.ast.ASTCompilationUnit;
import net.sourceforge.pmd.ast.ASTType;
import net.sourceforge.pmd.ast.Comment;
import net.sourceforge.pmd.ast.SimpleNode;

import org.jaxen.JaxenException;

public class PmdSourceExtractor extends AbstractSourceExtractor {

    private final PmdParser pmdParser;

    public PmdSourceExtractor(PmdParser pmdParser) {
        this.pmdParser = pmdParser;
    }

    private ASTCompilationUnit getAst(File file) throws FileNotFoundException {
        return pmdParser.getAst(file);
    }
    
    public String getAutoSnippetingComment(String className, String methodName) {
        String[] toAvoidStackOverflow = new String[] {};
        return getAutoSnippetingComment(className, methodName, toAvoidStackOverflow);
    }

    public String getAutoSnippetingComment(String className, String methodName, String... parameterNames) {
        try {
            File file = pmdParser.getFile(className);
            ASTCompilationUnit ast = getAst(file);
            String xpathString = "//ClassOrInterfaceBodyDeclaration[./MethodDeclaration/@MethodName='" + methodName + "']";
            List<SimpleNode> simpleNodes = ast.findChildNodesWithXPath(xpathString);
            SimpleNode simpleNode = selectSimpleNodeWithParameterName(simpleNodes, parameterNames);
            List<Comment> comments = ast.getComments();
            List<Comment> methodInnerComments = new ArrayList<Comment>();
            for (Comment comment : comments) {
                if(comment.getBeginLine()>simpleNode.getBeginLine() && 
                    comment.getEndLine()<simpleNode.getEndLine()) {
                    methodInnerComments.add(comment);
                }
            }
            String classSource = getClassSource(file);
            List<String> split = new ArrayList<String>();
            Comment previous = null;
            for (Comment comment : methodInnerComments) {
                
                String extract = RowColExtractor.extract(classSource, comment.getBeginLine(), comment.getBeginColumn()+2, comment.getEndLine(), comment.getEndColumn()-1);
                if(!extract.startsWith("!")) {
                    continue;
                } 
                if(extract.trim().equals("!")) {
                    extract = "";
                } else {
                    extract = getLinesFrom(2, extract);
                }
                if(previous!=null) {
                    String codeBetweenComments = RowColExtractor.extract(classSource, previous.getEndLine(), previous.getEndColumn(), comment.getBeginLine(), comment.getBeginColumn());
                    split.add(unindentBlock(getLinesFrom(2,codeBetweenComments),true,""));
                }
                split.add(unindentBlock(extract, true, ""));
                previous = comment;
            }
            String result = "";
            boolean snip = false;
            for (String part : split) {
                if(snip) {
                    result += ">>>>"+NewLine.STR;
                    result += escapeTroublesomeCharacters(part);
                    result += "<<<<"+NewLine.STR;
                } else {
                    result += part;
                }
                snip = !snip;
            }
            return result;
        } catch (FileNotFoundException e) {
            throw new RuntimeException( "Cannot read " + className, e );
        } catch (JaxenException e) {
            throw new RuntimeException( "Cannot read " + className, e );
        }
    }



    private SimpleNode selectSimpleNodeWithParameterName(List<SimpleNode> simpleNodes, String... parameterNames)
            throws JaxenException {
        for (SimpleNode simpleNodeCandidate : simpleNodes) {
            String xpathString2 = "MethodDeclaration/MethodDeclarator/FormalParameters/FormalParameter/Type";
            List<ASTType> arguments = simpleNodeCandidate.findChildNodesWithXPath(xpathString2);
            if(arguments!=null&&parameterNames!=null&& arguments.size()==parameterNames.length) {
                int score = 0;
                for(int i = 0; i < arguments.size();i++) {
                    if(parameterNames[i].contains(arguments.get(i).getTypeImage())) {
                        score++;
                    }
                }
                if(score==arguments.size())  {
                    return simpleNodeCandidate;
                }
            }
        }
        return simpleNodes.get(0);
    }

    public String getClassComment(Class<?> clazz) {
        return getClassComment(clazz.getName());
    }

    public String getClassComment(String className) {
            try {
                File file = pmdParser.getFile(className);
                ASTCompilationUnit ast = getAst(file);
                String classSource = getClassSource(file);
                List<Comment> comments = ast.getComments();
                for (Comment comment : comments) {
                    if(!classSource.contains("/*!")) continue;
                    String c = RowColExtractor.extract(classSource, comment.getBeginLine(), comment.getBeginColumn(), comment.getEndLine(), comment.getEndColumn()+1);
                    if(c.startsWith("/*!!")) {
                        return unindentBlock(c.substring(4, c.length()-2), true, "").trim()+NewLine.STR;
                    }
                }
                return "";
            } catch (FileNotFoundException e) {
                throw new RuntimeException( "Cannot read class " + className, e );
            }
    }

    private String getClassSource(File file) {
        try {
            String result = "";
            String line = null;
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            while((line=fileReader.readLine())!=null) {
                result += line+NewLine.STR;
            }
            return result.substring(0, result.length());
        } catch (FileNotFoundException e) {
            throw new RuntimeException( "Cannot read file "+ file.getAbsolutePath(), e );
        } catch (IOException e) {
            throw new RuntimeException( "Cannot read file "+ file.getAbsolutePath(), e );
        }
    }
    public String getClassSource(String name) {
        return getClassSource(pmdParser.getFile(name));
    }

    public String getCodeAfterCommentMarker(String marker1, String code) {
        return getCodeAfterCommentMarkerStatic(marker1, code);
    }

    public String getCodeAfterMarkers(String className, String methodName, String marker1)
            throws UnderlyingBugException {
        String code = getCodeFromMethod(className, methodName);
        return getCodeAfterCommentMarkerStatic(marker1, code);
    }

    public String getCodeBetweenCommentMarkers(String marker1, String marker2, String code) {
        return getCodeBetweenCommentMarkersStatic(marker1, marker2, code);
    }

    public String getCodeBetweenMarkers(String className, String methodName, String marker1, String marker2) {
        String method = getCodeFromMethod(className, methodName);
        return getCodeBetweenCommentMarkersStatic(marker1, marker2, method);
    }

    public String getCodeFromMethod(String className, String methodName, String... parameterNames) {
        String xpathString = "//ClassOrInterfaceBodyDeclaration[./MethodDeclaration/@MethodName='" + methodName + "']";
        try {
            File file = pmdParser.getFile(className);
            ASTCompilationUnit ast = getAst(file);
            SimpleNode methodDeclaration =  selectSimpleNodeWithParameterName(ast.findChildNodesWithXPath(xpathString), parameterNames);
            String method = RowColExtractor.extract(new FileReader(file), methodDeclaration.getBeginLine(), 1, methodDeclaration.getEndLine(), methodDeclaration.getEndColumn()+1);
            String unindentBlock = unindentBlock(method, true, "");
            String substring = unindentBlock.substring(0, unindentBlock.length()-NewLine.STR.length());
            return substring;
        } catch (FileNotFoundException e) {
            throw new RuntimeException( "Cannot read file "+ className, e );
        } catch (JaxenException e) {
            throw new RuntimeException( "Cannot parse " + xpathString + " from " + className, e );
        }
    }


    public String getCommentsFromMethod(String className, String methodName, String... parameterNames) {
        return extractBumblebeeComment(getCodeFromMethod(className, methodName, parameterNames));
    }

    public String getFieldDeclaration(String className, String variableName) {
        String xpathString = "//ClassOrInterfaceBodyDeclaration[./FieldDeclaration/@VariableName='" + variableName + "']";
        try {
            File file = pmdParser.getFile(className);
            ASTCompilationUnit ast = getAst(file);
            SimpleNode methodDeclaration = (SimpleNode) ast.findChildNodesWithXPath(xpathString).get(0);
            String method = RowColExtractor.extract(new FileReader(file), methodDeclaration.getBeginLine(), 1, methodDeclaration.getEndLine(), methodDeclaration.getEndColumn()+1);
            String unindentBlock = unindentBlock(method, true, "");
            String substring = unindentBlock.substring(0, unindentBlock.length()-NewLine.STR.length());
            return substring;
        } catch (FileNotFoundException e) {
            throw new RuntimeException( "Cannot read file "+ className, e );
        } catch (JaxenException e) {
            throw new RuntimeException( "Cannot parse " + xpathString + " from " + className, e );
        }
    }

}
