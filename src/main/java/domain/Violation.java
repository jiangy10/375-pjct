package domain;

public class Violation {
    private String description;
    private String violationSourceClass;
    
    public Violation(String description, String violationSourceClass){
    	this.description = description;
    	this.violationSourceClass = violationSourceClass;
    }

    @Override
    public String toString() {
        return "Violation: " + description + " in class: " + violationSourceClass;
    }
}
