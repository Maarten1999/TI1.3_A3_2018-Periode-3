package simulator.map;

import simulator.Visitor;

import java.awt.*;

public class Store extends Target {

    private final float spawnRate = 0.1f;

    private String name;
    private Point gate;
    private float spawnRateTimer;

    public Store(String name, Point targetPoint) {
        super(name, targetPoint);
        this.name = name;
        this.gate = targetPoint;
        cap = 5;
        spawnRateTimer = 0;
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
        visitor.resetFood();
        visitor.onPlacement(new Point(gate.x * 32 + 16, gate.y * 32 + 16));
    }
}
