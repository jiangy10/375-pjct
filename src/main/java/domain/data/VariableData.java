package domain.data;

import domain.AccessModifiers;

public class VariableData {
	private String name;
	private boolean isAbstract;
	private AccessModifiers accessModifier;
	private String type;

	private boolean isStatic;
	
	public VariableData(String name, boolean isAbstract, AccessModifiers accessModifier, String type,boolean isStatic) {
		this.name = name;
		this.isAbstract = isAbstract;
		this.accessModifier = accessModifier;
		this.type = type;
		this.isStatic = isStatic;
	}

	public String getName() {
		return name;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public AccessModifiers getAccessModifier() {
		return accessModifier;
	}

	public String getType() {
		return type;
	}

	public boolean isStatic() { return isStatic; }
	
}
