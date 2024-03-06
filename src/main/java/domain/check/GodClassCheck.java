package domain.check;

import domain.Relation;
import domain.RelationshipTypes;
import domain.Violation;
import domain.data.ClassData;

import java.util.ArrayList;
import java.util.List;

public class GodClassCheck extends Check{
    private static final double GOD_CLASS_LIMIT_FACTOR = 3;

    public GodClassCheck(List<ClassData> classes, List<Relation> relations) {super(classes, relations);}

    @Override
    public List<Violation> checker() {
        List<Violation> violations = new ArrayList<>();
        for(ClassData classData : classes){
            int assocNum = 0;
            int depNum = 0;

            for(Relation r : relations){
                if(r.getFirstClass().equals(classData.getName()) && r.getRelationship() == RelationshipTypes.USES){
                    depNum++;
                }
                if(r.getFirstClass().equals(classData.getName()) && r.getRelationship() == RelationshipTypes.HAS){
                    assocNum++;
                }
            }

            if(assocNum + depNum >= classes.size() * GOD_CLASS_LIMIT_FACTOR){
                Violation violation = new Violation("Potential god class found " +
                        ", consider reducing this class' relationship count", classData.getName());
                violations.add(violation);
            }
        }
        return violations;
    }
}
