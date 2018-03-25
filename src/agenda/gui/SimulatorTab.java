package agenda.gui;


import simulator.Physics.PhysicsWorld;
import simulator.Visitors.StateManager;
import simulator.Visitors.Visitor;
import simulator.Visitors.VisitorManager;
import simulator.map.TargetManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class SimulatorTab extends JPanel implements ActionListener {

    private Timer timer;
    private StateManager stateManager;
    private VisitorManager visitorManager;
    private SimulatorPanel simulatorPanel;
    private ToolPanel toolPanel;
    private int amountOfVisitors = 0;
    private long beginTime = 0;
    private long ellapsedTime;
    private long lastTime;
    private int timeValue = 0;

    SimulatorTab() {
        setLayout(new BorderLayout());
        stateManager = new StateManager();
        visitorManager = new VisitorManager();
        simulatorPanel = new SimulatorPanel(this, visitorManager);
        toolPanel = new ToolPanel(this);
        add(simulatorPanel, BorderLayout.CENTER);
        add(toolPanel, BorderLayout.EAST);
        initTimer();
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

    double i = 0;//test

    @Override
    public void actionPerformed(ActionEvent e) {
        long currentTime = System.nanoTime();
        long deltaTime = currentTime - lastTime;
        ellapsedTime += deltaTime;
        checkForTimeSlotChange();

        float deltaTimeFloat = (float) ((double) deltaTime / 1000000000.0);
        lastTime = currentTime;

        if (visitorManager.getVisitors().size() < amountOfVisitors) {// && i > 1) {
            Visitor v = visitorManager.createVisitor(TargetManager.instance().getTarget("Entrance1"));
//            v.onPlacement(new Point(21 * 32, 1 * 32));
            i -= 1;
        }

        i += 0.3;

        if (deltaTimeFloat > 1)
            deltaTimeFloat = 1;

        this.visitorManager.update(deltaTimeFloat);

//        if (this.train != null) {
//            this.train.update(this.visitorManager);
//            if (this.train.isFinished())
//                this.train = null;
//        }
//        if (this.amountOfVisitors > this.visitorManager.getVisitors().size()) {
//            this.visitorManager.addVisitor();
//        }
        PhysicsWorld.getInstance().update(deltaTimeFloat);
        TargetManager.instance().update(deltaTimeFloat);
        simulatorPanel.repaint();
    }

    private void checkForTimeSlotChange() {
        int newTimeSlot = calculateTimeSlot();
        if (newTimeSlot > timeValue) {
            System.out.println("changed timeslot from " + timeValue + " to " + newTimeSlot);
            timeValue = newTimeSlot;
            stateManager.addState(timeValue, visitorManager.getVisitors());
            toolPanel.setSlider(newTimeSlot);
        }
    }

    private int calculateTimeSlot() {
        return (int) (ellapsedTime / 1e9 / 6) + ScheduleTab.START_HOUR * 2;
    }

    private void calculateEllapsedTime(int timeSlot) {
        ellapsedTime = (int) ((timeSlot - ScheduleTab.START_HOUR * 2) * 1e9 * 6);
    }

    public void keyPressed(KeyEvent e) {
        simulatorPanel.keyPressed(e);
    }

    public SimulatorPanel getSimulatorPanel() {
        return simulatorPanel;
    }

    public void clear() {
        visitorManager.clear();
        stateManager.clear();
        runTimer(false);
        repaint();
    }

    public void init(int amountOfVisitors) {
        this.amountOfVisitors = amountOfVisitors;
        this.ellapsedTime = lastTime = timeValue = 0;
        runTimer(true);
    }

    public void setToTime(int value) {
        visitorManager.clear();
        ArrayList<Visitor> visitors = new ArrayList<>(stateManager.getState(value));
        for (Visitor visitor : visitors)
            visitorManager.createVisitor(visitor);
        calculateEllapsedTime(value);
        lastTime = System.nanoTime();
        timeValue = value;
    }
}
