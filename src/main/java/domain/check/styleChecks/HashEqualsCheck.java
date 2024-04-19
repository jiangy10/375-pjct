package domain.check.styleChecks;

import domain.Relation;
import domain.Violation;
import domain.check.*;
import domain.data.ClassData;
import domain.data.MethodData;

import java.util.ArrayList;
import java.util.List;

public class HashEqualsCheck extends Check {

	public HashEqualsCheck(List<ClassData> classes, List<Relation> relations) {
		super(classes, relations);
	}

	@Override
	public List<Violation> checker() {
		boolean hasHash;
        boolean hasEq;
        for(ClassData classData: classes) {
        	hasHash = false;
        	hasEq = false;
            List<MethodData> methods = classData.getMethods();
            for (MethodData method : methods) {
            	if(method.getName().equals("hashCode")){
            		hasHash = true;
            	}
            	else if(method.getName().equals("equals")){
            		hasEq = true;
            	}
            }
            if(!((hasHash && hasEq) || (!hasHash && !hasEq))){
            	Violation violation = new Violation("Overriding hashCode or equals methods should also override the other", classData.getName());
            	violations.add(violation);
            }
        }
        return violations;
	}

}
