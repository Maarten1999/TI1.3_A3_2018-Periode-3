

import tiled.TiledMap;

import javax.swing.*;
import java.io.File;
import java.net.URISyntaxException;

public class SimulatorTab extends JPanel {

    public SimulatorTab() {
        try {
            File f = new File(getClass().getResource("maps/testMap.json").toURI());

            System.out.println(f.getPath());
        } catch (URISyntaxException e) {
            System.out.println(e.getInput());
            e.printStackTrace();
        }

        TiledMap map = new TiledMap(getClass().getResourceAsStream("maps/testMap.json"));

    }
}
