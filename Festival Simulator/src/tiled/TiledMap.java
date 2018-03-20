package tiled;

import pathfinding.PathFinding;

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
    private ArrayList<Target> targets = new ArrayList<>();
    private boolean[][] collisionMap;
    private int collisionTile = 305;

    public TiledMap(InputStream stream) {
        JsonReader reader;
        reader = Json.createReader(stream);
        JsonObject object = reader.readObject();

        // Init
        initLayers(object);
        initTiles(object);
        initImages();

        //init pathfinding map
        PathFinding.initialize(new boolean[width][height]);
    }

    public void draw(Graphics2D g2d, int layer) {
        this.layers.get(layer).draw(g2d);
    }

    private void initLayers(JsonObject object) {
        this.width = object.getInt("width");
        this.height = object.getInt("height");
        this.tileHeight = object.getInt("tileheight");
        this.tileWidth = object.getInt("tilewidth");

        JsonArray layersTemp = object.getJsonArray("layers");
        for (int i = 0; i < layersTemp.size(); i++) {
            switch (layersTemp.getJsonObject(i).getString("name")) {
                case "collision":
                    this.collisionMap = new boolean[height][width];
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            int value = layersTemp.getJsonObject(i).getJsonArray("data").getInt(y * height + x);
                            this.collisionMap[y][x] = value == collisionTile;
                        }
                    }
                    break;
                case "objects":
                    JsonArray tempTargets = layersTemp.getJsonObject(i).getJsonArray("objects");
                    for (int ii = 0; ii < tempTargets.size(); ii++) {
                        JsonObject target = layersTemp.getJsonObject(i).getJsonArray("objects").getJsonObject(ii);
                        Point point = new Point((target.getInt("x") + target.getInt("width") / 2) / tileHeight,
                                (target.getInt("y") + target.getInt("height") / 2) / tileHeight);
                        String name = target.getString("name");
                        this.targets.add(new Target(name, point));
                    }
                    break;
                default:
                    int[][] data = new int[height][width];
                    JsonArray array = layersTemp.getJsonObject(i).getJsonArray("data");
                    for (int y = 0; y < height; y++)
                        for (int x = 0; x < width; x++)
                            data[y][x] = Math.abs(array.getInt(y * height + x));
                    this.layers.add(new TiledLayer(data, this));
                    break;
            }
        }
    }

    private void initTiles(JsonObject object) {
        try {
            JsonArray tileSets = object.getJsonArray("tilesets");
            for (int i = 0; i < tileSets.size(); i++) {
                String path = tileSets.getJsonObject(i).getString("image");
                BufferedImage tileSet = ImageIO.read(getClass().getResource("\\..\\tilesets\\" + path));

                for (int y = 0; y < tileSet.getHeight(); y += tileHeight)
                    for (int x = 0; x < tileSet.getWidth(); x += tileWidth)
                        this.tiles.add(new TiledTile(tileSet.getSubimage(x, y, tileWidth, tileHeight)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initImages() {
        for (TiledLayer layer : this.layers) {
            layer.createBufferedImage();
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
