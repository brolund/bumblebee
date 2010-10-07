package com.agical.bumblebee.parser;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

public class TestWikiSyntax {
    
    private WikiSyntax wikiSyntax;

    @Before
    public void setupWikiSyntax() {
        wikiSyntax = new WikiSyntax();
    }
    
    @Test
    public void parseRemovesHeadAndBody() throws Exception {
        String actual = wikiSyntax.convertToHtml("Hej");
        assertEquals("<p>Hej </p>" + NewLine.STR, actual);
    }

    @Test
    public void parseRemovesHeadAndBodyAndParagraph() throws Exception {
        String actual = wikiSyntax.convertToInlineHtml("Hej");
        assertEquals("Hej", actual);
    }

    @Test
    public void parseHandlesCodeBlocks() throws Exception {
        String string = "Hej" + NewLine.STR + ">>>>" + NewLine.STR + "<p>CodeBlock</p>" + NewLine.STR + "<<<<" + NewLine.STR + "Hopp"+ NewLine.STR;
        String actual = wikiSyntax.convertToHtml(string);
        assertThat(actual, containsString("Hej"));
        assertThat(actual, containsString("<code><pre>"));
        assertThat(actual, containsString("&lt;p&gt;CodeBlock&lt;/p&gt;"));
        assertThat(actual, containsString("</pre></code>"));
        assertThat(actual, containsString("Hopp")); 
        
    }
    
    private Matcher<String> containsString(final String string) {
        return new BaseMatcher<String>() {
            public boolean matches(Object object) {
                return object.toString().contains(string);
            }

            public void describeTo(Description description) {
                description.appendText("String matches");
            }
        };
    }

    @Test
    public void parseHandlesOneLineCodeBlock() throws Exception {
        String string = "\n>>>>\n<p>CodeBlock</p>\n<<<<\n";
        String actual = wikiSyntax.convertToHtml(string);
        String desired = "<code><pre>\n&lt;p&gt;CodeBlock&lt;/p&gt;\n</pre></code>";
        assertThat(actual, is(desired));
    }

    @Test
    public void parseHandlesMultipleCodeBlocks() throws Exception {
        String string = "Bumblebee will provide a default stylesheet, but if you want to have a special stylesheet, add a line like this:" + NewLine.STR + 
        		">>>>" + NewLine.STR + 
        		"#{configuration.stylesheet='src/site/css/mystylesheet.css'}" + NewLine.STR + 
        		"<<<<" + NewLine.STR + 
        		"It will be copied to the same folder as the =target_file= and linked to " + NewLine.STR + 
        		"in the document." + NewLine.STR + 
        		"" + NewLine.STR + 
        		"Bumblebee will always write the [[bumblebee-default-stylesheet.css][bumblebee-default-stylesheet.css]] to the " + NewLine.STR + 
        		"target folder, and you can include it and extend it from your CSS like this:" + NewLine.STR + 
        		"" + NewLine.STR + 
        		">>>>" + NewLine.STR + 
        		"@import url(bumblebee-default-stylesheet.css);" + NewLine.STR + 
        		"" + NewLine.STR + 
        		"pre {" + NewLine.STR + 
        		"  background: #bbbbee; " + NewLine.STR + 
        		"}" + NewLine.STR + 
        		"<<<<";
        String actual = wikiSyntax.convertToHtml(string);
        String desired = "<p>Bumblebee will provide a default stylesheet, but if you want to have a special stylesheet, add a line like this: </p>" + NewLine.STR + 
        		"<code><pre>" + NewLine.STR + 
        		"#{configuration.stylesheet='src/site/css/mystylesheet.css'}" + NewLine.STR + 
        		"</pre></code><p>It will be copied to the same folder as the <code>target_file</code> and linked to  in the document. </p>" + NewLine.STR + 
        		"<p>Bumblebee will always write the <a href=\"bumblebee-default-stylesheet.css\" />bumblebee-default-stylesheet.css</a> to the  target folder, and you can include it and extend it from your CSS like this: </p>" + NewLine.STR + 
                "<code><pre>" + NewLine.STR + 
        		"@import url(bumblebee-default-stylesheet.css);" + NewLine.STR + 
        		"" + NewLine.STR + 
        		"pre {" + NewLine.STR + 
        		"  background: #bbbbee; " + NewLine.STR + 
        		"}" + NewLine.STR +
        		"</pre></code>";
        assertThat("\n" + actual, is("\n" + desired));
    }


    @Test
    public void nullResultsInEmptyString() throws Exception {
        String actual = wikiSyntax.convertToHtml(null);
        assertThat(actual, is(""));
    }
}
