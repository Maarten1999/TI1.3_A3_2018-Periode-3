package simulator.map;

import java.util.ArrayList;
import java.util.HashSet;

public class TargetManager {
    private static TargetManager instance;

    public static void initialize(ArrayList<Target> targets){
        if(instance == null)
            instance = new TargetManager(targets);
    }

    public static TargetManager instance(){
        return instance;
    }

    private HashSet<String> bathroomList;
    private HashSet<String> storeList;
    private HashSet<String> stageList;

    private ArrayList<Target> targets;

    private TargetManager(ArrayList<Target> targets){
        this.targets = targets;

        bathroomList = new HashSet<>();
        storeList = new HashSet<>();
        stageList = new HashSet<>();

        for(Target t : targets){
            String tname = t.getName();
            if(tname.contains("wc"))
                bathroomList.add(tname);
            else if (tname.contains("Store"))
                storeList.add(tname);
            else if (tname.contains("Stage"))
                stageList.add(tname);

            t.initialize();
        }
    }

    public void update(float deltaTimeFloat){
        for(Target t : targets)
            t.update(deltaTimeFloat);
    }

    public Target getTarget(String targetName){
        Target target = null;
        for (Target t : targets) {
            if(t.getName().equals(targetName)){
                target = t;
            }
        }
        return target;
    }

    public void clear(){
        for(Target t: targets){
            t.clear();
        }
    }

    public HashSet<String> getBathroomList() {
        return bathroomList;
    }

    public HashSet<String> getStoreList() {
        return storeList;
    }

    public HashSet<String> getStageList() {
        return stageList;
    }
}
