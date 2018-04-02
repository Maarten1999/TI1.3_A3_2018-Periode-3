package simulator;

import simulator.pathfinding.PathFinding;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Visitor {
    private Point2D position;
    private double angle;
    private BufferedImage image;
    private double speed;
    private Point2D targetPosition = new Point2D.Double(500, 500);
    private String targetName;
    private int health = (int) (50 + Math.random() * 50);
    private int damage = (int) (5 + Math.random() * 10);
    private int collisionTime = 0;
    private int punchingTime = 50;

    private int width;
    private int height;
    private int size;
    private boolean circle;
    private int killedVisitor;

    Visitor(BufferedImage image, int width, int height) {
        this.image = image;
        this.size = image.getHeight();
        position = new Point2D.Double(this.size / 2 + Math.random() * (width - this.size),
                this.size / 2 + Math.random() * (height - this.size));
        angle = Math.random() * 2 * Math.PI;
        speed = 6 + 7 * Math.random();
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics2D g2d) {
        AffineTransform tx = new AffineTransform();
        tx.translate(position.getX() - image.getWidth() / 2, position.getY() - image.getHeight() / 2);
        tx.rotate(angle, image.getWidth() / 2, image.getHeight() / 2);
        g2d.drawImage(image, tx, null);
    }

    public void update(ArrayList<Visitor> visitors) {
        Point2D diff = new Point2D.Double(
                targetPosition.getX() - position.getX(),
                targetPosition.getY() - position.getY()
        );

        double targetAngle = Math.atan2(diff.getY(), diff.getX());

        double angleDiff = angle - targetAngle;
        while (angleDiff < -Math.PI)
            angleDiff += 2 * Math.PI;
        while (angleDiff > Math.PI)
            angleDiff -= 2 * Math.PI;

        if (angleDiff < 0)
            angle += 0.1;
        else if (angleDiff > 0)
            angle -= 0.1;


        Point2D lastPosition = position;
        position = new Point2D.Double(
                position.getX() + speed * Math.cos(angle),
                position.getY() + speed * Math.sin(angle));

        boolean hasCollision = hasCollision(visitors) || isWithinMapBounds();

        if (hasCollision) {
            position = lastPosition;
            angle += 0.2;
        }
    }

    public boolean hasCollision(ArrayList<Visitor> visitors) {
        boolean hasCollision = false;

        for (int i = 0; i < visitors.size(); i++) {
            if (visitors.get(i) == this)
                continue;
            double distance = position.distance(visitors.get(i).position);
            if (distance < this.size) {
                this.collisionTime++;
                hasCollision = true;

                // Punching mechanics
                if (this.collisionTime >= this.punchingTime) {
                    this.collisionTime = this.punchingTime - 20;
                    if (visitors.get(i).dealDamage(this.damage)) {
                        this.killedVisitor = i;
                    }
                }
            }
        }
        if (!hasCollision)
            this.collisionTime = 0;
        return hasCollision;
    }

    private boolean isWithinMapBounds() {
        boolean outOfBounds = false;

        if (this.position.getX() > this.width - this.size / 2 || this.position.getX() < this.size / 2
                || this.position.getY() > this.height - this.size / 2 || this.position.getY() < this.size / 2) {

            outOfBounds = true;

        }
        return outOfBounds;
    }


    public void setTarget(Point2D targetPosition) {
        this.targetPosition = targetPosition;
        this.targetName = targetName;
    }

    private boolean dealDamage(int damage) {
        this.health -= damage;
        return this.health <= 0;
    }

    public int getHealth() {
        return health;
    }

    public Shape getCircle() {
        return new Ellipse2D.Double(this.position.getX() - this.size / 2,
                this.position.getY() - this.size / 2,
                this.size, this.size);
    }

    public Point2D getPosition() {
        return position;
    }

    public int getKilledVisitor() {
        return killedVisitor;
    }
}
