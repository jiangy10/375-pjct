package domain.check;

import domain.Relation;
import domain.Violation;
import domain.data.ClassData;

import java.util.*;

public abstract class Check {
	protected static List<ClassData> classes;
	protected List<Relation> relations;
	protected List<Violation> violations = new ArrayList<Violation>();

	public Check(List<ClassData> classes, List<Relation> relations) {
		Check.classes = classes;
		this.relations = relations;
	}
	
	abstract public List<Violation> checker(); //change to void and just add it to the violations list
}
