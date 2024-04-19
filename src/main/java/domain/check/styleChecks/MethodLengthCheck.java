package domain.check.styleChecks;

import domain.Relation;
import domain.Violation;
import domain.check.*;
import domain.data.ClassData;
import domain.data.MethodData;

import java.util.ArrayList;
import java.util.List;

public class MethodLengthCheck extends Check {

    private static final int MAX_LEN = 250;
    public MethodLengthCheck(List<ClassData> classes, List<Relation> relations) {
        super(classes, relations);
    }

    @Override
    public List<Violation> checker() {
        for(ClassData classData : classes){
            List<MethodData> methods = classData.getMethods();
            for (MethodData method : methods) {
                String methodName = method.getName();
                if (method.getInstructionSize() > MAX_LEN) {
                    Violation violation = new Violation(methodName + "(..) is " + (method.getInstructionSize() - MAX_LEN) + " instructions too long", classData.getName());
                    violations.add(violation);
                }
            }
        }
        return violations;
    }
}
