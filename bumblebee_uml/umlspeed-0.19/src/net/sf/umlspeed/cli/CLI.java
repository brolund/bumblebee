package net.sf.umlspeed.cli;

import java.io.File;

import net.sf.umlspeed.Settings;
import net.sf.umlspeed.codegen.CodeGenerator;
import net.sf.umlspeed.parser.Parser;
import net.sf.umlspeed.svg.SVGRenderer;
import net.sf.umlspeed.xmi.XMIOutput;

public class CLI {

    public final static String VERSION = "0.19";

    public static void main(String[] args) {
        new CLI(true, true).start(args);
    }

    /**
     * @param standalone Whether we can exit the VM on errors 
     *        and buffer console output to Settings.console
     */
    public CLI(boolean standalone) {
        this(standalone, true);
    }
    
    /**
     * @param standalone Whether we can exit the VM on errors
     * @param outputHTML Whether to output HTML to a file
     */
    public CLI(boolean standalone, boolean outputHTML) {
        Settings.reset();
        Settings.standalone = standalone;
        Settings.outputHTML = outputHTML;
    }
    
    public void start(String[] args) {

        // Process command line arguments
        processArgs(args);

        // If we have some verbosity, show the copyright notice
        if (Settings.verbosityLevel > 0)
            printCopyright();

        // Perform lexical analysis/parsing on code
        Parser parser = new Parser();
        parser.bufferFile(Settings.inputFile);
        parser.parse();

        // Render output according to options set
        if (Settings.outputSVG)
            new SVGRenderer().renderAllDiagrams();
        if (Settings.outputXMI)
            new XMIOutput().outputXMI();
        if (Settings.outputCode) {
            ((CodeGenerator) Settings.languages.get(Settings.outputCodeLanguage))
                .generate();
        }

    }

    public void processArgs(String[] args) {

        if (args.length == 0) {
            printCopyright();
            printUsage();
        }

        // Figure out options
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-h") || args[i].equals("--help"))
                printUsage();
            if (args[i].equals("--output-svg") || args[i].equals("-s"))
                Settings.outputSVG = true;
            if (args[i].equals("--output-xmi") || args[i].equals("-x"))
                Settings.outputXMI = true;
            if (args[i].equals("--output-png") || args[i].equals("-p")) {
                Settings.outputSVG = true;
                Settings.outputPNG = true;
            }
            if (args[i].equals("--output-code") || args[i].equals("-c")) {
                Settings.outputCode = true;
                i++;
                if (i >= args.length || args[i].startsWith("-")) {
                    print("--output-code requires a langauge option.\n");
                    printUsage();
                }
                if (Settings.languages.get(args[i]) == null) {
                    print("--output-code does not support language '" + args[i] + "'.\n");
                    printUsage();
                }
                Settings.outputCodeLanguage = args[i];
            }
            if (args[i].equals("--html-template-file") || args[i].equals("-t")) {
                i++;
                if (i >= args.length || args[i].startsWith("-")) {
                    print("--html-template-file requires a file parameter.\n");
                    printUsage();
                }
                File f = new File(args[i]);
                if (!f.exists()) {
                    print("HTML Template file '" + args[i] + "' does not exist.");
                    printUsage();
                }
                Settings.htmlTemplate = f;
            }
            if (args[i].equals("--project-name")) {
                i++;
                if (i >= args.length || args[i].startsWith("-")) {
                    print("--project-name requires a parameter.\n");
                    printUsage();
                }
                Settings.projectName = args[i];
            }
            if (args[i].equals("--project-version")) {
                i++;
                if (i >= args.length || args[i].startsWith("-")) {
                    print("--project-version requires a parameter.\n");
                    printUsage();
                }
                Settings.projectVersion = args[i];
            }
            if (args[i].equals("-v"))
                Settings.verbosityLevel = 1;
            if (args[i].equals("-vv"))
                Settings.verbosityLevel = 2;
            if (args[i].equals("-vvv"))
                Settings.verbosityLevel = 3;
            if (args[i].equals("--version") || args[i].equals("-V")) {
                printCopyright();
                if (Settings.standalone) System.exit(0);
            }
        }

        // No filename supplied if the last arg is an option
        String filename = args[args.length - 1];
        if (filename.startsWith("-")) {
            printUsage();
        }

        // Fail if file doesn't exist
        Settings.inputFile = new File(filename);
        if (!Settings.inputFile.exists()) {
            printNoFile(filename);
        }

        // If no output settings specified, default to SVG
        if (!Settings.outputSVG && !Settings.outputXMI && !Settings.outputCode)
            Settings.outputSVG = true;

    }

    public void printCopyright() {
        print("umlspeed " + VERSION + " - UML compiler");
        print("Copyright(c) 2007, R.Rawson-Tetley");
        print("");
        print("This program is distributed in the hope that it will be useful,");
        print("but WITHOUT ANY WARRANTY; without even the implied warranty of");
        print("MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU");
        print("General Public Licence v2 for more details.");
        print("");
    }

    public void printUsage() {
        print("Usage: umlspeed [options] file");
        print("");
        print("Options:");
        print("");
        print("  -s, --output-svg                  Output SVG renderings of diagrams.");
        print("  -p, --output-png                  Output PNG renderings of diagrams.");
        print("                                    (requires ImageMagick convert in PATH)");
        print("  -t, --html-template-file <file>   HTML template file for docs output.");
        print("      --project-name                Sets the project name for docs.");
        print("      --project-version             Sets the project version for docs.");
        print("  -x, --output-xmi                  Output XMI document of entities.");
        print("  -c, --output-code <LANG>          Output code in the language specified");
        print("                                    (java, python)");
        print("");
        print("  -V, --version                     Show the umlspeed version.");
        print("  -v[vv]                            Increase output verbosity.");
        print("  -h, --help                        Print this help.");
        print("");
        if (Settings.standalone) System.exit(1);
    }

    public void printNoFile(String filename) {
        print("File '" + filename + "' does not exist.");
        if (Settings.standalone) System.exit(1);
    }

    public static void print(String s) {
        if (Settings.standalone)
            System.out.println(s);
        else
            Settings.console.append(s + "\n");
    }

    public static void print(String s, int verbosityLevel) {
        if (Settings.verbosityLevel >= verbosityLevel)
            print(s);
    }

}
