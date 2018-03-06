import tiled.TiledMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class SimulatorTab extends JPanel implements ActionListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private VisitorManager visitorManager;
    private TiledMap map;
    private Camera camera;
    private  Point2D  pressedPoint = new Point2D.Double(0,0);

    SimulatorTab() {
        map = new TiledMap(getClass().getResourceAsStream("maps/test2.json"));
        camera = new Camera();
        this.visitorManager = new VisitorManager(this.map);
        new Timer(1000/60, this).start();
        addMouseListener(this);
        addMouseWheelListener(this);
        addMouseMotionListener(this);
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
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        ArrayList<Visitor> visitors = this.visitorManager.getVisitors();
        for (Visitor visitor : visitors)
            visitor.setTarget(e.getPoint());
    }

    @Override
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
