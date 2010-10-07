package com.agical.bumblebee.uml;

import java.io.File;

import org.apache.batik.apps.rasterizer.DestinationType;
import org.apache.batik.apps.rasterizer.SVGConverter;
import org.apache.batik.apps.rasterizer.SVGConverterException;

public class BatikUtility {

    public void convertSvgFilesToPngFiles(File targetDir, String[] filePaths) throws SVGConverterException {
        SVGConverter converter = new SVGConverter();
        converter.setSources(filePaths);
        converter.setDst(targetDir);
        converter.setDestinationType(DestinationType.PNG);
        converter.setSecurityOff(true);
        converter.execute();
    }


}
