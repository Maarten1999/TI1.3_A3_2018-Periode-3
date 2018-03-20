package simulator;

import simulator.map.TiledMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class VisitorManager {

    private BufferedImage visitorImage;
    private BufferedImage bloodStain;
    ArrayList<Visitor> visitors = new ArrayList<>();
    ArrayList<Point2D> bloodStains = new ArrayList<>();
    private int width;
    private int height;
    private int tileSize;

    public VisitorManager(TiledMap map, int amountOfVisitors) {
        try {
            this.visitorImage = ImageIO.read(getClass().getResource("/visitor2.png"));
            this.bloodStain = ImageIO.read(getClass().getResource("/blood.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.height = map.getHeight();
        this.width = map.getWidth();
        this.tileSize = map.getTileSize();
        while (visitors.size() < amountOfVisitors) {
            addVisitor();
        }
    }

    public void draw(Graphics2D g2d) {
        double rotation = 0;
        for (Point2D stain : this.bloodStains) {
            AffineTransform tx = new AffineTransform();
            tx.translate(stain.getX() - this.bloodStain.getWidth() / 2, stain.getY() - this.bloodStain.getHeight() / 2);
            rotation += 0.1;
            tx.rotate(rotation, this.bloodStain.getHeight() / 2, this.bloodStain.getWidth() / 2);
            g2d.drawImage(this.bloodStain, tx, null);
        }
        for (Visitor visitor : visitors)
            visitor.draw(g2d);
    }

    public void update() {
        for (int i = 0; i < this.visitors.size(); i++) {
            visitors.get(i).update();
        }
    }

    public ArrayList<Visitor> getVisitors() {
        return visitors;
    }

    public void addVisitor() {
        Visitor visitor = new Visitor(this.visitorImage, this.width * this.tileSize,
                this.height * this.tileSize);
//        if (!visitor.hasCollision(visitors))
            visitors.add(visitor);
    }

    public void removeVisitor(int index) {
        this.bloodStains.add(this.visitors.get(index).getPosition());
        visitors.get(index).onRemove();
        this.visitors.remove(index);
    }
}
