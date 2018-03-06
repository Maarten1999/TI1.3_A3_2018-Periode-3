package tiled;

import javax.imageio.ImageIO;
import javax.json.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TiledMap {

    private int width;
    private int height;
    private int tileHeight;
    private int tileWidth;

    private ArrayList<TiledTile> tiles = new ArrayList<>();
    private ArrayList<TiledLayer> layers = new ArrayList<>();

    public TiledMap(InputStream stream) {
        JsonReader reader;
        reader = Json.createReader(stream);
        JsonObject object = reader.readObject();

        // Init
        initLayers(object);
        initTiles(object);
    }

    public void draw(Graphics2D g2d) {
        for (TiledLayer tiledLayer : this.layers) {
            tiledLayer.draw(g2d);
        }
    }

    public void draw(Graphics2D g2d, int layer) {
        this.layers.get(layer).draw(g2d);
    }

    private void initLayers(JsonObject object) {
        this.width = object.getInt("width");
        this.height = object.getInt("height");

        JsonArray layersTemp = object.getJsonArray("layers");
        for (int i = 0; i < layersTemp.size(); i++) {
            int[][] data = new int[height][width];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    data[y][x] = layersTemp.getJsonObject(i).getJsonArray("data").getInt(y * height + x);
                }
            }
            this.layers.add(new TiledLayer(data, this));
        }
    }

    private void initTiles(JsonObject object) {
        try {
            JsonArray tileSets = object.getJsonArray("tilesets");
            for (int i = 0; i < tileSets.size(); i++) {
                String path = tileSets.getJsonObject(i).getString("image");
                BufferedImage tileSet = ImageIO.read(getClass().getResource("\\..\\tilesets\\" + path));

                this.tileHeight = tileSets.getJsonObject(i).getInt("tileheight");
                this.tileWidth = tileSets.getJsonObject(i).getInt("tilewidth");

                for (int y = 0; y < tileSet.getHeight(); y += tileHeight) {
                    for (int x = 0; x < tileSet.getWidth(); x += tileWidth) {
                        this.tiles.add(new TiledTile(tileSet.getSubimage(x, y, tileWidth, tileHeight)));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TiledTile getTile(int index) {
        return this.tiles.get(index);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTileSize() {
        return tileHeight;
    }

    public int getLayerCount() {
        return layers.size();
    }
}
