package domain.data;


import domain.AccessModifiers;

import java.util.List;

public class MethodData extends Data{
	private List<String> parameters;
	private List<MethodInstructionData> instructions;
	private AccessModifiers accessModifier;

	private String returnType;
  	private int insnSize;
	private boolean isStatic;

	public MethodData(String methodName, boolean isAbstract, List<String> parameters, List<MethodInstructionData> instructions, 
			AccessModifiers accessModifier,int insnSize, boolean isStatic, String returnType) {
		super(methodName, isAbstract);
		this.parameters = parameters;
		this.instructions = instructions;
		this.accessModifier = accessModifier;
		this.isStatic = isStatic;
		this.returnType = returnType;
		this.insnSize = insnSize;
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