package domain;

import java.util.ArrayList;
import java.util.List;

public class ObserverCheck extends Check{

    public ObserverCheck(List<ClassData> classes, List<Relation> relations) {
        super(classes, relations);
    }

    @Override
    public List<Violation> checker() {
    	List<Violation> violations = new ArrayList<Violation>();
        List<ClassData> absClasses = new ArrayList<ClassData>();
        List<ClassData> possibleObservers = new ArrayList<ClassData>();
        for(ClassData cls : classes) {
        	if(cls.isAbstract() || cls.isInterface()) {
        		absClasses.add(cls);
        	}
        }
        if(absClasses.isEmpty()) {
        	System.out.println("No observer pattern found");
        	return violations; //no observer pattern found
        }
        
        //try to find all possible abstract observers (classes with an update method)
        for(ClassData cls : absClasses) {
        	for(MethodData method : cls.getMethods()) {
        		if(method.getMethodName().contains("update")) {
        			possibleObservers.add(cls);
        		}
        	}
        }
        if(possibleObservers.isEmpty()) { //no observer since the whole purpose is to have abstract observer so new ones can be easily added
        	System.out.println("No observer pattern found");
        	return violations; //no observer pattern found
        }
        
        //try to identify possible abstract subjects
        List<ClassData> possibleSubjects = new ArrayList<ClassData>();
        for(ClassData cls : classes) {
        	if(cls.getMethods().size() >= 3) {
        		
	        	String reg = "";
	        	String rem = "";
	        	boolean hasNotify = false;
	        	
	        	for(MethodData method : cls.getMethods()) {
	        		if(method.getMethodName().contains("notify")) {
	        			hasNotify = true;
	        		}
	        		else if(method.getMethodName().contains("add") || method.getMethodName().contains("register")) {
	        			if(method.getParameters().size() == 1) {
	        				reg = method.getParameters().get(0);
	        			}
	        			else {
	        				continue;
	        			}
	        		}
	        		else if(method.getMethodName().contains("delete") || method.getMethodName().contains("remove")) {
	        			if(method.getParameters().size() == 1) {
	        				rem = method.getParameters().get(0);
	        			}
	        			else {
	        				continue;
	        			}
	        		}
	        	}
	        	
	        	if(!(!hasNotify || reg.isEmpty() || rem.isEmpty())) {
	        		if(reg.equals(rem)) {
	        			for(ClassData abs : absClasses) {
	        	        	if(abs.getName().equals(reg)) {
	        	        		possibleSubjects.add(cls);
	        	        		break;
	        	        	}
	        	        }
	        		}
	        	}
        	}
        }
        if(possibleSubjects.isEmpty()) {
        	System.out.println("No observer pattern found");
        	return violations; //no observer pattern found
        }
        else  {
        	for(ClassData subcls : possibleSubjects) {
        		for(ClassData obcls : possibleObservers) {
        			for(Relation r : this.relations) {
        				if(r.getRelationship().equals(RelationshipTypes.USES) || r.getRelationship().equals(RelationshipTypes.HAS)) {
        					if(r.getFirstClass().equals(subcls.getName()) && r.getSecondClass().equals(obcls.getName())) {
        						Violation violation = new Violation("Found subject", "Subject: " + subcls.getName() + "Abstract Observer: " + obcls.getName());
        						violations.add(violation);
        					}
        				}
        			}
        		}
        	}
        }        
        
        
        return violations;
    }
}
