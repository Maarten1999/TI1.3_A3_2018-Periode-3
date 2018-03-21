package simulator.map;

import simulator.Visitor;

import java.awt.*;

public class Store extends Target {
    private String name;
    private Point gate;

    public Store(String name, Point targetPoint) {
        super(name, targetPoint);
        this.name = name;
        this.gate = targetPoint;
    }

    @Override
    public void updateVisitor(Visitor visitor) {
        //
    }

    @Override
    public void teleport(Point point1, Point point2, Visitor visitor) {
        //
    }
}
