package domain.check;

import domain.Relation;
import domain.Violation;
import domain.data.ClassData;
import domain.data.MethodData;
import domain.data.VariableData;

import java.util.ArrayList;
import java.util.List;

public class NameCheck extends Check {

    private List<Violation> violations;

    public NameCheck(List<ClassData> classes, List<Relation> relations) {
        super(classes, relations);
    }

    public static boolean isGoodClassName(String className) {
        return className != null && !className.isEmpty() && Character.isUpperCase(className.charAt(0))
                && !className.contains("_") && className.matches("^[A-Z][a-zA-Z0-9]*$");
    }

    @Override
    public List<Violation> checker() {
        for (ClassData classData : classes) {
            if (!isGoodClassName(classData.getName())) {
                checkClassName(classData);
            }
            checkFieldNames(classData);
            checkMethodNames(classData);
        }
        return this.violations;
    }

    private void checkClassName(ClassData classData) {
        if (!isGoodClassName(classData.getName())) {
            Violation violation = new Violation("Bad Class Name - " + classData.getName(), classData.getName());
            this.violations.add(violation);
        }
    }

    private void checkFieldNames(ClassData classData) {
        List<VariableData> fields = classData.getFields();
        if (fields != null) {
            for (VariableData variableData : fields) {
                boolean hasBadName = !variableData.getName().matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
                if (hasBadName) {
                    Violation violation = new Violation("Bad Variable Name - " + variableData.getName(), classData.getName());
                    this.violations.add(violation);
                }
            }
        }
    }

    private void checkMethodNames(ClassData classData) {
        List<MethodData> methods = classData.getMethods();
        if (methods != null) {
            for (MethodData methodData : methods) {
                boolean hasBadName = !methodData.getMethodName().matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
                if (hasBadName) {
                    Violation violation = new Violation("Bad Method Name - " + methodData.getMethodName(), classData.getName());
                    violations.add(violation);
                }
            }
        }
    }
}
