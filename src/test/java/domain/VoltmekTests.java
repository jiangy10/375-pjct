package domain;

import domain.check.Check;
import domain.check.FacadeCheck;
import domain.check.GodClassCheck;
import domain.check.MethodLengthCheck;
import domain.data.ClassData;
import domain.data.MethodData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class VoltmekTests {
    
    @Test
    void runStyleChecks(){
        /*
        These tests are extremely simple because of how simple the algorithm is. All the check really needs is the
        method's instruction list size, and to some extent the names of the methods and class.
        Anything else could be null and not affect the test
         */

        List<MethodData> methods = new ArrayList<>();
        List<ClassData> classes = new ArrayList<>();

        //Test 1: one class with one short method
        methods.add(new MethodData("Method1", false, null, null,
                null, 100, false, null));
        classes.add(new ClassData("Class1", methods, null, false, false, false));
        Check methodLengthCheck = new MethodLengthCheck(classes, null);

        assert(methodLengthCheck.checker().isEmpty());


        //Test 2: one class with one long method
        methods.clear();
        classes.clear();
        methods.add(new MethodData("Method1", false, null, null,
                null, 500, false, null));
        classes.add(new ClassData("Class1", methods, null, false, false, false));
        methodLengthCheck = new MethodLengthCheck(classes, null);

        assert(!methodLengthCheck.checker().isEmpty());

        //Test 3: two classes with one long method each
        methods.clear();
        classes.clear();
        methods.add(new MethodData("Method1", false, null, null,
                null, 500, false, null));
        classes.add(new ClassData("Class1", methods, null, false, false, false));
        classes.add(new ClassData("Class2", methods, null, false, false, false));
        methodLengthCheck = new MethodLengthCheck(classes, null);

        assert(methodLengthCheck.checker().size() == 2);

        //Test 4: two classes with no methods
        methods.clear();
        classes.clear();
        classes.add(new ClassData("Class1", methods, null, false, false, false));
        classes.add(new ClassData("Class2", methods, null, false, false, false));
        methodLengthCheck = new MethodLengthCheck(classes, null);

        assert(methodLengthCheck.checker().isEmpty());

        //Test 5: one class with three long methods
        methods.clear();
        classes.clear();
        methods.add(new MethodData("Method1", false, null, null,
                null, 500, false, null));
        methods.add(new MethodData("Method2", false, null, null,
                null, 200, false, null));
        methods.add(new MethodData("Method3", false, null, null,
                null, 400, false, null));
        classes.add(new ClassData("Class1", methods, null, false, false, false));
        methodLengthCheck = new MethodLengthCheck(classes, null);

        assert(methodLengthCheck.checker().size() == 2);

    }

    @Test
    void runPrincipleChecks(){

        List<MethodData> methods = new ArrayList<>();
        List<ClassData> classes = new ArrayList<>();
        List<Relation> relations = new ArrayList<>();

        //Test 1: one class with no relations
        classes.add(new ClassData("Class1", methods, null, false, false, false));
        Check godClassCheck = new GodClassCheck(classes, relations);

        assert(godClassCheck.checker().isEmpty());

        //Test 2: one class with five HAS-A relations
        classes.clear();
        classes.add(new ClassData("Class1", methods, null, false, false, false));
        relations.add(new Relation("Class1", "Class2", RelationshipTypes.HAS));
        relations.add(new Relation("Class1", "Class3", RelationshipTypes.HAS));
        relations.add(new Relation("Class1", "Class4", RelationshipTypes.HAS));
        relations.add(new Relation("Class1", "Class5", RelationshipTypes.HAS));
        relations.add(new Relation("Class1", "Class6", RelationshipTypes.HAS));
        godClassCheck = new GodClassCheck(classes, relations);

        assert(!godClassCheck.checker().isEmpty());

        //Test 3: one class with multiple mixed relations
        classes.clear();
        classes.add(new ClassData("Class1", methods, null, false, false, false));
        relations.add(new Relation("Class1", "Class2", RelationshipTypes.HAS));
        relations.add(new Relation("Class1", "Class3", RelationshipTypes.USES));
        relations.add(new Relation("Class1", "Class4", RelationshipTypes.USES));
        relations.add(new Relation("Class1", "Class5", RelationshipTypes.HAS));
        relations.add(new Relation("Class1", "Class6", RelationshipTypes.USES));
        relations.add(new Relation("Class1", "Class7", RelationshipTypes.HAS));

        godClassCheck = new GodClassCheck(classes, relations);

        assert(!godClassCheck.checker().isEmpty());

    }

    @Test
    void runPatternChecks(){
        List<MethodData> methods = new ArrayList<>();
        List<ClassData> classes = new ArrayList<>();
        List<Relation> relations = new ArrayList<>();

        //Test 1: main with multiple sub-system dependencies
        //(not a facade because the point of facades is to shield the client (main) from these complex subsystems)
        methods.add(new MethodData("main", false, null, null,
                AccessModifiers.PUBLIC, 0, true, null));
        classes.add(new ClassData("Class1", methods, null, false, false, false));
        relations.add(new Relation("Class1", "Class2", RelationshipTypes.USES));
        relations.add(new Relation("Class1", "Class3", RelationshipTypes.USES));
        relations.add(new Relation("Class1", "Class4", RelationshipTypes.USES));
        relations.add(new Relation("Class1", "Class5", RelationshipTypes.USES));
        relations.add(new Relation("Class1", "Class6", RelationshipTypes.USES));
        relations.add(new Relation("Class1", "Class7", RelationshipTypes.USES));
        Check facadeCheck = new FacadeCheck(classes, relations);

        assert(facadeCheck.checker().isEmpty());

        //Test 2: not main with multiple sub-system dependencies
        methods.clear();
        classes.clear();
        relations.clear();
        classes.add(new ClassData("Class1", methods, null, false, false, false));
        relations.add(new Relation("Class1", "Class2", RelationshipTypes.USES));
        relations.add(new Relation("Class1", "Class3", RelationshipTypes.USES));
        relations.add(new Relation("Class1", "Class4", RelationshipTypes.USES));
        relations.add(new Relation("Class1", "Class5", RelationshipTypes.USES));
        relations.add(new Relation("Class1", "Class6", RelationshipTypes.USES));
        relations.add(new Relation("Class1", "Class7", RelationshipTypes.USES));
        facadeCheck = new FacadeCheck(classes, relations);

        assert(!facadeCheck.checker().isEmpty());
    }
}
