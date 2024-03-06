package domain.check;

import domain.Relation;
import domain.Violation;
import domain.data.ClassData;
import domain.data.MethodData;
import domain.data.VariableData;

import java.util.ArrayList;
import java.util.List;

public class NameCheck extends Check {
    public NameCheck(List<ClassData> classes, List<Relation> relations) {
        super(classes, relations);
    }

    public static boolean isGoodClassName(String className) {
        return className != null && !className.isEmpty() && Character.isUpperCase(className.charAt(0))
                && !className.contains("_") && className.matches("^[A-Z][a-zA-Z0-9]*$");
    }

    @Override
    public List<Violation> checker() {
        List<Violation> violations = new ArrayList<Violation>();
        for (ClassData classData : classes) {
            if (!isGoodClassName(classData.getName())) {
                Violation violation = new Violation("Bad Class Name - " + classData.getName(), classData.getName());
                violations.add(violation);
            }

            List<VariableData> fields = classData.getFields();
            if (fields != null) {
                for (VariableData variableData : fields) {
                    boolean hasBadName = !variableData.getName().matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
                    if (hasBadName) {
                        Violation violation = new Violation("Bad Variable Name - " + variableData.getName(), classData.getName());
                        violations.add(violation);
                    }
                }
            }

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
        return violations;
    }
}
