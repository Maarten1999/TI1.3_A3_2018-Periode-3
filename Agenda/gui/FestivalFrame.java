package agenda.gui;

import javax.swing.*;
import java.awt.*;

public class FestivalFrame extends JFrame {

    public FestivalFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1280, 720));
        setResizable(false);
        setLocationRelativeTo(null);

        JMenuBar menu = new JMenuBar();
        menu.add(new JMenu("Menu1"));
        setJMenuBar(menu);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("test1", new JTextArea("hallo1"));
        tabs.addTab("test2", new JTextArea("hallo2"));
        tabs.addTab("test3", new JTextArea("hallo3"));
        add(tabs);

        setVisible(true);
    }
}
