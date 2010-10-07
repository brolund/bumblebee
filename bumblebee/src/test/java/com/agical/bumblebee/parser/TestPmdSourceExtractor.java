package com.agical.bumblebee.parser;



public class TestPmdSourceExtractor extends TestSourceExtractor {
    protected SourceExtractorInterface getSourceExtractor(String src, String test, String helpers) {
        return new PmdSourceExtractor(new PmdParser("src/main/java,src/test/java"));
    }
}
