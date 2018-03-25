package simulator.Visitors;

import java.util.ArrayList;
import java.util.HashMap;

public class StateManager {

    private HashMap<Integer, ArrayList<Visitor>> visitorHours = new HashMap<>();

    public void addState(int timeSlot, ArrayList<Visitor> visitors) {
        ArrayList<Visitor> newVisitors = new ArrayList<>();
        for (Visitor visitor : visitors)
            newVisitors.add(new Visitor(visitor));
        visitorHours.put(timeSlot, newVisitors);
    }

    public ArrayList<Visitor> getState(int timeSlot) {
        return visitorHours.get(timeSlot);
    }

    public void clear() {
        visitorHours.clear();
    }
}
