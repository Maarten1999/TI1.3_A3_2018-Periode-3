package simulator.Visitors;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class VisitorManager {

    private BufferedImage visitorImage;
    private ArrayList<Visitor> visitorList;
    private boolean drawDebug;

    public VisitorManager(){
       visitorList = new ArrayList<>();
       initializeImages();
    }

    private void initializeImages(){
        try {
            this.visitorImage = ImageIO.read(getClass().getResource("/visitor2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Visitor> getVisitorList() {
        return visitorList;
    }

    public void setDebugMode(boolean debug){
        drawDebug = debug;
    }

    public void update(float deltatimeFloat){
        for(Visitor v : visitorList)
            v.update(deltatimeFloat);
    }

    public void draw(Graphics2D graphics2D){
        if(drawDebug)
            for(Visitor v: visitorList)
                v.drawDebug(graphics2D);

        for(Visitor v: visitorList)
            v.draw(graphics2D);
    }

    public Visitor createVisitor(){
        Visitor v = new Visitor(visitorImage);
        visitorList.add(v);
        return v;
    }

    public void removeVisitor(Visitor v){
        v.onRemoval();
        visitorList.remove(v);
    }

    public void Clear(){
        for(Visitor v: visitorList){
            v.onRemoval();
        }
        visitorList.clear();
    }
}
