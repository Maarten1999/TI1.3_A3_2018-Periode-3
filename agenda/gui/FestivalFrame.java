package agenda.gui;

import agenda.data.Artist;
import agenda.data.Schedule;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class FestivalFrame extends JFrame implements WindowFocusListener {

    private Schedule schedule;
    private final ScheduleTab scheduleTab;

    public FestivalFrame() {
        //Make test schedule
        super("De beste agenda die je ooit zult zien!");
        this.schedule = new Schedule(9, 2, 2018, "Testname");
        this.schedule.addArtist(new Artist("Boef", 10));
        this.schedule.addArtist(new Artist("Paul Lindelauf", 1));
        this.schedule.addArtist(new Artist("Paul de Mast", 100));

        // Window settings
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1280, 720));
        setResizable(false);
        setLocationRelativeTo(null);
        addWindowFocusListener(this);
//        ImageIcon img = new ImageIcon(getClass().getResource("/icon.png"));
//        setIconImage(img.getImage());

        // Menu bar
        addMenuBar();

        // Tabs
        JTabbedPane tabs = new JTabbedPane();
        scheduleTab = new ScheduleTab(this.schedule);
        tabs.addTab("Schedule", scheduleTab);
        PerformanceTab performanceTab = new PerformanceTab(this.schedule);
        tabs.addTab("Performances", performanceTab);
        tabs.addTab("Artists", new ArtistTab(this.schedule));
        add(tabs);
        tabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                switch (tabs.getSelectedIndex()) {
                    case 0:
                        scheduleTab.refresh();
                        break;
                    case 1:
                        performanceTab.refresh();
                        break;
                }
            }
        });

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

    @Override
    public void windowGainedFocus(WindowEvent e) {
        scheduleTab.refresh();
    }

    @Override
    public void windowLostFocus(WindowEvent e) {}
}
