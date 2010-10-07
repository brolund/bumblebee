package net.sf.umlspeed.codegen;

import java.io.File;
import java.io.FileOutputStream;
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
 * Java Code Generator
 */
public class JavaOutput implements CodeGenerator {
    
    /** The current file contents buffer */
    private StringBuffer s = null;
    /** The current output file */
    private File f = null;
    /** The current namespace */
    private String namespace = "";
    /** Tab character */
    private String TAB = "    ";
    /** Java primitive types */
    final String[] javaPrimitives = { "boolean", "byte", "char", "int", "long", "double", "float" };
    
    public void generate() {
        
        for (Iterator i = DataStore.namespaces.keySet().iterator(); i.hasNext(); ) {
            
            namespace = i.next().toString();
            
            for (Iterator it = ((List) DataStore.namespaces.get(namespace)).iterator(); it.hasNext(); ) {
                Entity e = (Entity) it.next();
                getFile(e);
                if (e instanceof Interface) {
                    Interface iface = (Interface) e;
                    outputInterface(iface);
                }
                if (e instanceof Class) {
                    Class cl = (Class) e;
                    outputClass(cl);
                }
                saveFile();
            }
            
        }

    }
    
    /** Figures out the filename for the current entity and creates
     *  the directory path upto it. */
    private void getFile(Entity e) {
        // Make namespace directory
        f = new File(Settings.getOutputDir() + namespace.replace('.', File.separatorChar));
        f.mkdirs();
        // Name of our new file
        f = new File(Settings.getOutputDir() + namespace.replace('.', File.separatorChar) + 
            File.separator + e.getName() + ".java");
    }
    
    private void outputInterface(Interface i) {
        s = new StringBuffer();
        s.append("package " + namespace + ";\n\n\n");
        
        if (!i.getComment().equals("")) {
            s.append("/**\n   ");
            s.append(i.getComment());
            s.append("\n */\n");
        }
        
        s.append("public interface ").append(i.getName()).append(" ");
        
        if (i.getInterfaces().size() > 0) {
            s.append("extends ");
            String ifs = "";
            for (int z = 0; z < i.getInterfaces().size(); z++) {
                if (!ifs.equals("")) ifs += ", ";
                ifs += i.getInterfaces().get(z).toString();
            }
            s.append(ifs).append(" ");
        }
        s.append("{\n\n");
        
        outputOperations(i.getOperations(), false);
        
        s.append("}");
    }
    
    private void outputClass(Class c) {
        s = new StringBuffer();
        s.append("package " + namespace + ";\n\n\n");
        
        if (!c.getComment().equals("")) {
            s.append("/**\n   ");
            s.append(c.getComment());
            s.append("\n */\n");
        }
        
        String mod = "public";
        if (c.isFriend()) mod = "";
        if (c.isPrivate()) mod = "private";
        if (c.isAbstract()) mod += " abstract";
        s.append(mod).append(" class ").append(c.getName()).append(" ");
        
        // Use first base class for java
        if (c.getBaseClasses().size() > 0) {
            s.append("extends ").append(
                c.getBaseClasses().get(0).toString())
                .append(" ");
        }
        s.append("{\n\n");
        
        outputFields(c.getFields());
        outputOperations(c.getOperations(), true);
        
        s.append("}");
        
    }

    private void outputOperations(List ops, boolean doBodies) {
        
        for (Iterator it = ops.iterator(); it.hasNext(); ) {
            Operation o = (Operation) it.next();
            String vis = "";
            switch (o.getAccess()) {
                case Operation.SCOPE_PRIVATE: vis = "private"; break;
                case Operation.SCOPE_PROTECTED: vis = "protected"; break;
                case Operation.SCOPE_PUBLIC: vis = "public"; break;
            }
            s.append(TAB + vis + " " + o.getReturnType() + " " + o.getName());
            
            // Arguments
            String args = "";
            for (Iterator itt = o.getArguments().iterator(); itt.hasNext(); ) {
                Field f = (Field) itt.next();
                if (!args.equals("")) args += ", ";
                args += f.getType() + " " + f.getName();
            }
            s.append("(").append(args).append(")");
            
            if (!doBodies) {
                s.append(";\n");
            }
            else {
                s.append(" {\n").append(TAB);
                if (!o.getReturnType().equals("void")) {
                    String rt = "null";
                    s.append(TAB).append("return ");
                    for (int z = 0; z < javaPrimitives.length; z++) {
                        if (o.getReturnType().equals(javaPrimitives[z])) {
                            if (o.getReturnType().equals("boolean"))
                                rt = "false";
                            else if (o.getReturnType().equals("char"))
                                rt = "'0'";
                            else 
                                rt = "0";
                        }
                    }
                    s.append(rt).append(";\n").append(TAB);
                }
                s.append("}\n\n");
            }

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
            s.append(TAB + vis + " " + f.getType() + " " + f.getName() + ";\n"); 
        }
        s.append("\n");
    }
    
    private void saveFile() {
        CLI.print("JavaOutput: Writing to '" + f.getAbsolutePath() + "'", 1);
        try {        
            FileOutputStream o = new FileOutputStream(f);
            o.write(s.toString().getBytes());
            o.flush();
            o.close();
        }
        catch (Exception e) {
            CLI.print("Failed writing file: " + e.getMessage());
            e.printStackTrace();
            if (Settings.standalone)
                System.exit(1);
        }
    }
    
}
