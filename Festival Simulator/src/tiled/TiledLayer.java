package tiled;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class TiledLayer {

    private TiledMap map;
    private int[][] data;
    private BufferedImage image;

    TiledLayer(int[][] data, TiledMap map) {
        this.map = map;
        this.data = data;
    }

    public void createBufferedImage() {
        this.image = new BufferedImage(this.map.getWidth() * this.map.getTileSize(),
                this.map.getHeight() * this.map.getTileSize(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        for (int y = 0; y < this.map.getHeight(); y++) {
            for (int x = 0; x < this.map.getWidth(); x++) {
                if (data[y][x] <= 0)
                    continue;

                g2d.drawImage(
                        this.map.getTile(data[y][x] - 1).getImage(),
                        AffineTransform.getTranslateInstance(x * this.map.getTileSize(), y * this.map.getTileSize()),
                        null);
            }
        }
        g2d.dispose();
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(this.image, 0, 0, null);
    }
}
