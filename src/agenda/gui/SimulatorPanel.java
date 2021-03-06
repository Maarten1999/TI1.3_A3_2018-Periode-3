package agenda.gui;

import agenda.data.Performance;
import agenda.data.Schedule;
import agenda.data.Stage;
import simulator.Camera;
import simulator.Physics.PhysicsWorld;
import simulator.Train;
import simulator.Visitors.Visitor;
import simulator.Visitors.VisitorManager;
import simulator.map.Target;
import simulator.map.TargetManager;
import simulator.map.TiledMap;
import simulator.pathfinding.PathFinding;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class SimulatorPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

    private VisitorManager visitorManager;
    private SimulatorTab simulatorTab;

    private int windowWidth, windowHeight;

    private TiledMap map;
    private Train train;
    private Camera camera;
    private Point2D previousMouseCoordinates;
    private Point pathCoord;

    private boolean drawDebug;

    private Schedule schedule;

    SimulatorPanel(SimulatorTab simulatorTab, VisitorManager visitorManager, Schedule schedule) {
        this.schedule = schedule;
        map = new TiledMap(getClass().getResourceAsStream("\\..\\..\\maps\\festivalMap.json"));
        PhysicsWorld.initialize(new Point(map.getWidth(), map.getHeight()));
        this.visitorManager = visitorManager;
        this.simulatorTab = simulatorTab;
        camera = new Camera();
        setBackground(new Color(60, 100, 40));
        addMouseListener(this);
        addMouseWheelListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                setWindowWidth(e.getComponent().getWidth());
                setWindowHeight(e.getComponent().getHeight());
            }
        });

        camera.translate(this.windowWidth / 2 - map.getActualWidth() / 2, this.windowHeight / 2 - map.getActualHeight() / 2);
        camera.centerZoom(this.windowWidth / 2, this.windowHeight / 2);
        camera.zoom(0.1);
    }

    public void setSchedule(Schedule schedule){
        this.schedule = schedule;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform prevTrans = g2d.getTransform();
        g2d.setTransform(camera.getTransform());

        int layerCount = this.map.getLayerCount();
        for (int i = 0; i <= layerCount - 1; i++) {
            this.map.draw(g2d, i);
        }
        this.visitorManager.draw(g2d);

        if (this.train != null)
            this.train.draw(g2d);

        if(drawDebug)
            PhysicsWorld.getInstance().draw(g2d, 1);

        if(pathCoord != null) {
            g2d.setColor(Color.cyan);
            g2d.drawRect((int)pathCoord.getX() * 32, (int)pathCoord.getY() * 32, 32, 32);
        }

//        for(Performance p: schedule.getPerformances()){
//            p.getStage().Draw(g2d);
//        }

        for(String s: TargetManager.instance().getStageList()){
            Target t =TargetManager.instance().getTarget(s);
            Stage stage = (Stage)t;
            stage.Draw(g2d);
        }

        g2d.setTransform(prevTrans);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_T:
                if (train == null)
                    train = new Train(map.getWidth() * map.getTileSize(), -200,
                            (int) (100 + Math.random() * (map.getHeight() * map.getTileSize() - 200)), 10);
                break;
            case KeyEvent.VK_D:
                drawDebug = !drawDebug;
                visitorManager.setDebugMode(drawDebug);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
//        ArrayList<Visitor> visitors = this.visitorManager.getVisitors();
//        for (Visitor visitor : visitors) {
//            try {
//                visitor.setTarget(camera.getTransform().inverseTransform(e.getPoint(), null));
//                repaint();
//            } catch (NoninvertibleTransformException e1) {
//                e1.printStackTrace();
//            }
//        }
    }

    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            Point2D mouseCoordinates = e.getPoint();
            camera.translate(mouseCoordinates.getX() - previousMouseCoordinates.getX(),
                    mouseCoordinates.getY() - previousMouseCoordinates.getY());
            repaint();
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double unitScroll = (-e.getUnitsToScroll() / 9.0);
        if (camera.getZoom() + unitScroll >= 0.4 && camera.getZoom() + unitScroll < 5) {
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
        if (SwingUtilities.isLeftMouseButton(e)) {
            Point2D mouseCoordinates = e.getPoint();
            previousMouseCoordinates = new Point2D.Double(mouseCoordinates.getX() - camera.getTranslateX(),mouseCoordinates.getY() - camera.getTranslateY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
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
            repaint();
        }
    }

    public void mouseClicked(MouseEvent e) {
//        Point2D n = new Point2D.Double(0, 0);
//        try {
//            n = camera.getTransform().inverseTransform(e.getPoint(), null);
//        } catch (NoninvertibleTransformException e1) {
//            e1.printStackTrace();
//        }
//        if(e.getButton() == MouseEvent.BUTTON3) {
//            pathCoord = new Point((int) n.getX() / 32, (int) n.getY() / 32);
//            PathFinding.instance().generateMap(pathCoord.toString(), pathCoord);
//            for(Visitor v : visitorManager.getVisitorList()) {
//                v.setTarget(pathCoord);
//            }
//        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
    }

    public TiledMap getMap() { return map; }
}
