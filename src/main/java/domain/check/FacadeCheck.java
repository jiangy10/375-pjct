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
    private List<Violation> violations = new ArrayList<Violation>();

    public FacadeCheck(List<ClassData> classes, List<Relation> relations) {super(classes, relations);}

    @Override
    public List<Violation> checker() {
        for(ClassData classData : classes){
            List<MethodData> methodDatas = classData.getMethods();
            boolean isMain = false;

    private boolean isMainClass(ClassData classData) {
        for(MethodData methodData : classData.getMethods()) {
            if (methodData.getName().contains("main") &&
                    methodData.getAccessModifier() == AccessModifiers.PUBLIC &&
                    methodData.isStatic()) {
                return true;
            }
        }
        return false;
    }

    private void checkForFacade(ClassData classData) {
        int deps = 0;
        for(Relation relation : relations) {
            if (relation.getFirstClass().equals(classData.getName()) &&
                    relation.getRelationship() == RelationshipTypes.USES) {
                deps++;
            }
        }
        if (deps >= DEPENDENCY_THRESHOLD) {
            Violation violation = new Violation("Facade found", classData.getName());
            violations.add(violation);
        }
    }
}
