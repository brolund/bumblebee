package net.sf.umlspeed.xmi;

import java.io.File;
import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import net.sf.umlspeed.Settings;
import net.sf.umlspeed.cli.CLI;
import net.sf.umlspeed.entities.Class;
import net.sf.umlspeed.entities.DataStore;
import net.sf.umlspeed.entities.Entity;
import net.sf.umlspeed.entities.Field;
import net.sf.umlspeed.entities.Interface;
import net.sf.umlspeed.entities.Operation;

/**
 * XMI Outputter.
 */
public class XMIOutput {
    
    private final static String XMI_VERSION = "1.3";
    private StringBuffer buf = new StringBuffer();
    
    public void outputXMI() {
        outputHeader();
        for (Iterator i = DataStore.namespaces.keySet().iterator(); i.hasNext(); ) {
            String namespace = i.next().toString();
            startPackage(namespace);
            for (Iterator it = ((List) DataStore.namespaces.get(namespace)).iterator(); it.hasNext(); ) {
                Entity e = (Entity) it.next();
                if (e instanceof Interface) {
                    Interface iface = (Interface) e;
                    outputInterface(iface);
                }
                if (e instanceof Class) {
                    Class cl = (Class) e;
                    outputClass(cl);
                }
            }
            endPackage();
        }
        outputFooter();
        saveToDisk();
    }
        
    private static int nextID = 1000;
    
    /** Returns the next XMI ID */
    private String getID() {
        nextID++;
        return Integer.toString(nextID);
    }
    
    private void outputHeader() {
        buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        buf.append("<XMI xmlns:UML=\"org.omg/standards/UML\" verified=\"false\" timestamp=\"\" xmi.version=\"" + XMI_VERSION + "\">\n");
        buf.append("<XMI.header>\n");
        buf.append("  <XMI.documentation>\n");
        buf.append("    <XMI.exporter>umlspeed http://www.rawsontetley.org</XMI.exporter>\n");
        buf.append("    <XMI.exporterVersion>" + CLI.VERSION + "</XMI.exporterVersion>\n");
        buf.append("    <XMI.exporterEncoding>UnicodeUTF8</XMI.exporterEncoding>\n");
        buf.append("  </XMI.documentation>\n");
        buf.append("  <XMI.model xmi.name=\"umlspeed\" href=\"\" />\n");
        buf.append("  <XMI.metamodel xmi.name=\"UML\" xmi.version=\"" + XMI_VERSION + "\" />\n");
        buf.append("</XMI.header>\n");
        buf.append("<XMI.content>\n"); 
        buf.append("<UML:Model>\n");
        buf.append("<UML:Stereotype visibility=\"public\" xmi.id=\"3\" name=\"datatype\" />\n");
        buf.append("<UML:Stereotype visibility=\"public\" xmi.id=\"42\" name=\"interface\" />\n");
    }
    
    private void outputFooter() {
        buf.append("</UML:Model>\n");
        buf.append("</XMI.content>\n");
        buf.append("</XMI>");
    }
    
    private void startPackage(String name) {
        buf.append(
            MessageFormat.format(
               "  <UML:Package visibility=\"public\" xmi.id=\"{0}\" name=\"{1}\" >\n",
               new Object[] {
                       getID(),
                       name
               }));
    }
    
    private void endPackage() {
        buf.append("  </UML:Package>\n");
    }
    
    private void outputInterface(Interface i) {
        buf.append(
            MessageFormat.format(
                "  <UML:Interface stereotype=\"42\" visibility=\"public\" xmi.id=\"{0}\" isAbstract=\"true\" name=\"{1}\" comment=\"{2}\" >\n",
                new Object[] {
                        getID(),
                        i.getName(),
                        xmlEscape(i.getComment())
                }));
        outputOperations(i.getOperations());
        buf.append("  </UML:Interface>\n");
    }
    
    private void outputClass(Class c) {
        String modifier = "public";
        if (c.isFriend()) modifier = "friend";
        if (c.isPrivate()) modifier = "private";
        buf.append(
                MessageFormat.format(
                    "  <UML:Class visibility=\"{0}\" xmi.id=\"{1}\" name=\"{2}\" comment=\"{3}\" isAbstract=\"{4}\" >\n",
                    new Object[] {
                            modifier,
                            getID(),
                            c.getName(),
                            xmlEscape(c.getComment()),
                            new Boolean(c.isAbstract())
                    }));
        outputOperations(c.getOperations());
        outputFields(c.getFields());
        buf.append("  </UML:Class>\n");
    }

    private void outputOperations(List ops) {
        for (Iterator it = ops.iterator(); it.hasNext(); ) {
            
            Operation o = (Operation) it.next();
            String vis = "";
            switch (o.getAccess()) {
                case Operation.SCOPE_PRIVATE: vis = "private"; break;
                case Operation.SCOPE_PROTECTED: vis = "protected"; break;
                case Operation.SCOPE_PUBLIC: vis = "public"; break;
            }
            buf.append(
                MessageFormat.format(
                "    <UML:Operation visibility=\"{0}\" xmi.id=\"{1}\" type=\"{2}\" name=\"{3}\">\n",
                new Object[] {
                    vis,
                    getID(),
                    o.getReturnType(),
                    o.getName()
                }));
            
            // Arguments
            for (Iterator itt = o.getArguments().iterator(); itt.hasNext(); ) {
                Field f = (Field) itt.next();
                buf.append(
                    MessageFormat.format(
                    "      <UML:Parameter visibility=\"public\" xmi.id=\"{0}\" value=\"\" type=\"{1}\" name=\"{2}\" />\n",
                    new Object[] {
                        getID(),
                        f.getType(),
                        f.getName()
                    }));
            }
            buf.append("    </UML:Operation>\n");
        }
    }
    
    private void outputFields(List o) {
        for (Iterator it = o.iterator(); it.hasNext(); ) {
            Field f = (Field) it.next();
            String vis = "";
            switch (f.getAccess()) {
                case Field.SCOPE_PRIVATE: vis = "private"; break;
                case Field.SCOPE_PROTECTED: vis = "protected"; break;
                case Field.SCOPE_PUBLIC: vis = "public"; break;
            }
            buf.append(
                MessageFormat.format(
                "    <UML:Attribute visibility=\"{0}\" xmi.id=\"{1}\" type=\"{2}\" name=\"{3}\" />\n",
                new Object[] {
                        vis,
                        getID(),
                        f.getType(),
                        f.getName()
                }));
        }
    }
    
    private String xmlEscape(String s) {
        s = s.replaceAll("&", "&amp;");
        s = s.replaceAll(">", "&gt;");
        s = s.replaceAll("<", "&lt;");
        s = s.replaceAll("\"", "&quot;");
        return s;
    }
    
    private void saveToDisk() {
        // Figure out the new filename - inputfile with extension 
        // (if there is one) changed to .xmi
        String path = Settings.inputFile.getAbsolutePath();
        int lastdot = path.lastIndexOf(".");
        if (lastdot == -1) lastdot = path.length();
        path = path.substring(0, lastdot) + ".xmi";
        path = path.substring(path.lastIndexOf(File.separator) + 1);
        path = Settings.getOutputDir() + path; 
        CLI.print("XMIOutput: Writing to '" + path + "'", 1);
        
        try {        
            File f = new File(path);
            FileOutputStream o = new FileOutputStream(f);
            o.write(buf.toString().getBytes());
            o.flush();
            o.close();
        }
        catch (Exception e) {
            CLI.print("Failed writing file: " + e.getMessage());
            e.printStackTrace();
            if (Settings.standalone)
                System.exit(1);
            return;
        }
    }
    
}
