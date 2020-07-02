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

    public ResourceAgent agent;


    /*
    Constructor
     */
    public AGVManager(ResourceAgent agent) {
        this.agvAgents = new HashMap<>();
        this.agent = agent;
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
    Resource Agent
     */
    private int getCurrentTime() {
        return agent.getCurrentTime();
    }

    /*
    Schedule Manager
     */
    private ScheduleManager getScheduleManager() {
        return agent.getScheduleManager();
    }

    private boolean isValidToRegister(AGVAgent agvAgent) {
        return getScheduleManager().isValidToRegister(agvAgent.getAgvId(), getCurrentTime());
    }
}
