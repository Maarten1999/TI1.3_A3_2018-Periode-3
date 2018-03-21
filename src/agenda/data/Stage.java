package agenda.data;

import simulator.Visitor;
import simulator.map.Target;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;

public class Stage extends Target implements Serializable {
    private String name;
    private int capacity;
    private Point entrance;
    private Point exit;

    public Stage(String name, int capacity, Point entrance, Point exit) {
        super(name, entrance);
        this.name = name;
        this.capacity = capacity;
        this.entrance = entrance;
        this.exit = exit;
    }

    public String getName() {
        return name;
    }

    @Override
    public void updateVisitor(Visitor visitor) {
        //
    }

    @Override
    public void teleport(Point point1, Point point2, Visitor visitor) {
        //
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
