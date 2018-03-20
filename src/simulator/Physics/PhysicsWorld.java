package simulator.Physics;

import org.dyn4j.UnitConversion;
import org.dyn4j.collision.AxisAlignedBounds;
import org.dyn4j.collision.Bounds;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

import java.awt.*;
import java.awt.geom.Point2D;

public class PhysicsWorld {

    private static PhysicsWorld instance;
    private int visitorHitBox = 12;

    public static void initialize(Point size){
        if(instance == null)
            instance = new PhysicsWorld(size);
    }

    public static PhysicsWorld getInstance(){
        return instance;
    }


    private Point size;
    private World world;


    private PhysicsWorld(Point size) {
        this.size = size;
        world = new World();
        world.setGravity(new Vector2(0, 0));

        Rectangle rLong = Geometry.createRectangle(size.getX() * 32, 1);
        Rectangle rShort = Geometry.createRectangle(1, size.getY() * 32);

        Body b = new Body();
        b.addFixture(rLong);
        b.setMass(MassType.INFINITE);
        b.getTransform().setTranslation(size.getX() * 16, -1);
        world.addBody(b);
        b = new Body();
        b.addFixture(rLong);
        b.setMass(MassType.INFINITE);
        b.getTransform().setTranslation(size.getX() * 16, size.getY() * 32);
        world.addBody(b);
        b = new Body();
        b.addFixture(rShort);
        b.setMass(MassType.INFINITE);
        b.getTransform().setTranslation(-1, size.getY() * 16);
        world.addBody(b);
        b = new Body();
        b.addFixture(rShort);
        b.setMass(MassType.INFINITE);
        b.getTransform().setTranslation(size.getX() * 32, size.getY() * 16);
        world.addBody(b);
    }

    public Body getBody(Point2D position) {
        Body b = new Body();
        b.addFixture(Geometry.createCircle(visitorHitBox));
        b.setMass(MassType.NORMAL);
        b.getTransform().setTranslation(position.getX(), position.getY());
        world.addBody(b);
        return b;
    }

    public void removeBody(Body body){
        world.removeBody(body);
    }

    public void update(){
        world.step(1000 / 60);
    }

    public void draw(Graphics2D g, float zoom){
        g.setColor(Color.CYAN);
        DebugDraw.draw(g, world, zoom);
    }
}
