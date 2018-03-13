package tiled;

import java.awt.*;

public class Target {

    private String name;
    private Point targetPoint;

    public Target(String name, Point targetPoint) {
        this.name = name;
        this.targetPoint = targetPoint;
    }

    public String getName() {
        return name;
    }

    public Point getTargetPoint() {
        return targetPoint;
    }
}
