import tiled.TiledMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class VisitorManager {

    private BufferedImage image;
    ArrayList<Visitor> visitors = new ArrayList<>();

    VisitorManager(TiledMap map) {
        try {
            this.image = ImageIO.read(getClass().getResource("/visitor2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (visitors.size() < 1500) {
            Visitor visitor = new Visitor(this.image, map.getWidth() * map.getTileSize(),
                    map.getHeight() * map.getTileSize());
            if (!visitor.hasCollision(visitors))
                visitors.add(visitor);
        }
    }

    public void draw(Graphics2D g2d) {
        for (Visitor visitor : visitors)
            visitor.draw(g2d);
    }

    public void update() {
        for (int i = 0; i < this.visitors.size(); i++) {
            visitors.get(i).update(visitors);
        }
    }

    public ArrayList<Visitor> getVisitors() {
        return visitors;
    }
}
