package ResourceAgent;

import CommunicationManager.CommunicationManagerInterface;
import CommunicationManager.IntersectionCommunication;
import Order.Order;
import ResourceAgent.OrderMap.OrderManager;
import ResourceAgent.ResourceMap.ResourceManager;
import com.github.rinde.rinsim.core.model.comm.CommDeviceBuilder;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.List;


public class IntersectionAgent extends InfrastructureAgent {

    /*
    The list of neighbors of this intersection agent.
     */
    private List<Neighbor> neighbours;

    /*
    The communication manager of this intersection agent.
        The manager handles all the contact with other
        entities in the world.
     */
    private CommunicationManagerInterface commManager;

    /*
    The resource manager of this intersection agent.
        The manager handles all feasibility interaction
        that involves resources.
     */
    private ResourceManager resourceManager;

    /*
    The Package manager of this intersection agent.
        The manager handles all feasibility interaction
        that involves resources.
     */
    private OrderManager orderManager;



    /*
    Constructor
     */
    public IntersectionAgent(Point vertex1, Point vertex2, Point vertex3)
            throws NullPointerException {
        super(1, getTraversalTime());
        if (vertex1 == null || vertex2 == null || vertex3 == null) {
            throw new NullPointerException(
                    "INTERSECTION AGENT | ONE OF THE GIVEN VERTICES EQUAL TO NULL"
            );
        }

        neighbours = new ArrayList<>();
        neighbours.add(new Neighbor(vertex1));
        neighbours.add(new Neighbor(vertex2));
        neighbours.add(new Neighbor(vertex3));

        setResourceId();
    }

    public IntersectionAgent(Point vertex1, Point vertex2, Point vertex3, Point vertex4) {
        this(vertex1, vertex2, vertex3);
        neighbours.add(new Neighbor(vertex4));
    }


    /*
    Types
     */
    @Override
    public boolean isIntersectionAgent() {
        return true;
    }

    /*
    Capacity
     */
    public static int getTraversalTime() {
        return 2;
    }

    /*
    Neighbors
     */
    @Override
    public boolean checkAllNeighborsSet() {
        for (Neighbor neighbour: neighbours) {
            if (! neighbour.agentSet()) {
                return false;
            }
        }
        return true;
    }

    public boolean isValidNeighborId(int neighborId) {
        for (Neighbor neighbor: neighbours) {
            if (neighbor.matchesResourceId(neighborId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ResourceAgent getNeighborAgent(int resourceId)
            throws IllegalArgumentException {
        if (! isValidNeighborId(resourceId)) {
            throw new IllegalArgumentException(
                    "INTERSECTION AGENT | THIS AGENT HAS NO NEIGHBOR WITH GIVEN ID"
            );
        }
        return getNeighborWithId(resourceId);
    }

    @Override
    public Point getConnectionWithNeighbor(int resourceId) {
        if (! isValidNeighborId(resourceId)) {
            throw new IllegalArgumentException(
                    "INTERSECTION AGENT | THIS AGENT HAS NO NEIGHBOR WITH GIVEN ID"
            );
        }
        Point connection = null;
        for (Neighbor neighbor: neighbours) {
            if (neighbor.matchesResourceId(resourceId)) {
                connection = neighbor.getConnection();
            }
        }
        return connection;
    }

    private List<Integer> getNeighborIds() {
        List<Integer> ids = new ArrayList<>();
        for (Neighbor neighbor: neighbours) {
            ids.add(neighbor.getResourceId());
        }

        return ids;
    }

    public ResourceAgent getNeighborWithId(int neighborId) {
        for (Neighbor neighbor: neighbours) {
            if (neighbor.matchesResourceId(neighborId)) {
                return neighbor.getAgent();
            }
        }

        throw new IllegalArgumentException(
                "INTERSECTION AGENT | NO NEIGHBOR WITH GIVEN CONNECTIONS PRESENT"
        );
    }

    public List<Integer> getOtherNeighborIds(int firstNeighborId) {
        List<Integer> otherNeighborIds = new ArrayList<>();
        for (ResourceAgent neighbor: getOtherNeighbors(firstNeighborId)) {
            otherNeighborIds.add(neighbor.getResourceId());
        }

        return otherNeighborIds;
    }

    public List<ResourceAgent> getOtherNeighbors(int firstNeighborId)
            throws IllegalArgumentException {
        if (! isValidNeighborId(firstNeighborId)) {
            throw new IllegalArgumentException(
                    "INTERSECTION AGENT | GIVEN RESOURCE ID IS NOT THE ID OF ANY NEIGHBOR"
            );
        }

        List<ResourceAgent> otherNeighbors = new ArrayList<>();
        for (Neighbor neighbor: neighbours) {
            if (! neighbor.matchesResourceId(firstNeighborId)) {
                otherNeighbors.add(neighbor.getAgent());
            }
        }
        return otherNeighbors;
    }

    @Override
    public void addNeighbor(Point connection, ResourceAgent agent)
            throws IllegalArgumentException {
        boolean connectionValidated = false;
        for (Neighbor neighbor: neighbours) {
            if (neighbor.isValidConnection(connection)) {
                connectionValidated = true;
                neighbor.setAgent(agent);
            }
        }
        if (! connectionValidated) {
            throw new IllegalArgumentException(
                    "INTERSECTION AGENT | CONNECTION NOT PART OF THIS INTERSECTION"
            );
        }
        if (checkAllNeighborsSet()) {
            setResourceManager();
            setOrderManager();
        }
    }

    @Override
    public void connectNeighbor(ResourceAgent agent)
            throws IllegalArgumentException {
        boolean neighborConnected = false;
        for (Neighbor neighbor: neighbours) {
            try {
                agent.addNeighbor(neighbor.getConnection(), this);
                neighbor.setAgent(agent);
                neighborConnected = true;
            } catch (IllegalArgumentException e) {}
        }
        if (! neighborConnected) {
            throw new IllegalArgumentException(
                    "INTERSECTION AGENT | CONNECTING NEIGHBOR WITH NO ADJACENT CONNECTIONS"
            );
        }
        if (checkAllNeighborsSet()) {
            setResourceManager();
            setOrderManager();
        }
    }

    public List<Point> getExitPoints(Point entryPoint) {
        List<Point> exits = new ArrayList<>();
        boolean entryValidated = false;
        for (Neighbor neighbor: neighbours) {
            if (neighbor.isValidConnection(entryPoint)) {
                entryValidated = true;
            } else {
                exits.add(neighbor.getConnection());
            }
        }
        if(! entryValidated) {
            System.out.println("INTERSECTION AGENT | REQUESTING EXIT POINTS WITHOUT VALID ENTRY POINT");
        }
        return exits;
    }

    /*
    Comm User
     */
    @Override
    public Optional<Point> getPosition() {
        return null;
    }

    @Override
    public void setCommDevice(CommDeviceBuilder commDeviceBuilder) {
        commManager = new IntersectionCommunication(this, commDeviceBuilder.build());
    }

    /*
    Resource Manager
     */
    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    private void setResourceManager() {
        resourceManager = new ResourceManager(getNeighborIds());
    }

    /*
    Order Manager
     */
    public OrderManager getOrderManager() {
        return orderManager;
    }

    private void setOrderManager() {
        orderManager = new OrderManager(getNeighborIds());
    }

    /*
    Tick Listener
     */
    @Override
    public void tick(TimeLapse timeLapse) {
        commManager.checkMessages();
        // TODO: evaporate resource map
        // TODO: evaporate package map
        // => all responsibility of manager, just pass along the tick
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
        String str =  "IntersectionAgent: ";
        for (Neighbor neighbor: neighbours) {
            str = str.concat(neighbor.toString() + " + ");
        }
        return str;
    }
}
