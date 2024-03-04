package domain;
import java.util.Objects;

public class Relation {
	private String firstClass;
	private String secondClass;
	private RelationshipTypes relationship;
	
	public Relation(String firstClass, String secondClass, RelationshipTypes relationship) {
		this.firstClass = firstClass;
		this.secondClass = secondClass;
		this.relationship = relationship;
	}

	public String getFirstClass() {
		return firstClass;
	}

	public String getSecondClass() {
		return secondClass;
	}

	public RelationshipTypes getRelationship() {
		return relationship;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return
				this.firstClass.equals(((Relation) o).firstClass) &&
				this.secondClass.equals(((Relation) o).secondClass) &&
				this.relationship.equals(((Relation) o).relationship);
	}


	@Override
	public String toString() {
		return "Relation{" +
				"firstClass='" + firstClass + '\'' +
				", secondClass='" + secondClass + '\'' +
				", relationship=" + relationship +
				'}';
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstClass, secondClass, relationship);
	}



}
