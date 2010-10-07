package com.agical.bumblebee.acceptance.helpers;

import org.junit.Test;


public class UsingWikiSyntax {
    /*!!
    To enable simple, but powerful, formatting, a wiki dialect is used: 
    [[http://www.oqube.com/projects/muse-java/muse-parser/muse-syntax-status.html][The Muse wiki syntax]] 
    */
    @Test
    public void basicInlineFormatting() throws Exception {
        /*!
        The basic inline formattings available are *emphasize*,  **strong**, 
        =fixed font= and _underlined_. It is accomplished like this:
        >>>>
        #{meth}
        <<<<
        (for some strange reason the double-asterisk of strong requires two 
        leading spaces to not clog together with the preceeding
        emphasize)
        */
    }
    @Test
    public void aNoteOnIndentingComments() throws Exception {
        /*!
        Since wiki syntax is sensitive for indents and other subtle formattings, 
        it is very important to indent your comments consistently. 
        Each line in the comment will be stripped with the same indenting character 
        sequence as *the first line in the comment is indented with*. I.e. *do not leave the first line 
        of a comment empty*.
        
        If you are getting strange results, the reason is often that different lines are 
        indented differently, e.g. one line is indented with spaces and another line with tabs etc.
        */
    }
    @Test
    public void crossReferencingWithinTheDocument() throws Exception {
        /*!
        Bumblebee will write anchor names for all suites, classes and methods in the document.
        This is what enables the index generation, and it can be used when you want to make cross 
        references within the document. To do this, use a link tag like this:
        >>>>
        [[#com.agical.bumblebee.acceptance.helpers.UsingWikiSyntax.basicInlineFormatting][Basic inline formatting]]
        <<<<
        and it will render this link:
        [[#com.agical.bumblebee.acceptance.helpers.UsingWikiSyntax.basicInlineFormatting][Basic inline formatting]]
        
        The common format is:
        >>>>
        [[#classname.methodname][Link text]]
        <<<<
        */
    }
    
    @Test
    public void headings() throws Exception {
        /*!
        Headlines are accomplished with leading  asterisks.
        
        * First level
        
        ** Second level
        
        *** Third level

        **** Fourth level (lowest level)

        It is accomplished like this:
        >>>>
        #{meth}
        <<<<
        Note the necessary space after the last asterisk and the necessary empty 
        line before each headline.
        
        These headings are parsed by the Wiki-syntax processor, and they may or 
        may not be part of the structural information of the document, depending 
        on how well the parser can be integrated with. So, until further notice, 
        consider these Wiki-headings as a presentational sugar rather than
        structural information.
        */
    }

    @Test
    public void tables() throws Exception {
        /*!
        Tables can also be used:
        column heading 1||column heading 2
        1.1|1.2
        2.1|2.2
        And the code looks like this:
        >>>>
        #{meth}
        <<<<
        */
    }
    
}
