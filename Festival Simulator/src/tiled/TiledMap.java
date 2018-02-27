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

    private int[] map;

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
            BufferedImage layers = ImageIO.read(getClass().getResource(path));

            tileHeight = root.getJsonObject("layers").getJsonObject("tile").getInt("height");
            tileWidth = root.getJsonObject("layers").getJsonObject("tile").getInt("width");

            for(int y = 0; y < layers.getHeight(); y += tileHeight)
            {
                for(int x = 0; x < layers.getWidth(); x += tileWidth)
                {
                    tiles.add(layers.getSubimage(x, y, tileWidth, tileHeight));
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }

        map = new int[height * width];
            for(int i = 0; i < width * height; i++) {
                map[i] = root.getJsonArray("layers").getJsonObject(0).getJsonArray("data").getInt(i);
            }
    }

//    void draw(Graphics2D g2d)
//    {
//
//        for(int y = 0; y < height; y++)
//        {
//            for(int x = 0; x < width; x++)
//            {
//                if(map[y][x] < 0)
//                    continue;
//
//                g2d.drawImage(
//                        tiles.get(map[y][x]),
//                        AffineTransform.getTranslateInstance(x*tileWidth, y*tileHeight),
//                        null);
//            }
//        }
//    }
}
