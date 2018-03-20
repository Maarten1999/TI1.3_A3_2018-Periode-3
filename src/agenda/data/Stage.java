package agenda.data;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;

public class Stage implements Serializable {
    private String name;
    private int capacity;
    private Point entrance;
    private Point exit;

    public Stage(String name, int capacity, Point entrance, Point exit) {
        this.name = name;
        this.capacity = capacity;
        this.entrance = entrance;
        this.exit = exit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Point getEntrance() {
        return entrance;
    }

    public Point getExit() {
        return exit;
    }

    @Override
    public String toString() {
        return name;
    }
}
