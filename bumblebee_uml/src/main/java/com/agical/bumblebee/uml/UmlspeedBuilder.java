package com.agical.bumblebee.uml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.umlspeed.Settings;
import net.sf.umlspeed.entities.DataStore;
import net.sf.umlspeed.parser.Parser;
import net.sf.umlspeed.svg.SVGRenderer;
import net.sourceforge.pmd.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.ast.ASTCompilationUnit;
import net.sourceforge.pmd.ast.ASTFormalParameter;
import net.sourceforge.pmd.ast.ASTImportDeclaration;
import net.sourceforge.pmd.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.ast.ASTResultType;
import net.sourceforge.pmd.ast.ASTType;
import net.sourceforge.pmd.ast.ASTTypeArgument;
import net.sourceforge.pmd.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.ast.SimpleJavaNode;

import org.apache.batik.apps.rasterizer.SVGConverterException;
import org.jaxen.JaxenException;

import com.agical.bumblebee.parser.CannotFindSourceException;
import com.agical.bumblebee.parser.NewLine;
import com.agical.bumblebee.parser.PmdParser;

public class UmlspeedBuilder {
    
    private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[] {};
    private PmdParser pmdParser;

    public UmlspeedBuilder(PmdParser pmdParser) {
        super();
        this.pmdParser = pmdParser;
    }
    public void classdiagramForMethod(String baseDir, String baseName, String clazz, String method, String focusClass) {
        try {
            cleanStaticCrapInUmlspeed();
            File baseDirFile = new File(baseDir);
            baseDirFile.mkdirs();
            File umsFile = new File(baseDirFile,  baseName + ".ums");
            Class<?>[] classesUsedInMethod = getClassesUsedInMethod(getClass(clazz), method, EMPTY_CLASS_ARRAY).toArray(EMPTY_CLASS_ARRAY);
            writeClassesAndClassDiagram(new FileWriter(umsFile), baseName, classesUsedInMethod);
            generateSvgFiles(umsFile, baseDirFile);
            new BatikUtility().convertSvgFilesToPngFiles(baseDirFile, new String[] {baseDir + baseName + ".svg"});
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException( e.getMessage() , e );
        } catch (SVGConverterException e) {
            e.printStackTrace();
            throw new RuntimeException( e.getMessage(), e );
        }
    }
    private void cleanStaticCrapInUmlspeed() {
        Settings.reset();
    }
    private Class<?> getClass(String clazz) {
        try {
            return Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException( "Could not create Class from name: " + clazz, e );
        }
    }
    public void classdiagramForClasses(String baseDir, String baseName, Class<?>[] classes) {
        try {
            cleanStaticCrapInUmlspeed();
            File baseDirFile = new File(baseDir);
            baseDirFile.mkdirs();
            File umsFile = new File(baseDirFile,  baseName + ".ums");
            writeClassesAndClassDiagram(new FileWriter(umsFile), baseName, classes);
            generateSvgFiles(umsFile, baseDirFile);
            new BatikUtility().convertSvgFilesToPngFiles(baseDirFile, new String[] {baseDir + baseName + ".svg"});
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException( e.getMessage() , e );
        } catch (SVGConverterException e) {
            e.printStackTrace();
            throw new RuntimeException( e.getMessage(), e );
        }
    }

    public String generateClassesDeclaration(Class<?>... classes) {
        String result = "";
        String namespace = "";
        for (Class<?> class1 : classes) {
            String newNamespace = "namespace " + class1.getPackage().getName() + ";" + NewLine.STR;
            if(!newNamespace.equals(namespace)) {
                namespace = newNamespace;
                result += namespace;
            }
            try {
                ASTCompilationUnit ast = pmdParser.getAst(class1);
                result += "class " + class1.getSimpleName() + " {" + NewLine.STR + 
                "    comment = \"\";" + NewLine.STR + 
                "    operations {" + NewLine.STR;
                List<ASTMethodDeclaration> methods = ast.findChildrenOfType(ASTMethodDeclaration.class);
                for (ASTMethodDeclaration method : methods) {
                    result += "        " + getPublicity(method) + method.getMethodName() + "(";
                    result += getArguments(method) + "): " + getResultType(method) + ";" + NewLine.STR;
                }
                result += "    }" + NewLine.STR + 
                "}" + NewLine.STR;
            } catch (CannotFindSourceException e) {
                result += "class " + class1.getSimpleName() + " {" + NewLine.STR + 
                "    comment = \"\";" + NewLine.STR + 
                "    operations {" + NewLine.STR;
                Method[] methods = class1.getDeclaredMethods();
                for (Method method : methods) {
                    result += "        " + getPublicity(method) + method.getName() + "(";
                    result += getArguments(method) + "): " + method.getReturnType().getSimpleName() + ";" + NewLine.STR;
                }
                result += "    }" + NewLine.STR + 
                "}" + NewLine.STR;
            }
            
        }
        return result;
    }

    private String getResultType(ASTMethodDeclaration method) {
        ASTResultType resultType = method.getResultType();
        if(resultType.isVoid()) return "void";
        ASTType type = resultType.getFirstChildOfType(ASTType.class);
        String result = getType(type);
        return result;
    }


    private String getType(ASTType type) {
        try {
            String result = type.getTypeImage();
            String genericTypes = getTypeArguments(type);
            
            return result + genericTypes;
        } catch (JaxenException e) {
            throw new RuntimeException( "Could not XPath type \n" + type, e );
        }
    }


    private String getTypeArguments(SimpleJavaNode type) throws JaxenException {
        String genericTypes = "";
        List<ASTTypeArgument> typeArguments = type.findChildNodesWithXPath("./ReferenceType/ClassOrInterfaceType/TypeArguments/TypeArgument");
        if(!typeArguments.isEmpty()) {
            genericTypes +="<";
            String betweenGenericArguments = "";
            for (ASTTypeArgument typeArgument : typeArguments) {
                ASTClassOrInterfaceType innerType = typeArgument.getFirstChildOfType(ASTClassOrInterfaceType.class);
                genericTypes+=betweenGenericArguments;
                betweenGenericArguments = " ";
                genericTypes += innerType.getImage() + getTypeArguments(typeArgument);
            }
            genericTypes +=">";
        }
        return genericTypes;
    }


    private String getArguments(ASTMethodDeclaration method) {
        List<ASTFormalParameter> parameters = new ArrayList<ASTFormalParameter>();
        method.findChildrenOfType(ASTFormalParameter.class, parameters, true);
        String result = "";
        String comma = "";
        for (ASTFormalParameter formalParameter : parameters) {
            result += comma;
            comma = ", ";
            ASTVariableDeclaratorId variableDeclaratorId = formalParameter.findChildrenOfType(ASTVariableDeclaratorId.class).get(0);
            result +=  variableDeclaratorId.getImage() + ": " + getType(formalParameter.getTypeNode());
        }
        return result;
    }
    private String getArguments(Method method) {
        String result = "";
        String comma = "";
        int argNr = 0;
        for (Class<?> formalParameter : method.getParameterTypes()) {
            result += comma;
            comma = ", ";
            result +=  "arg" + argNr + ": " + formalParameter.getClass().getSimpleName();
        }
        return result;
    }

    private String getPublicity(ASTMethodDeclaration method) {
        if(method.isPublic()) return "+";
        if(method.isPrivate()) return "-";
        if(method.isProtected()) return "#";
        return "#";
    }
    private String getPublicity(Method method) {
        int modifiers = method.getModifiers();
        if(Modifier.isPublic(modifiers)) return "+";
        if(Modifier.isPrivate(modifiers)) return "-";
        if(Modifier.isProtected(modifiers)) return "#";
        return "#";
    }



    public String generateClassDiagram(String diagramName, Class<?> focusClass, Class<?>... classes) {
        String result = "classdiagram " + diagramName + " {" + NewLine.STR + 
        "    comment = \"\";" + NewLine.STR + 
        "    layout = satellite (" + classes[0].getSimpleName() + ");" + NewLine.STR + 
        "    entities {" + NewLine.STR + 
        getEntities(Arrays.asList(classes)) + 
        "    }" + NewLine.STR + 
        "    documentation = \"\";" + NewLine.STR + 
        "}" + NewLine.STR;
        return result;
    }


    private String getEntities(List<Class<?>> classes) {
        String result = "";
        Set<Class<?>> remainingClasses = new HashSet<Class<?>>(classes);
        Set<String> addedRelations = new HashSet<String>();
        
        String indent = "        ";
        String lineEnd = ";"+NewLine.STR;
        for (Class<?> class1: classes) {
            for (Class<?> class2: classes) {
                    if(hasDependency(class1, class2)) {
                        String line = indent + class1.getSimpleName() + " depends " + class2.getSimpleName() + lineEnd;
                        if(!addedRelations.contains(line)) {
                            result += line;
                            addedRelations.add(line);
                            remainingClasses.remove(class1);
                            remainingClasses.remove(class2);
                        }
                    }
                    if(doesExtend(class1, class2)) {
                        String line = indent + class1.getSimpleName() + " extends " + class2.getSimpleName() + lineEnd;
                        if(!addedRelations.contains(line)) {
                            result += line;
                            addedRelations.add(line);
                            remainingClasses.remove(class1);
                            remainingClasses.remove(class2);
                        }
                    }
            }
        }
        for (Class<?> clazz : remainingClasses) {
            result += indent + clazz.getSimpleName()+ lineEnd;
        }
        return result;
    }


    private boolean doesExtend(Class<?> origin, Class<?> possibleSuperClass) {
        return !possibleSuperClass.equals(origin) && possibleSuperClass.isAssignableFrom(origin);
    }

    private boolean hasDependency(Class<?> origin, Class<?> other) {
        for (Method method : origin.getMethods()) {
            if(method.getReturnType().equals(other)) return true;
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (Class<?> parameter : parameterTypes) {
                if(parameter.equals(other)) return true;
            }
        }
        return false;
    }

    private static class CompareClassAsString implements Comparator<Class<?>> {
        public int compare(Class<?> o1, Class<?> o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
    public interface ClassFilter {
        boolean accept(Class<?> clazz);
    }
    public class AnyFilter implements ClassFilter {
        ClassFilter[] classFilters;
        public AnyFilter(ClassFilter... classFilters) {
            this.classFilters = classFilters;
        }
        public boolean accept(Class<?> clazz) {
            for (ClassFilter filter : classFilters) {
                if(!filter.accept(clazz)) return false;
            }
            return true;
        }
        
    }
    public class AcceptNonPrimitiveFilter implements ClassFilter {
        public boolean accept(Class<?> clazz) {
            return !clazz.isPrimitive();
        }
    }
    public class AcceptNonJavaLangFilter implements ClassFilter {
        public boolean accept(Class<?> clazz) {
            return !clazz.getPackage().toString().startsWith("java.lang");
        }
    }
    
    public void writeClassesAndClassDiagram(String fileName, String diagramName, Class<?>... classes) {
        try {
            File file = new File(fileName);
            file.getParentFile().mkdirs();
            writeClassesAndClassDiagram(new FileWriter(file), diagramName, classes);
        } catch (IOException e) {
            throw new RuntimeException( "Couldn't write diagram to " + fileName, e );
        }
    }
    public void writeClassesAndClassDiagram(Writer stringWriter, String diagramName, Class<?>... classes) {
        try {
            stringWriter.append(generateClassesDeclaration(classes)).append(generateClassDiagram(diagramName, classes[0], classes));
        } catch (IOException e) {
            throw new RuntimeException( "", e );
        } finally {
            try {
                stringWriter.close();
            } catch (IOException e) {
                throw new RuntimeException( "", e );
            }
        }
    }


    public static void generateSvgFiles(File umsDiagramFile, File svgTargetDir) {
        Settings.inputFile = umsDiagramFile;
        Parser parser = new Parser();
        parser.bufferFile(Settings.inputFile);
        parser.parse();
        svgTargetDir.mkdirs();
        Settings.outputDir = svgTargetDir.getAbsolutePath();
        SVGRenderer renderer = new SVGRenderer();
        // Render each diagram and add its name to a list
        for (Iterator it = DataStore.orderedDiagrams.iterator(); it.hasNext();) {
            String diagramEntity = it.next().toString();
            renderer.render(diagramEntity);
        }
    }

    public String generateClassDiagram(String diagramName, Class<?> classWithMethod, String methodName, Class<?>... arguments) {
        Collection<Class<?>> classesUsedInMethod = getClassesUsedInMethod(classWithMethod, methodName, arguments);
        Class<?>[] classes = classesUsedInMethod.toArray(EMPTY_CLASS_ARRAY);
        return generateClassDiagram(diagramName, classWithMethod, classes);
    }

    private Collection<Class<?>> getClassesUsedInMethod(Class<?> classWithMethod, String methodName, Class<?>[] arguments) {
        ASTCompilationUnit ast;
        try {
            ast = pmdParser.getAst(classWithMethod);
        } catch (CannotFindSourceException e1) {
            return new ArrayList<Class<?>>();
        }
        List<ASTImportDeclaration> imports = ast.findChildrenOfType(ASTImportDeclaration.class);
        Map<String, Class<?>> simpleNameToClass = new HashMap<String, Class<?>>();
        for (ASTImportDeclaration importDeclaration : imports) {
            if(!importDeclaration.isStatic()) {
                String className = importDeclaration.getImportedName();
                try {
                    Class<?> clazz = Class.forName(className);
                    simpleNameToClass.put(clazz.getSimpleName(), clazz);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException( "", e );
                }
            }
        }
        List<ASTMethodDeclaration> methods = ast.findChildrenOfType(ASTMethodDeclaration.class);
        List<Class<?>> usedClasses = new ArrayList<Class<?>>();
        usedClasses.addAll(Arrays.asList(arguments));
        for (ASTMethodDeclaration methodDeclaration : methods) {
            if(methodDeclaration.getMethodName().equals(methodName)) {
                List<ASTClassOrInterfaceType> types = methodDeclaration.findChildrenOfType(ASTClassOrInterfaceType.class);
                for (ASTClassOrInterfaceType type : types) {
                    Class<?> classFromSimpleName = getClassFromSimpleName(type.getImage(), classWithMethod, simpleNameToClass);
                    usedClasses.add(classFromSimpleName);
                }
            }
        }
        return removeDuplicatesKeepOrder(usedClasses);
    }

    private <T> Collection<T> removeDuplicatesKeepOrder(List<T> list) {
        Set<T> set = new HashSet<T>();
        List<T> uniqueList = new ArrayList<T>();
        for (T t : list) {
            if(set.contains(t)) continue;
            uniqueList.add(t);
        }
        return uniqueList;
    }

    private Class<?> getClassFromSimpleName(String simpleName, Class<?> classWithMethod,
            Map<String, Class<?>> simpleNameToClass) {
        if(simpleNameToClass.containsKey(simpleName)) {
            return simpleNameToClass.get(simpleName);
        }
        try {
            Class<?> clazz = Class.forName(classWithMethod.getPackage().getName() + "." + simpleName);
            return clazz;
        } catch (ClassNotFoundException e) {
            // Ok, keep looking
        }
        try {
            Class<?> clazz = Class.forName("java.lang." + simpleName);
            return clazz;
        } catch (ClassNotFoundException e) {
            // Ok, keep looking
        }
        try {
            Class<?> clazz = Class.forName(simpleName);
            return clazz;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException( "Could not find class with simple name: " + simpleName);
        }
    }
}
