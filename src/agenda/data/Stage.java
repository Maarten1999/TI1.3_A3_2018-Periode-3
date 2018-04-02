package agenda.data;

import simulator.Visitors.Visitor;
import simulator.map.Target;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;

public class Stage extends Target implements Serializable {

    private String name;
    private int capacity;
    private Point entrance;
    private Point exit;
    private Rectangle rectangle;

    public Stage(String name, Rectangle rectangle, int capacity, Point entrance, Point exit) {
        super(name, entrance);//test
        this.rectangle = rectangle;
        this.name = name;
        this.capacity = capacity;
        cap = capacity;
        this.entrance = entrance;
        this.exit = exit;
    }

    public String getName() {
        return name;
    }

    @Override
    public void update(float deltatimeFloat){

    }

    public void Draw(Graphics2D g2d){
        if(visitors == null)
            System.out.println("lame");

            for (Visitor v : visitors) {
                int x = (int) (rectangle.getX() + (Math.random() * rectangle.width));
                int y = (int) (rectangle.getY() + (Math.random() * rectangle.height));
                g2d.drawImage(v.image, null, x, y);
            }

    }

    @Override
    public void removeVisitor(simulator.Visitors.Visitor visitor) {
        if(visitors.contains(visitor)) {
            visitors.remove(visitor);
            visitor.onPlacement(new Point(exit.x * 32 + 16, exit.y * 32 + 16));
        }
    }


    public void setName(String name) {
        this.name = name;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
        cap = capacity;
    }
    public int getCapacity() {
        return capacity;
    }
    public Point getEntrance() {
        return entrance;
    }
    public Point getExit() { return exit; }


    @Override
    public String toString() {
        String[] stageName = name.split("(?=\\p{Upper})");
        String name = "";
        for (int i = 0; i < stageName.length; i++) {
            if(i < stageName.length - 1)
                name += stageName[i] + " ";
            else
                name += stageName[i];
        }
        return name;
    }
}
