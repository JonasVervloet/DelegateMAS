package CommunicationManager;

import Ant.AntInterface;
import ResourceAgent.IntersectionAgent;
import ResourceAgent.OrderMap.OrderManager;
import ResourceAgent.ResourceAgent;
import ResourceAgent.ResourceMap.ResourceManager;
import ResourceAgent.Schedule.PossibleReservation;
import ResourceAgent.Schedule.ScheduleManager;
import ResourceAgent.Schedule.ScheduleRequest;
import com.github.rinde.rinsim.core.model.comm.CommDevice;
import org.apache.commons.math3.util.Pair;

import java.util.List;

public class IntersectionCommunication extends AbstractCommunicationManager {

    /*
    The intersection agent that delegates this communication
        manager.
     */
    private IntersectionAgent agent;


    /*
    Constructor
     */
    public IntersectionCommunication(IntersectionAgent agent, CommDevice device) {
        super(device);
        this.agent = agent;
    }


    /*
    Message
     */
    @Override
    protected void handleAntMessage(AntInterface message) {
        message.visitIntersectionAgent(this);
    }

    public void sendMessageToOtherNeighbors(int firstNeighborId, AntInterface contents) {
        for (ResourceAgent neighbor: getOtherNeighbors(firstNeighborId)) {
            sendMessage(neighbor, contents.cloneAnt());
        }
    }

    public void sendMessageToNeighbor(int neighborId, AntInterface contents) {
        sendMessage(getResourceAgentWithId(neighborId), contents);
    }

    /*
    Intersection Agent
     */
    public int getResourceId() {
        return agent.getResourceId();
    }

    public int getLength() {
        return getScheduleManager().getTraversalTime();
    }

    private ResourceAgent getResourceAgentWithId(int neighborId) {
        return agent.getNeighborWithId(neighborId);
    }

    public List<Integer> getOtherNeighborIds(int firstNeighborId) {
        return agent.getOtherNeighborIds(firstNeighborId);
    }

    private List<ResourceAgent> getOtherNeighbors(int firstNeighborId) {
        return agent.getOtherNeighbors(firstNeighborId);
    }

    /*
    Resource Manager
     */
    private ResourceManager getResourceManager() {
        return agent.getResourceManager();
    }

    public List<Pair<Integer, Integer>> getMinDistancesToResource(int resourceId, int arrivalId) {
        return getResourceManager().getMinDistances(resourceId, arrivalId);
    }

    public void registerResource(int neighborId, int resourceId, int distance) {
        getResourceManager().registerResource(neighborId, resourceId, distance);
    }

    /*
    Order Manager
     */
    private OrderManager getOrderManager() {
        return agent.getOrderManager();
    }

    public List<Pair<Integer, Integer>> getMinDistancesToOrder(int arrivalId) {
        return getOrderManager().getMinDistancesToOrder(arrivalId);
    }

    public void registerOrder(int neighborId, int orderId, int distance) {
        getOrderManager().registerOrder(neighborId, orderId, distance);
    }

    /*
    ScheduleManager
     */
    private ScheduleManager getScheduleManager() {
        return agent.getScheduleManager();
    }

    public List<PossibleReservation> getPossibleReservations(int start, int end, int agvId, int destinationId) {
        ScheduleRequest request = new ScheduleRequest(
                start, end, agvId, destinationId
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
