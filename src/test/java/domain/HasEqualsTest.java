package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import domain.check.styleChecks.HashEqualsCheck;
import domain.data.ClassData;
import domain.data.MethodData;
import domain.data.MethodInstructionData;
import org.junit.jupiter.api.Test;

public class HasEqualsTest {

	@Test
    public void testGoodOverride() {
        List<MethodData> methods = Arrays.asList(
        		new MethodData("equals", false, List.of("object"),
                        List.of(new MethodInstructionData("instruction1", "owner1")),
                        AccessModifiers.PUBLIC, 1, false, "boolean"),
        		new MethodData("hashCode", false, List.of(),
                        List.of(new MethodInstructionData("instruction1", "owner1")),
                        AccessModifiers.PUBLIC, 1, false, "int")
        );
        
        List<MethodData> methods2 = Arrays.asList(
        		
        );

        ClassData goodClass = new ClassData("GoodClass", methods, null, false, false, false);
        ClassData goodClass2 = new ClassData("GoodClass2", methods2, null, false, false, false);

        HashEqualsCheck hashEqualsCheck = new HashEqualsCheck(Arrays.asList(goodClass), null);
        List<Violation> violations = hashEqualsCheck.checker();
        
        HashEqualsCheck hashEqualsCheck2 = new HashEqualsCheck(Arrays.asList(goodClass2), null);
        List<Violation> violations2 = hashEqualsCheck2.checker();

        assertEquals(0, violations.size());
        assertEquals(0, violations2.size());
    }

    @Test
    public void testBadOverride() {
        List<MethodData> methods = Arrays.asList(
        		new MethodData("equals", false, List.of("object"),
                        List.of(new MethodInstructionData("instruction1", "owner2")),
                        AccessModifiers.PUBLIC, 1, false, "boolean")
        );
        
        List<MethodData> methods2 = Arrays.asList(
        		new MethodData("hashCode", false, List.of(),
                        List.of(new MethodInstructionData("instruction1", "owner3")),
                        AccessModifiers.PUBLIC, 1, false, "int")
        );

        ClassData badClass = new ClassData("BadClass", methods, null, false, false, false);
        ClassData badClass2 = new ClassData("BadClass2", methods2, null, false, false, false);

        HashEqualsCheck hashEqualsCheck = new HashEqualsCheck(Arrays.asList(badClass), null);
        List<Violation> violations = hashEqualsCheck.checker();
        
        HashEqualsCheck hashEqualsCheck2 = new HashEqualsCheck(Arrays.asList(badClass2), null);
        List<Violation> violations2 = hashEqualsCheck2.checker();

        assertEquals(1, violations.size());
        assertEquals("Violation: Overriding hashCode or equals methods should also override the other in class: BadClass", violations.get(0).toString());
        assertEquals(1, violations2.size());
        assertEquals("Violation: Overriding hashCode or equals methods should also override the other in class: BadClass", violations.get(0).toString());
    }

}
