package simulator.map;

import java.awt.*;

public class Gate extends Target{
    private String name;
    private Point targetPoint;

    public Gate(String name, Point targetPoint) {
        super(name, targetPoint);
        this.name = name;
        this.targetPoint = targetPoint;
    }

    @Override
    public void update(float deltatimeFloat){

    }

    @Override
    public void removeVisitor(simulator.Visitors.Visitor visitor) {

    }
}
