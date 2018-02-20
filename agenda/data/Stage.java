package agenda.data;

public class Stage {
    String name;
    int capacity;

    public Stage(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapicity() {
        return capacity;
    }

    public void setCapicity(int capicity) {
        this.capacity = capicity;
    }

    @Override
    public String toString() {
        return name;
    }
}
