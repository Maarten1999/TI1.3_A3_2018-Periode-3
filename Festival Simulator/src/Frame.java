
import tiled.TiledMap;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    private TiledMap tiledMap;

    public static void main(String[] args) {
        new Frame();
    }

    private Frame() {
        super("De beste festival simulator die je ooit zult zien!");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1280, 720));
        setLocationRelativeTo(null);
        setContentPane(new SimulatorTab());

        setVisible(true);
    }
}
