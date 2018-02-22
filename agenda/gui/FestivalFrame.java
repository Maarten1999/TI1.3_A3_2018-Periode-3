package agenda.gui;

import agenda.data.Artist;
import agenda.data.Schedule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.*;

public class FestivalFrame extends JFrame implements WindowFocusListener {

    private PerformanceTab performanceTab;
    private Schedule schedule;
    private ArtistTab artistTab;
    private ScheduleTab scheduleTab;
    private JFileChooser fileChooser;

    public FestivalFrame() {
        //Make test schedule
        super("De beste agenda die je ooit zult zien!");
        this.schedule = new Schedule();
//        this.schedule.addArtist(new Artist("Boef", 10));
//        this.schedule.addArtist(new Artist("Paul Lindelauf", 1));
//        this.schedule.addArtist(new Artist("Paul de Mast", 100));

        // Window settings
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1280, 720));
        setResizable(false);
        setLocationRelativeTo(null);
        addWindowFocusListener(this);

        // Menu bar
        addMenuBar();

        // File chooser
        addFileChooser();

        // Tabs
        JTabbedPane tabs = new JTabbedPane();
        this.scheduleTab = new ScheduleTab(this);
        tabs.addTab("Schedule", this.scheduleTab);
        this.performanceTab = new PerformanceTab(this);
        tabs.addTab("Performances", this.performanceTab);
        this.artistTab = new ArtistTab(this);
        tabs.addTab("Artists", this.artistTab);
        add(tabs);
        tabs.addChangeListener(e -> {
            switch (tabs.getSelectedIndex()) {
                case 0:
                    this.scheduleTab.refresh();
                    break;
                case 1:
                    this.performanceTab.refresh();
                    break;
            }
        });

        setVisible(true);
    }

    private void addFileChooser() {
        this.fileChooser = new JFileChooser();
    }

    private void addMenuBar() {
        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("File");

        // New
        JMenuItem newButton = new JMenuItem("New festival");
        file.add(newButton);
        newButton.addActionListener(e -> {
            this.schedule = new Schedule();
            this.scheduleTab.refresh();
            this.artistTab.refresh();
            this.performanceTab.refresh();
        });

        // Open
        JMenuItem open = new JMenuItem("Open festival");
        file.add(open);
        open.addActionListener(e -> {
            System.out.println("Open");

            File workingDirectory = new File(System.getProperty("user.dir"));
            this.fileChooser.setCurrentDirectory(workingDirectory);
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                System.out.println("Opening: " + file.getName() + ".");
                try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(selectedFile))) {
                    this.schedule = (Schedule) inputStream.readObject();
                    this.scheduleTab.refresh();
                    this.artistTab.refresh();
                    this.performanceTab.refresh();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        // Save
        JMenuItem save = new JMenuItem("Save festival");
        file.add(save);
        save.addActionListener(e -> {
            System.out.println("Saved");

            File workingDirectory = new File(System.getProperty("user.dir"));
            this.fileChooser.setCurrentDirectory(workingDirectory);
            int returnVal = fileChooser.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File selectedFile1 = fileChooser.getSelectedFile();
                try(ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(selectedFile1))){
                    outputStream.writeObject(schedule);
                }
                catch(Exception exception){exception.printStackTrace();}
            }
        });
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

    public Schedule getSchedule() {
        return schedule;
    }
}
