package ResourceAgent;

import ResourceAgent.Schedule.ScheduleManager;
import com.github.rinde.rinsim.core.model.comm.CommUser;
import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;

public abstract class ResourceAgent implements TickListener, CommUser {

    /*
    Counter that is used to give every resource
        a unique id.
     */
    private static int idCounter = 10;

    /*
    A unique id among resource agents.
        Machines do not get a unique id, but
        get a id that is linked to the type
        of machine.
     */
    private int resourceId;

    /*
    The schedule manager of this resource agent.
        The
     */
    private ScheduleManager scheduleManager;

    /*
    The AGV manager of this resource agent.
     */
    private AGVManager agvManager;

    /*
    The number of ticks before one time unit passes.
     */
    private static int timeAdvancementFrequency = 100;

    /*
    Counter that counts the number of ticks.
     */
    private int tickCounter;


    /*
    Constructor
     */
    public ResourceAgent(int capacity, int traversalTime)
            throws IllegalArgumentException {
        if (! isValidCapacity(capacity)) {
            throw new IllegalArgumentException(
                    "RESOURCE AGENT | THE GIVEN CAPACITY IS NOT A VALID ONE"
            );
        }
        if(! isValidTraversalTime(traversalTime)) {
            throw new IllegalArgumentException(
                    "RESOURCE AGENT | THE GIVEN TRAVERSAL TIME IS NOT A VALID ONE"
            );
        }
        scheduleManager = new ScheduleManager(capacity, traversalTime);
        agvManager = new AGVManager();
        tickCounter = 0;
    }

    /*
    Types
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
    Resource ID
     */
    protected static int next() {
        idCounter += 1;
        return idCounter;
    }

    public int getResourceId() {
        return resourceId;
    }

    protected void setResourceId() {
        resourceId = provideId();
    }

    protected int provideId() {
        return next();
    }

    /*
    Neighbors
     */
    public abstract boolean checkAllNeighborsSet();

    public abstract void addNeighbor(Point connection, ResourceAgent agent) throws IllegalArgumentException;

    public abstract void connectNeighbor(ResourceAgent agent) throws IllegalArgumentException;

    /*
    ScheduleManager
     */
    private boolean isValidCapacity(int capacity) {
        return capacity > 0;
    }

    private boolean isValidTraversalTime(int traversalTime) {
        return traversalTime > 0;
    }

    public ScheduleManager getScheduleManager() {
        return scheduleManager;
    }

    /*
    AGV Manager
     */
    public AGVManager getAgvManager() {
        return agvManager;
    }

    /*
    Time Advancement Frequency
     */
    private static boolean isValidTimeAdvancementFrequency(int aFrequency) {
        return aFrequency > 0;
    }

    private static int getTimeAdvancementFrequency() {
        return timeAdvancementFrequency;
    }

    public static void setTimeAdvancementFrequency(int aFrequency)
            throws IllegalArgumentException {
        if (! isValidTimeAdvancementFrequency(aFrequency)) {
            throw new IllegalArgumentException(
                    "RESOURCE AGENT | THE GIVEN FREQUENCY IS NOT A VALID ONE"
            );
        }
        timeAdvancementFrequency = aFrequency;
    }

    /*
    Tick Counter
     */
    private boolean tickCounterEqualsFrequency() {
        return getTickCounter() % getTimeAdvancementFrequency() == 0;
    }

    private int getTickCounter() {
        return tickCounter;
    }

    public int getCurrentTime() {
        return (getTickCounter() / getTimeAdvancementFrequency());
    }

    private void incrementTickCounter() {
        tickCounter += 1;
    }

    private void resetTickCounter() {
        tickCounter = 0;
    }

    /*
    Tick Listener
     */
    @Override
    public void tick(TimeLapse timeLapse) {
        scheduleManager.tick();
        incrementTickCounter();
        if (tickCounterEqualsFrequency()) {
            resetTickCounter();
            scheduleManager.advanceTime(getCurrentTime());
        }
    }
}
