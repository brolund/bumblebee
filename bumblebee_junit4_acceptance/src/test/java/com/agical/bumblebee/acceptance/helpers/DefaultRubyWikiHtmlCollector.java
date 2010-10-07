package com.agical.bumblebee.acceptance.helpers;

import java.io.File;

import com.agical.bumblebee.ruby.RubyCollector;

/**
 * This class is used only in Bumblebee
 */
public class DefaultRubyWikiHtmlCollector extends RubyCollector {
    public DefaultRubyWikiHtmlCollector() {
        super(getSourceRoots());
    }

    private static File[] getSourceRoots() {
        File src = new File( "src/main/java");
        File test = new File( "src/test/java");
        File bumblebee_src = new File( "../bumblebee/src/main/java");
        File bumblebee_jruby_src = new File( "../bumblebee_jruby/src/main/java");
        File bumblebee_selenium_src = new File( "../bumblebee_selenium/src/main/java");
        File bumblebee_swing_src = new File( "../bumblebee_swing/src/main/java");
        File bumblebee_uml_src = new File( "../bumblebee_uml/src/main/java");
        return new File[] {
                src,
                test,
                bumblebee_src,
                bumblebee_jruby_src, 
                bumblebee_selenium_src, 
                bumblebee_swing_src,
                bumblebee_uml_src};
    }
}
