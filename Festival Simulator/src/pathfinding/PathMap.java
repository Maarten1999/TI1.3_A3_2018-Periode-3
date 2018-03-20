package pathfinding;

import java.awt.*;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
public class PathMap {

//    public final int TILE_SIZE = 32;//debug variable

    private String name;
    private Point size;

    private PathNode[][] map;
    private int step;
    private LinkedList<Point> nodeList;

    private boolean succesfulCreated;

    private HashMap<Point, Point[]> calculatedRoutes;

    public PathMap(String name, Point startPoint, boolean [][] map){

        this.name = name;//set name
        size = new Point(map.length, map[0].length);//set map sizes
        step = 0;//debug value for checking the step

        succesfulCreated = CheckIfStartPointIsValid(startPoint, map);//check if start point is valid

        if(!succesfulCreated)//if not succesfully created return
            return;



        initializeMap(map);//init the node map based on map data
        nodeList = checkMap(startPoint);//check next nodes to be activated;

        while(nodeList.size() > 0){//while we have nodes to activate keep activating them
            nodeList = checkMap();
        }

        //cache for every calculated route
        calculatedRoutes = new HashMap<>();
    }

    //check if start point is valid
    private boolean CheckIfStartPointIsValid(Point startPoint, boolean[][] map){
        if(startPoint.x < 0 || startPoint.x >= map.length)//check if x is valid
            return false;
        if(startPoint.y < 0 || startPoint.y >= map[0].length)//check if y is valid
            return  false;
        return true;//return true if both are valid
    }

    private void initializeMap(boolean[][] mapData){
        map = new PathNode[size.x][size.y];

        for(int x = 0; x < size.x; x++){
            for(int y = 0; y < size.y; y++){
                if(!mapData[x][y])
                    map[x][y] = new PathNode();
            }
        }
    }


    public String getName(){
        return name;
    }

    public Point[] getRoute(Point position) {

        if (!succesfulCreated)//the path map wasnt created with the correct data so we return null
            return null;

        if (position.x < 0 || position.x >= size.x || position.y < 0 || position.y >= size.y)//check if position is inside the map size
            return null;

        if (map[position.x][position.y] == null)//if we dont have a node at this position
            return null;
        if (!map[position.x][position.y].isActivated)//check if the node is reachable
            return null;

        if (calculatedRoutes.containsKey(position))//if we have already calculated the route please reuse it
            return calculatedRoutes.get(position);

        //if we have not calculated this route before calculate it and store it
        Point[] points;
        PathNode n = map[position.x][position.y];
        points = new Point[n.step + 1];
        points[0] = position;
        int index = 1;

        while (!n.previousNode.equals(new Point(-1, -1))) {//while this node n does not equal an invalid point (-1, -1) == invalid
            points[index] = new Point(n.previousNode.x, n.previousNode.y);
            n = map[n.previousNode.x][n.previousNode.y];
            index++;
        }

        calculatedRoutes.put(position, points);
        System.out.println("new route calculated!");

        for(Point p : points){
            System.out.println(p.toString());
        }

        return points;
    }

    private LinkedList<Point> checkMap(Point startPoint){
        LinkedList<Point> nodeList = new LinkedList();

        if(map[startPoint.x][startPoint.y] != null){
            ActivateNode(startPoint, new Point(-1, -1), nodeList);
        }

        step++;
        return nodeList;
    }

    private LinkedList<Point> checkMap(){
        LinkedList<Point> nodeList = new LinkedList();

        while(this.nodeList.size() > 0){
            Point p = this.nodeList.pop();

            ActivateNode( new Point(p.x, p.y-1), p, nodeList);
            ActivateNode( new Point(p.x+1, p.y), p, nodeList);
            ActivateNode( new Point(p.x, p.y+1), p, nodeList);
            ActivateNode( new Point(p.x-1, p.y), p, nodeList);
        }

        step++;
        return nodeList;
    }

    private void ActivateNode(Point position, Point previousNode, LinkedList<Point> list){

        if(position.x < 0 || position.y < 0 || position.x >= size.x || position.y >= size.y)
            return;

        PathNode pn = map[position.x][position.y];

        if(pn == null || pn.isActivated)
            return;

        pn.isActivated = true;
        pn.step = step;
        pn.previousNode = previousNode;

        list.add(position);
    }


//    public void drawMap(Graphics2D g2d){
//        for(int x = 0; x < size.x; x++){
//            for(int y = 0; y < size.y; y++){
//                if(map[x][y] != null)
//                {
//                    int xPos = x * TILE_SIZE;
//                    int yPos = y * TILE_SIZE;
//                    g2d.setColor((map[x][y].isActivated)? Color.GREEN : Color.red);
//                    g2d.fillRect(xPos, yPos, TILE_SIZE, TILE_SIZE);
//                    g2d.setColor(Color.white);
//                    g2d.drawString(map[x][y].step + "", xPos + 16, yPos+16);
//                }
//            }
//        }
//    }
}
