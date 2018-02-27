package agenda.gui.schedulegraphics;

import agenda.data.Performance;
import agenda.data.Stage;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.Period;

public class SchedulePerformances {

    private static Color normalColor = new Color(255, 192, 138, 255);
    private static Color selectedColor = new Color(23, 192, 138, 255);
    private static Color deselectedColor = new Color(208, 53, 53, 255);

    private ScheduleGraphics parent;
    private Performance performance;
    private int performancePosition;
    private int performanceSize;
    private LocalDateTime timedif;
    private boolean selected;

    public SchedulePerformances(ScheduleGraphics parent, Performance performance){
        this.parent = parent;
        this.performance = performance;

        timedif = LocalDateTime.of(0, 1, 1, 0, 0, 0);
        timedif = timedif.plusHours(performance.getEndTime().getHour() - performance.getStartTime().getHour());
        timedif = timedif.plusMinutes(performance.getEndTime().getMinute() - performance.getStartTime().getMinute());

        this.performanceSize = timedif.getHour() * parent.getTimeSize();
        this.performancePosition = performance.getStartTime().getHour() *  parent.getTimeSize();
        this.performanceSize += (timedif.getSecond() * (parent.getTimeSize() / 60));
        this.performancePosition += (performance.getStartTime().getSecond() * (parent.getTimeSize() / 60));
        System.out.println(performanceSize);
        System.out.println(performancePosition);
    }

    public boolean hasSameStage(Stage s){
        return performance.getStage().equals(s);
    }

    public void DrawSchedule(Graphics2D g2d, int lane){
        int y = parent.getTopBorderSpacing() + (lane * parent.getStageSize());

        int ss = parent.getStageSize();

        g2d.setColor(selected?selectedColor:deselectedColor);
        g2d.fillRect(performancePosition + parent.getLeftBorderSpacing(), y + 1, performanceSize, parent.getStageSize() - 2);
        g2d.setColor(normalColor);
        g2d.fillRect(performancePosition + parent.getLeftBorderSpacing() + 1, y + 2, performanceSize - 2, parent.getStageSize() - 4);
    }
}
