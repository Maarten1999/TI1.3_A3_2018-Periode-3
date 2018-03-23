package agenda.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

import static javax.swing.GroupLayout.Alignment.CENTER;

class ToolPanel extends JPanel {

    private SimulatorPanel simulatorPanel;


    ToolPanel(SimulatorPanel simulatorPanel) {
        GridBagConstraints c = new GridBagConstraints();
        JButton togglePause = new JButton("Pause");
        togglePause.addActionListener(e ->{
            if(togglePause.getText().equals("Pause"))
                togglePause.setText("Play");
            else
                togglePause.setText("Pause");
        });
        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(e ->{
            if(stopButton.getText().equals("Stop"))
                stopButton.setText("Start");
            else
                stopButton.setText("Stop");
        });
        JLabel timeLabel = new JLabel(ScheduleTab.START_HOUR + ":00", JLabel.CENTER);
        JSlider slider = new JSlider(ScheduleTab.START_HOUR * 2, ScheduleTab.END_HOUR * 2, ScheduleTab.START_HOUR * 2);
        slider.addChangeListener(e -> timeLabel.setText(calculateTime(slider.getValue())));
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(2);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        timeLabel.setPreferredSize(new Dimension(35, 30));
        slider.setPreferredSize(new Dimension(280,60));
        Hashtable labels = new Hashtable();
        for (int i = ScheduleTab.START_HOUR; i <= ScheduleTab.END_HOUR; i++)
            labels.put(i * 2, new JLabel(i + ""));
        slider.setLabelTable(labels);
        this.simulatorPanel = simulatorPanel;


        Box mainBox = Box.createVerticalBox();
        Box box1 = Box.createHorizontalBox();

        box1.add(togglePause);
        box1.add(Box.createRigidArea(new Dimension(50,0)));
        box1.add(stopButton);
        mainBox.add(box1);

        mainBox.add(Box.createRigidArea(new Dimension(0,20)));
        Box box2 = Box.createHorizontalBox();

        box2.add(slider);
        box2.add(timeLabel);
        mainBox.add(box2);
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
