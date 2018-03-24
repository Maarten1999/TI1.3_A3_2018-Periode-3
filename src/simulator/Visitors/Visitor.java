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
    private BufferedImage image;
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

    private Target currentTarget;

    public Visitor(BufferedImage image){
        this.image = image;
        speed = 75 + 30 * Math.random();
        angle = 0;
        position = new Point2D.Double(0, 0);
        previousPosition = new Point2D.Double(0, 0);
        entertainmentTarget = new Point2D.Double(0, 0).toString();
        isVisitorActive = false;

        foodLevel = 100 + Math.random() * 100;
        foodFactor = Math.random();

        bathroomLevel = Math.random() * 200;
        bathroomFactor = Math.random();
    }

    public void setTarget(Point2D newTarget){
        entertainmentTarget = newTarget.toString();
        map = PathFinding.instance().getPathMap(newTarget.toString());
    }

    public void setTarget(String targetName){
        entertainmentTarget = targetName;
        map = PathFinding.instance().getPathMap(targetName);
    }

    public void update(float deltatimeFloat){
        updateServices(deltatimeFloat);
        updateMovement();
        //example
        //if(tijd && alsOpslaan)
        //  Timeline.instance().addVisitorState(GetState());
    }

    ///public VisitorState GetState();

    //public void SetState();

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
                inNeedOfService = true;
            }
        }
//        else if(foodLevel < 0) {
//
//
//            HashSet<String> targets = TargetManager.instance().getStoreList();
//            if(!targets.isEmpty()) {
//                int distance = 1900000000;
//                String currentSelection = targets.iterator().next();
//
//                for (String t : targets)
//                {
//                    PathMap pm = PathFinding.instance().getPathMap(t);
//
//                    Point[] p = pm.getRoute(new Point((int)position.getX() / 32, (int)position.getY() / 32));
//
//                    if(p == null)
//                        p = pm.getRoute(new Point((int)previousPosition.getX() / 32, (int)previousPosition.getY() / 32));
//
//                    if(p == null)
//                        continue;
//
//                    int length = p.length;
//
//                    if( length < distance) {
//                        currentSelection = t;
//                        distance = length;
//                    }
//                }
//
//                serviceTarget = currentSelection;
//                map = PathFinding.instance().getPathMap(serviceTarget);
//                inNeedOfService = false;
//            }
//        }

    }

    private void updateMovement(){

        //no need to update;
        if(!isVisitorActive)
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
                    TargetManager.instance().getTarget(serviceTarget).addVisitor(this);
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

        movementAngle = Math.atan2(diffForMovement.getY(), diffForMovement.getX());

        double x = speed * Math.cos(movementAngle);
        double y = speed * Math.sin(movementAngle);

        body.setLinearVelocity(x, y);
        body.applyForce(new Vector2(x, y));
    }

    public void drawDebug(Graphics2D graphics2D) {

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
        if(!isVisitorActive)
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
