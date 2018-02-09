package agenda.gui;

import javax.swing.*;
import java.awt.*;

public class FestivalFrame extends JFrame {

    public FestivalFrame() {
        // Window settings
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1280, 720));
        setResizable(false);
        setLocationRelativeTo(null);

        // Menu bar
        addMenuBar();

        // Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Schedule", new ScheduleTab());
        tabs.addTab("Performances", new PerformanceTab());
        tabs.addTab("Artists", new ArtistTab());
        add(tabs);

        setVisible(true);
    }

    private void addMenuBar() {
        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("File");
        file.add("Open festival");
        file.add("Save festival");
        menu.add(file);

        JMenu about = new JMenu("About");
        about.add("About this version");
        menu.add(about);
        setJMenuBar(menu);
    }
}
