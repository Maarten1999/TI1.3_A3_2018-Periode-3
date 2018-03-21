package simulator.map;

import simulator.Visitor;
import java.awt.*;

public abstract class Target {
    private String name;
    private Point targetPoint;

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
}
