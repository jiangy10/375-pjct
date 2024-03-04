package domain;

import java.util.List;

public interface ClassLoader {
	public List<Relation> getRelations();
	public List<ClassData> loadClasses();
}
