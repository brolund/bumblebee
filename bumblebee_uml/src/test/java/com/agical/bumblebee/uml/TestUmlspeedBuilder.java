package com.agical.bumblebee.uml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Ignore;
import org.junit.Test;

import com.agical.bumblebee.parser.NewLine;
import com.agical.bumblebee.parser.PmdParser;


public class TestUmlspeedBuilder {

    private static final String BUNCH_CLASS_DIAGRAM = "classdiagram mydiagram {" + NewLine.STR + 
            		"    comment = \"\";" + NewLine.STR + 
            		"    layout = satellite (UmlClass);" + NewLine.STR + 
            		"    entities {" + NewLine.STR + 
                    "        UmlClass depends OtherObject;" + NewLine.STR + 
                    "        OtherObject extends TheSuperClass;" + NewLine.STR + 
                    "        TestUmlspeedBuilder;" + NewLine.STR + 
            		"    }" + NewLine.STR + 
            		"    documentation = \"\";" + NewLine.STR + 
            		"}" + NewLine.STR;
    private UmlspeedBuilder builder = new UmlspeedBuilder(new PmdParser("src/main/java,src/test/java"));
    private Class<?>[] bunchOfClasses = new Class<?>[] {UmlClass.class, OtherObject.class, TheSuperClass.class, TestUmlspeedBuilder.class};

    @Test
    public void generateClassDefinition() throws Exception {
        String classItem = builder.generateClassesDeclaration(UmlClass.class);
        assertEquals("namespace com.agical.bumblebee.uml;" + NewLine.STR + 
        		"class UmlClass {" + NewLine.STR + 
        		"    comment = \"\";" + NewLine.STR + 
        		"    operations {" + NewLine.STR + 
                "        +meth(): void;" + NewLine.STR + 
                "        +parameterizedMethod(parameter: String): void;" + NewLine.STR + 
                "        +returnValue(i: int, indata: OtherObject): OtherObject;" + NewLine.STR + 
                "        #packageMethod(): void;" + NewLine.STR + 
                "        #protectedMethod(): void;" + NewLine.STR + 
                "        -privateMethod(): void;" + NewLine.STR + 
                "        +generics(map: Map<OtherObject UmlClass>): List<String>;" + NewLine.STR + 
                "        +nestedGenerics(map: Map<OtherObject List<String>>): List<String>;" + NewLine.STR + 
                "    }" + NewLine.STR + 
        		"}" + NewLine.STR, classItem);
    }
    
    @Test
    public void generateClassDiagram() throws Exception {
        String classDiagram = builder.generateClassDiagram("mydiagram", UmlClass.class, bunchOfClasses);
        assertEquals( BUNCH_CLASS_DIAGRAM , classDiagram);
    }
    
    @Test
    public void generateDiagramForAllClassesUsedInAMethod() throws Exception {
        String classDiagram = builder.generateClassDiagram("mydiagram", this.getClass(), "methodUsingBunchOfClasses", UmlClass.class);
        assertEquals( BUNCH_CLASS_DIAGRAM , classDiagram);
    }
    @Test
    @Ignore
    public void generateDiagramAlsoForUsedReferences() throws Exception {
         fail();
    }
    @Test
    @Ignore
    public void generateDiagramAlsoForOverloadedMethods() throws Exception {
         fail();
    }
    private void methodUsingBunchOfClasses() {
        TheSuperClass theSuperClass = new OtherObject();
        TestUmlspeedBuilder testUmlspeedBuilder = new TestUmlspeedBuilder();
        UmlClass umlClass = new UmlClass();
    }
    @Test
    public void writeClassDiagramFile() throws Exception {
        Class<?>[] classes = new Class<?>[] {UmlClass.class, OtherObject.class, TheSuperClass.class}; 
        Writer stringWriter = new StringWriter();
        builder.writeClassesAndClassDiagram(stringWriter, "mydiagram", classes);
        
    }
    
}
