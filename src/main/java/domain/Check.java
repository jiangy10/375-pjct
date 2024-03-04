package domain;

import java.util.List;

public abstract class Check {
	protected static List<ClassData> classes;
	protected List<Relation> relations;

	public Check(List<ClassData> classes, List<Relation> relations) {
		Check.classes = classes;
		this.relations = relations;
	}
	
	abstract public List<Violation> checker(); //change to void and just add it to the violations list
}
