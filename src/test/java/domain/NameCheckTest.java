package domain;

import domain.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NameCheckTest {

    @Test
    public void testGoodClassAndVariableNames() {
        // Create a class with a good name and variables with good names
        List<VariableData> fields = Arrays.asList(
                new VariableData("goodVar1", false, AccessModifiers.PRIVATE, "String", false),
                new VariableData("goodVar2", false, AccessModifiers.PUBLIC, "int", false)
        );

        ClassData goodClass = new ClassData("GoodClass", null, fields, false, false, false);

        // Create the NameCheck checker and check for violations
        NameCheck nameCheck = new NameCheck(Arrays.asList(goodClass), null);
        List<Violation> violations = nameCheck.checker();

        // Assert that no violations are detected
        assertEquals(0, violations.size());
    }

    @Test
    public void testBadClassName() {
        // Create a class with a bad name
        ClassData badClass = new ClassData("bad_class", null, null, false, false, false);

        // Create the NameCheck checker and check for violations
        NameCheck nameCheck = new NameCheck(Arrays.asList(badClass), null);
        List<Violation> violations = nameCheck.checker();

        // Assert that a violation for bad class name is detected
        assertEquals(1, violations.size());
        assertEquals("Violation: Bad Class Name - bad_class in class: bad_class", violations.get(0).toString());
    }

    @Test
    public void testBadVariableName() {
        // Create a class with a variable that has a bad name
        List<VariableData> fields = Arrays.asList(
                new VariableData("badVar!1", false, AccessModifiers.PRIVATE, "String", false)
        );

        ClassData badVarClass = new ClassData("BadVarClass", null, fields, false, false, false);

        // Create the NameCheck checker and check for violations
        NameCheck nameCheck = new NameCheck(Arrays.asList(badVarClass), null);
        List<Violation> violations = nameCheck.checker();

        // Assert that a violation for bad variable name is detected
        assertEquals(1, violations.size());
        assertEquals("Violation: Bad Variable Name - badVar!1 in class: BadVarClass", violations.get(0).toString());
    }

    @Test
    public void testBadClassNameStartingWithNumber() {
        ClassData badClass = new ClassData("123Name", null, null, false, false, false);

        NameCheck nameCheck = new NameCheck(Arrays.asList(badClass), null);
        List<Violation> violations = nameCheck.checker();

        assertEquals(1, violations.size());
        assertEquals("Violation: Bad Class Name - 123Name in class: 123Name", violations.get(0).toString());

    }
}
