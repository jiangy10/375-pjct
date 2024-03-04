package domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HollywoodTest {
    @Test
    public void testHollywoodPrincipleViolation_Catches() {
        List<MethodData> methodsInClassA = List.of(
                new MethodData("methodInA", false, List.of("int"),
                        List.of(new MethodInstructionData("instruction1", "owner1")),
                        AccessModifiers.PUBLIC, 1, false, null)
        );

        List<MethodData> methodsInClassB = List.of(
                new MethodData("methodInB", false, List.of("String"),
                        List.of(new MethodInstructionData("instruction2", "owner2")),
                        AccessModifiers.PUBLIC, 1, false, null)
        );

        List<VariableData> fieldsInClassA = List.of(
                new VariableData("fieldInA", false, AccessModifiers.PRIVATE, "int", false)
        );

        ClassData classA = new ClassData("ClassA", methodsInClassA, fieldsInClassA, false, false, false);

        List<VariableData> fieldsInClassB = List.of(
                new VariableData("fieldInB", false, AccessModifiers.PUBLIC, "String", false)
        );

        ClassData classB = new ClassData("ClassB", methodsInClassB, fieldsInClassB, false, false, false);

        Relation relation = new Relation(classB.getName(), classA.getName(), RelationshipTypes.USES);

        HollywoodPrinciple hollywoodPrincipleChecker = new HollywoodPrinciple(Arrays.asList(classA, classB), Arrays.asList(relation));
        List<Violation> violations = hollywoodPrincipleChecker.checker();

        assertEquals(1, violations.size());
        assertEquals("Violation: Hollywood Principle Violation: ClassA does not call methods in ClassB in class: HollywoodCheck", violations.get(0).toString());
    }

    @Test
    public void testHollywoodPrincipleViolation_AvoidedGetter() {
        List<MethodData> methodsInClassA = List.of(
                new MethodData("methodInA", false, List.of("int"),
                        List.of(new MethodInstructionData("instruction1", "owner1")),
                        AccessModifiers.PUBLIC, 1, false, null)
        );

        List<VariableData> fieldsInClassA = List.of(
                new VariableData("fieldInA", false, AccessModifiers.PRIVATE, "int", false)
        );

        ClassData classA = new ClassData("ClassA", methodsInClassA, fieldsInClassA, false, false, false);

        List<MethodData> methodsInClassB = List.of(
                new MethodData("differentMethodName", false, List.of("String"),
                        List.of(new MethodInstructionData("instruction2", "owner2")),
                        AccessModifiers.PUBLIC, 1, false, null)
        );

        List<VariableData> fieldsInClassB = List.of(
                new VariableData("fieldInB", false, AccessModifiers.PUBLIC, "String", false)
        );

        ClassData classB = new ClassData("ClassB", methodsInClassB, fieldsInClassB, false, false, false);
        Relation relation = new Relation(classB.getName(), classA.getName(), RelationshipTypes.USES);

        HollywoodPrinciple hollywoodPrincipleChecker = new HollywoodPrinciple(Arrays.asList(classA, classB), Arrays.asList(relation));
        List<Violation> violations = hollywoodPrincipleChecker.checker();
        assertEquals(1, violations.size());
    }
}
