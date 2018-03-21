package simulator.map;

import simulator.Visitor;

import java.awt.*;

public class Toilet extends Target {
    private String name;
    private Point targetPoint;

    public Toilet(String name, Point targetPoint) {
        super(name, targetPoint);
        this.name = name;
        this.targetPoint = targetPoint;
    }

    @Override
    public void updateVisitor(Visitor visitor) {
        //
    }

    @Override
    public void teleport(Point point1, Point point2, Visitor visitor) {
        return;
    }
}
