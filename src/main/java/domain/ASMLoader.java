package domain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

public class ASMLoader {
	private String filepath;
	private boolean isValid;
	
	public ASMLoader(String fp) {

		this.filepath = fp;
		FolderWrapper folder = new FolderWrapper(filepath);
		List<String> classNames = folder.getClassNames();
		if(classNames == null){
			this.isValid = false;
		}else{
			this.isValid = true;

		}
	}
	
	public List<ClassNode> loadClassNodes() throws IOException{

		FolderWrapper folder = new FolderWrapper(filepath);
		List<String> classNames = folder.getClassNames();
		// One way to read in a Java class with ASM:
		// Step 1. ASM's ClassReader does the heavy lifting of parsing the compiled Java class.
		List<ClassNode> classNodes=new ArrayList<ClassNode>();
		for (String className: classNames) {
			ClassReader reader = new ClassReader(className);

			// Step 2. ClassNode is just a data container for the parsed class
			ClassNode classNode = new ClassNode();

			// Step 3. Tell the Reader to parse the specified class and store its data in our ClassNode.
			// EXPAND_FRAMES means: I want my code to work. (Always pass this flag.)
			reader.accept(classNode, ClassReader.EXPAND_FRAMES);
			classNodes.add(classNode);
		}
		return classNodes;
	}

	public boolean isValid() {return isValid;}
}
