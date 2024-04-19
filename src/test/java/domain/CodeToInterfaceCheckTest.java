package domain;


import domain.check.Check;
import domain.check.principleChecks.CodeToInterfaceCheck;
import domain.data.ClassData;
import domain.data.MethodData;
import domain.data.VariableData;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CodeToInterfaceCheckTest {

    @Test
    public void testFields(){
        ClassData class0 = new ClassData("Class0",null,null, false,false, false);
        ClassData class1 = new ClassData("Class1",null,null, true,false, false);
        
        VariableData var0 = new VariableData("myField0",false,AccessModifiers.PRIVATE,"Class0",false);
        VariableData var1 = new VariableData("myField1",false,AccessModifiers.PRIVATE,"Class1",false);

        ClassData class2 = new ClassData("Class2",null,List.of(var0,var1),false,false, false);

        Relation relation0 = new Relation("Class2","Class0",RelationshipTypes.HAS);
        Relation relation1 = new Relation("Class2","Class1",RelationshipTypes.HAS);

        Check codeToInterfaceCheck = new CodeToInterfaceCheck(List.of(class0,class1,class2),List.of(relation0,relation1));
        List<Violation> violations = codeToInterfaceCheck.checker();
        assertEquals(1,violations.size());
        assertEquals("Violation: Class2 HAS Class0 which is not an interface or abstract class in class: Class0",violations.get(0).toString());
    }

    @Test
    public void testDependency(){
        ClassData class0 = new ClassData("Class0",null,null, true,false, false);
        ClassData class1 = new ClassData("Class1",null,null, false,false, false);

        MethodData method0 = new MethodData("method0",false,null,null,
                AccessModifiers.PUBLIC,0,false, null);
        MethodData method1 = new MethodData("method1",false,null,null,
                AccessModifiers.PUBLIC,0,false, null);
        ClassData class2 = new ClassData("Class2",List.of(method0,method1),null,false,false, false);

        Relation relation0 = new Relation("Class2","Class0",RelationshipTypes.USES);
        Relation relation1 = new Relation("Class2","Class1",RelationshipTypes.USES);

        Check codeToInterfaceCheck = new CodeToInterfaceCheck(List.of(class0,class1,class2),List.of(relation0,relation1));
        List<Violation> violations = codeToInterfaceCheck.checker();
        assertEquals(1,violations.size());
        assertEquals("Violation: Class2 USES Class1 which is not an interface or abstract class in class: Class1",violations.get(0).toString());
    }

    @Test
    public void testExtend(){
        ClassData class0 = new ClassData("Class0",null,null, false,true, false);
        ClassData class1 = new ClassData("Class1",null,null, false,false, false);

        ClassData class2 = new ClassData("Class2",null,null,false,false, false);

        Relation relation0 = new Relation("Class2","Class0",RelationshipTypes.EXTENDS);
        Relation relation1 = new Relation("Class2","Class1",RelationshipTypes.EXTENDS);

        Check codeToInterfaceCheck = new CodeToInterfaceCheck(List.of(class0,class1,class2),List.of(relation0,relation1));
        List<Violation> violations = codeToInterfaceCheck.checker();
        assertEquals(1,violations.size());
        assertEquals("Violation: Class2 EXTENDS Class1 which is not an interface or abstract class in class: Class1",violations.get(0).toString());
    }


}
