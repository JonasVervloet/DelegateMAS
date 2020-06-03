package ResourceAgent;

import CommunicationManager.CommunicationManagerInterface;
import CommunicationManager.PDCommunication;
import DelegateMAS.FeasibilityMAS;
import Order.Order;
import com.github.rinde.rinsim.core.model.comm.CommDeviceBuilder;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import com.google.common.base.Optional;

import javax.swing.text.html.Option;


public class PDAgent extends ResourceAgent {

    /*
    The type of PD Agent.
     */
    private PDType type;

    /*
    The neighbor of this PD Agent.
     */
    private Neighbor neighbor;

    /*
    The communication manager of this PD agent.
        The manager handles all the contact with
        other entities in the world.
     */
    private PDCommunication commManager;

    /*
    The feasibility MAS module of this PD agent.
       The module regularly sends out feasibility
       agents.
     */
    private FeasibilityMAS feasibilityMAS;


    /*
    Constructor
     */
    public PDAgent(Point vertex1, PDType type) {
        super(getCapacity(type), getTraversalTim(type));
        this.neighbor = new Neighbor(vertex1);
        this.type  = type;
        this.feasibilityMAS = new FeasibilityMAS(this);
        setResourceId();
    }


    /*
    Resource Agent
     */
    @Override
    protected int provideId() {
        if (type == PDType.MACHINE_A) {
            return 1;
        } else if (type == PDType.MACHINE_B) {
            return 2;
        } else if (type == PDType.MACHINE_C) {
            return 3;
        } else if (type == PDType.MACHINE_D) {
            return 4;
        } else {
            return next();
        }
    }

    private static int getCapacity(PDType type) {
        return 1;
    }

    private static int getTraversalTim(PDType type) {
        return 4;
    }

    /*
    Types
     */
    @Override
    public boolean isStorageSpace() {
        return type == PDType.STORAGE;
    }

    @Override
    public boolean isMachineAgent() {
        return (type == PDType.MACHINE_A ||
                type == PDType.MACHINE_B ||
                type == PDType.MACHINE_C ||
                type == PDType.MACHINE_D);
    }

    @Override
    public boolean isDeliveryPoint() {
        return  type == PDType.DELIVER_POINT;
    }

    /*
    Neighbors
     */
    @Override
    public boolean checkAllNeighborsSet() {
        return neighbor.agentSet();
    }

    public int getNeighborId() {
        return getNeighborAgent().getResourceId();
    }

    public ResourceAgent getNeighborAgent()
            throws IllegalStateException {
        if (! neighbor.agentSet()) {
            throw new IllegalStateException(
                    "PD AGENT | THERE HAS NOT YET BEEN ASSIGNED A AGENT TO THE NEIGHBOR OBJECT OF THIS AGENT"
            );
        }
        return neighbor.getAgent();
    }

    @Override
    public void addNeighbor(Point connection, ResourceAgent agent)
            throws IllegalArgumentException, IllegalStateException {
        if (neighbor.agentSet()) {
            throw new IllegalStateException(
                    "PD AGENT | THIS PD AGENT HAS ALREADY A RESOURCE AGENT FOR ITS NEIGHBOR"
            );
        } else if (! neighbor.isValidConnection(connection)) {
            throw new IllegalArgumentException(
                    "PD AGENT | THE CONNECTION DOES NOT MATCH"
            );
        } else {
            neighbor.setAgent(agent);
        }
    }

    @Override
    public void connectNeighbor(ResourceAgent agent)
            throws IllegalArgumentException {
        try {
            agent.addNeighbor(neighbor.getConnection(), this);
            neighbor.setAgent(agent);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "INTERSECTION AGENT | CONNECTING NEIGHBOR WITH NO ADJACENT CONNECTIONS"
            );
        }
    }

    /*
    Communication Manager
     */
    @Override
    public Optional<Point> getPosition() {
        return Optional.of(neighbor.getConnection());
    }

    @Override
    public void setCommDevice(CommDeviceBuilder commDeviceBuilder) {
        commManager = new PDCommunication(this, commDeviceBuilder.build());
    }

    public PDCommunication getCommunicationManager() {
        return commManager;
    }

    /*
    Feasibility MAS
     */
    public FeasibilityMAS getFeasibilityMAS() {
        return feasibilityMAS;
    }

    public void registerOrder(Order order) {
        feasibilityMAS.registerOrder(order);
    }

    /*
    Tick Listener
     */
    @Override
    public void tick(TimeLapse timeLapse) {
        super.tick(timeLapse);
        commManager.checkMessages();
        feasibilityMAS.tick();
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
        return "PD agent: " + neighbor.toString();
    }

    /*
    ORDER
     */
//    public boolean hasOrder() {
//        return (order != null && ! busy);
//    }
//
//    public boolean hasAcceptedInteraction() {
//        return interactionAccepted;
//    }
//
//    public boolean isAvailable() {
//        return (order == null);
//    }
//
//    public boolean canBeUsed() {
//        return (! hasOrder() && agent == null && ! hasAcceptedInteraction());
//    }
//
//    public Order pickupOrder() {
//        if (! hasOrder()) {
//            System.out.println("PD AGENT | NO ORDER PRESENT TO PICK UP");
//            return null;
//        } else {
//            Order result = order;
//            order = null;
//            interactionAccepted = false;
//            result.pickup();
//            return result;
//        }
//    }
//
//    public void deliverOrder(Order order) {
//        if (order == null) {
//            System.out.println("PD AGENT | DELIVERING NULL ORDER");
//        } else {
//            interactionAccepted = false;
//            order.deliverToNextStop();
//            if (! order.finished()) {
//                this.order = order;
//                busy = true;
//                ordercnt = 0;
//            }
//        }
//    }
//
//    private PossibleInteraction getEarliestInteraction() {
//        PossibleInteraction best = null;
//        int earliest = Integer.MAX_VALUE;
//        for (PossibleInteraction pickup: possibleInteractions) {
//            if (pickup.getPickupTime() < earliest) {
//                best = pickup;
//                earliest = pickup.getPickupTime();
//            }
//        }
//        return best;
//    }
//
//    public void informPossibleInteraction(int time, String vehicleName, Path reversePath, boolean pickup) {
//        if (! interactionAccepted) {
//            if (pickup) {
//                if (hasOrder()) {
//                    PossibleInteraction possible = new PossibleInteraction(time, vehicleName, reversePath);
//                    possibleInteractions.add(possible);
//                } else {
//                    System.out.println("PD AGENT | INFORMING POSSIBLE PICK UP BUT NO ORDER PRESENT");
//                }
//            } else {
//                if (canBeUsed()) {
//                    PossibleInteraction possible = new PossibleInteraction(time, vehicleName, reversePath);
//                    possibleInteractions.add(possible);
//                } else {
//                    System.out.println("PD AGENT | INFORM POSSIBLE USAGE BUT THIS AGENT CAN'T BE USED RIGHT NOW");
//                }
//            }
//        } else {
//            System.out.println("PD AGENT | INFORM ABOUT INTERACTION, BUT AGENT ALREADY ACCEPTED AN INTERACTION");
//        }
//    }
//
//    public void retractInteraction(int time, String vehicleName) {
//        if (! interactionAccepted) {
//            PossibleInteraction retraction = null;
//            for (PossibleInteraction pickup : possibleInteractions) {
//                if (time == pickup.getPickupTime()
//                        && vehicleName.equals(pickup.getVehicle())) {
//                    retraction = pickup;
//                    break;
//                }
//            }
//            if (retraction == null) {
//                System.out.println(hasOrder());
//                System.out.println("PD AGENT | NO PICK UP TO RETRACT");
//            } else {
//                possibleInteractions.remove(retraction);
//            }
//        }
//    }
//
//    public void retractAcceptedInteraction(int time, String vehicleName) {
//        if (interactionAccepted) {
//            interactionAccepted = false;
//        } else {
//            System.out.println("PD AGENT | RETRACTING ACCEPTED INTERACTION BUT NO INTERACTION WAS ACCEPTED");
//        }
//    }
//
//    public boolean acceptPickup(int time, String vehicleName) {
//        if (! hasOrder()) {
//            possibleInteractions = new ArrayList<>();
//            System.out.println("PD AGENT | NO ORDER TO PICK UP");
//        }
//        while (! possibleInteractions.isEmpty()) {
//            PossibleInteraction best = getEarliestInteraction();
//            if (best.getPickupTime() == time &&
//                    vehicleName.equals(best.getVehicle())) {
//                possibleInteractions = new ArrayList<>();
//                interactionAccepted = true;
//                return true;
//            } else {
//                System.out.println("request after tick message");
//                new RequestAfterTickMsg(best.getVehicle(), best.getPath());
//            }
//        }
//        return false;
//    }
//
//    public boolean acceptUsage(int time, String vehicleName) {
//        if (interactionAccepted) {
//            return false;
//        } else if (! canBeUsed()) {
//            System.out.println("PD AGENT | THIS AGENT CANNOT BE USED RIGHT NOW");
//            return false;
//        }
//        while (! possibleInteractions.isEmpty()) {
//            PossibleInteraction best = getEarliestInteraction();
//            if (best.getPickupTime() == time && vehicleName.equals(best.getVehicle())) {
//                possibleInteractions = new ArrayList<>();
//                interactionAccepted = true;
//                return true;
//            } else {
//                System.out.println("request after tick message");
//                new RequestAfterTickMsg(best.getVehicle(), best.getPath());
//            }
//        }
//        return false;
//    }
//
//    public void registerOrder(Order order) {
//        if (order == null) {
//            System.out.println("PD AGENT | SETTING NULL ORDER");
//        } else {
//            this.order = order;
//        }
//    }

    /*
    AGV AGENT
     */
//    @Override
//    public void registerAGVAgent(AGVAgent.AGVAgent newAgent) {
//        if (newAgent == null) {
//            System.out.println("PD AGENT | REGISTERING NULL AGV AGENT");
//        } else if(agent != null) {
//            System.out.println("PD AGENT | REGISTERING AGV AGENT WHILE ALREADY ONE PRESENT");
//            System.out.println(getConnection());
//            System.out.println(agent.getName());
//            System.out.println(newAgent.getName());
//        } else {
//            this.agent = newAgent;
//        }
//    }
//
//    @Override
//    public void unregisterAGVAgent(AGVAgent.AGVAgent oldAgent) {
//        if (oldAgent == null) {
//            System.out.println("PD AGENT | UNREGISTERING NULL AGV AGENT");
//        } else if (agent == null ||! oldAgent.equals(agent)) {
//            System.out.println("PD AGENT | UNRESTERING AGENT THAT WAS NOT REGISTERED");
//        } else {
//            agent = null;
//        }
//    }
}
