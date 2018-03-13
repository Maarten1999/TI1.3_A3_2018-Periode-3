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
                int value = data[y][x];
                if (value <= 0)
                    continue;

                BufferedImage image;
                if (hasBit(value, 32))
                    image = flipVertical(this.map.getTile((int) (Math.pow(2, 32) - data[y][x] - 1)).getImage());
                else if (hasBit(value, 31))
                    image = flipHorizontal(this.map.getTile((int) (Math.pow(2, 31) - data[y][x] - 1)).getImage());
                else
                    image = this.map.getTile(data[y][x] - 1).getImage();
                g2d.drawImage(
                        image, AffineTransform.getTranslateInstance(x * this.map.getTileSize(),
                                y * this.map.getTileSize()), null);
            }
        }
        g2d.dispose();
    }

    private boolean hasBit(int value, int bit) {
        return (value >> (bit - 1) & 1) == 1;
    }

    private BufferedImage flipHorizontal(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, image.getHeight(), 0, -image.getWidth(), image.getHeight(), null);
        g.dispose();
        return newImage;
    }

    private BufferedImage flipVertical(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, image.getHeight(), image.getWidth(), -image.getHeight(), null);
        g.dispose();
        return newImage;
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(this.image, 0, 0, null);
    }
}
