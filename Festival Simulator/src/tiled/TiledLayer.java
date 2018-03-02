package tiled;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class TiledLayer {

    private TiledMap map;
    private int[][] data;

    TiledLayer(int[][] data, TiledMap map) {
        this.map = map;
        this.data = data;
    }

    public void draw(Graphics2D g2d) {
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
    }
}
