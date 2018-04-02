package simulator.map;

import simulator.Visitor;

import java.awt.*;

public class Gate extends Target{
    private final float spawnRate = 0.1f;

    private String name;
    private Point targetPoint;
    private float spawnRateTimer;

    public Gate(String name, Point targetPoint) {
        super(name, targetPoint);
        this.name = name;
        this.targetPoint = targetPoint;
        cap = 500;
        spawnRateTimer = 0;
        System.out.println("gate " + targetPoint.toString());
    }

    @Override
    public void update(float deltatimeFloat){
        spawnRateTimer += deltatimeFloat;

        if(spawnRateTimer > spawnRate && visitors.size() > 0){
            spawnRateTimer -= spawnRate;
            removeVisitor(visitors.get(0));
        }
    }

    @Override
    public void removeVisitor(simulator.Visitors.Visitor visitor) {
        visitors.remove(visitor);
        visitor.onPlacement(new Point(targetPoint.x * 32 + 16, targetPoint.y * 32 + 16));
    }

}
