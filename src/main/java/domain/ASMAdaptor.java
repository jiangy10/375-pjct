package domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import domain.data.ClassData;
import domain.data.MethodData;
import domain.data.MethodInstructionData;
import domain.data.VariableData;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

public class ASMAdaptor implements ClassLoader{
	private ASMLoader asmLoader;

	private List<Relation> relations;

	List<String> relationString;

	public ASMAdaptor(ASMLoader asmLoader, List<Relation> relations) {
		this.asmLoader = asmLoader;
		this.relations = relations;
		this.relationString = new ArrayList<>();
	}

	@Override
	public List<ClassData> loadClasses() {
		List<ClassNode> classNodes;
		List<ClassData> classDatas = new ArrayList<ClassData>();
		try {
			classNodes = this.asmLoader.loadClassNodes();
			if(classNodes == null) return classDatas;
			for(ClassNode node : classNodes) {
				Relation relation = new Relation(processName(node.name), processName(node.superName), RelationshipTypes.EXTENDS);
				if(!node.superName.contains("java") && !relationString.contains(relation.toString())){
					relations.add(relation);
					relationString.add(relation.toString());
				}
				String name = processName(node.name);
				for (String anInterface : node.interfaces) {
                    relation = new Relation(processName(node.name), processName(anInterface), RelationshipTypes.IMPLEMENTS);
					if (!relationString.contains(relation.toString())) {
						relations.add(relation);
						relationString.add(relation.toString());
					}
				}
				List<MethodData> methods = loadMethods(node.methods, name);
				List<VariableData> fields = loadVariables(node.fields, name);
				boolean isInterface = ((node.access & Opcodes.ACC_INTERFACE) != 0);
				boolean isAbstract = ((node.access & Opcodes.ACC_ABSTRACT) != 0);
				boolean isEnum = ((node.access & Opcodes.ACC_ENUM) != 0);
				ClassData classData = new ClassData(name, methods, fields, isInterface, isAbstract, isEnum);
				classDatas.add(classData);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return classDatas;
	}

	private String processName(String name) {
		if (name == null) {
			return ""; // Or some other appropriate default value or handling
		}
		int lastIndex = name.lastIndexOf('/');
		if (lastIndex != -1) {
			return name.substring(lastIndex + 1);
		}
		return name;
	}

	private List<MethodData> loadMethods(List<MethodNode> methodNodes, String className) {
		List<MethodData> methods = new ArrayList<>();

		for (MethodNode node : methodNodes) {
			List<MethodInsnNode> methodInstructions = new ArrayList<MethodInsnNode>();
			String name = node.name;
			boolean isAbstract = (node.access & Opcodes.ACC_ABSTRACT) != 0;

			Type[] parameters = Type.getArgumentTypes(node.desc);
			List<String> params = new ArrayList<String>();
			for (Type parameter : parameters) {
				params.add(processName(parameter.toString()));
			}

			InsnList instructions = node.instructions;
			AccessModifiers am;

			if ((node.access & Opcodes.ACC_PUBLIC) != 0) {
				am = AccessModifiers.PUBLIC;
			} else if ((node.access & Opcodes.ACC_PRIVATE) != 0) {
				am = AccessModifiers.PRIVATE;
			} else if ((node.access & Opcodes.ACC_PROTECTED) != 0) {
				am = AccessModifiers.PACKAGE_PROTECTED;
			} else {
				am = AccessModifiers.DEFAULT;
			}

			String returnType = processName(node.desc).replaceAll("[^a-zA-Z]", "");
//			System.out.println("desc: " + node.desc);
//			System.out.println(returnType);
//			Type retType = Type.getReturnType(node.desc);
//			System.out.println("ret: " + retType);
//			System.out.println("-----------");
			Relation relation = new Relation(processName(className), processName(returnType), RelationshipTypes.USES);
			if (!node.desc.contains("java") && returnType.length() > 3 &&
					!relationString.contains(relation.toString()) && !returnType.equals(processName(className))) {
				relations.add(relation);
				relationString.add(relation.toString());
			}

			int insnSize = instructions.size();
			for (int i = 0; i < insnSize; i++) {
				AbstractInsnNode insn = instructions.get(i);
				if (insn instanceof MethodInsnNode) { // TODO: Come back and change later
					MethodInsnNode mInsn = (MethodInsnNode) insn;
					methodInstructions.add(mInsn);
				}
			}
			List<MethodInstructionData> methodInsnDatas = loadInstruction(methodInstructions,name);
			boolean isStatic = ((node.access & Opcodes.ACC_STATIC)!=0);
			MethodData methodData = new MethodData(name, isAbstract, params, methodInsnDatas, am, insnSize,
					isStatic, processName(returnType));

			methods.add(methodData);
		}
		return methods;
	}


	private List<VariableData> loadVariables(List<FieldNode> fieldNodes, String className){
		List<VariableData> parameters = new ArrayList<VariableData>();
		for (FieldNode node:fieldNodes) {
			String name = node.name;
			boolean isAbstract = false;
			AccessModifiers accessModifiers = AccessModifiers.DEFAULT;
			if(node.access == 1) accessModifiers = AccessModifiers.PUBLIC;
			else if (node.access == 2) accessModifiers = AccessModifiers.PRIVATE;
			else if (node.access == 4) accessModifiers = AccessModifiers.PACKAGE_PROTECTED;
			if(!node.desc.contains("java") && processName(node.desc).length() > 4
					&& !processName(className).equals(processName(node.desc.substring(0, node.desc.length() - 1)))){
				Relation relation = new Relation(processName(className),
						processName(node.desc.substring(0, node.desc.length() - 1)), RelationshipTypes.HAS);
				if(!relationString.contains(relation.toString())) {
					relations.add(relation);
					relationString.add(relation.toString());
				}
			}
			AccessModifiers accessModifier = accessModifiers;
			String type = null; //TODO: come back and change this
			boolean isStatic = ((node.access & Opcodes.ACC_STATIC)!=0);
			VariableData var = new VariableData(name, isAbstract, accessModifier, type, isStatic);
			parameters.add(var);
		}
		return parameters;
	}

	private List<MethodInstructionData> loadInstruction(List<MethodInsnNode> insnNodes, String className){
		List<MethodInstructionData> instructions = new ArrayList<>();
		for (MethodInsnNode insnNode: insnNodes) {
			String methodCall = insnNode.name;
			String owner=insnNode.owner;
			MethodInstructionData methodInstruction = new MethodInstructionData(methodCall,owner);
			instructions.add(methodInstruction);
		}
		return instructions;
	}

	public List<Relation> getRelations() {
		return relations;
	}

}
