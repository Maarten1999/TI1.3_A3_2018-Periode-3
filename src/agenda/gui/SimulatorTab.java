package agenda.gui;

import simulator.Train;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class SimulatorTab extends JPanel {

    private SimulatorPanel simulatorPanel;
    private ToolPanel toolPanel;

    SimulatorTab() {
        setLayout(new BorderLayout());
        simulatorPanel = new SimulatorPanel(1280, 720);
        toolPanel = new ToolPanel(simulatorPanel);
        add(simulatorPanel, BorderLayout.CENTER);
        add(toolPanel, BorderLayout.EAST);
    }

    public void keyPressed(KeyEvent e) {
        simulatorPanel.keyPressed(e);
    }
}
