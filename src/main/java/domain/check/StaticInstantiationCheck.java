package domain.check;

import domain.data.MethodInstructionData;
import domain.Relation;
import domain.Violation;
import domain.data.ClassData;
import domain.data.MethodData;
import domain.data.VariableData;

import java.util.ArrayList;
import java.util.List;

public class StaticInstantiationCheck extends Check {
    private final String VIOLATION_DESCRIPTION = ": Class with static fields and methods is instantiated in: ";
    private final String INIT_METHOD = "<init>";
    public StaticInstantiationCheck(List<ClassData> classes, List<Relation> relations) {
        super(classes, relations);
    }


    @Override
    public List<Violation> checker() {
        List<Violation> violations = new ArrayList<Violation>();
        for (ClassData classData: classes){
            if(checkStaticClass(classData)){
                violations.addAll(checkIsInstantiated(classData));
            }
        }
        return violations;
    }
    private String processName(String name) {
        int lastIndex = name.lastIndexOf('/');
        if (lastIndex != -1) {
            return name.substring(lastIndex + 1);
        }
        return name;
    }
    private List<Violation> checkIsInstantiated(ClassData staticClass){

        List<Violation> violations = new ArrayList<Violation>();
        for (ClassData classData: classes){
            for(MethodData method: classData.getMethods()){

                for (MethodInstructionData insn: method.getInstructions()){
                    if(processName(insn.getOwner()).equals(staticClass.getName())
                            &&insn.getName().equals(INIT_METHOD)){
                        violations.add(new Violation(staticClass.getName()+VIOLATION_DESCRIPTION+classData.getName(),staticClass.getName()));

                    }
                }
            }
        }
        return violations;
    }

    private boolean checkStaticClass(ClassData classData){

        return (checkStaticFields(classData.getFields()) && checkStaticMethods(classData.getMethods()));
    }

    private boolean checkStaticFields(List<VariableData> fields){
        boolean isStatic = true;
        if (fields.size()<=0){
            return false;
        }
        for (VariableData field: fields){
            if(!field.isStatic()){
                isStatic = false;
            }
        }
        return isStatic;
    }

    private boolean checkStaticMethods(List<MethodData> methods){
        boolean isStatic = true;
        if (methods.size()<=0){
            return false;
        }
        for (MethodData method: methods){
            if((!method.isStatic())&&(!method.getMethodName().equals(INIT_METHOD))){
                isStatic = false;
            }
        }
        return isStatic;
    }

}
