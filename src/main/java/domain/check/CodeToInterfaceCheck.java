package domain.check;

import domain.Relation;
import domain.Violation;
import domain.data.ClassData;

import java.util.*;

public class CodeToInterfaceCheck extends Check {
    private Set<String> classNames;
    private Hashtable<String, ClassData> classTable;

    private final String DESC = " which is not an interface or abstract class";
    public CodeToInterfaceCheck(List<ClassData> classes, List<Relation> relations) {
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
        List<Violation> violations = new ArrayList<Violation>();
        for(Relation relation: relations){
            String secondClassName = relation.getSecondClass();
            if(classNames.contains(secondClassName)){
                ClassData secondClass = classTable.get(secondClassName);
                if((!secondClass.isInterface())&&(!secondClass.isAbstract())){
                    violations.add(
                            new Violation(
                                    relation.getFirstClass()+" "
                                            +relation.getRelationship().toString()+" "+secondClassName +DESC
                                    ,secondClassName));
                }
            }
        }
        return violations;
    }
}
