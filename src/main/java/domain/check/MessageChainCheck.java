package domain.check;

import domain.Relation;
import domain.RelationshipTypes;
import domain.Violation;
import domain.data.ClassData;
import domain.data.MethodData;

import java.util.ArrayList;
import java.util.List;

public class MessageChainCheck extends Check{

    public MessageChainCheck(List<ClassData> classes, List<Relation> relations) {
        super(classes, relations);
    }

    @Override
    public List<Violation> checker() {
    	List<Violation> violations = new ArrayList<Violation>();
        for(ClassData classData: classes) {
        	if(classData.isEnum()) {continue;}
            List<MethodData> methods = classData.getMethods();
            for (MethodData method : methods) {
            	String retType = method.getReturnType();
            	boolean isSub = false; //if return type is a subclass of itself
            	
            	List<String> subclasses = new ArrayList<String>();
            	for(Relation rel : this.relations) {
            		if(rel.getSecondClass().equals(classData.getName()) && rel.getRelationship().equals(RelationshipTypes.EXTENDS)) {
            			subclasses.add(rel.getFirstClass());
            		}
            	}
            	boolean hasSub = subclasses.isEmpty();
            	List<String> been = new ArrayList<String>();
            	while(hasSub) {
//            		if(been.containsAll(subclasses)) {
//            			hasSub = false;
//            			break;
//            		}
            		List<String> newSub = new ArrayList<String>();
	            	for(String s : subclasses) {
	            		if(been.contains(s)) continue;
	            		for(Relation rel : this.relations) {
	                		if(rel.getSecondClass().equals(s) && rel.getRelationship().equals(RelationshipTypes.EXTENDS)) {
	                			newSub.add(rel.getFirstClass());
	                		}
	                	}
	            		been.add(s);
	            	}
	            	if(newSub.isEmpty()) {
            			hasSub = false;
            			break;
            		}
	            	else {
	            		subclasses.addAll(newSub);
	            	}
            	}
            	
            	isSub = subclasses.contains(retType);
            	if(retType.equals(classData.getName()) || isSub) {
            		Violation violation = new Violation("Returning the object itself or its subclass might indicate message chaining, please check.", classData.getName());
                	violations.add(violation);
            	}
            }
        }
        return violations;
    }
}
