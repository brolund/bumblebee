package net.sf.umlspeed;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.sf.umlspeed.codegen.JavaOutput;
import net.sf.umlspeed.codegen.PythonOutput;
import net.sf.umlspeed.entities.DataStore;
import net.sf.umlspeed.svg.SVGColours;

public abstract class Settings {
    
    // ========= COMMAND LINE STORAGE FLAGS ===================
    
    /** Command line output switches */
    public static boolean outputXMI = false;
    public static boolean outputSVG = false;
    public static boolean outputPNG = false;
    public static boolean outputCode = false;
    
    /** The language to output in if outputCode is true */
    public static String outputCodeLanguage = "";
    
    /** HTML template file */
    public static File htmlTemplate = null;
    
    /** The input file */
    public static File inputFile = null;
    
    /** Default directory to output files */
    public static String outputDir = System.getProperty("user.dir");
    
    /** How much logging to output (0-2) */
    public static int verbosityLevel = 0;
    
    // ==========================================================
    
    // ==========================================================
    // Maven-specific flags. These translate to keys used from
    // the Maven plugin ($PROJECTNAME, $PROJECTVERSION $PUBLISHDATE)
    // ==========================================================
    public static String projectName = "";
    public static String projectVersion = "";
    public static String publishDate = "";
    // ==========================================================
    
    /** 
     *  The gap between entities - if this value is not -1, then
     *  diagram renderers will use this value instead.
     */
    public static int marginoverride = -1;
    
    /** Console output for non-standalone mode */
    public static StringBuffer console = new StringBuffer();
    /** HTML output for access from plugins */
    public static StringBuffer html = new StringBuffer();
    /** Whether an error has occurred */
    public static boolean errorFlag = false;
    /** Whether we're in standalone compiler mode */
    public static boolean standalone = true;
    /** Whether to output the HTML to a file */
    public static boolean outputHTML = true;
    
    public static Map languages = new HashMap();
    static {
        languages.put("java", new JavaOutput());
        languages.put("python", new PythonOutput());
    }
    
    public static String getOutputDir() {
        if (!outputDir.endsWith(File.separator))
            outputDir += File.separator;
        return outputDir;
    }
    
    public static void reset() {
        standalone = true;
        console = new StringBuffer();
        errorFlag = false;
        outputXMI = false;
        outputSVG = false;
        outputPNG = false;
        outputHTML = false;
        outputCode = false;
        outputCodeLanguage = "";
        inputFile = null;
        htmlTemplate = null;
        outputDir = System.getProperty("user.dir");
        verbosityLevel = 0;
        projectName = "";
        projectVersion = "";
        publishDate = "";
        DataStore.reset();
        SVGColours.reset();
    }
}
