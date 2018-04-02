package simulator.map;

import agenda.data.Stage;
import simulator.pathfinding.PathFinding;

import javax.imageio.ImageIO;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
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
    private int actualWidth, actualHeight;

    private ArrayList<TiledTile> tiles = new ArrayList<>();
    private ArrayList<TiledLayer> layers = new ArrayList<>();
    private ArrayList<Target> targets = new ArrayList<>();
    private ArrayList<Stage> stages = new ArrayList<>();
    private boolean[][] collisionMap;
    private int collisionTile1 = 311;
    private int collisionTile2 = 801;

    public TiledMap(InputStream stream) {
        JsonReader reader;
        reader = Json.createReader(stream);
        JsonObject object = reader.readObject();

        // Init
        initLayers(object);
        initTiles(object);
        initImages();

        //init pathfinding map
        PathFinding.initialize(collisionMap);
        TargetManager.initialize(targets);
    }

    public void draw(Graphics2D g2d, int layer) {
        this.layers.get(layer).draw(g2d);
    }

    private void initLayers(JsonObject object) {
        this.width = object.getInt("width");
        this.height = object.getInt("height");
        this.tileHeight = object.getInt("tileheight");
        this.tileWidth = object.getInt("tilewidth");

        this.actualWidth = object.getInt("tilewidth")* width;
        this.actualHeight = object.getInt("tileheight")* height;
        JsonArray layersTemp = object.getJsonArray("layers");
        for (int i = 0; i < layersTemp.size(); i++) {
            switch (layersTemp.getJsonObject(i).getString("name")) {
                case "collision":
                    this.collisionMap = new boolean[height][width];
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            int value = layersTemp.getJsonObject(i).getJsonArray("data").getInt(y * width + x);
                            this.collisionMap[y][x] = !((value == collisionTile1) || (value == collisionTile2));
                        }
                    }
                    break;
                case "objects":
                    JsonArray tempTargets = layersTemp.getJsonObject(i).getJsonArray("objects");
                    for (int ii = 0; ii < tempTargets.size(); ii++) {
                        JsonObject target = tempTargets.getJsonObject(ii);
                        String name = target.getString("name");
                        String type = target.getString("type");
                        switch (type) {
                            case "Stage":
                                Point entrancePointStage = new Point(target.getJsonObject("properties").getInt("EntranceX"), target.getJsonObject("properties").getInt("EntranceY"));
                                Point exitPointStage = new Point(target.getJsonObject("properties").getInt("ExitX"), target.getJsonObject("properties").getInt("ExitY"));

                                Stage stage = new Stage(name, target.getJsonObject("properties").getInt("capacity"), entrancePointStage, exitPointStage);

                                stages.add(stage);
                                targets.add(stage);
                                break;
                            case "Gate":
//                                int max = (target.getJsonObject("properties").getInt("EndTileX") - target.getJsonObject("properties").getInt("StartTileX")) / 2;
//                                for (int j = 0; j < max; j++) {
//                                    Point entrancePoint = new Point(target.getJsonObject("properties").getInt("StartTileX") + j, target.getJsonObject("properties").getInt("Y"));
//                                    Point exitPoint = new Point(target.getJsonObject("properties").getInt("EndTileX") - j, target.getJsonObject("properties").getInt("Y"));
//                                    targets.add(new Gate("Entrance" + (j + 1), entrancePoint));
//                                    targets.add(new Gate("Exit" + (j + 1), exitPoint));
//                                }
//                                break;
                                Point gatePoint = new Point(target.getJsonObject("properties").getInt("StartTileX") + 4, target.getJsonObject("properties").getInt("Y"));
                                targets.add(new Gate("Gate", gatePoint));
                                break;
                            case "toilet":
                                Point toiletPoint = new Point(target.getJsonObject("properties").getInt("tileX"), target.getJsonObject("properties").getInt("tileY"));
                                targets.add(new Toilet(name, toiletPoint));
                                break;
                            case "Store":
                                Point storePoint = new Point(target.getJsonObject("properties").getInt("EntranceX"), target.getJsonObject("properties").getInt("EntranceY"));
                                targets.add(new Store(name, storePoint));
                                break;
                        }
                    }
                    break;
                default:
                    int[][] data = new int[height][width];
                    JsonArray array = layersTemp.getJsonObject(i).getJsonArray("data");
                    for (int y = 0; y < height; y++)
                        for (int x = 0; x < width; x++)
                            data[y][x] = Math.abs(array.getInt(y * width + x));
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
                BufferedImage tileSet = ImageIO.read(getClass().getResource("\\..\\..\\tilesets\\" + path));

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
    }//number of tiles horizontal

    public int getHeight() {
        return height;
    }//number of tiles vertical
    public int getActualWidth() {
        return actualWidth;
    }

    public int getActualHeight() {
        return actualHeight;
    }

    public int getTileSize() {
        return tileHeight;
    }

    public int getLayerCount() {
        return layers.size();
    }

    public ArrayList<Stage> getStages() { return stages; }
}
