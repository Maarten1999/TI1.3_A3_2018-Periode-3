import tiled.TiledMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class SimulatorTab extends JPanel implements ActionListener, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

    private VisitorManager visitorManager;
    private int windowWidth, windowHeight;
    private TiledMap map;
    private Train train;
    private int amountOfVisitors = 100;
    private Camera camera;
    private Point2D  previousMouseCoordinates;
    private double minzoom;

   public SimulatorTab(int windowWidth, int windowHeight) {
        this.windowWidth  = windowWidth;
        this.windowHeight = windowHeight;

        map = new TiledMap(getClass().getResourceAsStream("maps/test3.json"));
        this.visitorManager = new VisitorManager(this.map, this.amountOfVisitors);
        camera = new Camera();
        setBackground(new Color(60, 100, 40));
        addMouseListener(this);
        addMouseWheelListener(this);
        new Timer(1000/60, this).start();
        addMouseMotionListener(this);
        addKeyListener(this);


        //camera.translate(0,-720);
        camera.translate(this.windowWidth /2-map.getActualWidth()/2, this.windowHeight /2-map.getActualHeight()/2);
        camera.centerZoom(this.windowWidth /2, this.windowHeight /2);
        camera.zoom(this.windowWidth /((double) this.windowHeight));
        System.out.println(camera.getZoom());
        System.out.println(this.windowWidth /((double) this.windowHeight));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setTransform(camera.getTransform());
        int layerCount = this.map.getLayerCount();
        this.map.draw(g2d, 0);
        if (layerCount >= 2)
            this.map.draw(g2d, 1);
        this.visitorManager.draw(g2d);
        if (this.train != null)
            this.train.draw(g2d);

        // paints remaining layers (layer 0 and 1 are ground and path)
        if (layerCount >= 3) {
            for (int i = 2; i <= layerCount - 1; i++) {
                this.map.draw(g2d, i);
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.visitorManager.update();
        if (this.train != null) {
            this.train.update(this.visitorManager);
            if (this.train.isFinished())
                this.train = null;
        }
        if (this.amountOfVisitors > this.visitorManager.getVisitors().size()) {
            this.visitorManager.addVisitor();
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        ArrayList<Visitor> visitors = this.visitorManager.getVisitors();
        for (Visitor visitor : visitors) {
            try {
                visitor.setTarget(camera.getTransform().inverseTransform(e.getPoint(),null));
            } catch (NoninvertibleTransformException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W && this.train == null) {
            this.train = new Train(this.map.getWidth() * this.map.getTileSize(), -200,
                    (int) (100 + Math.random() * (this.map.getHeight() * this.map.getTileSize() - 200)), 10);
        }
    }

    public void keyTyped(KeyEvent e) {
    }



    public void keyReleased(KeyEvent e) {
    }


    public void mouseDragged(MouseEvent e) {

        Point2D mouseCoordinates = e.getPoint();
        camera.translate(mouseCoordinates.getX() - previousMouseCoordinates.getX(), mouseCoordinates.getY() - previousMouseCoordinates.getY());

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double unitScroll = (-e.getUnitsToScroll()/9.0);
        if(camera.getZoom() + unitScroll >= 1+windowWidth/((double)windowHeight) && camera.getZoom() + unitScroll < 9) {
            camera.zoom(-(e.getUnitsToScroll()) / 9.0);
            Point2D centerZoom = null;
            try {
                centerZoom = camera.getTransformIV().inverseTransform(e.getPoint(), null);
            } catch (NoninvertibleTransformException e1) {
                e1.printStackTrace();
            }
            camera.centerZoom(centerZoom.getX(), centerZoom.getY());

        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point2D mouseCoordinates = e.getPoint();
        previousMouseCoordinates = new Point2D.Double(mouseCoordinates.getX()-camera.getTranslateX() , mouseCoordinates.getY()-camera.getTranslateY());


    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println(camera.getTransformIVI().transform( new Point2D.Double(0,  -( map.getActualHeight()-windowHeight)),null).getY());
        if(camera.getTranslateX() > -camera.getTransformIVI().transform( new Point2D.Double(0,  0 ),null).getX()){
            camera.translate(-camera.getTransformIVI().transform( new Point2D.Double(0,  0 ),null).getX(), camera.getTranslateY());
        }
        else if(camera.getTranslateX() <  -(camera.getTransformIVI().transform( new Point2D.Double(map.getActualWidth(),  0 ),null).getX()-windowWidth)){
            camera.translate(-(camera.getTransformIVI().transform( new Point2D.Double( map.getActualWidth(),  0),null).getX()-windowWidth), camera.getTranslateY());
        }
        if(camera.getTranslateY() > -camera.getTransformIVI().transform( new Point2D.Double(0,  0 ),null).getY()){
            camera.translate(camera.getTranslateX(),-camera.getTransformIVI().transform( new Point2D.Double(0,  0 ),null).getY());
        }
        else if(camera.getTranslateY() < -(camera.getTransformIVI().transform( new Point2D.Double(0,  map.getActualHeight()),null).getY()-windowHeight)){
            camera.translate(camera.getTranslateX(), -(camera.getTransformIVI().transform( new Point2D.Double(0,  map.getActualHeight()),null).getY()-windowHeight));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
