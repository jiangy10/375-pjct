package domain.data;

import java.util.List;

public class ClassData {
	private String name;
	private List<MethodData> methods;
	private List<VariableData> fields;
	private boolean isInterface;
	private boolean isAbstract;
	private boolean isEnum;
	
	public ClassData(String name, List<MethodData> methods, List<VariableData> fields, boolean isInterface, boolean isAbstract, boolean isEnum) {
		this.name = name;
		this.methods = methods;
		this.fields = fields;
		this.isInterface = isInterface;
		this.isAbstract = isAbstract;
		this.isEnum = isEnum;
	}

	public String getName() {
		return name;
	}


	public List<MethodData> getMethods() {
		return methods;
	}


	public List<VariableData> getFields() {
		return fields;
	}


	public boolean isInterface() {
		return isInterface;
	}



	public boolean isAbstract() {
		return isAbstract;
	}

	
	public boolean isEnum() {
		return isEnum;
	}
}
