package presentation;

import domain.*;
import domain.ClassLoader;
import domain.check.*;
import domain.check.patternChecks.*;
import domain.check.principleChecks.*;
import domain.check.styleChecks.*;
import domain.data.ClassData;

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
        String codeSource = "";
        String configPath = "";
        while(true){
            System.out.println("Please insert the directory to the package " +
                    "you would like to test (e.g. C:\\folder\\package)" + '\n' +
                    "Enter 'Test' to run on the domain layer of the linter ");

            codeSource = sc.nextLine();
            if(codeSource.equals("Test")){
                codeSource = ".\\src\\main\\java\\domain";
            }

            System.out.println("Please enter the configuration file path \n " +
                    "(If no configuration needed, don't need to enter anything; " +
                    "\nor enter 'TestConfig' to run with configuration of the linter)");
            configPath = sc.nextLine();

            System.out.println("\nRunning checks on code source: " + codeSource +
                    "\n with configuration: " + configPath + "\n");
            if(configPath.equals("TestConfig")) {
                loadAllClasses(codeSource);
                checkWithConfig(".\\config.properties");
            }
            else if (!configPath.isEmpty()){
                loadAllClasses(codeSource);
                checkWithConfig(configPath);
            }
            else{
                loadAllClasses(codeSource);
                checkAll();
            }
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


        violations = nameCheck.checker();
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

    public static void checkWithConfig(String configFilePath) {
        ConfigManager configManager = new ConfigManager(configFilePath);

        // Checking all style checks
        System.out.println("Running all style chesks...");

        if(configManager.isCheckEnabled("styleCheck", "NameCheck")) {
            Check nameCheck = new NameCheck(classes, relations);
            violations = nameCheck.checker();
            System.out.println("Performed name check for all classes, methods and variables. Violations found:");
            for (Violation violation : violations) {
                System.out.println("    " + violation.toString());
            }
        }
        if(configManager.isCheckEnabled("styleCheck", "HashEqualsCheck")) {
            Check hashEq = new HashEqualsCheck(classes, relations);
            violations = hashEq.checker();
            System.out.println("Performed hashEquals check for all classes. Violations found:");
            for(Violation violation : violations) {
                System.out.println("    " + violation.toString());
            }
        }
        if(configManager.isCheckEnabled("styleCheck", "MethodLengthCheck")) {
            Check meLen = new MethodLengthCheck(classes, relations);
            violations = meLen.checker();
            System.out.println("Performed method length check for all methods. Violations found:");
            for(Violation violation : violations) {
                System.out.println("    " + violation.toString());
            }
        }
        if(configManager.isCheckEnabled("styleCheck", "StaticInstantiationCheck")) {
            Check staticImp = new StaticInstantiationCheck(classes, relations);
            violations = staticImp.checker();
            System.out.println("Performed static instantiation check for all classes and methods. Violations found:");
            for(Violation violation : violations) {
                System.out.println("    " + violation.toString());
            }
        }

        //now all principal checks
        System.out.println("Running all principal chesks...");

        if(configManager.isCheckEnabled("principalCheck", "HollywoodPrinciple")) {
            Check hollywood = new HollywoodPrinciple(classes, relations);
            violations = hollywood.checker();
            System.out.println("Performed Hollywood principle check for all classes, methods and variables. Violations found:");
            for(Violation violation : violations) {
                System.out.println("    " + violation.toString());
            }
        }
        if(configManager.isCheckEnabled("principalCheck", "InformationHiding")) {
            Check infoHiding = new InformationHiding(classes, relations);
            violations = infoHiding.checker();
            System.out.println("Performed Information Hiding check for all classes, methods and variables. Violations found:");
            for(Violation violation : violations) {
                System.out.println("    " + violation.toString());
            }
        }
        if(configManager.isCheckEnabled("principalCheck", "MessageChainCheck")) {
            Check msgChain = new MessageChainCheck(classes, relations);
            violations = msgChain.checker();
            System.out.println("Performed message chain check for all classes. Violations found:");
            for(Violation violation : violations) {
                System.out.println("    " + violation.toString());
            }
        }
        if(configManager.isCheckEnabled("principalCheck", "GodClassCheck")) {
            Check godClass = new GodClassCheck(classes, relations);
            violations = godClass.checker();
            System.out.println("Performed god class check for all classes. Violations found:");
            for(Violation violation : violations) {
                System.out.println("    " + violation.toString());
            }
        }
        if(configManager.isCheckEnabled("principalCheck", "CodeToInterfaceCheck")) {
            Check codeToInter = new CodeToInterfaceCheck(classes, relations);
            violations = codeToInter.checker();
            System.out.println("Performed code to interface principle check for all classes. Violations found:");
            for(Violation violation : violations) {
                System.out.println("    " + violation.toString());
            }
        }

        //now all pattern checks
        System.out.println("Running all pattern chesks...");

        if(configManager.isCheckEnabled("patternCheck", "ObserverCheck")) {
            Check observer = new ObserverCheck(classes, relations);
            violations = observer.checker();
            System.out.println("Performed observer detect for all classes. Patterns found:");
            for(Violation violation : violations) {
                System.out.println("    " + violation.toString());
            }
        }
        if(configManager.isCheckEnabled("patternCheck", "FacadeCheck")) {
            Check facade = new FacadeCheck(classes, relations);
            violations = facade.checker();
            System.out.println("Performed facade detect for all classes. Patterns found:");
            for(Violation violation : violations) {
                System.out.println("    " + violation.toString());
            }
        }
        if(configManager.isCheckEnabled("patternCheck", "StrategyCheck")) {
            Check strategy = new StrategyCheck(classes, relations);
            violations = strategy.checker();
            System.out.println("Performed strategy detect for all classes. Patterns found:");
            for(Violation violation : violations) {
                System.out.println("    " + violation.toString());
            }
        }

    }

}
