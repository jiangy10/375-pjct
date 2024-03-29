package domain;

import static org.easymock.EasyMock.*;

import domain.data.*;
import org.easymock.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ASMAdaptorTest {

    private ASMLoader mockLoader;
    private ASMAdaptor adaptor;

    @BeforeEach
    public void setUp() {
        mockLoader = createMock(ASMLoader.class);
        adaptor = new ASMAdaptor(mockLoader, new ArrayList<>());
    }

    @Test
    public void whenNoClassNodes_thenNoClassDatasAreCreated() throws IOException {
        expect(mockLoader.loadClassNodes()).andReturn(new ArrayList<>());
        replay(mockLoader);

        List<ClassData> classDatas = adaptor.loadClasses();

        assertTrue(classDatas.isEmpty(), "Expected no ClassData objects when no class nodes are present");
        verify(mockLoader);
    }

    @Test
    public void testLoadClassesWithOneClassNode() throws IOException {
        List<ClassNode> nodes = new ArrayList<>();
        ClassNode node = new ClassNode();
        node.name = "com/example/TestClass";
        node.superName = "java/lang/Object"; // Assume it extends Object
        // Simulate minimal content for one class node
        node.methods = new ArrayList<>();
        node.fields = new ArrayList<>();
        nodes.add(node);

        expect(mockLoader.loadClassNodes()).andReturn(nodes);
        replay(mockLoader);

        List<ClassData> classDatas = adaptor.loadClasses();

        assertEquals(1, classDatas.size(), "Expected exactly one ClassData object");
        assertEquals("TestClass", classDatas.get(0).getName(), "Class name did not match");
        verify(mockLoader);
    }


    @Test
    public void testLoadClassesWithManyClassNodes() throws IOException {
        final int classNodeCount = 100;
        List<ClassNode> nodes = new ArrayList<>();
        for (int i = 0; i < classNodeCount; i++) {
            ClassNode node = new ClassNode();
            node.name = "com/example/TestClass" + i;
            node.superName = "java/lang/Object"; // Assume each extends Object
            // Simulate minimal content for class nodes
            node.methods = new ArrayList<>();
            node.fields = new ArrayList<>();
            nodes.add(node);
        }

        expect(mockLoader.loadClassNodes()).andReturn(nodes);
        replay(mockLoader);

        List<ClassData> classDatas = adaptor.loadClasses();

        assertEquals(classNodeCount, classDatas.size(), "Expected the same number of ClassData objects as class nodes");
        for (int i = 0; i < classNodeCount; i++) {
            assertEquals("TestClass" + i, classDatas.get(i).getName(), "ClassData name did not match expected for class node " + i);
        }
        verify(mockLoader);
    }


    @Test
    public void testLoadClassesWithNoMethods() throws IOException {
        List<ClassNode> nodes = new ArrayList<>();
        ClassNode node = new ClassNode();
        node.name = "com/example/NoMethodClass";
        node.superName = "java/lang/Object"; // Ensure superName is set
        // No methods added intentionally
        nodes.add(node);

        expect(mockLoader.loadClassNodes()).andReturn(nodes);
        replay(mockLoader);

        List<ClassData> classDatas = adaptor.loadClasses();

        assertEquals(1, classDatas.size());
        ClassData classData = classDatas.get(0);
        assertTrue(classData.getMethods().isEmpty(), "Expected no methods in the ClassData");

        verify(mockLoader);
    }


    @Test
    public void testLoadClassesWithOneMethod() throws IOException {
        List<ClassNode> nodes = new ArrayList<>();
        ClassNode node = new ClassNode();
        node.name = "com/example/OneMethodClass";
        node.superName = "java/lang/Object"; // Ensure superName is set to avoid NPE
        node.methods = new ArrayList<>();

        MethodNode methodNode = new MethodNode();
        methodNode.name = "testMethod";
        methodNode.desc = "()V"; // Void method with no parameters
        node.methods.add(methodNode);

        nodes.add(node);

        expect(mockLoader.loadClassNodes()).andReturn(nodes);
        replay(mockLoader);

        List<ClassData> classDatas = adaptor.loadClasses();

        assertEquals(1, classDatas.size());
        assertEquals(1, classDatas.get(0).getMethods().size(), "Expected exactly one method in ClassData");

        verify(mockLoader);
    }


    @Test
    public void testLoadClassesWithManyMethods() throws IOException {
        final int methodCount = 100; // Define "many" as 100 for this test
        List<ClassNode> nodes = new ArrayList<>();
        ClassNode node = new ClassNode();
        node.name = "com/example/ManyMethodsClass";
        node.superName = "java/lang/Object"; // Ensure superName is set
        node.methods = new ArrayList<>();

        for (int i = 0; i < methodCount; i++) {
            MethodNode methodNode = new MethodNode();
            methodNode.name = "testMethod" + i;
            methodNode.desc = "()V"; // All methods are void with no parameters
            node.methods.add(methodNode);
        }

        nodes.add(node);

        expect(mockLoader.loadClassNodes()).andReturn(nodes);
        replay(mockLoader);

        List<ClassData> classDatas = adaptor.loadClasses();

        assertEquals(1, classDatas.size());
        assertEquals(methodCount, classDatas.get(0).getMethods().size(), "Expected many methods in ClassData");

        verify(mockLoader);
    }

    @Test
    public void testLoadClassesWithNoFields() throws IOException {
        List<ClassNode> nodes = new ArrayList<>();
        ClassNode node = new ClassNode();
        node.name = "com/example/ClassWithoutFields";
        node.superName = "java/lang/Object"; // Assume extends Object implicitly
        node.fields = new ArrayList<>(); // No fields
        nodes.add(node);

        expect(mockLoader.loadClassNodes()).andReturn(nodes);
        replay(mockLoader);

        List<ClassData> classDatas = adaptor.loadClasses();

        assertEquals(1, classDatas.size(), "Expected exactly one ClassData object");
        assertTrue(classDatas.get(0).getFields().isEmpty(), "Expected no fields in the ClassData");
        verify(mockLoader);
    }

    @Test
    public void testLoadClassesWithOneField() throws IOException {
        List<ClassNode> nodes = new ArrayList<>();
        ClassNode node = new ClassNode();
        node.name = "com/example/ClassWithOneField";
        node.superName = "java/lang/Object";
        FieldNode fieldNode = new FieldNode(Opcodes.ACC_PRIVATE, "myField", "Ljava/lang/String;", null, null);
        node.fields = new ArrayList<>(List.of(fieldNode));
        nodes.add(node);

        expect(mockLoader.loadClassNodes()).andReturn(nodes);
        replay(mockLoader);

        List<ClassData> classDatas = adaptor.loadClasses();

        assertEquals(1, classDatas.size());
        assertEquals(1, classDatas.get(0).getFields().size(), "Expected exactly one field in ClassData");
        verify(mockLoader);
    }

    @Test
    public void testLoadClassesWithManyFields() throws IOException {
        final int fieldCount = 100;
        List<ClassNode> nodes = new ArrayList<>();
        ClassNode node = new ClassNode();
        node.name = "com/example/ClassWithManyFields";
        node.superName = "java/lang/Object";
        node.fields = new ArrayList<>();
        for (int i = 0; i < fieldCount; i++) {
            FieldNode fieldNode = new FieldNode(Opcodes.ACC_PRIVATE, "field" + i, "Ljava/lang/String;", null, null);
            node.fields.add(fieldNode);
        }
        nodes.add(node);

        expect(mockLoader.loadClassNodes()).andReturn(nodes);
        replay(mockLoader);

        List<ClassData> classDatas = adaptor.loadClasses();

        assertEquals(1, classDatas.size());
        assertEquals(fieldCount, classDatas.get(0).getFields().size(), "Expected many fields in ClassData");
        verify(mockLoader);
    }

    @Test
    public void testLoadClassesWithMethodNoInstructions() throws IOException {
        List<ClassNode> nodes = new ArrayList<>();
        ClassNode classNode = new ClassNode();
        classNode.name = "com/example/ClassWithEmptyMethod";
        classNode.superName = "java/lang/Object"; // Assume extends Object implicitly
        MethodNode methodNode = new MethodNode(Opcodes.ACC_PUBLIC, "emptyMethod", "()V", null, null);
        // No instructions added to methodNode
        classNode.methods = new ArrayList<>(List.of(methodNode));
        nodes.add(classNode);

        expect(mockLoader.loadClassNodes()).andReturn(nodes);
        replay(mockLoader);

        List<ClassData> classDatas = adaptor.loadClasses();

        assertEquals(1, classDatas.size(), "Expected exactly one ClassData object");
        assertEquals(1, classDatas.get(0).getMethods().size(), "Expected one method in ClassData");
        assertTrue(classDatas.get(0).getMethods().get(0).getInstructions().isEmpty(), "Expected no instructions in the method");
        verify(mockLoader);
    }

//    @Test
//    public void testLoadClassesWithMethodOneInstruction() throws IOException {
//        List<ClassNode> nodes = new ArrayList<>();
//        ClassNode classNode = new ClassNode();
//        classNode.name = "com/example/ClassWithOneInstructionMethod";
//        classNode.superName = "java/lang/Object";
//        MethodNode methodNode = new MethodNode(Opcodes.ACC_PUBLIC, "singleInstructionMethod", "()V", null, null);
//        InsnList instructions = new InsnList();
//        instructions.add(new InsnNode(Opcodes.RETURN)); // Add single RETURN instruction
//        methodNode.instructions = instructions;
//        classNode.methods = new ArrayList<>(List.of(methodNode));
//        nodes.add(classNode);
//
//        expect(mockLoader.loadClassNodes()).andReturn(nodes);
//        replay(mockLoader);
//
//        List<ClassData> classDatas = adaptor.loadClasses();
//
//        assertEquals(1, classDatas.size());
//        assertEquals(1, classDatas.get(0).getMethods().size(), "Expected one method in ClassData");
//        assertEquals(1, classDatas.get(0).getMethods().get(0).getInstructions().size(), "Expected one instruction in the method");
//        verify(mockLoader);
//    }

//    @Test
//    public void testLoadClassesWithMethodManyInstructions() throws IOException {
//        final int instructionCount = 100; // Define "many" as 100 for this test
//        List<ClassNode> nodes = new ArrayList<>();
//        ClassNode classNode = new ClassNode();
//        classNode.name = "com/example/ClassWithManyInstructionsMethod";
//        classNode.superName = "java/lang/Object";
//        MethodNode methodNode = new MethodNode(Opcodes.ACC_PUBLIC, "manyInstructionsMethod", "()V", null, null);
//        InsnList instructions = new InsnList();
//        for (int i = 0; i < instructionCount; i++) {
//            instructions.add(new VarInsnNode(Opcodes.ALOAD, 0)); // Add ALOAD instruction as an example
//        }
//        instructions.add(new InsnNode(Opcodes.RETURN)); // Add a RETURN at the end
//        methodNode.instructions = instructions;
//        classNode.methods = new ArrayList<>(List.of(methodNode));
//        nodes.add(classNode);
//
//        expect(mockLoader.loadClassNodes()).andReturn(nodes);
//        replay(mockLoader);
//
//        List<ClassData> classDatas = adaptor.loadClasses();
//
//        assertEquals(1, classDatas.size());
//        assertEquals(1, classDatas.get(0).getMethods().size(), "Expected one method in ClassData");
//        assertEquals(instructionCount + 1, classDatas.get(0).getMethods().get(0).getInstructions().size(), "Expected many instructions in the method");
//        verify(mockLoader);
//    }


}
