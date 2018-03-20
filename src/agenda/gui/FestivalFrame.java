package agenda.gui;

import agenda.data.Schedule;
import agenda.data.Stage;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.*;

public class FestivalFrame extends JFrame implements WindowFocusListener {

    private Schedule schedule;
    private ScheduleTab scheduleTab;
    private PerformanceTab performanceTab;
    private ArtistTab artistTab;
    private SimulatorTab simulatorTab;
    private JFileChooser fileChooser;

    public FestivalFrame() {
        //Make test schedule
        this.schedule = new Schedule();
        addMockStages();
        setTitle(this.schedule.getName());

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
        this.scheduleTab = new ScheduleTab(schedule);
        tabs.addTab("Schedule", this.scheduleTab);
        this.performanceTab = new PerformanceTab(schedule);
        tabs.addTab("Performances", this.performanceTab);
        this.artistTab = new ArtistTab(schedule);
        tabs.addTab("Artists", this.artistTab);
        this.simulatorTab = new SimulatorTab(1280, 720);
        tabs.addTab("Simulator", this.simulatorTab);
        add(tabs);
        tabs.addChangeListener(e -> {
            switch (tabs.getSelectedIndex()) {
                case 0:
                    this.scheduleTab.refresh();
                    break;
                case 1:
                    this.performanceTab.refresh();
                    break;
                case 3:
                    this.simulatorTab.requestFocusInWindow();
                    break;
            }
        });

        setVisible(true);
    }

    private void addFileChooser() {
        this.fileChooser = new JFileChooser();
        FileNameExtensionFilter ftvExtensionFilter = new FileNameExtensionFilter("ftv file", "ftv");
        fileChooser.setFileFilter(ftvExtensionFilter);
        fileChooser.setAcceptAllFileFilterUsed(false);
    }


    private void addMockStages(){
        for (int i = 0; i < 6; i++) {
            this.schedule.addStage(new Stage("Stage " + (i + 1), 500));
        }
    }

    private void addMenuBar() {
        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("File");

        // New
        JMenuItem newButton = new JMenuItem("New festival");
        file.add(newButton);
        newButton.addActionListener(e -> {
            this.schedule.empty();
            addMockStages();
            this.scheduleTab.refresh();
            this.artistTab.refresh();
            this.performanceTab.refresh();
        });

        // Open
        JMenuItem open = new JMenuItem("Open festival");
        file.add(open);
        open.addActionListener(e -> {

            File workingDirectory = new File(System.getProperty("user.dir"));
            this.fileChooser.setCurrentDirectory(workingDirectory);
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(selectedFile))) {
                    this.schedule.load( (Schedule) inputStream.readObject());
                    setTitle(selectedFile.getName());
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
            File workingDirectory = new File(System.getProperty("user.dir"));
            this.fileChooser.setCurrentDirectory(workingDirectory);
            int returnVal = fileChooser.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File selectedFile1 = fileChooser.getSelectedFile();
                if(!selectedFile1.getAbsolutePath().endsWith(".ftv"))
                    selectedFile1 = new File(selectedFile1.getName()+".ftv");
                schedule.setName(selectedFile1.getName());
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
}
