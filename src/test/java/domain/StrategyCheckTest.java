package domain;

import domain.check.Check;
import domain.check.StrategyCheck;
import domain.data.ClassData;
import domain.data.MethodData;
import domain.data.MethodInstructionData;
import domain.data.VariableData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StrategyCheckTest {

    @Test
    public void testValidStrategyPattern() {
        List<VariableData> variableDataList = Arrays.asList(
                new VariableData("strategy", false, AccessModifiers.PRIVATE, "Strategy", false)
        );
        ClassData strategyClass = new ClassData("Strategy",null,null,true,false, false);
        List<MethodData> methodDataList = Arrays.asList(
                new MethodData("setStrategy",false,Arrays.asList("Strategy"),
                        new ArrayList<>(),AccessModifiers.PUBLIC,0,false, null),
                new MethodData("callStrategy",false,new ArrayList<>(),
                                        Arrays.asList(new MethodInstructionData(null,"Strategy")),
                        AccessModifiers.PUBLIC,1,false, null)
        );

        ClassData callerClass = new ClassData("GoodClass", methodDataList, variableDataList,
                false, false, false);

        Relation relation = new Relation("GoodClass","Strategy",RelationshipTypes.HAS);

        Check strategyCheck = new StrategyCheck(Arrays.asList(strategyClass,callerClass),Arrays.asList(relation));
        List<Violation> violations = strategyCheck.checker();
        assertEquals(1, violations.size());
        assertEquals("Violation: Strategy pattern dectected in class: GoodClass",violations.get(0).toString());
    }

}