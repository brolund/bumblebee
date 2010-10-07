package net.sf.umlspeed.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public abstract class DataStore {
    
    public static Map classes = new HashMap();
    public static Map interfaces = new HashMap();
    public static Map diagrams = new HashMap();
    public static Vector orderedDiagrams = new Vector();
    
    public static Map actors = new HashMap();
    public static Map usecases = new HashMap();
    
    public static Map components = new HashMap();
    public static Map deployments = new HashMap();
    
    // All entities in one map
    public static Map entities = new HashMap();
    
    // Files imported
    public static Map filesimported = new HashMap();
    
    /** The current namespace that entites will go into */
    public static String namespace = "default";
    public static Map namespaces = new HashMap();
    
    public static void addEntityToNameSpace(String namespace, Entity e) {
        // Lookup this namespace first
        List l = (List) namespaces.get(namespace);
        if (l == null) {
            l = new ArrayList();
            namespaces.put(namespace, l);
        }
        l.add(e);
    }
    
    public static void reset() {
        classes = new HashMap();
        interfaces = new HashMap();
        diagrams = new HashMap();
        orderedDiagrams = new Vector();
        actors = new HashMap();
        usecases = new HashMap();
        components = new HashMap();
        deployments = new HashMap();
        entities = new HashMap();
        namespace = "default";
        namespaces = new HashMap();
        filesimported = new HashMap();
    }

}
