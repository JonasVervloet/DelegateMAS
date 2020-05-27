package ResourceAgent;

import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;

//import AGVAgent.AGVAgent;
//import Ants.Path;
//import Ants.RequestAfterTickMsg;
//import MASProject;
//import Order;


public class PDAgent extends ResourceAgent {

    private Neighbor neighbor;
    private PDType type;

//    private Order order;
//    private boolean busy;
//    private boolean interactionAccepted;
//    private int ordercnt;
//    private AGVAgent.AGVAgent agent;
//    private List<PossibleInteraction> possibleInteractions;


    public PDAgent(Point vertex1, PDType type) {
        neighbor = new Neighbor(vertex1);
        this.type  = type;

//        order = null;
//        busy = false;
//        ordercnt = 0;
//        possibleInteractions = new ArrayList<>();
    }


    /*
    TYPES
     */
    @Override
    public boolean isDeliveryPoint() {
        return  type == PDType.DELIVER_POINT;
    }

    @Override
    public boolean isMachineAgent() {
        return (type == PDType.MACHINE_A ||
                type == PDType.MACHINE_B ||
                type == PDType.MACHINE_C ||
                type == PDType.MACHINE_D);
    }

    @Override
    public boolean isStorageSpace() {
        return type == PDType.STORAGE;
    }

    /*
    NEIGHBOUR
     */
    public boolean hasConnection(Point point) {
        return neighbor.isValidConnection(point);
    }

    public Point getConnection() {
        return neighbor.getConnection();
    }

    @Override
    public boolean checkAllNeighborsSet() {
        return neighbor.agentSet();
    }

    public ResourceAgent getNeighborAgent() {
        return neighbor.getAgent();
    }

    @Override
    public ResourceAgent getNeighbor(Point point)
            throws IllegalArgumentException {
        if (neighbor.isValidConnection(point)) {
            return  neighbor.getAgent();
        } else {
           throw new IllegalArgumentException(
                   "PD AGENT | NO NEIGHBOURS WITH GIVEN CONNECTION PRESENT"
           );
        }
    }

    @Override
    public void addNeighbor(Point connection, ResourceAgent agent)
            throws IllegalArgumentException {
        if (neighbor.isValidConnection(connection)) {
            neighbor.setAgent(agent);
        } else {
            throw new IllegalArgumentException(
                    "PD AGENT | TRYING TO SET AN AGENT FOR THIS PD AGENT WHILE THE CONNECTION DOES NOT MATCH"
            );
        }
    }

    @Override
    public void connectNeighbor(ResourceAgent agent) throws IllegalArgumentException {
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
    TYPE
     */
    public boolean isStorageAgent() {
        return type.equals(PDType.STORAGE);
    }

    public boolean isDeliveryAgent() {
        return type.equals(PDType.DELIVER_POINT);
    }

    public PDType getType() {
        return type;
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
    COMMMUNICATION
     */
//    public void sendMessageToNeighbour(MessageContents message) {
//        super.sendMessage(message, neighbour.getAgent());
//    }
//
//    public void sendMessageToAGV(MessageContents message, String agvname) {
//        if (agent != null && agent.getName().equals(agvname)) {
//            agent.receiveMessage(message);
//        } else {
//            System.out.println(agent.getName());
//            System.out.println(agvname);
//            System.out.println("PD AGENT | NO AGENT WITH THAT NAME PRESENT");
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
//
//    @Override
//    public boolean checkAllNeighborsSet() {
//        return false;
//    }
//
//    @Override
//    public ResourceAgent.ResourceAgent getNeighbor(Point point) {
//        return null;
//    }
//
//    @Override
//    public void addNeighbor(Point connection, ResourceAgent.ResourceAgent agent) {
//
//    }

    /*
        TICK LISTENENER
         */
    @Override
    public void tick(TimeLapse timeLapse) {
//        if (busy) {
//            ordercnt += 1;
//            if (ordercnt == MASProject.MACHINE_WORKTIME) {
//                busy = false;
//                interactionAccepted = false;
//                order.reactivate(getConnection());
//                ordercnt = 0;
//            }
//        }
    }

    @Override
    public String toString() {
        return "PD agent: " + neighbor.toString();
    }
}
