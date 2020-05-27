package ResourceAgent;

import ResourceAgent.Schedule.Schedule;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;

//import AGVAgent.AGVAgent;

public class RoadAgent extends ResourceAgent {

    private Neighbor neighbor1;
    private Neighbor neighbor2;
    private Schedule schedule;

//    private List<AGVAgent.AGVAgent> agents;

    public RoadAgent(Point vertex1, Point vertex2) {
        neighbor1 = new Neighbor(vertex1);
        neighbor2 = new Neighbor(vertex2);

//        agents = new ArrayList<>();

        // TODO: change to actual capacity
//        schedule = new Schedule(1);
    }


    /*
    TYPES
     */
    @Override
    public boolean isRoadAgent() {
        return true;
    }

    /*
    NEIGHBOURS
     */
    @Override
    public boolean checkAllNeighborsSet() {
        return (neighbor1.agentSet() &&
                neighbor2.agentSet());
    }

    public ResourceAgent getNeighbor(Point point)
            throws IllegalArgumentException {
        if (neighbor1.isValidConnection(point)) {
            return neighbor1.getAgent();
        } else if (neighbor2.isValidConnection(point)) {
            return neighbor2.getAgent();
        } else {
            throw new IllegalArgumentException(
                    "ROAD AGENT | NO NEIGHBOUR WITH GIVEN CONNECTION PRESENT"
            );
        }
    }

    @Override
    public void addNeighbor(Point connection, ResourceAgent agent)
            throws IllegalArgumentException {
        if (neighbor1.isValidConnection(connection)) {
            neighbor1.setAgent(agent);
        } else if (neighbor2.isValidConnection(connection)) {
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

    public boolean hasConnection(Point connection) {
        return (neighbor1.isValidConnection(connection) ||
                neighbor2.isValidConnection(connection));
    }

    public Point getExitPoint(Point entryPoint)
            throws IllegalArgumentException {
        if (neighbor1.isValidConnection(entryPoint)) {
            return neighbor2.getConnection();
        } else if (neighbor2.isValidConnection(entryPoint)) {
            return neighbor1.getConnection();
        } else {
            throw new IllegalArgumentException(
                    "ROAD AGENT | REQUESTING EXIT POINT WITHOUT VALID ENTRY POINT"
            );
        }
    }

    public int getTravelTime() {
        Point conn1 = neighbor1.getConnection();
        Point conn2 = neighbor2.getConnection();
        if (conn1.x == conn2.x) {
            return ((int) Math.abs((conn1.y - conn2.y) / 2));
        } else if (conn1.y == conn2.y) {
            return ((int) Math.abs((conn1.x - conn2.x)/2));
        } else {
            return ((int) (Math.abs((conn1.y  - conn2.y)/2)  + Math.abs((conn1.x - conn2.x)/2)));
        }
    }

    /*
    COMMUNICATION
     */
//    public void sendMessage(MessageContents message, Point destination) {
//        if (neighbour1.getConnection().equals(destination)) {
//            super.sendMessage(message, neighbour1.getAgent());
//        } else if (neighbour2.getConnection().equals(destination)) {
//            super.sendMessage(message, neighbour2.getAgent());
//        } else {
//            System.out.println("ROAD AGENT | SENDING MESSAGE TO DESTINATION THAT HAS NO CONNECTION WITH THIS AGENT");
//        }
//    }

    /*
    AGV AGENT
     */
//    public boolean isValidRegistration(AGVAgent.AGVAgent agent) {
//        return schedule.isValidRegistration(agent.getName());
//    }
//
//    @Override
//    public void registerAGVAgent(AGVAgent.AGVAgent vehicle) {
//        for (AGVAgent.AGVAgent agent: agents) {
//            if (agent.equals(vehicle)) {
//                System.out.println("ROAD AGENT | AGV AGENT ALREADY REGISTERED");
//            }
//        }
//        if (getCapacity() <= agents.size()){
//            System.out.println("ROAD AGENT | FULL CAPACITY ALREADY REACHED");
//        } else if (! isValidRegistration(vehicle)) {
//            System.out.println("ROAD AGENT | THIS AGV AGENT CAN NOT REGISTER AT THIS MOMENT");
//        } else {
//            agents.add(vehicle);
//        }
//    }
//
//    @Override
//    public void unregisterAGVAgent(AGVAgent.AGVAgent vehicle) {
//        boolean found = false;
//        for (AGVAgent.AGVAgent agent: agents) {
//            if (agent.equals(vehicle)) {
//                found = true;
//            }
//        }
//        if (found) {
//            agents.remove(vehicle);
//        } else {
//            System.out.println("ROAD AGENT | THIS AGV AGENT IS NOT REGISTERED HERE");
//        }
//    }

    /*
    SCHEDULE
     */
//    public List<FreeSlot> getFreeSlots(Point entryPoint, Point exitPoint) {
//        List<FreeSlot> freeslots = schedule.computeFreeSlots(entryPoint, exitPoint);
//        List<FreeSlot> result = new ArrayList<>();
//        for(FreeSlot slot: freeslots) {
//            if (slot.getEndTime() - slot.getStartTime() >= getTravelTime()) {
//                result.add(slot);
//            }
//        }
//        return result;
//    }
//
//    public boolean makeReservation(int start , int end, String vehicleName, Point entry, Point exit) {
//        return schedule.makeReservation(start, end, vehicleName, entry, exit);
//    }

    /*
    TICK LISTENENER
     */
    @Override
    public void tick(TimeLapse timeLapse) {
//        schedule.evaporate();
    }

    @Override
    public String toString() {
        return "RoadAgent: " + neighbor1.toString() + " + " +neighbor2.toString();
    }
}
