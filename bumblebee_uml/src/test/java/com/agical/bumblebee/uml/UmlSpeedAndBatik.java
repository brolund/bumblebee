package com.agical.bumblebee.uml;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;

import org.junit.Test;

import com.agical.bumblebee.parser.PmdParser;


public class UmlSpeedAndBatik {

    @Test
    public void testUmlspeedAndBatikGeneration() throws Exception {
//        File umsDiagramFile = new File("/home/daniel/dev/bumblebee/bumblebee/src/test/resources/umlspeed.diagrams.ums");
        File umsDiagramFile = new File("target/ums/mydiagram.ums");
        umsDiagramFile.getParentFile().mkdirs();
        UmlspeedBuilder umlspeedBuilder = new UmlspeedBuilder(new PmdParser("src/main/java,src/test/java"));
        FileWriter fileWriter = new FileWriter(umsDiagramFile);
        umlspeedBuilder.writeClassesAndClassDiagram(fileWriter, "mydiagram", OtherObject.class, UmlClass.class, TheSuperClass.class);
        fileWriter.close();
        File svgTargetDir = new File("target/svg");
        String[] filePaths = new String[] {svgTargetDir.getAbsolutePath() + "/mydiagram.svg"};
        File targetDir = new File("target/png");
        File targetFile = new File(targetDir, "mydiagram.png");
        targetFile.delete();

        UmlspeedBuilder.generateSvgFiles(umsDiagramFile, svgTargetDir);
        new BatikUtility().convertSvgFilesToPngFiles(targetDir, filePaths);
        
        assertTrue(targetFile.exists());
    }

}
