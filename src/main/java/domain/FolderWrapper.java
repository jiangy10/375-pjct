package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FolderWrapper {

    private File[] javaFiles;
    private ArrayList<String> classNameStrings;
    public FolderWrapper(String directoryPath){
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("The specified directory does not exist.");
            return;
        }
        this.javaFiles = directory.listFiles((dir, name) -> name.endsWith(".java"));

        if (javaFiles == null|| javaFiles.length==0 ) {
            System.err.println("Error while listing files.");
            return;
        }
        classNameStrings = new ArrayList<String>();
        for (File javaFile : javaFiles) {
            String javaName = javaFile.getName();

            String className = javaFile.getParentFile().getName()+ "." +(javaName.substring(0,javaName.length()-5));
            this.classNameStrings.add(className);
//            System.out.println(classNameStrings.get(classNameStrings.size()-1));

            //processClassFile(classFile);
        }
    }
    public ArrayList<String> getClassNames(){
        return this.classNameStrings;
    }
}
