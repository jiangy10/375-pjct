package domain.check;

import domain.Relation;
import domain.RelationshipTypes;
import domain.Violation;
import domain.check.Check;
import domain.data.ClassData;
import domain.data.MethodData;

import java.util.ArrayList;
import java.util.List;

public class HollywoodPrinciple extends Check {

    public HollywoodPrinciple(List<ClassData> classes, List<Relation> relations) {
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

        // Check for violations of the Hollywood Principle
        for (Relation relation : relations) {
            ClassData firstClass = getClassByClassName(relation.getFirstClass());
            ClassData secondClass = getClassByClassName(relation.getSecondClass());
            RelationshipTypes relationship = relation.getRelationship();
            if(firstClass == null || secondClass == null || relationship == null){
                return violations;
            }

            if (relationship == RelationshipTypes.USES) {
                // If there is a USES relationship, ensure that the second class calls methods in the first class
                if (!isCalledBySecondClass(firstClass, secondClass)) {
                    violations.add(new Violation("Hollywood Principle Violation: " + secondClass.getName()
                            + " does not call methods in " + firstClass.getName(), "HollywoodCheck"));
                }
            }
        }

        return violations;
    }

    private boolean isCalledBySecondClass(ClassData firstClass, ClassData secondClass) {
        // Check if any method in the first class is called by the second class
        for (MethodData method : firstClass.getMethods()) {
            if (!isMethodCalledBySecondClass(method, secondClass)) {
                return false;
            }
        }

        return true;
    }

    private boolean isMethodCalledBySecondClass(MethodData method, ClassData secondClass) {
        // Get the method name and parameter types
        String methodName = method.getMethodName();
        List<String> parameterTypes = method.getParameters();

        // Get the methods of the second class
        List<MethodData> secondClassMethods = secondClass.getMethods();

        for (MethodData secondClassMethod : secondClassMethods) {
            if (isSameMethod(method, secondClassMethod)) {
                return true;
            }
        }

        return false;
    }

    private boolean isSameMethod(MethodData method1, MethodData method2) {
        return method1.getParameters().equals(method2.getParameters())
                && method1.getMethodName().equals(method2.getMethodName());
    }


}
