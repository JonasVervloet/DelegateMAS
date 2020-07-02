package CommunicationManager;

import Ant.AntInterface;
import DelegateMAS.FeasibilityMAS;
import ResourceAgent.PDAgent;
import ResourceAgent.ResourceAgent;
import ResourceAgent.AGVManager;
import ResourceAgent.Schedule.PossibleReservation;
import ResourceAgent.Schedule.ScheduleManager;
import ResourceAgent.Schedule.ScheduleRequest;
import com.github.rinde.rinsim.core.model.comm.CommDevice;
import com.github.rinde.rinsim.core.model.comm.Message;
import com.github.rinde.rinsim.core.model.comm.MessageContents;

import java.util.List;

public class PDCommunication extends AbstractCommunicationManager {

    /*
    The PD agent that delegates this communication manager.
     */
    private PDAgent agent;


    /*
    Constructor
     */
    public PDCommunication(PDAgent agent, CommDevice device)
            throws NullPointerException {
        super(device);

        if (agent == null) {
            throw new NullPointerException(
                    "PD COMMUNICATION | THE GIVEN PD AGENT IS NULL"
            );
        }
        this.agent = agent;
    }


    /*
    PD Agent
     */
    private boolean isStorageAgent() {
        return agent.isStorageSpace();
    }

    public boolean matchesResourceId(int aResourceId) {
        return aResourceId == getResourceId();
    }

    private int getNeighborId() {
        return agent.getNeighborId();
    }

    public int getResourceId() {
        return agent.getResourceId();
    }

    private ResourceAgent getNeighborAgent() {
        return agent.getNeighborAgent();
    }

    /*
    Feasibility MAS
     */
    public boolean harborsOrder() {
        return getFeasibilityMAS().harborsOrder();
    }

    public boolean couldPickupOrder(int pickupTime) {
        return getFeasibilityMAS().couldPickupOrder(pickupTime);
    }

    public List<Integer> getOrderDestinations() {
        return getFeasibilityMAS().getOrderDestinations();
    }

    private FeasibilityMAS getFeasibilityMAS() {
        return agent.getFeasibilityMAS();
    }

    /*
    AGV Manager
     */
    private AGVManager getAGVManager() {
        return agent.getAgvManager();
    }

    /*
    Message
     */
    @Override
    protected void handleAntMessage(AntInterface message) {
        message.visitPDAgent(this);
    }

    public void sendMessageToNeighbor(MessageContents contents) {
        sendMessage(getNeighborAgent(), contents);
    }

    public void sendMessageToAGVAgent(int agvId, MessageContents contents) {
        sendMessage(getAGVManager().getAGVAgent(agvId), contents);
    }

    /*
    Schedule Manager
     */
    private ScheduleManager getScheduleManager() {
        return agent.getScheduleManager();
    }

    public List<PossibleReservation> getPossibleReservations(int start, int end, int agvId) {
        ScheduleRequest request = new ScheduleRequest(start, end, agvId, getNeighborId());
        return getScheduleManager().getPossibleReservations(request);
    }

    public void makeReservation(int start, int end, int agvId) {
        ScheduleRequest request = new ScheduleRequest(start, end, agvId, getNeighborId());
        getScheduleManager().makeReservation(request);
        if (isStorageAgent() && harborsOrder()) {
            getFeasibilityMAS().reservePickup(start, agvId);
        } else {
            System.out.println("PD communication: reserving at storage agent without order");
        }
    }

    public void makeInitialReservation(int start, int end, int agvId) {
        ScheduleRequest request = new ScheduleRequest(start, end, agvId, getNeighborId());
        getScheduleManager().makeReservation(request);
    }
}
