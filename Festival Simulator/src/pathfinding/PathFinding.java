package pathfinding;

import java.awt.*;
import java.util.HashMap;

public class PathFinding {
    private boolean[][] map;

    private HashMap<String, PathMap> mapList;

    public PathFinding(boolean[][] map){
        this.map = map;
        mapList = new HashMap<>();
    }

    public void generateMap(String mapName, Point position){
        if(!mapList.containsKey(mapName))
            mapList.put(mapName, new PathMap(mapName, position, map));
    }

    public PathMap getPathMap(String mapName){
        if(mapList.containsKey(mapName))
            return mapList.get(mapName);
        else
            return null;
    }
}
