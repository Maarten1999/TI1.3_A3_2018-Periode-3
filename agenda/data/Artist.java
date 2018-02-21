package agenda.data;

import java.io.Serializable;

public class Artist implements Serializable {
    private String name;
    private int popularity;

    public Artist(String name, int popularity) {
        this.name = name;
        this.popularity = popularity;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }
}
