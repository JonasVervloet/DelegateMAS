package ResourceAgent;

import CommunicationManager.CommunicationManagerInterface;
import CommunicationManager.RoadCommunication;
import com.github.rinde.rinsim.core.model.comm.CommDeviceBuilder;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import com.google.common.base.Optional;

public class RoadAgent extends InfrastructureAgent {

    /*
    The first neighbor of this road agent.
     */
    private Neighbor neighbor1;

    /*
    The second neighbor of this road agent.
     */
    private Neighbor neighbor2;

    /*
    The communication manager of this road agent.
        The manager handles all the contact with
        other entities in the world.
     */
    private CommunicationManagerInterface commManager;


    /*
    Constructor
     */
    public RoadAgent(Point vertex1, Point vertex2) {
        super(getCapacity(vertex1, vertex2), getTraversalTime(vertex1, vertex2));
        neighbor1 = new Neighbor(vertex1);
        neighbor2 = new Neighbor(vertex2);
    }


    /*
    Resource Agent
     */
    private static int getTraversalTime(Point vertex1, Point vertex2) {
        int diffX = (int) Math.abs((vertex1.x - vertex2.x) / 2);
        int diffY = (int) Math.abs((vertex1.y - vertex2.y) / 2);

        return diffX + diffY;
    }

    private static int getCapacity(Point vertex1, Point vertex2) {
        return getTraversalTime(vertex1, vertex2);
    }

    /*
    Types
     */
    @Override
    public boolean isRoadAgent() {
        return true;
    }

    /*
    Neighbors
     */
    @Override
    public boolean checkAllNeighborsSet() {
        return (neighbor1.agentSet() &&
                neighbor2.agentSet());
    }

    private boolean isValidNeighborId(int aResourceId)
            throws IllegalStateException {
        if (! checkAllNeighborsSet()) {
            throw new IllegalStateException(
                    "ROAD AGENT | NOT ALL NEIGHBORS HAVE BEEN SET"
            );
        }
        return (neighbor1.matchesResourceId(aResourceId) ||
                neighbor2.matchesResourceId(aResourceId));
    }

    public int getOtherNeighborId(int firstNeighborId) {
        return getOtherNeighborAgent(firstNeighborId).getResourceId();
    }

    public ResourceAgent getNeighborAgent(int resourceId)
            throws IllegalArgumentException {
        if (! isValidNeighborId(resourceId)) {
            throw new IllegalArgumentException(
                    "ROAD AGENT | GIVEN RESOURCE ID IS NOT THE ID OF ANY NEIGHBOR"
            );
        }

        if (neighbor1.matchesResourceId(resourceId)) {
            return neighbor1.getAgent();
        } else {
            return neighbor2.getAgent();
        }
    }

    public ResourceAgent getOtherNeighborAgent(int firstNeighborId)
            throws IllegalArgumentException {
        if (! isValidNeighborId(firstNeighborId)) {
            throw new IllegalArgumentException(
                    "ROAD AGENT | GIVEN RESOURCE ID IS NOT THE ID OF ANY NEIGHBOR"
            );
        }

        if (neighbor1.matchesResourceId(firstNeighborId)) {
            return neighbor2.getAgent();
        } else {
            return neighbor1.getAgent();
        }
    }

    @Override
    public void addNeighbor(Point connection, ResourceAgent agent)
            throws IllegalArgumentException {
        if (neighbor1.isValidConnection(connection)) {
            if (neighbor1.agentSet()) {
                throw new IllegalStateException(
                        "ROAD AGENT | THIS NEIGHBOR HAS ALREADY BEEN SET"
                );
            }
            neighbor1.setAgent(agent);
        } else if (neighbor2.isValidConnection(connection)) {
            if (neighbor2.agentSet()) {
                throw new IllegalStateException(
                        "ROAD AGENT | THIS NEIGHBOR HAS ALREADY BEEN SET"
                );
            }
            neighbor2.setAgent(agent);
        } else {
            throw new IllegalArgumentException(
                    "ROAD AGENT | SETTING AGENT USING AN INVALID CONNECTION"
            );
        }
    }

    @Override
    public void connectNeighbor(ResourceAgent agent)
            throws IllegalArgumentException {
        try {
            agent.addNeighbor(neighbor1.getConnection(), this);
            neighbor1.setAgent(agent);
        } catch (IllegalArgumentException e1) {
            try {
                agent.addNeighbor(neighbor2.getConnection(), this);
                neighbor2.setAgent(agent);
            } catch (IllegalArgumentException e2) {
                throw new IllegalArgumentException(
                        "ROAD AGENT | CONNECTING NEIGHBOR WITH NO ADJACENT CONNECTIONS"
                );
            }
        }
    }

    /*
    Comm User
     */
    @Override
    public Optional<Point> getPosition() {
        double x = (neighbor1.getConnection().x +
                neighbor2.getConnection().x) / 2;
        double y = (neighbor1.getConnection().y +
                neighbor2.getConnection().y) / 2;
        return Optional.of(new Point(x, y));
    }

    @Override
    public void setCommDevice(CommDeviceBuilder commDeviceBuilder) {
        commManager = new RoadCommunication(this, commDeviceBuilder.build());
    }

    /*
    Tick Listener
     */
    @Override
    public void tick(TimeLapse timeLapse) {
        commManager.checkMessages();
    }

    @Override
    public void afterTick(TimeLapse timeLapse) {
        commManager.checkMessages();
    }

    /*
    String
     */
    @Override
    public String toString() {
        return "RoadAgent: " + neighbor1.toString() + " + " + neighbor2.toString();
    }
}
