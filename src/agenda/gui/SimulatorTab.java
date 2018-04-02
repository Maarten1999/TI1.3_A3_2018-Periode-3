package agenda.gui;

import agenda.data.Performance;
import agenda.data.Schedule;
import simulator.Physics.PhysicsWorld;
import simulator.Visitors.Visitor;
import simulator.Visitors.VisitorManager;
import simulator.Visitors.VisitorStateManager;
import simulator.map.TargetManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalTime;
import java.util.ArrayList;

public class SimulatorTab extends JPanel implements ActionListener, KeyListener {

    private Schedule schedule;
    private Timer timer;
    private VisitorManager visitorManager;
    private SimulatorPanel simulatorPanel;
    private ToolPanel toolPanel;
    private int amountOfVisitors = 0;
    private long beginTime = 0;
    private long ellapsedTime;
    private long lastTime;
    private int timeValue = 0;
    private boolean loadState;

    SimulatorTab(Schedule schedule) {
        this.schedule = schedule;
        setLayout(new BorderLayout());
        visitorManager = new VisitorManager();
        simulatorPanel = new SimulatorPanel(this, visitorManager, this.schedule);
        toolPanel = new ToolPanel(this);
        add(simulatorPanel, BorderLayout.CENTER);
        add(toolPanel, BorderLayout.EAST);
        addKeyListener(this);
        initTimer();
    }

    public void setSchedule(Schedule schedule){
        this.schedule = schedule;
        simulatorPanel.setSchedule(schedule);
    }

    public void initTimer() {
        timer = new Timer(1, this);
        lastTime = System.nanoTime();
    }

    public void runTimer(boolean run) {
        if (run) {
            timer.start();
            lastTime = System.nanoTime();
        } else
            timer.stop();
    }

    float time = 0;

    @Override
    public void actionPerformed(ActionEvent e) {
        long currentTime = System.nanoTime();
        long deltaTime = currentTime - lastTime;
        ellapsedTime += deltaTime;
        checkForTimeSlotChange();

        float deltaTimeFloat = (float) ((double) deltaTime / 1000000000.0);
        lastTime = currentTime;

        if (deltaTimeFloat > 1)
            deltaTimeFloat = 1;

        time += 0.2 * deltaTimeFloat;

        this.visitorManager.update(deltaTimeFloat);

        PhysicsWorld.getInstance().update(deltaTimeFloat);
        TargetManager.instance().update(deltaTimeFloat);
        simulatorPanel.repaint();
    }

    private void checkForTimeSlotChange() {
        int newTimeSlot = calculateTimeSlot();

        //checkForPerformances(timeValue);

        if (newTimeSlot > timeValue) {
            System.out.println(getTimeSlot());
            System.out.println("changed timeslot from " + timeValue + " to " + newTimeSlot);
            timeValue = newTimeSlot;
            VisitorStateManager.getInstance().saveState(timeValue, visitorManager.getVisitorList());
            loadState = false;
            toolPanel.setSlider(newTimeSlot);
        }
    }

    String check = "none";

    private void checkForPerformances(int timeslot){
        LocalTime lt = LocalTime.of((timeslot * 30) / 60, (timeslot * 30) % 60, 0, 0);
        ArrayList<Performance> performances = new ArrayList<>();

        int popularity = 0;
        int participants = 0;
        String check = "";

        for(Performance p: schedule.getPerformances())
        {
            if(p.getStartTime().isBefore(lt.plusMinutes(1)) && p.getEndTime().isAfter(lt.minusMinutes(5))) {
                performances.add(p);
                popularity += p.getPopularity();
                participants++;
                check += p.getName();
            }
        }

        ArrayList<Visitor> visitors = visitorManager.getVisitorList();

        if(!this.check.equals(check)){
            this.check = check;

            int done = 0;

            if(performances.size() > 0) {
                Performance lastp = null;
                for (Performance p : performances) {
                    float numOfVisitors = ((float) p.getPopularity() / (float) popularity);

                    check += " " + p.getName() + " " + numOfVisitors + "%";

                    int vis = (int) ((float) amountOfVisitors * numOfVisitors);

                    //System.out.println(" " + p.getName() + " " + numOfVisitors + "%" + " " + vis);

                    for (int i = done; i < vis + done; i++) {
                        visitors.get(i).setTarget(p.getStage().getName());
                    }
                    lastp = p;
                    done = vis;
                }

                if(lastp != null)
                    for(int i = done; i < amountOfVisitors; i++) {
                        visitors.get(i).setTarget(lastp.getStage().getName());
                    }
            }
            else{
                for(Visitor v: visitors){
                    v.setTarget("Gate");
                    v.stop();
                }
            }


        }

    }

    private int calculateTimeSlot() {
        return (int) (ellapsedTime / 1e9 / 3) + ScheduleTab.START_HOUR * 2;
    }

    private void calculateEllapsedTime(int timeSlot) {
        ellapsedTime = (int) ((timeSlot - ScheduleTab.START_HOUR * 2) * 1e9 * 3);
    }

    private int getTimeSlot(){
        return (int)time + (ScheduleTab.START_HOUR * 2);
    }

    private float getTimeSlotF(){
        return time + (ScheduleTab.START_HOUR * 2);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        simulatorPanel.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public SimulatorPanel getSimulatorPanel() {
        return simulatorPanel;
    }

    public void clear() {
        check = "none";
        visitorManager.Clear();
        TargetManager.instance().clear();
        VisitorStateManager.getInstance().clear();
        runTimer(false);
        repaint();
    }

    public void init(int amountOfVisitors) {
        this.amountOfVisitors = amountOfVisitors;
        this.ellapsedTime = lastTime = timeValue = 0;

        for(int i = 0; i < amountOfVisitors; i++){
            visitorManager.createVisitor();
        }

        runTimer(true);
    }

    public void setToTime(int value) {
        //System.out.println(value);
        if(loadState) {
            TargetManager.instance().clear();
            VisitorStateManager.getInstance().loadState(value);
        }
        calculateEllapsedTime(value);
        lastTime = System.nanoTime();
        timeValue = value;
        loadState = true;
    }
}