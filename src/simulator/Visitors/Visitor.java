package simulator.Visitors;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;
import simulator.Physics.PhysicsWorld;
import simulator.map.Target;
import simulator.map.TargetManager;
import simulator.pathfinding.PathFinding;
import simulator.pathfinding.PathMap;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Iterator;

public class Visitor {
    private Point2D position;
    private Point2D previousPosition;

    private double angle;
    private double movementAngle;
    private double speed;
    public BufferedImage image;
    private Body body;

    private PathMap map;
    private Point[] route;

    private double foodLevel;
    private double bathroomLevel;

    private double foodFactor;
    private double bathroomFactor;

    private boolean isVisitorActive;

    private boolean inNeedOfService;
    private String entertainmentTarget;
    private String serviceTarget;

    private Target containingTarget;

    private boolean eventStop;

    public Visitor(BufferedImage image){
        this.image = image;
        speed = 75 + 30 * Math.random();
        angle = 0;
        position = new Point2D.Double(0, 0);
        previousPosition = new Point2D.Double(0, 0);
        entertainmentTarget = new Point2D.Double(0, 0).toString();

        foodLevel = 60 + Math.random() * 80;
        foodFactor = Math.random() * 2;

        bathroomLevel = Math.random() * 100;
        bathroomFactor = Math.random() * 2;

        isVisitorActive = false;

        containingTarget = TargetManager.instance().getTarget("Gate");
        containingTarget.addVisitor(this);
    }

    public void stop(){
        eventStop = true;
    }

    public void setTarget(Point2D newTarget){
        entertainmentTarget = newTarget.toString();
        map = PathFinding.instance().getPathMap(newTarget.toString());
    }

    public void setTarget(String targetName){
        //System.out.println(targetName);
        entertainmentTarget = targetName;
        map = PathFinding.instance().getPathMap(targetName);

        if(containingTarget == null)
            return;

        if(!containingTarget.getName().equals(entertainmentTarget) && !containingTarget.getName().equals("Gate")) {
            containingTarget.removeVisitor(this);
            containingTarget = null;
        }
    }

    public void update(float deltatimeFloat){
        if(!eventStop)
            updateServices(deltatimeFloat);
        updateMovement();
    }

    //return a state
    public VisitorState getState(){
        return new VisitorState(isVisitorActive, position, foodLevel, bathroomLevel, inNeedOfService, serviceTarget, entertainmentTarget, containingTarget, this);
    }

    //set a state
    public void setState(VisitorState state){
        //remove body if we have one
        if(body != null)
            onRemoval();

        isVisitorActive = state.getIsActive();
        position = state.getPosition();
        foodLevel = state.getFoodlevel();
        bathroomLevel = state.getBathroomlevel();
        inNeedOfService = state.getIsInNeedOfService();
        serviceTarget = state.getServiceTarget();
        entertainmentTarget = state.getEntertainmentTargetTarget();
        containingTarget = state.getContainingTarget();

        eventStop = false;

        //if are outside an target create our body else add to target
        if(containingTarget == null) {
            onPlacement(position);
        }
        else {
            containingTarget.addVisitor(this);
        }
    }

    private void updateServices(float deltatimeFloat){
        foodLevel -= foodFactor * deltatimeFloat;
        bathroomLevel -= bathroomFactor * deltatimeFloat;

        if(bathroomLevel < 0) {
            HashSet<String> targets = TargetManager.instance().getBathroomList();

            if(!targets.isEmpty()){
                int distance = 1900000000;
                String currentSelection = targets.iterator().next();

                for (String t : targets)
                {
                    PathMap pm = PathFinding.instance().getPathMap(t);

                    Point[] p = pm.getRoute(new Point((int)position.getX() / 32, (int)position.getY() / 32));

                    if(p == null)
                        p = pm.getRoute(new Point((int)previousPosition.getX() / 32, (int)previousPosition.getY() / 32));

                    if(p == null)
                        continue;

                    int length = p.length;

                    if( length < distance) {
                        currentSelection = t;
                        distance = length;
                    }
                }

                serviceTarget = currentSelection;
                map = PathFinding.instance().getPathMap(serviceTarget);
                if(containingTarget != null) {
                    containingTarget.removeVisitor(this);
                    containingTarget = null;
                }
                inNeedOfService = true;
            }
        }
        else if(foodLevel < 0) {


            HashSet<String> targets = TargetManager.instance().getStoreList();
            if(!targets.isEmpty()) {
                int distance = 1900000000;
                String currentSelection = targets.iterator().next();

                for (String t : targets)
                {
                    PathMap pm = PathFinding.instance().getPathMap(t);

                    Point[] p = pm.getRoute(new Point((int)position.getX() / 32, (int)position.getY() / 32));

                    if(p == null)
                        p = pm.getRoute(new Point((int)previousPosition.getX() / 32, (int)previousPosition.getY() / 32));

                    if(p == null)
                        continue;

                    int length = p.length;

                    if( length < distance) {
                        currentSelection = t;
                        distance = length;
                    }
                }

                serviceTarget = currentSelection;
                map = PathFinding.instance().getPathMap(serviceTarget);
                if(containingTarget != null) {
                    containingTarget.removeVisitor(this);
                    containingTarget = null;
                }
                inNeedOfService = true;
            }
        }

    }

    private void updateMovement(){

        //no need to update;
        if(!isVisitorActive || body == null)
            return;

        //updatebody position
        Vector2 v = body.getTransform().getTranslation();

        if(route != null)
            position = new Point2D.Double(v.x, v.y);

        //set angle of visitor
        Point2D diffForAngle = new Point2D.Double(
                previousPosition.getX() - position.getX(),
                previousPosition.getY() - position.getY()
        );


        if(map == null)
            return;

        Point[] p = map.getRoute(new Point((int) position.getX() / 32, (int) position.getY() / 32));

        if(p != null) {
            if(p.length > 1)
                route = p;
            else if (p.length <= 1){
                if(inNeedOfService){
                    containingTarget = TargetManager.instance().getTarget(serviceTarget);
                    if(containingTarget != null)
                        containingTarget.addVisitor(this);
                    return;
                }else{
                    containingTarget = TargetManager.instance().getTarget(entertainmentTarget);
                    if(containingTarget != null)
                        containingTarget.addVisitor(this);
                    return;
                }

            }
        }
        else if (route == null)
            return;


        previousPosition = position;

        angle = Math.toRadians(180) + Math.atan2(diffForAngle.getY(), diffForAngle.getX());

        //movement
        Point2D diffForMovement = new Point2D.Double(
                (route[1].getX() * 32) + 16 - position.getX(),
                (route[1].getY() * 32) + 16 - position.getY()
        );

        movementAngle = Math.atan2(diffForMovement.getY(), diffForMovement.getX()) + Math.toRadians(Math.random() * 26);

        double x = speed * Math.cos(movementAngle);
        double y = speed * Math.sin(movementAngle);

        body.setLinearVelocity(x, y);
        body.applyForce(new Vector2(x, y));
    }

    public void drawDebug(Graphics2D graphics2D) {

        if(!isVisitorActive || body == null)
            return;

//        if(map != null)
//            map.drawMap(graphics2D);

        graphics2D.setColor(Color.red);
        graphics2D.drawLine((int) position.getX(), (int) position.getY(), (int) position.getX() + (int) (Math.cos(movementAngle) * 64), (int) position.getY() + (int) (Math.sin(movementAngle) * 64));

        if (route != null) {
            //map.drawMap(graphics2D);

            Point2D prevP = new Point2D.Double((position.getX() - 16) / 32, (position.getY() - 16) / 32);
            for (Point p : route) {
                graphics2D.setColor(Color.RED);
                graphics2D.drawLine((int) (prevP.getX() * 32) + 16, (int) (prevP.getY() * 32) + 16,
                        (int) p.getX() * 32 + 16, (int) p.getY() * 32 + 16);

                prevP = p;
            }
        }

        graphics2D.setColor(Color.red);
        graphics2D.drawLine((int) position.getX(), (int) position.getY(), (int) position.getX() + (int) (Math.cos(movementAngle) * 64), (int) position.getY() + (int) (Math.sin(movementAngle) * 64));
    }

    public void draw(Graphics2D graphics2D){
        if(!isVisitorActive || body == null)
            return;

        AffineTransform tx = new AffineTransform();
        tx.translate(body.getTransform().getTranslation().x - image.getWidth() / 2, body.getTransform().getTranslation().y - image.getHeight() / 2);

        tx.rotate(movementAngle, image.getWidth() / 2, image.getHeight() / 2);

        graphics2D.drawImage(image, tx, null);
    }

    public void onPlacement(Point2D placementPosition){
        position = placementPosition;
        previousPosition = placementPosition;
        body = PhysicsWorld.getInstance().getBody(placementPosition);
        isVisitorActive = true;
        containingTarget = null;
    }

    public void onRemoval(){
        PhysicsWorld.getInstance().removeBody(body);
        body = null;
        isVisitorActive = false;
    }

    public void resetBathroom(){
        bathroomLevel = 200;
        inNeedOfService = false;
        map = PathFinding.instance().getPathMap(entertainmentTarget);
    }

    public void resetFood(){
        foodLevel = 200;
        bathroomLevel -= 13;
        inNeedOfService = false;
        map = PathFinding.instance().getPathMap(entertainmentTarget);
    }
}
