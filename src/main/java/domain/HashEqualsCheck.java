package domain;

import java.util.ArrayList;
import java.util.List;

public class HashEqualsCheck extends Check{

	public HashEqualsCheck(List<ClassData> classes, List<Relation> relations) {
		super(classes, relations);
	}

	@Override
	public List<Violation> checker() {
		List<Violation> violations = new ArrayList<Violation>();
		boolean hasHash;
        boolean hasEq;
        for(ClassData classData: classes) {
        	hasHash = false;
        	hasEq = false;
            List<MethodData> methods = classData.getMethods();
            for (MethodData method : methods) {
            	if(method.getMethodName().equals("hashCode")){ 
            		hasHash = true;
            	}
            	else if(method.getMethodName().equals("equals")){
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
