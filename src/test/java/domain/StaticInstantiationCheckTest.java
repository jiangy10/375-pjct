package domain;

import domain.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class StaticInstantiationCheckTest {

    @Test
    public void testStaticInstantiation(){

        List<VariableData> variableData1 = List.of(
                new VariableData("var1",false,AccessModifiers.PRIVATE,"Craze",true),
                new VariableData("var2",false,AccessModifiers.PRIVATE,"Crome",true)
        );

        List<VariableData> variableData2 = List.of(
                new VariableData("var1",false,AccessModifiers.PRIVATE,"Craze",false),
                new VariableData("var2",false,AccessModifiers.PRIVATE,"Crome",false)
        );

        List<MethodData> methodData1 = List.of(
                new MethodData("m1",false,new ArrayList<>(),
                        new ArrayList<>(),AccessModifiers.PUBLIC,0,true, null),
                new MethodData("m2",false,new ArrayList<>(),
                        new ArrayList<>(),AccessModifiers.PUBLIC,0,true, null)
        );

        List<MethodInstructionData> insn = List.of(new MethodInstructionData("<init>","Class1"));

        List<MethodData> methodData2 = List.of(
                new MethodData("m1",false,null,insn,AccessModifiers.PUBLIC,
                        0,true, null),
                new MethodData("m2",false,null,new ArrayList<>(),AccessModifiers.PUBLIC,
                        0,true, null)
        );


        ClassData class0 = new ClassData("Class0",new ArrayList<>(),new ArrayList<>(), false,true, false);
        ClassData class1 = new ClassData("Class1",methodData1,variableData1, false,false, false);
        ClassData class2 = new ClassData("Class2",methodData2,variableData2,false,false, false);




        Check staticInstantiationCheck = new StaticInstantiationCheck(List.of(class0,class1,class2),new ArrayList<>());
        List<Violation> violations = staticInstantiationCheck.checker();
        assertEquals(1,violations.size());
        assertEquals("Violation: Class1: Class with static fields and methods is instantiated in: Class2 in class: Class1",violations.get(0).toString());
    }

    @Test
    public void testNonStaticInstantiation(){
        List<VariableData> variableData1 = List.of(
                new VariableData("var1",false,AccessModifiers.PRIVATE,"Craze",true),
                new VariableData("var2",false,AccessModifiers.PRIVATE,"Crome",false)
        );

        List<VariableData> variableData2 = List.of(
                new VariableData("var1",false,AccessModifiers.PRIVATE,"Craze",true),
                new VariableData("var2",false,AccessModifiers.PRIVATE,"Crome",true)
        );

        List<MethodData> methodData1 = List.of(
                new MethodData("m1",false,new ArrayList<>(),new ArrayList<>(),AccessModifiers.PUBLIC,
                        0,true, null),
                new MethodData("m2",false,new ArrayList<>(),new ArrayList<>(),AccessModifiers.PUBLIC,
                        0,true, null)
        );

        List<MethodInstructionData> insn = List.of(new MethodInstructionData("<clinit>","Class1"));

        List<MethodData> methodData2 = List.of(
                new MethodData("m1",false,null,insn,AccessModifiers.PUBLIC,
                        0,true, null),
                new MethodData("m2",false,null,new ArrayList<>(),AccessModifiers.PUBLIC,
                        0,true, null)
        );


        ClassData class0 = new ClassData("Class0",methodData2,variableData2, false,true, false);
        ClassData class1 = new ClassData("Class1",methodData1,variableData1, false,false, false);
        ClassData class2 = new ClassData("Class2",methodData2,variableData2,false,false, false);




        Check staticInstantiationCheck = new StaticInstantiationCheck(List.of(class0,class1,class2),new ArrayList<>());
        List<Violation> violations = staticInstantiationCheck.checker();
        assertEquals(0,violations.size());

    }
}
