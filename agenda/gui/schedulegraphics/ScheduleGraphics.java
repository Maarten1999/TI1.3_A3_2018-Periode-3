package agenda.gui.schedulegraphics;

import agenda.data.Performance;
import agenda.data.Schedule;
import agenda.data.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

public class ScheduleGraphics extends JPanel{

    private final Color stageColor1 = new Color(77, 155, 183, 127);
    private final Color stageColor2 = new Color(255, 192, 138, 127);
    private final Color timeColor1 = new Color(130, 130, 130, 127);
    private final Color timeColor2 = new Color(190, 190, 190, 127);

    private ArrayList<Stage> stageList;
    private ArrayList<SchedulePerformances> performanceList;

    private int topBorderSpacing = 20;
    private int leftBorderSpacing = 128;
    private int stageSize = 48;
    private int timeSize = 64;

    private Schedule _schedule;

    public ScheduleGraphics(Schedule schedule){
        setBackground(Color.lightGray);
        _schedule = schedule;
        stageList = new ArrayList<>();
        performanceList = new ArrayList<>();

        this.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {

            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {
                if(_schedule != null)
                    loadPerformances();
            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
    }

    public void loadPerformances(){
        stageList.clear();
        performanceList.clear();

        stageList = _schedule.getStages();
        for(Performance p: _schedule.getPerformances()) {
            performanceList.add(new SchedulePerformances(this, p));
        }
    }

    public void setTopBorderSpacing(int spacing){
        topBorderSpacing = spacing;
    }

    public void setLeftBorderSpacing(int spacing){
        leftBorderSpacing = spacing;
    }

    public int getTopBorderSpacing(){
        return topBorderSpacing;
    }

    public int getLeftBorderSpacing(){
        return leftBorderSpacing;
    }

    public int getStageSize(){
        return stageSize;
    }

    public int getTimeSize(){
        return timeSize;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;


        if(_schedule != null)
            drawBackgroundStages(g2d);

        drawBackgroundTime(g2d);

        if(_schedule != null)
            drawPerformances(g2d);
    }

    private void drawBackgroundStages(Graphics2D g2d){
        //color boolean which alternates between the 2 colors
        boolean stageColor = false;

        for(int i = 0; i < stageList.size(); i++){
            Stage s = stageList.get(i);
            int y = (i * stageSize) + topBorderSpacing;

            g2d.setColor(stageColor? stageColor1 : stageColor2);
            g2d.fillRect(0, y, this.getWidth(), stageSize);
            g2d.setColor(Color.black);
            g2d.drawString(s.getName(), 0, (i*stageSize) + (stageSize / 2) + topBorderSpacing);
            stageColor = !stageColor;
        }
    }

    private void drawBackgroundTime(Graphics2D g2d){
        boolean timeColer = false;
        timeSize = (int)((this.getSize().getWidth() - leftBorderSpacing) / 24);
        int offset = ((timeSize * 24) + leftBorderSpacing) - this.getWidth();
        for(int i = 0; i < 24; i++){
            g2d.setColor(timeColer?timeColor1:timeColor2);
            int x = leftBorderSpacing + (i * timeSize);
            g2d.fillRect(x, 0, timeSize, this.getHeight());
            g2d.setColor(Color.black);
            g2d.drawString(i + "", x + 3, topBorderSpacing / 2);
            timeColer = !timeColer;
        }
    }

    private void drawPerformances(Graphics2D g2d){
        for(int i = 0; i <  stageList.size(); i++){
            Stage s = stageList.get(i);
            for(int sp = 0; sp < performanceList.size(); sp++){
                SchedulePerformances sps = performanceList.get(sp);
                boolean shit = sps.hasSameStage(s);
                if(shit)
                    sps.DrawSchedule(g2d, i);
            }
        }
    }
}
