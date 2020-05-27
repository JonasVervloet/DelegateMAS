package ResourceAgent;

import AGVAgent.AGVAgent;
import ResourceAgent.Schedule.ScheduleManager;

import java.util.HashMap;
import java.util.Map;

public class AGVManager {

    /*
    A map that links the AgvIds to the AGV Agents that this
        AGV Manager is currently managing.
     */
    private Map<Integer, AGVAgent> agvAgents;

    /*
    Schedule Manager of the resource agents that delegates
        this AGV Manager. The schedule manager is used to
        validate registering AGV Agents.
     */
    private ScheduleManager scheduleManager;


    /*
    Constructor
     */
    public AGVManager() {
        agvAgents = new HashMap<>();
    }


    /*
    AGV Agents
     */
    public int getNbOfAgvAgents() {
        return agvAgents.size();
    }

    public AGVAgent getAGVAgent(int agvId)
            throws IllegalArgumentException {
        if (! agvAgents.containsKey(agvId)) {
            throw new IllegalArgumentException(
                    "AGV MANAGER | THIS MANAGER CURRENTLY DOES NOT MANAGE AN AGV AGENT WITH GIVEN ID"
            );
        }

        return agvAgents.get(agvId);
    }

    public void registerAGVAgent(AGVAgent agvAgent)
            throws IllegalArgumentException {
        int agvId = agvAgent.getAgvId();

        if (! isValidToRegister(agvAgent)) {
            throw new IllegalArgumentException(
                    "AGV MANAGER | THE GIVEN AGV AGENT IS NOT A VALID ONE TO REGISTER"
            );
        }

        agvAgents.put(agvId, agvAgent);
    }

    public void unregisterAGVAgent(int agvId)
            throws IllegalArgumentException {
        if (! agvAgents.containsKey(agvId)) {
            throw new IllegalArgumentException(
                    "AGV MANAGER | THIS MANAGER CURRENTLY DOES NOT MANAGE AN AGV AGENT WITH GIVEN ID"
            );
        }

        agvAgents.remove(agvId);
    }

    /*
    Schedule Manager
     */
    private boolean isValidToRegister(AGVAgent agvAgent) {
        return scheduleManager.isValidToRegister(agvAgent.getAgvId());
    }

    public void setScheduleManager(ScheduleManager aScheduleManager)
            throws NullPointerException {
        if (aScheduleManager == null) {
            throw new NullPointerException(
                    "AGV MANAGER | SETTING A SCHEDULE MANAGER THAT IS EQUAL TO NULL"
            );
        }
        scheduleManager = aScheduleManager;
    }
}
