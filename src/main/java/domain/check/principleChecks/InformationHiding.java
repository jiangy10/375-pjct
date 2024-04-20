package domain.check.principleChecks;

import domain.*;
import domain.check.Check;
import domain.data.ClassData;
import domain.data.MethodData;
import domain.data.MethodInstructionData;
import domain.data.VariableData;

import java.util.ArrayList;
import java.util.List;

public class InformationHiding extends Check {
    public InformationHiding(List<ClassData> classes, List<Relation> relations) {
        super(classes, relations);
    }

    public static ClassData getClassByClassName(String className) {
        for (ClassData classData : classes) {
            if (classData.getName().equals(className)) {
                return classData;
            }
        }
        return null;
    }
    @Override
    public List<Violation> checker() {
        List<Violation> violations = new ArrayList<>();



        for (ClassData classData : classes) {
            List<VariableData> fields = classData.getFields();
            if (fields != null) {
                for (VariableData variableData : fields) {
                    if (variableData.getAccessModifier() != null && !variableData.getAccessModifier().equals(AccessModifiers.PRIVATE)) {
                        Violation violation = new Violation("Access modifier public variable - " + variableData.getName(), classData.getName());
                        violations.add(violation);
                    }
                }
            }
        }

        if (relations == null) {
            return violations;
        }

        for (Relation relation : relations) {
            if (relation.getRelationship().equals(RelationshipTypes.EXTENDS)) {
                ClassData firstClass = getClassByClassName(relation.getFirstClass());
                ClassData secondClass = getClassByClassName(relation.getSecondClass());
                if (firstClass == null || secondClass == null || isMethodListEmpty(firstClass.getMethods()) || isMethodListEmpty(secondClass.getMethods())) {
                    return violations;
                }
                boolean used = false;
                for (MethodData methodData : secondClass.getMethods()) {
                    List<MethodInstructionData> methodInstructionData = methodData.getInstructions();
                    if (methodInstructionData != null) {
                        for (MethodInstructionData methodInstructionData1 : methodInstructionData) {
                            if (methodInstructionData1.getName().equals(methodData.getName())) {
                                used = true;
                            }
                        }
                    }
                }
                if (!used) {
                    Violation violation = new Violation("Information hiding, getter could have been avoided", firstClass.getName());
                    violations.add(violation);
                }
            }
        }

        return violations;
    }

    private boolean isMethodListEmpty(List<MethodData> methods) {
        return methods == null || methods.isEmpty();
    }
}
