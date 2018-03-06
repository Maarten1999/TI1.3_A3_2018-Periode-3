import tiled.TiledMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SimulatorTab extends JPanel implements ActionListener, MouseMotionListener {

    private VisitorManager visitorManager;
    private TiledMap map;

    SimulatorTab() {
        map = new TiledMap(getClass().getResourceAsStream("maps/test2.json"));

        this.visitorManager = new VisitorManager(this.map);
        new Timer(1000/60, this).start();

        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
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
    public void mouseDragged(MouseEvent e) {}
}
