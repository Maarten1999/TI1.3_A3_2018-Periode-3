package simulator.Visitors;


import java.util.ArrayList;
import java.util.HashMap;

public class VisitorStateManager {

    private static VisitorStateManager instance;

    public static VisitorStateManager getInstance(){
        if(instance == null)
            instance = new VisitorStateManager();
        return instance;
    }

    private HashMap<Integer, ArrayList<VisitorState>> visitorStateList;

    private VisitorStateManager(){
        visitorStateList = new HashMap<>();
    }

    public void saveState(int timeslot, ArrayList<Visitor> visitorList){
        ArrayList<VisitorState> visitorStates = new ArrayList<>();
        for(Visitor v: visitorList){
            visitorStates.add(v.getState());
        }
        visitorStateList.put(timeslot, visitorStates);

    }

    public void loadState(int timeslot){
        if(visitorStateList.containsKey(timeslot))
        {
            ArrayList<VisitorState> states = visitorStateList.get(timeslot);
            for(VisitorState vs: states){
                vs.applyState();
            }
        }
    }

    public void clear(){
        visitorStateList.clear();
    }

}
