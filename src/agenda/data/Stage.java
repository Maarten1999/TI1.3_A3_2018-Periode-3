package agenda.data;

import simulator.Visitor;
import simulator.map.Target;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;

public class Stage extends Target implements Serializable {
    private String name;
    private int capacity;
    private Point entrance;
    private Point exit;

    public Stage(String name, int capacity, Point entrance, Point exit) {
        super(name, entrance);//test
        this.name = name;
        this.capacity = capacity;
        this.entrance = entrance;
        this.exit = exit;
    }

    public String getName() {
        return name;
    }

    @Override
    public void update(float deltatimeFloat){

    }

    @Override
    public void removeVisitor(simulator.Visitors.Visitor visitor) {

    }


    public void setName(String name) {
        this.name = name;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public int getCapacity() {
        return capacity;
    }
    public Point getEntrance() {
        return entrance;
    }
    public Point getExit() { return exit; }


    @Override
    public String toString() {
        String[] stageName = name.split("(?=\\p{Upper})");
        String name = "";
        for (int i = 0; i < stageName.length; i++) {
            if(i < stageName.length - 1)
                name += stageName[i] + " ";
            else
                name += stageName[i];
        }
        return name;
    }
}
