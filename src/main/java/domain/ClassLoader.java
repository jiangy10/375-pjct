package domain;

import domain.data.ClassData;

import java.util.List;

public interface ClassLoader {
	public List<Relation> getRelations();
	public List<ClassData> loadClasses();
}
