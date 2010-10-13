package com.agical.bumblebee.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.sun.org.apache.regexp.internal.RE;

public abstract class TestSourceExtractor {
    private static final Class<SomeClass> SOME_CLASS = SomeClass.class;
    private static final Class<SomeClassWithTabs> SOME_CLASS_WITH_TABS = SomeClassWithTabs.class;
    private static final String SOME_CLASS_NAME = SOME_CLASS.getName();
    private static final String SOME_CLASS_NAME_WITH_TABS = SOME_CLASS_WITH_TABS.getName();
    private SourceExtractorInterface extractor;
    
    protected abstract SourceExtractorInterface getSourceExtractor(String src, String test, String helpers);

    @Before
    public void before() throws Exception {
        String src = new File(new File("."), "src/main/java").getAbsolutePath();
        String test = new File(new File("."), "src/test/java").getAbsolutePath();
        String helpers = new File(new File("."), "src/test/helpers").getAbsolutePath();
        extractor = getSourceExtractor(src, test, helpers);
    }

    @Test
    public void getCodeForOverloadedMethod() throws Exception {
        String codeFromMethod = extractor.getCodeFromMethod(SOME_CLASS_NAME, "trickyMethod", String.class.getName(),
                Long.class.getName());
        String expected = 
            "public String trickyMethod(String p, Long lng) {" + NewLine.STR +
            "    lng++;" + NewLine.STR +
            "    return p;" + NewLine.STR + 
            "}";
        assertEquals(expected, codeFromMethod);
    }
    
    @Test
    public void getCodeForMethod() throws Exception {
        String codeFromMethod = extractor.getCodeFromMethod(SOME_CLASS_NAME, "trickyMethod", String.class.getName(),
                int.class.getName());
        assertEquals("public String trickyMethod(String p, int i) throws IllegalArgumentException {" + NewLine.STR
                + "    i++;" + NewLine.STR + "    return p;" + NewLine.STR + "}", codeFromMethod);
    }
    @Test
    public void getCodeForMultilineAnnotations() throws Exception {
        String codeFromMethod = extractor.getCodeFromMethod(SOME_CLASS_NAME, "multiLineAnnotations");
        assertEquals("@Before" + NewLine.STR + 
                "@Ignore" + NewLine.STR + 
                "public void multiLineAnnotations() {" + NewLine.STR + "}", codeFromMethod);
    }
    @Test
    public void getCodeForMethodWithoutParameters() throws Exception {
        String codeFromMethod = extractor.getCodeFromMethod(SOME_CLASS_NAME, "nonOverloadedMethodWithArguments");
        assertEquals("public String nonOverloadedMethodWithArguments(String s, Long lng) {" + NewLine.STR
                + "    return s + lng;" + NewLine.STR + "}", codeFromMethod);
    }
    @Test
    public void getIndentedComment() throws Exception {
        String comments = extractor.getCommentsFromMethod(SOME_CLASS, SOME_CLASS.getMethod("methodWithIndentedComment"));
        assertEquals("doc" + NewLine.STR + "    single indent" + NewLine.STR + "        double indent" + NewLine.STR
                + "        " + NewLine.STR + "back to unindented after empty line" + NewLine.STR, comments);
    }
    
    @Test
    public void getBumblebeeCommentsFromMethod() throws Exception {
        assertEquals("doc1" + NewLine.STR + NewLine.STR + "doc2" + NewLine.STR, extractor.getCommentsFromMethod(SOME_CLASS,
                SOME_CLASS.getMethod("methodWithTwoComments")));
    }
    @Test
    public void getCodeBetweenMarkers() throws Exception {
        String codeBetweenMarkers2 = extractor.getCodeBetweenMarkers(SOME_CLASS_NAME, "methodWithTwoComments", "marker1",
                "marker2");
        assertEquals("String codeInBetween = \"\\n\";" + NewLine.STR + "{" + NewLine.STR + "    boolean blocked = false;"
                + NewLine.STR + "}" + NewLine.STR, codeBetweenMarkers2);
        assertNotContains("*/", codeBetweenMarkers2);
    }
    @Test
    @Ignore
    public void getCodeAfterMarker() throws Exception {
        String codeAfterMarker2 = extractor.getCodeAfterMarkers(SOME_CLASS_NAME, "methodWithTwoComments", "marker2");
        assertEquals("Integer moreCode = 666;" + NewLine.STR, codeAfterMarker2);
    }
    @Test
    public void getCodeBetweenEmptyMarkers() throws Exception {
        String codeBetweenMarkers2 = extractor.getCodeBetweenMarkers(SOME_CLASS_NAME, "methodWithEmptyMarkers", "marker1",
                "marker2");
        assertEquals("String codeInBetweenEmptyMarkers = \"\";" + NewLine.STR + "{" + NewLine.STR
                + "    boolean blocked = false;" + NewLine.STR + "}" + NewLine.STR, codeBetweenMarkers2);
        assertNotContains("*/", codeBetweenMarkers2);
        assertNotContains("marker", codeBetweenMarkers2);
    }
    @Test
    public void getCodeBetweenEmptyComments() throws Exception {
        String codeBetweenMarkers2 = extractor.getCodeBetweenMarkers(SOME_CLASS_NAME, "methodWithEmptyMarkers", "marker1",
                "marker2");
        assertEquals("String codeInBetweenEmptyMarkers = \"\";" + NewLine.STR + "{" + NewLine.STR
                + "    boolean blocked = false;" + NewLine.STR + "}" + NewLine.STR, codeBetweenMarkers2);
        assertNotContains("*/", codeBetweenMarkers2);
        assertNotContains("marker", codeBetweenMarkers2);
    }
    @Test
    public void getCommentsWithEmptyMarker() throws Exception {
        String codeBetweenMarkers2 = extractor.getCommentsFromMethod(SOME_CLASS_NAME, "usingSmallerCodeSnippets");
        assertNotContains("*/", codeBetweenMarkers2);
        assertNotContains("m3", codeBetweenMarkers2);
    }
    @Test
    public void getCommentsFromMethodWithoutCommentsReturnsEmptyString() throws Exception {
        assertEquals("", extractor.getCommentsFromMethod(SOME_CLASS, SOME_CLASS.getMethod("method")));
    }
    @Test
    public void getClassComment() throws Exception {
        assertEquals("Class comment" + NewLine.STR + NewLine.STR + "    indented part" + NewLine.STR, extractor
                .getClassComment(SOME_CLASS_NAME));
    }
    @Test
    public void getClassCommentWithTabs() throws Exception {
        assertEquals("Class comment" + NewLine.STR + NewLine.STR + "    indented part" + NewLine.STR, extractor
                .getClassComment(SOME_CLASS_NAME_WITH_TABS));
    }
    @Test
    public void getNonExistingClassCommentReturnsEmptyStringEvenWithMethodComments() throws Exception {
        /*!
        This comment should not appear in the result
        */
        assertEquals("", extractor.getClassComment(getClass()));
        assertEquals("", extractor.getClassComment(getClass().getName()));
    }
    
    @Test
    public void getCodeForClass() throws Exception {
        String classSource = extractor.getClassSource(ShortClass.class.getName());
        String expected = "package com.agical.bumblebee.parser;" + NewLine.STR + "public class ShortClass {"
                + NewLine.STR + "    public void meth() {" + NewLine.STR + "    // TODO Auto-generated method stub"
                + NewLine.STR + "    }" + NewLine.STR + "}" + NewLine.STR;
        assertEquals(expected, classSource);
    }
    
    @Test
    public void getCommentsByAutoSnippeting() throws Exception {
        String classSource = extractor.getAutoSnippetingComment(SOME_CLASS.getName(), "methodForAutoSnippeting");
        String expected = "First comment" + NewLine.STR + ">>>>" + NewLine.STR
                + "String snippetedCode = \\\"Because it is between comments\\\";" + NewLine.STR
                + "String htmlEscaped = \\\"< > &\\\\n\\\";" + NewLine.STR
                + "String rubyStringEscaped = \\\"#{'#'}{\\\";" + NewLine.STR + "{" + NewLine.STR
                + "    boolean blocked = false;" + NewLine.STR + "}" + NewLine.STR + "<<<<" + NewLine.STR;
        assertEquals(expected, classSource);
        
    }
    @Test
    public void getSingleCommentsByAutoSnippeting() throws Exception {
        String classSource = extractor.getAutoSnippetingComment(SOME_CLASS.getName(), "methodWithSingleComment");
        String expected = "doc" + NewLine.STR;
        assertEquals(expected, classSource);
        
    }
    
    @Test
    public void getMethodWithComment() throws Exception {
        String method = "";
        method = extractor.getCodeFromMethod(SOME_CLASS_NAME, "mended");
        
        String expected = "public void mended() {" + NewLine.STR + 
        		"    /*!" + NewLine.STR + 
        		"    A comment" + NewLine.STR + 
        		"    */" + NewLine.STR + 
        		"    System.out.println(\"Bug fixed?\");" + NewLine.STR + 
        		"}";
        
        assertEquals(expected, method);
    }
    @Test
    public void getEmptyMethod() throws Exception {
        String method = extractor.getCodeFromMethod(SOME_CLASS_NAME, "emptyMethod");
        
        String expected = "public void emptyMethod() {}";
        
        assertEquals(expected, method);
    }
    @Test
    public void getNoLine() throws Exception {
        String testString = "first line" + NewLine.STR + "second line" + NewLine.STR;
        assertEquals("", extractor.getLinesBetween(1, 1, testString));
    }
    @Test
    public void getSingleLine() throws Exception {
        String testString = "first line" + NewLine.STR + "second line" + NewLine.STR;
        assertEquals("first line" + NewLine.STR, extractor.getLinesBetween(1, 2, testString));
    }
    @Test
    public void getTwoLines() throws Exception {
        String testString = "first line" + NewLine.STR + "second line" + NewLine.STR + "third line" + NewLine.STR;
        assertEquals("first line" + NewLine.STR + "second line" + NewLine.STR, extractor.getLinesBetween(1, 3, testString));
    }
    @Test
    public void testRegExp() throws Exception {
        String m = "multiLineAnnotations";
        String regexp = "(@[^@]*) " + m; // + "[\\w\\n\\r]*\\([^\\)|\\n|\\r]*\\))";
        String code = "    @Before" + NewLine.STR + 
        		"    @Ignore" + NewLine.STR + 
        		"    public void multiLineAnnotations() {" + NewLine.STR + 
        		"    }" + NewLine.STR + 
        		"    ";
        RE re = new RE(regexp, RE.MATCH_MULTILINE);
        re.match(code);
        String twoindents = re.getParen(0);
    }
    @Test
    public void testExtractVariableDeclaration() throws Exception {
         String variable = "public static final Runnable runnable = new Runnable() {" + NewLine.STR  + 
         		"    public void run() {" + NewLine.STR  + 
         		"        // empty" + NewLine.STR  + 
         		"    }" + NewLine.STR  + 
         		"};";
         assertEquals(variable, extractor.getFieldDeclaration(SOME_CLASS_NAME, "runnable"));
    }
    private void assertNotContains(String expected, String inThisString) {
        assertFalse(inThisString.contains(expected));
    }
    
    
}
