package presentation;

import domain.*;
import domain.ClassLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LinterMain {
    private static List<ClassData> classes;

    private static List<Violation> violations;

    private static List<Relation> relations;

    public static void main(String[] args) throws IOException{
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("Please insert the directory to the package " +
                    "you would like to test (e.g. C:\\folder\\package)" + '\n' +
                    "Enter 'Test' to run on the domain layer of the linter ");

            String input = sc.nextLine();
            if(input.equals("Test")){
                input = ".\\src\\main\\java\\domain";
            }
            loadAllClasses(input);
        }
    }
    
    public static void loadAllClasses(String packagePath){
    	System.out.println("Loading files ...");
    	ASMLoader asmLoader = new ASMLoader(packagePath);
        if(!asmLoader.isValid()){
            System.out.println("No class files in given directory: " + packagePath);
        }else{
            System.out.println("Files loaded");

            ClassLoader asmAdaptor = new ASMAdaptor(asmLoader, new ArrayList<>());
            relations = asmAdaptor.getRelations();
            for(Relation relation: relations) {
                System.out.println(relation.toString());
            }
            classes = asmAdaptor.loadClasses();
            checkAll();
        }

    }


    public static void checkAll(){
        Check nameCheck = new NameCheck(classes, relations);
        Check infoHiding = new InformationHiding(classes, relations);
        Check hollywood = new HollywoodPrinciple(classes,relations);

        Check hashEq = new HashEqualsCheck(classes, relations);
        Check msgChain = new MessageChainCheck(classes, relations);
        Check observer = new ObserverCheck(classes, relations);

        Check meLen = new MethodLengthCheck(classes, relations);
        Check godClass = new GodClassCheck(classes, relations);
        Check facade = new FacadeCheck(classes, relations);

        Check codeToInter = new CodeToInterfaceCheck(classes, relations);
        Check staticImp = new StaticInstantiationCheck(classes, relations);
        Check strategy = new StrategyCheck(classes, relations);


        List<Violation> violations = nameCheck.checker();
        System.out.println("Performed name check for all classes, methods and variables. Violations found:");
        for(Violation violation : violations) {
            System.out.println("    " + violation.toString());
        }

        violations = infoHiding.checker();
        System.out.println("Performed Information Hiding for all classes, methods and variables. Violations found:");
        for(Violation violation : violations) {
            System.out.println("    " + violation.toString());
        }

        violations = hollywood.checker();
        System.out.println("Performed Hollywood principle check for all classes, methods and variables. Violations found:");
        for(Violation violation : violations) {
            System.out.println("    " + violation.toString());
        }
        
    	violations = hashEq.checker();
        System.out.println("Performed hashEquals check for all classes. Violations found:");
        for(Violation violation : violations) {
            System.out.println("    " + violation.toString());
        }
        
    	violations = msgChain.checker();
        System.out.println("Performed message chain check for all classes. Violations found:");
        for(Violation violation : violations) {
            System.out.println("    " + violation.toString());
        }
        
    	violations = observer.checker();
        System.out.println("Performed observer detect for all classes. Patterns found:");
        for(Violation violation : violations) {
            System.out.println("    " + violation.toString());
        }

        violations = meLen.checker();
        System.out.println("Performed method length check for all methods. Violations found:");
        for(Violation violation : violations) {
            System.out.println("    " + violation.toString());
        }
        violations = godClass.checker();
        System.out.println("Performed god class check for all classes. Violations found:");
        for(Violation violation : violations) {
            System.out.println("    " + violation.toString());
        }
        violations = facade.checker();
        System.out.println("Performed facade detect for all classes. Patterns found:");
        for(Violation violation : violations) {
            System.out.println("    " + violation.toString());
        }

        violations = codeToInter.checker();
        System.out.println("Performed code to interface principle check for all classes. Violations found:");
        for(Violation violation : violations) {
            System.out.println("    " + violation.toString());
        }
        violations = staticImp.checker();
        System.out.println("Performed static implementation check for all classes and methods. Violations found:");
        for(Violation violation : violations) {
            System.out.println("    " + violation.toString());
        }
        violations = strategy.checker();
        System.out.println("Performed strategy detect for all classes. Patterns found:");
        for(Violation violation : violations) {
            System.out.println("    " + violation.toString());
        }
    }
}
