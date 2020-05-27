package ResourceAgent;

import ResourceAgent.Schedule.Schedule;
import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import java.util.*;

//import AGVAgent.AGVAgent;
//import Ants.*;

public abstract class ResourceAgent implements TickListener {

    private int capacity;


    ResourceAgent() {
        capacity = 1;
    }

    /*
    TYPES
     */
    public boolean isIntersectionAgent() {
        return false;
    }

    public boolean isRoadAgent() {
        return false;
    }

    public boolean isDeliveryPoint() {
        return false;
    }

    public boolean isMachineAgent() {
        return false;
    }

    public boolean isStorageSpace() {
        return false;
    }


    /*
    NEIGHBORS
     */
    public abstract boolean checkAllNeighborsSet();

    public abstract ResourceAgent getNeighbor(Point point);

    public abstract void addNeighbor(Point connection, ResourceAgent agent) throws IllegalArgumentException;

    public abstract void connectNeighbor(ResourceAgent agent) throws IllegalArgumentException;

//    public void sendMessage(MessageContents contents, ResourceAgent destination) {
//        destination.receiveMessage(contents);
//    }

//    public void receiveMessage(MessageContents contents) {
//        if (contents.getClass() == RandomExplorationAnt.class) {
//            RandomExplorationAnt ant = (RandomExplorationAnt) contents;
//            ant.inspectAgent(this);
//        } else if (contents.getClass() == IntentionAnt.class) {
//            ((IntentionAnt) contents).inspectAgent(this);
//        } else if (contents.getClass() == TypeExplorationAnt.class) {
//            ((TypeExplorationAnt) contents).inspectAgent(this);
//        } else if (contents.getClass() == PointExplorationAnt.class) {
//            ((PointExplorationAnt) contents).inspectAgent(this);
//        } else if (contents.getClass() == RequestAfterTickMsg.class) {
//            ((RequestAfterTickMsg) contents).inspectAgent(this);
//        } else if (contents.getClass() == RetractInteractionMsg.class) {
//            ((RetractInteractionMsg) contents).inspectAgent(this);
//        } else if (contents.getClass() == RetractAcceptedInteractionMsg.class) {
//            ((RetractAcceptedInteractionMsg) contents).inspectAgent(this);
//        } else {
//            System.out.println("RESOURCE AGENT | MESSAGE OF UNKNOWN TYPE RECEIVED");
//            System.out.println(contents.getClass());
//        }
//    }

//    public void registerAGVAgent(AGVAgent.AGVAgent agent) {}

//    public void unregisterAGVAgent(AGVAgent.AGVAgent agent) {}

    @Override
    public void tick(TimeLapse timeLapse) {}

    @Override
    public void afterTick(TimeLapse timeLapse) {}
}
