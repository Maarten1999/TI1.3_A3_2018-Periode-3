package simulator.map;

import java.util.ArrayList;

public class TargetManager {
    private static TargetManager instance;

    public static void initialize(ArrayList<Target> targets){
        if(instance == null)
            instance = new TargetManager(targets);
    }

    public static TargetManager instance(){
        return instance;
    }

    private ArrayList<Target> targets;

    private TargetManager(ArrayList<Target> targets){
        this.targets = targets;
    }

    private Target getTarget(String targetName){
        Target target = null;
        for (Target t : targets) {
            if(t.getName().equals(targetName)){
                target = t;
            }
        }
        return target;
    }
}
