package CommunicationManager;

import Ant.AntInterface;
import ResourceAgent.ResourceAgent;
import ResourceAgent.RoadAgent;
import ResourceAgent.Schedule.PossibleReservation;
import ResourceAgent.Schedule.ScheduleManager;
import ResourceAgent.Schedule.ScheduleRequest;
import com.github.rinde.rinsim.core.model.comm.CommDevice;

import java.util.List;

public class RoadCommunication extends AbstractCommunicationManager {

    /*
    The road agent that delegates this communication
        manager.
     */
    private RoadAgent agent;


    /*
    Constructor
     */
    public RoadCommunication(RoadAgent agent, CommDevice device) {
        super(device);
        this.agent = agent;
    }


    /*
    Message
     */
    @Override
    protected void handleAntMessage(AntInterface message) {
        message.visitRoadAgent(this);
    }

    public void sendToOtherNeighbor(int firstNeighborId, AntInterface contents) {
        sendMessage(getOtherNeighborAgent(firstNeighborId), contents);
    }

    public void sendToNeighbor(int nextNeighbor, AntInterface contents) {
        sendMessage(getNeighborAgent(nextNeighbor), contents);
    }

    /*
    Road Agent
     */
    public int getResourceId() {
        return agent.getResourceId();
    }

    public int getLength() {
        return getScheduleManager().getTraversalTime();
    }

    private int getOtherNeighborId(int arrivalId) {
        return agent.getOtherNeighborId(arrivalId);
    }

    private ResourceAgent getOtherNeighborAgent(int firstNeighborId) {
        return agent.getOtherNeighborAgent(firstNeighborId);
    }

    private ResourceAgent getNeighborAgent(int resourceId) {
        return agent.getNeighborAgent(resourceId);
    }

    /*
    ScheduleManager
     */
    private ScheduleManager getScheduleManager() {
        return agent.getScheduleManager();
    }

    public List<PossibleReservation> getPossibleReservations(int start, int end, int agvId, int arrivalId) {
        ScheduleRequest request = new ScheduleRequest(
                start, end, agvId, getOtherNeighborId(arrivalId)
        );
        return getScheduleManager().getPossibleReservations(request);
    }

    public void makeReservation(int start, int end, int agvId, int arrivalId) {
        ScheduleRequest request = new ScheduleRequest(
                start, end, agvId, arrivalId
        );
        getScheduleManager().makeReservation(request);
    }
}
