package domain.check.principleChecks;

import domain.data.ClassData;

public abstract class RelationCheck {
    private int relationScore = 0;
    private int relationStandard;
    private ClassData classA;
    private ClassData classB;

    public void incrementRelationScore(int number) {
        this.relationScore += number;
    }

    public void checkRelation() {
    }

    public int getRelationScore() {
        return this.relationScore;
    }

    public boolean reachStandard(){
        return this.relationScore >= relationStandard;
    }
}
