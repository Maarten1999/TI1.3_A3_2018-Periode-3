package pathfinding;

import java.awt.*;

public class PathNode {

    public Point previousNode;
    public boolean isActivated;
    public int step;

    public PathNode(){
        previousNode = new Point(-1, -1);
        isActivated = false;
        step = -1;
    }


}
