package simulator.Visitors;

import simulator.map.Target;

import java.awt.geom.Point2D;

public class VisitorState {

    private boolean isActive;
    private boolean inNeedOfService;

    private Point2D position;
    private double foodlevel, bathroomlevel;

    private String serviceTarget;
    private String entertainmentTarget;

    private Target containingTarget;

    private Visitor visitor;

    public VisitorState(boolean isActive, Point2D position, double foodlevel, double bathroomlevel, boolean inNeedOfService,  String serviceTarget, String entertainmentTarget, Target containingTarget, Visitor visitor){
        this.isActive = isActive;
        this.position = position;
        this.foodlevel = foodlevel;
        this.bathroomlevel = bathroomlevel;
        this.inNeedOfService = inNeedOfService;
        this.serviceTarget = serviceTarget;
        this.entertainmentTarget = entertainmentTarget;
        this.containingTarget = containingTarget;
        this.visitor = visitor;
    }

    public boolean getIsActive(){
        return isActive;
    }

    public boolean getIsInNeedOfService(){
        return inNeedOfService;
    }

    public Point2D getPosition() {
        return position;
    }

    public double getFoodlevel(){
        return foodlevel;
    }

    public double getBathroomlevel(){
        return bathroomlevel;
    }

    public String getServiceTarget(){
        return serviceTarget;
    }

    public String getEntertainmentTargetTarget(){
        return entertainmentTarget;
    }

    public Target getContainingTarget() {
        return containingTarget;
    }

    public void applyState(){
        visitor.setState(this);
    }
}
