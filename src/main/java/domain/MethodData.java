package domain;


import org.objectweb.asm.Type;

import java.util.List;

public class MethodData {
	private String methodName;
	private boolean isAbstract;
	private List<String> parameters;
	private List<MethodInstructionData> instructions;
	private AccessModifiers accessModifier;

	private String returnType;
  private int insnSize;
	private boolean isStatic;

	public MethodData(String methodName, boolean isAbstract, List<String> parameters, List<MethodInstructionData> instructions, 
			AccessModifiers accessModifier,int insnSize, boolean isStatic, String returnType) {

		this.methodName = methodName;
		this.isAbstract = isAbstract;
		this.parameters = parameters;
		this.instructions = instructions;
		this.accessModifier = accessModifier;
		this.isStatic = isStatic;
		this.returnType = returnType;
		this.insnSize = insnSize;
	}

	public String getMethodName() {
		return methodName;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public List<MethodInstructionData> getInstructions() {
		return instructions;
	}

	public AccessModifiers getAccessModifier(){return accessModifier;}

	public int getInstructionSize(){ return insnSize; }

  public boolean isStatic() { return isStatic; }
  
  public String getReturnType() {return this.returnType;}

}