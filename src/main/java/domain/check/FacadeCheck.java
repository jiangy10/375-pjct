package domain.check;

import domain.AccessModifiers;
import domain.Relation;
import domain.RelationshipTypes;
import domain.Violation;
import domain.data.ClassData;
import domain.data.MethodData;

import java.util.ArrayList;
import java.util.List;

public class FacadeCheck extends Check{
    private static final int DEPENDENCY_THRESHOLD = 5;

    public FacadeCheck(List<ClassData> classes, List<Relation> relations) {super(classes, relations);}

    @Override
    public List<Violation> checker() {
        List<Violation> violations = new ArrayList<>();
        for(ClassData classData : classes){
            List<MethodData> methodDatas = classData.getMethods();
            boolean isMain = false;

            for(MethodData methodData : methodDatas){ //Find which class is main, main cannot be a facade
                if (methodData.getMethodName().contains("main") &&
                        methodData.getAccessModifier() == AccessModifiers.PUBLIC &&
                        methodData.isStatic()){ //Check for public methods called main
                    isMain = true;
                }
            }

            if(!isMain){//Check not main for calling several methods from different classes
                int deps = 0;
                for(Relation relation : relations){
                    if(relation.getFirstClass().equals(classData.getName()) &&
                            relation.getRelationship() == RelationshipTypes.USES){
                        deps ++;
                    }
                }
                if(deps >= DEPENDENCY_THRESHOLD){
                    Violation violation = new Violation("Facade found",
                            classData.getName());
                    violations.add(violation);
                }
            }
        }
        return violations;
    }
}
