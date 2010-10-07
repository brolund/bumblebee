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
 * Python Code Generator
 */
public class PythonOutput implements CodeGenerator {
    
    /** The current file contents buffer */
    private StringBuffer s = null;
    /** The current output file */
    private File f = null;
    /** The current namespace */
    private String namespace = "";
    /** Tab character */
    private String TAB = "    ";
    
    public void generate() {
        
        for (Iterator i = DataStore.namespaces.keySet().iterator(); i.hasNext(); ) {
            
            namespace = i.next().toString();
            getFile();
            s = new StringBuffer();
            s.append("# module " + namespace + "\n\n\n");
            
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
            saveFile();
            
        }

    }
    
    /** Figures out the filename for the current entity and creates
     *  the directory path upto it. */
    private void getFile() {
        // namespace becomes module name.
        f = new File(Settings.getOutputDir() + namespace + ".py");
    }
    
    private void outputInterface(Interface i) {
        
        s.append("class ").append(i.getName());
        
        if (i.getInterfaces().size() > 0) {
            s.append("(");
            String ifs = "";
            for (int z = 0; z < i.getInterfaces().size(); z++) {
                if (!ifs.equals("")) ifs += ", ";
                ifs += i.getInterfaces().get(z).toString();
            }
            s.append(ifs).append(")");
        }
        s.append(":\n");
        
        s.append(TAB + "\"\"\"\n");
        s.append(TAB + i.getComment() + "\n");
        s.append(TAB + "\"\"\"\n\n");
        
        outputOperations(i.getOperations());
        
    }
    
    private void outputClass(Class c) {
        
        s.append("class ").append(c.getName());
        
        if (c.getBaseClasses().size() > 0) {
            s.append("(");
            String ifs = "";
            for (int z = 0; z < c.getBaseClasses().size(); z++) {
                if (!ifs.equals("")) ifs += ", ";
                ifs += c.getBaseClasses().get(z).toString();
            }
            s.append(ifs).append(")");
        }
        s.append(":\n");
        
        s.append(TAB + "\"\"\"\n");
        s.append(TAB + c.getComment() + "\n");
        s.append(TAB + "\"\"\"\n\n");
        
        outputFields(c.getFields());
        outputOperations(c.getOperations());
        
    }

    private void outputOperations(List ops) {
        
        for (Iterator it = ops.iterator(); it.hasNext(); ) {
            Operation o = (Operation) it.next();
            String vis = "";
            if (o.getAccess() == Operation.SCOPE_PRIVATE) vis = "__";
            s.append(TAB + "def " + vis + o.getName() + "(self");
            
            // Arguments
            String args = "";
            for (Iterator itt = o.getArguments().iterator(); itt.hasNext(); ) {
                Field f = (Field) itt.next();
                args += ", " + f.getName();
            }
            s.append(args).append("):\n");
            s.append(TAB + TAB + "pass\n\n");

        }
    }
    
    private void outputFields(List o) {
        for (Iterator it = o.iterator(); it.hasNext(); ) {
            Field f = (Field) it.next();
            String vis = "";
            if (f.getAccess() == Field.SCOPE_PRIVATE) vis = "__";
            s.append(TAB + vis + f.getName() + " = None\n"); 
        }
        s.append("\n");
    }
    
    private void saveFile() {
        CLI.print("PythonOutput: Writing to '" + f.getAbsolutePath() + "'", 1);
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
