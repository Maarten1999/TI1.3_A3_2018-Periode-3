package agenda.gui;

import agenda.data.Artist;
import agenda.data.Performance;
import agenda.data.Schedule;
import agenda.data.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PopupWindow extends JDialog {

    private Schedule schedule;
    private Performance performance;
    private ArrayList<Artist> tempArtistList;
    private boolean artistSelected;

    public PopupWindow(Schedule schedule, Performance performance) {
        this.schedule = schedule;
        this.performance = performance;
        this.artistSelected = false;
        setTitle(this.performance.getName());
        JPanel mainPanel = initPanel();
        JPanel subPanel = initSubPanelEdit();
        mainPanel.add(subPanel);
        setVisible(true);
    }

    public PopupWindow(Schedule schedule) {
        this.schedule = schedule;
        this.artistSelected = false;
        setTitle("New Performance");
        JPanel mainPanel = initPanel();
        JPanel subPanel = initSubPanelNew();
        mainPanel.add(subPanel);
        setVisible(true);
    }

    private JPanel initPanel() {
        setModal(true);
        setSize(new Dimension(360, 340));
        setLocationRelativeTo(null);
        setResizable(false);
        JPanel mainPanel = (JPanel) getContentPane();
        mainPanel.setLayout(null);
        return mainPanel;

    }


    private JPanel initSubPanelNew() {
        JPanel subPanel = new JPanel();
        subPanel.setBounds(20, 20, getWidth() - 40, getHeight() - 80);
        subPanel.setLayout(new GridLayout(6, 2, 20, 20));

        // Name
        subPanel.add(new JLabel("Name:"));
        JTextField name = new JTextField("");
        subPanel.add(name);

        // Artist
        subPanel.add(new JLabel("Artist:"));
        JButton chooseArtistsButton = new JButton("Select artist(s)");
        chooseArtistsButton.addActionListener(e -> {
            ArtistPopupWindow artistPopupWindow = new ArtistPopupWindow(this.schedule, tempArtistList);
            tempArtistList = artistPopupWindow.getArtistList();
            this.artistSelected = artistPopupWindow.isArtistSelected();
        });
        subPanel.add(chooseArtistsButton);

        // Stage
        subPanel.add(new JLabel("Stage:"));
        JComboBox stageBox = new JComboBox();
        ArrayList<Stage> stages = this.schedule.getStages();
        for (Stage stage : stages) {
            stageBox.addItem(stage);
        }
        subPanel.add(stageBox);

        // Start time
        subPanel.add(new JLabel("Start time:"));
        JSpinner startTime = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(startTime, "HH:mm");
        startTime.setEditor(timeEditor);
        startTime.setValue(new Date());
        subPanel.add(startTime);

        // End time
        subPanel.add(new JLabel("End time:"));
        JSpinner endTime = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditorEnd = new JSpinner.DateEditor(endTime, "HH:mm");
        endTime.setEditor(timeEditorEnd);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, 1);
        endTime.setValue(cal.getTime());
        subPanel.add(endTime);

        // Save button
        JButton save = new JButton("Save");
        save.addActionListener(e -> {
            LocalTime startLocalTime = LocalDateTime.ofInstant(((Date) startTime.getValue()).toInstant(),
                    ZoneId.systemDefault()).toLocalTime();
            LocalTime endLocalTime = LocalDateTime.ofInstant(((Date) endTime.getValue()).toInstant(),
                    ZoneId.systemDefault()).toLocalTime();
            if (!isGoodTime(startLocalTime, endLocalTime)) {
                JOptionPane.showMessageDialog(this,
                        "Start time must be earlier than end time!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!stageAvailable((Stage) stageBox.getSelectedItem(), startLocalTime, endLocalTime)) {
                JOptionPane.showMessageDialog(this,
                        "Stage is not available within the selected timeframe!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!isWithinFestivalTime(startLocalTime, endLocalTime)) {
                JOptionPane.showMessageDialog(this,
                        "The timeframe of the performance is not within the festival timeframe!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if(!artistSelected) {
                JOptionPane.showMessageDialog(this,
                        "There is no artist selected!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if(name.getText().equals("")){
                JOptionPane.showMessageDialog(this,
                        "The performance doesn't have a name!", "Error", JOptionPane.ERROR_MESSAGE);
            }else {
                if (tempArtistList != null)
                    for (Artist a : tempArtistList) {
                        if (!artistAvailable(a, startLocalTime, endLocalTime)) {
                            JOptionPane.showMessageDialog(this, "Artist is not available within the selected timeframe!", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

            this.performance = new Performance();
            this.performance.setName(name.getText());
            this.performance.setArtists(tempArtistList);
            this.performance.setStage((Stage) stageBox.getSelectedItem());
            this.performance.setStartTime(startLocalTime);
            this.performance.setEndTime(endLocalTime);
            this.schedule.addPerformace(this.performance);
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
        });
        subPanel.add(save);
        return subPanel;
    }


    private JPanel initSubPanelEdit() {
        JPanel subPanel = new JPanel();
        subPanel.setBounds(20, 20, getWidth() - 40, getHeight() - 80);
        subPanel.setLayout(new GridLayout(6, 2, 20, 20));

        // Name
        subPanel.add(new JLabel("Name:"));
        JTextField name = new JTextField(this.performance.getName());
        subPanel.add(name);


//        // Artist old
//        subPanel.add(new JLabel("Artist:"));
//        JComboBox artistBox = new JComboBox();
//        ArrayList<Artist> artists = this.schedule.getArtists();
//        for (Artist artist : artists) {
//            artistBox.addItem(artist);
//        }
//        artistBox.setSelectedItem(this.performance.getArtist());
//        subPanel.add(artistBox);

        // Artist new
        subPanel.add(new JLabel("Artist:"));
        JButton chooseArtistsButton = new JButton("Select artist(s)");
        tempArtistList = performance.getArtists();
        chooseArtistsButton.addActionListener(e -> {
            ArtistPopupWindow artistPopupWindow = new ArtistPopupWindow(this.schedule, tempArtistList);
            tempArtistList = artistPopupWindow.getArtistList();
        });
        subPanel.add(chooseArtistsButton);

        // Stage
        subPanel.add(new JLabel("Stage:"));
        JComboBox stageBox = new JComboBox();
        ArrayList<Stage> stages = this.schedule.getStages();
        for (Stage stage : stages) {
            stageBox.addItem(stage);
        }
        stageBox.setSelectedItem(this.performance.getStage());
        subPanel.add(stageBox);

        // Start time
        subPanel.add(new JLabel("Start time:"));
        JSpinner startTime = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(startTime, "HH:mm");
        startTime.setEditor(timeEditor);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        LocalTime ltStart = this.performance.getStartTime();
        try {
            startTime.setValue(format.parseObject(ltStart.toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        subPanel.add(startTime);

        // End time
        subPanel.add(new JLabel("End time:"));
        JSpinner endTime = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditorEnd = new JSpinner.DateEditor(endTime, "HH:mm");
        endTime.setEditor(timeEditorEnd);
        LocalTime ltEnd = this.performance.getEndTime();
        try {
            endTime.setValue(format.parseObject(ltEnd.toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        subPanel.add(endTime);

        // Save button
        JButton save = new JButton("Save");
        save.addActionListener(e -> {
            LocalTime startLocalTime = LocalDateTime.ofInstant(((Date) startTime.getValue()).toInstant(),
                    ZoneId.systemDefault()).toLocalTime();
            LocalTime endLocalTime = LocalDateTime.ofInstant(((Date) endTime.getValue()).toInstant(),
                    ZoneId.systemDefault()).toLocalTime();

            if (!isGoodTime(startLocalTime, endLocalTime)) {
                JOptionPane.showMessageDialog(this,
                        "Start time must be earlier than end time!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!stageAvailable((Stage) stageBox.getSelectedItem(), startLocalTime, endLocalTime)) {
                JOptionPane.showMessageDialog(this,
                        "Stage is not available within the selected timeframe!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!isWithinFestivalTime(startLocalTime, endLocalTime)) {
                JOptionPane.showMessageDialog(this,
                        "The timeframe of the performance is not within the festival timeframe!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if(tempArtistList.size() == 0){
                JOptionPane.showMessageDialog(this,
                        "There is no artist selected!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if(name.getText().equals("")){
                JOptionPane.showMessageDialog(this,
                        "The performance doesn't have a name!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
            if (tempArtistList != null)
                for (Artist a : tempArtistList) {
                    if (!artistAvailable(a, startLocalTime, endLocalTime)) {
                        JOptionPane.showMessageDialog(this, "Artist is not available within the selected timeframe!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                this.performance.setName(name.getText());
                this.performance.setArtists(tempArtistList);
                this.performance.setStage((Stage) stageBox.getSelectedItem());
                this.performance.setStartTime(startLocalTime);
                this.performance.setEndTime(endLocalTime);
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
        });
        subPanel.add(save);

        // Delete button
        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> {
            this.schedule.getPerformances().remove(this.performance);
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });
        subPanel.add(delete);
        return subPanel;
    }

    private boolean isGoodTime(LocalTime t1, LocalTime t2) {
        return !(t1.isAfter(t2) || t1.equals(t2));
    }

    private boolean artistAvailable(Artist artist, LocalTime start, LocalTime end) {
        boolean returnValue = true;
        for (Performance performance1 : this.schedule.getPerformances()) {
            if (performance1.getArtists().contains(artist) && timeOverlaps(start, end,
                    performance1.getStartTime(), performance1.getEndTime())
                    && performance1 != this.performance) {
                returnValue = false;
                break;
            }
        }
        return returnValue;
    }

    private boolean stageAvailable(Stage stage, LocalTime start, LocalTime end) {
        boolean returnValue = true;
        for (Performance performance1 : this.schedule.getPerformances()) {
            if (stage  == performance1.getStage() && timeOverlaps(start, end,
                    performance1.getStartTime(), performance1.getEndTime())
                    && performance1 != this.performance) {
                returnValue = false;
                break;
            }
        }
        return returnValue;
    }

    private boolean timeOverlaps(LocalTime s1, LocalTime e1, LocalTime s2, LocalTime e2) {
        return s1.isBefore(e2) && s2.isBefore(e1) && !s1.equals(e2) && !s2.equals(e1);
    }

    private boolean isWithinFestivalTime(LocalTime s, LocalTime e) {
        return s.isAfter(LocalTime.of(ScheduleTab.START_HOUR,
                0)) || s.equals(LocalTime.of(ScheduleTab.START_HOUR, 0))
                && e.isBefore(LocalTime.of(ScheduleTab.END_HOUR, 59)) ||
                e.equals(LocalTime.of(ScheduleTab.END_HOUR, 59));
    }
}
