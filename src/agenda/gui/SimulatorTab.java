package agenda.gui;

import simulator.Camera;
import simulator.Physics.PhysicsWorld;
import simulator.Train;
import simulator.Visitor;
import simulator.VisitorManager;
import simulator.map.TiledMap;
import simulator.pathfinding.PathFinding;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class SimulatorTab extends JPanel implements ActionListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private VisitorManager visitorManager;
    private int windowWidth, windowHeight;
    private TiledMap map;
    private Train train;
    private int amountOfVisitors = 100;
    private Camera camera;
    private Point2D previousMouseCoordinates;
    private Point pathCoord;

    SimulatorTab(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

        // out/production/Festival Planner/agenda/gui/SimulatorTab.class
        // out/production/Festival Planner/maps/test3.json
        map = new TiledMap(getClass().getResourceAsStream("\\..\\..\\maps\\test3.json"));
        PhysicsWorld.initialize(new Point(map.getWidth(), map.getHeight()));
        this.visitorManager = new VisitorManager(this.map, this.amountOfVisitors);
        camera = new Camera();
        setBackground(new Color(60, 100, 40));
        addMouseListener(this);
        addMouseWheelListener(this);
        new Timer(1000 / 60, this).start();
        addMouseMotionListener(this);

        camera.translate(this.windowWidth / 2 - map.getActualWidth() / 2, this.windowHeight / 2 - map.getActualHeight() / 2);
        camera.centerZoom(this.windowWidth / 2, this.windowHeight / 2);
        camera.zoom(0.1);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W && train == null) {
                    train = new Train(map.getWidth() * map.getTileSize(), -200,
                            (int) (100 + Math.random() * (map.getHeight() * map.getTileSize() - 200)), 10);
                }
            }
        });
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

        //PhysicsWorld.getInstance().draw(g2d, 1);
        if(pathCoord != null) {
            g2d.setColor(Color.cyan);
            g2d.drawRect((int)pathCoord.getX() * 32, (int)pathCoord.getY() * 32, 32, 32);
        }
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
        PhysicsWorld.getInstance().update();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        ArrayList<Visitor> visitors = this.visitorManager.getVisitors();
        for (Visitor visitor : visitors) {
            try {
                visitor.setTarget(camera.getTransform().inverseTransform(e.getPoint(), null));
            } catch (NoninvertibleTransformException e1) {
                e1.printStackTrace();
            }
        }
    }


    public void mouseDragged(MouseEvent e) {
        Point2D mouseCoordinates = e.getPoint();
        camera.translate(mouseCoordinates.getX() - previousMouseCoordinates.getX(),
                mouseCoordinates.getY() - previousMouseCoordinates.getY());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double unitScroll = (-e.getUnitsToScroll() / 9.0);
        if (camera.getZoom() + unitScroll >= 1 && camera.getZoom() + unitScroll < 5) {
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
    public void mousePressed(MouseEvent e) {
        Point2D mouseCoordinates = e.getPoint();
        previousMouseCoordinates = new Point2D.Double(mouseCoordinates.getX() - camera.getTranslateX(), mouseCoordinates.getY() - camera.getTranslateY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (camera.getTranslateX() > -camera.getTransformIVI().transform(new Point2D.Double(0, 0), null).getX()) {
            camera.translate(-camera.getTransformIVI().transform(new Point2D.Double(0, 0), null).getX(), camera.getTranslateY());
        } else if (camera.getTranslateX() < -(camera.getTransformIVI().transform(new Point2D.Double(map.getActualWidth(), 0), null).getX() - windowWidth)) {
            camera.translate(-(camera.getTransformIVI().transform(new Point2D.Double(map.getActualWidth(), 0), null).getX() - windowWidth), camera.getTranslateY());
        }
        if (camera.getTranslateY() > -camera.getTransformIVI().transform(new Point2D.Double(0, 0), null).getY()) {
            camera.translate(camera.getTranslateX(), -camera.getTransformIVI().transform(new Point2D.Double(0, 0), null).getY());
        } else if (camera.getTranslateY() < -(camera.getTransformIVI().transform(new Point2D.Double(0, map.getActualHeight()), null).getY() - windowHeight)) {
            camera.translate(camera.getTranslateX(), -(camera.getTransformIVI().transform(new Point2D.Double(0, map.getActualHeight()), null).getY() - windowHeight));
        }
    }

    Point m;

    public void mouseClicked(MouseEvent e) {
        Point2D n = new Point2D.Double(0, 0);
        try {
        n = camera.getTransform().inverseTransform(e.getPoint(), null);
    } catch (NoninvertibleTransformException e1) {
        e1.printStackTrace();
    }
        if(e.getButton() == MouseEvent.BUTTON3) {
            pathCoord = new Point((int) n.getX() / 32, (int) n.getY() / 32);
            PathFinding.instance().generateMap(pathCoord.toString(), pathCoord);
            for(Visitor v : visitorManager.getVisitors()) {
                v.setTarget(pathCoord);
            }
        }
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
