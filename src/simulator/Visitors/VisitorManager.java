package simulator.Visitors;

import simulator.map.Target;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class VisitorManager {

    private BufferedImage visitorImage;
    private ArrayList<Visitor> visitors;
    private boolean drawDebug;

    public VisitorManager(){
       visitors = new ArrayList<>();
       initializeImages();
    }

    private void initializeImages(){
        try {
            this.visitorImage = ImageIO.read(getClass().getResource("/visitor2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Visitor> getVisitors() {
        return visitors;
    }

    public void setDebugMode(boolean debug){
        drawDebug = debug;
    }

    public void update(float deltatimeFloat){
        for(Visitor v : visitors)
            v.update(deltatimeFloat);
    }

    public void draw(Graphics2D graphics2D){
        if(drawDebug)
            for(Visitor v: visitors)
                v.drawDebug(graphics2D);

        for(Visitor v: visitors)
            v.draw(graphics2D);
    }

    public Visitor createVisitor(Target entrance1){
        Visitor v = new Visitor(visitorImage);
        visitors.add(v);
        v.onPlacement(new Point(entrance1.getTargetPoint().x * 32, entrance1.getTargetPoint().y * 32));
        return v;
    }

    public void createVisitor(Visitor visitor){
        visitors.add(visitor);
        visitor.onPlacement(visitor.getPosition());
    }

    private void removeVisitor(Visitor v){
        v.onRemoval();
        visitors.remove(v);
    }

    public void clear() {
        while (visitors.size() != 0)
            removeVisitor(visitors.get(visitors.size() - 1));
    }

    public void setVisitors(ArrayList<Visitor> visitors) {
        this.visitors = new ArrayList<>(visitors);
    }
}
