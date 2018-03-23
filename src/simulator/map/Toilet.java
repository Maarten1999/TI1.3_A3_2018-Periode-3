package simulator.map;

import simulator.Visitors.Visitor;

import java.awt.*;

public class Toilet extends Target {
    private String name;
    private Point targetPoint;
    private double startTime = 0;

    public Toilet(String name, Point targetPoint) {
        super(name, targetPoint);
        this.name = name;
        this.targetPoint = targetPoint;
        cap = 1;
    }

    @Override
    public void update(float deltatimeFloat){
        if(visitors.size() >= cap) {
            startTime += deltatimeFloat;
            if(startTime > 1)
                removeVisitor(visitors.get(0));
        }
    }

    @Override
    public void removeVisitor(Visitor visitor) {
        visitors.remove(visitor);
        visitor.resetBathroom();
        visitor.onPlacement(new Point(targetPoint.x * 32 + 16, targetPoint.y * 32 + 16));
    }

}
