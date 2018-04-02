package simulator.map;

import simulator.Visitors.Visitor;
import simulator.pathfinding.PathFinding;

import java.awt.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class Target implements Serializable {
    private String name;
    private Point targetPoint;
    protected int cap;
    protected ArrayList<Visitor> visitors = new ArrayList<>();

    public Target(String name, Point targetPoint) {
        this.name = name;
        this.targetPoint = targetPoint;
        this.cap = cap;
    }

    public void initialize(){
        PathFinding.instance().generateMap(name, targetPoint);
    }

    public String getName() {
        return name;
    }

    public Point getTargetPoint() {
        return targetPoint;
    }

    public void addVisitor(Visitor visitor){
        if(visitors.size() >= cap)
            return;

        this.visitors.add(visitor);

        visitor.onRemoval();
    }

    public abstract void update(float deltatimeFloat);

    public abstract void removeVisitor(Visitor visitor);

    public void clear(){
        visitors.clear();
    }

    public ArrayList<Visitor> getVisitors() { return visitors; }
}
