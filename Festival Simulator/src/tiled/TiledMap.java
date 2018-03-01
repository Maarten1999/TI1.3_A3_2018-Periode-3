package tiled;

import javax.imageio.ImageIO;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TiledMap {

    private int width;
    private int height;

    private int tileHeight;
    private int tileWidth;

    private ArrayList<BufferedImage> tiles = new ArrayList<>();

    // TODO: Seperate layers
    private ArrayList<TiledLayer> layers = new ArrayList<>();
    private int[][] layer;

    public TiledMap(InputStream stream) {
        JsonReader reader = null;
        reader = Json.createReader(stream);
        JsonObject root = reader.readObject();

        this.width = root.getInt("width");
        this.height = root.getInt("height");

        //load the tilemap
        try {
            String path = root.getJsonArray("tilesets").getJsonObject(0).getString("image");
            System.out.println(path);
            BufferedImage layers = ImageIO.read(getClass().getResource("\\..\\tilesets\\" + path));

            tileHeight = root.getInt("tileheight");
            tileWidth = root.getInt("tilewidth");

            for (int y = 0; y < layers.getHeight(); y += tileHeight) {
                for (int x = 0; x < layers.getWidth(); x += tileWidth) {
                    tiles.add(layers.getSubimage(x, y, tileWidth, tileHeight));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        layer = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                layer[y][x] = root.getJsonArray("layers").getJsonObject(0).getJsonArray("data").getInt(y * height + x);
            }
        }
    }

    public void draw(Graphics2D g2d) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (layer[y][x] <= 0)
                    continue;

                g2d.drawImage(
                        tiles.get(layer[y][x]),
                        AffineTransform.getTranslateInstance(x * tileWidth, y * tileHeight),
                        null);
            }
        }
    }
}
