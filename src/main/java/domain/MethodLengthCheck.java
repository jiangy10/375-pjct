package domain;

import java.util.ArrayList;
import java.util.List;

public class MethodLengthCheck extends Check{

    private static final int MAX_LEN = 250;
    public MethodLengthCheck(List<ClassData> classes, List<Relation> relations) {
        super(classes, relations);
    }

    @Override
    public List<Violation> checker() {
        List<Violation> violations = new ArrayList<>();
        for(ClassData classData : classes){
            List<MethodData> methods = classData.getMethods();
            for (MethodData method : methods) {
                String methodName = method.getMethodName();
                if (method.getInstructionSize() > MAX_LEN) {
                    Violation violation = new Violation(methodName + "(..) is " + (method.getInstructionSize() - MAX_LEN) + " instructions too long", classData.getName());
                    violations.add(violation);
                }
            }
        }
        return violations;
    }
}
