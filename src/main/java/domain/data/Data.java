package domain.data;

public abstract class Data {
    private String name;
    private boolean isAbstract;

    public Data(String name, boolean isAbstract) {
        this.name = name;
        this.isAbstract = isAbstract;
    }

    public String getName() {
        return name;
    }

    public boolean isAbstract() {
        return isAbstract;
    }
}