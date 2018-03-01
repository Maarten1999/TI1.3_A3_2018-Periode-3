

import tiled.TiledMap;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;

public class SimulatorTab extends JPanel {

    private TiledMap map;

    public SimulatorTab() {
        try {
            File f = new File(getClass().getResource("maps/testMap.json").toURI());

            System.out.println(f.getPath());
        } catch (URISyntaxException e) {
            System.out.println(e.getInput());
            e.printStackTrace();
        }
        map = new TiledMap(getClass().getResourceAsStream("maps/testMap.json"));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        this.map.draw(g2d);
    }
}
