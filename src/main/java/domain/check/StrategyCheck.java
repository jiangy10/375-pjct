package domain.check;

import domain.data.MethodInstructionData;
import domain.Relation;
import domain.RelationshipTypes;
import domain.Violation;
import domain.data.ClassData;
import domain.data.MethodData;

import java.util.*;

public class StrategyCheck extends Check{
    private Set<String> classNames;
    private Hashtable<String, ClassData> classTable;

    private final String DESC = "Strategy pattern dectected";
    public StrategyCheck(List<ClassData> classes, List<Relation> relations) {
        super(classes, relations);
        setUpVariables();
    }

    private void setUpVariables(){
        classNames = new HashSet<String>();
        classTable = new Hashtable<String,ClassData>();
        for (ClassData classData:classes){
            classNames.add(classData.getName());
            classTable.put(classData.getName(),classData);
        }
    }
    @Override
    public List<Violation> checker() {
        for(Relation relation: relations){
            String firstClassName = relation.getFirstClass();
            String secondClassName = relation.getSecondClass();
            if(relation.getRelationship().equals(RelationshipTypes.HAS)&&classNames.contains(secondClassName)){
                ClassData secondClass = classTable.get(secondClassName);
                if(secondClass.isInterface() && classNames.contains(firstClassName)){
                    ClassData firstClass = classTable.get(relation.getFirstClass());
                    if(checkInterfaceMethodCall(firstClass,secondClass)){
                        violations.add(new Violation(DESC,firstClassName));
                    }

                }

            }
        }
        return violations;
    }

    private boolean checkInterfaceMethodCall(ClassData callerClass, ClassData interfaceClass){
        boolean strategyPassedInAtRuntime= false;
        boolean interfaceMethodIsCalled = false;

        for(MethodData methodData: callerClass.getMethods()){
            if(methodData.getParameters().contains(interfaceClass.getName())){
                strategyPassedInAtRuntime = true;
            }

            for(MethodInstructionData insn: methodData.getInstructions()){
                if(insn.getOwner().equals(interfaceClass.getName())){
                    interfaceMethodIsCalled = true;
                }
            }
        }
        return (strategyPassedInAtRuntime && interfaceMethodIsCalled);
    }
}
