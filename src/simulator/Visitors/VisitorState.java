package simulator.Visitors;

import simulator.map.Target;

import java.awt.geom.Point2D;

public class VisitorState {

    private int time;

    private boolean isActive;
    private Point2D position;
    private double foodlevel, bathroomlevel;

    private String currentTarget;
    private Target containingTarget;
    private Visitor visitor;

    public VisitorState(int time, boolean isActive, Point2D position, double foodlevel, double bathroomlevel, String currentTarget, Target containingTarget, Visitor visitor){
        this.time = time;
    }
}
