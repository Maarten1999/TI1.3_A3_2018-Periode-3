package simulator.Visitors;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;
import simulator.Physics.PhysicsWorld;
import simulator.pathfinding.PathFinding;
import simulator.pathfinding.PathMap;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

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

    private boolean isVisitorActive;

    private Point2D target;

    public Visitor(BufferedImage image){
        this.image = image;
        speed = 7 + 4 * Math.random();
        angle = 0;
        position = new Point2D.Double(0, 0);
        previousPosition = new Point2D.Double(0, 0);
        target = new Point2D.Double(0, 0);
        isVisitorActive = false;
    }

    public void setTarget(Point2D newTarget){
        this.target = newTarget;
        map = PathFinding.instance().getPathMap(newTarget.toString());
    }

    public void update(){
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

        if(p != null)
            System.out.println(p.length);
        else
            System.out.println("null");

        if(p != null) {
            if(p.length > 1)
                route = p;
        }
        else if (route == null)
            return;


        previousPosition = position;

        angle = Math.toRadians(180) + Math.atan2(diffForAngle.getY(), diffForAngle.getX());

        //movement
        Point2D diffForMovement = new Point2D.Double(
                (route[1].getX() * 32) + 16 - position.getX(),
                (route[1].getY() * 32) + 16 - position.getY()
//                target.getX() - position.getX(),
//                target.getY() - position.getY()
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
        tx.rotate(angle, image.getWidth() / 2, image.getHeight() / 2);

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
}
