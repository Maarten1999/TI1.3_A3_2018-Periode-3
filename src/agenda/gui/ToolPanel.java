package agenda.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

class ToolPanel extends JPanel {

    private SimulatorPanel simulatorPanel;


    ToolPanel(SimulatorPanel simulatorPanel) {
        this.simulatorPanel = simulatorPanel;


        JLabel timeLabel = new JLabel(ScheduleTab.START_HOUR + ":00", JLabel.CENTER);
        JLabel visitorLabel = new JLabel("0", JLabel.CENTER);

        JSlider visitorSlider = new JSlider(0, 500, 0);
        JSlider timeSlider = new JSlider(ScheduleTab.START_HOUR * 2, ScheduleTab.END_HOUR * 2, ScheduleTab.START_HOUR * 2);

        GridBagConstraints c = new GridBagConstraints();
        JButton togglePause = new JButton("Pause");
        togglePause.setEnabled(false);
        togglePause.addActionListener(e -> {
            if (togglePause.getText().equals("Pause"))
                togglePause.setText("Play");
            else
                togglePause.setText("Pause");
        });
        JButton stopButton = new JButton("Start");
        stopButton.addActionListener(e -> {
            if (stopButton.getText().equals("Stop")) {
                stopButton.setText("Start");
                visitorSlider.setEnabled(true);
                togglePause.setEnabled(false);
            } else {
                stopButton.setText("Stop");
                visitorSlider.setEnabled(false);
                togglePause.setEnabled(true);
            }
        });


        timeSlider.addChangeListener(e -> timeLabel.setText(calculateTime(timeSlider.getValue())));
        timeSlider.setMinorTickSpacing(1);
        timeSlider.setMajorTickSpacing(2);
        timeSlider.setPaintTicks(true);
        timeSlider.setPaintLabels(true);
        timeLabel.setPreferredSize(new Dimension(35, 30));
        timeSlider.setPreferredSize(new Dimension(280, 60));
        Hashtable labels = new Hashtable();
        for (int i = ScheduleTab.START_HOUR; i <= ScheduleTab.END_HOUR; i++)
            labels.put(i * 2, new JLabel(i + ""));
        timeSlider.setLabelTable(labels);

        visitorSlider.addChangeListener(e -> visitorLabel.setText(Integer.toString(visitorSlider.getValue())));
        visitorSlider.setMinorTickSpacing(10);
        visitorSlider.setMajorTickSpacing(100);
        visitorSlider.setSnapToTicks(true);
        visitorSlider.setPaintTicks(true);
        visitorSlider.setPaintLabels(true);


        JLabel timeSliderLabel = new JLabel("tijd: ");
        JLabel visitorSliderLabel = new JLabel("aantal bezoekers: ");


        Box mainBox = Box.createVerticalBox();

        mainBox.add(Box.createRigidArea(new Dimension(0, 20)));

        Box box1 = Box.createHorizontalBox();
        box1.add(togglePause);
        box1.add(Box.createRigidArea(new Dimension(50, 0)));
        box1.add(stopButton);
        mainBox.add(box1);

        mainBox.add(Box.createRigidArea(new Dimension(0, 20)));
        Box box2 = Box.createHorizontalBox();

        box2.add(timeSliderLabel);
        box2.add(timeSlider);
        box2.add(timeLabel);
        mainBox.add(box2);

        Box box3 = Box.createHorizontalBox();

        box3.add(visitorSliderLabel);
        box3.add(visitorSlider);
        box3.add(visitorLabel);
        mainBox.add(box3);


        timeSliderLabel.setPreferredSize(visitorSliderLabel.getPreferredSize());
        visitorLabel.setPreferredSize(timeLabel.getPreferredSize());

        add(mainBox);


    }

    private String calculateTime(int sliderValue) {
        String output;
        if (sliderValue % 2 == 0)
            output = sliderValue / 2 + ":00";
        else
            output = sliderValue / 2 + ":30";
        return output;
    }
}
