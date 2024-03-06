package domain;

import domain.data.ClassData;
import domain.data.MethodData;
import domain.data.MethodInstructionData;
import domain.data.VariableData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InformationHidingTest {
    @Test
    public void testInformationHidingPublicVariable() {
        List<VariableData> publicFields = Arrays.asList(
                new VariableData("publicField", false, AccessModifiers.PUBLIC, "String", false)
        );

        ClassData classWithPublicField = new ClassData("ClassWithPublicField", null, publicFields,
                false, false, false);

        InformationHiding informationHidingChecker = new InformationHiding(Arrays.asList(classWithPublicField), null);
        List<Violation> violations = informationHidingChecker.checker();

        assertEquals(1, violations.size());
        assertEquals("Violation: Access modifier public variable - publicField in class: ClassWithPublicField", violations.get(0).toString());
    }

    @Test
    public void testInformationHidingPrivateGetterNotCalled() {
        // Create a class with a private variable and a getter
        List<VariableData> privateFields = Arrays.asList(
                new VariableData("privateField", false, AccessModifiers.PRIVATE, "String", false)
        );

        List<MethodInstructionData> emptyInstructions = new ArrayList<>();  // Provide an empty list of instructions

        List<MethodData> getters = Arrays.asList(
                new MethodData("getPrivateField", false, null, emptyInstructions,
                        AccessModifiers.PUBLIC, 0, false, null)
        );

        ClassData classWithPrivateFieldAndGetter = new ClassData("ClassWithPrivateFieldAndGetter", getters,
                privateFields, false, false, false);

        // Create the InformationHiding checker and check for violations
        InformationHiding informationHidingChecker = new InformationHiding(Arrays.asList(classWithPrivateFieldAndGetter),
                null);
        List<Violation> violations = informationHidingChecker.checker();

        //Bug while writing test cases, the algorithm does not detect this edge case.
        assertEquals(1, violations.size());
    }
}
