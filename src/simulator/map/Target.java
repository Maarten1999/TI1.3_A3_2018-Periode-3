package simulator.map;

import simulator.Visitor;
import simulator.pathfinding.PathFinding;

import java.awt.*;
import java.util.ArrayList;

public abstract class Target {
    private String name;
    private Point targetPoint;
    ArrayList<Visitor> visitors = new ArrayList<>();

    public Target(String name, Point targetPoint) {
        this.name = name;
        this.targetPoint = targetPoint;
        //example
        //PathFinding.instance().generateMap(name, targetPoint);
    }

    public String getName() {
        return name;
    }

    public Point getTargetPoint() {
        return targetPoint;
    }

    public abstract void updateVisitor(Visitor visitor);
    public abstract void teleport(Point point1, Point point2, Visitor visitor);
    public void addVisitor(Visitor visitor) { this.visitors.add(visitor); }
    public ArrayList<Visitor> getVisitors() { return visitors; }
}
