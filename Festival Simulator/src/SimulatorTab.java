import tiled.TiledMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class SimulatorTab extends JPanel implements ActionListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private VisitorManager visitorManager;
    private TiledMap map;
    private Train train;
    private int amountOfVisitors = 100;
    private Camera camera;
    private  Point2D  pressedPoint = new Point2D.Double(0,0);

    SimulatorTab() {
        map = new TiledMap(getClass().getResourceAsStream("maps/test2.json"));
        requestFocus();
        setFocusable(true);
        this.visitorManager = new VisitorManager(this.map, this.amountOfVisitors);
        camera = new Camera();
        addMouseListener(this);
        addMouseWheelListener(this);
        new Timer(1000/60, this).start();
        addMouseMotionListener(this);
        addKeyListener(this);
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
        for (Visitor visitor : visitors)
            visitor.setTarget(e.getPoint());
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

    public void mouseDragged(MouseEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
    public void mouseDragged(MouseEvent e) {
        Point2D p2d = e.getPoint();
        camera.translate(p2d.getX()-pressedPoint.getX(), p2d.getY()-pressedPoint.getY());


    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        camera.setMousePoint(e.getPoint());
        if(camera.getZoom() + (-e.getUnitsToScroll()/5.0) > 0)
            camera.setZoom(-(e.getUnitsToScroll())/5.0);
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        pressedPoint.setLocation(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
