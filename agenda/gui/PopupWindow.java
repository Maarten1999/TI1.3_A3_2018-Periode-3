package agenda.gui;

import agenda.data.Performance;
import agenda.data.Schedule;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class PopupWindow extends JDialog {

    private Schedule schedule;
    private Performance performance;

    public PopupWindow(Schedule schedule, Performance performance) {
        this.schedule = schedule;
        this.performance = performance;
        setModal(true);
        setSize(new Dimension(360, 640));
        setLocationRelativeTo(null);
        JPanel panel = (JPanel) getContentPane();
//        JSpinner timeSpinner = new JSpinner( new SpinnerDateModel() );
//        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
//        timeSpinner.setEditor(timeEditor);
//        timeSpinner.setValue(new Date()); // will only show the current time
//        panel.add(timeSpinner);
        setVisible(true);
    }
}
