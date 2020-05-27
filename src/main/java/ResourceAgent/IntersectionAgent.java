package ResourceAgent;

import ResourceAgent.Schedule.Schedule;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

//import AGVAgent.AGVAgent;
//import Ants.ExplorationAnt;
//import Ants.RandomExplorationAnt;
//import Ants.TypeExplorationAnt;

public class IntersectionAgent extends ResourceAgent {

    private List<Neighbor> neighbours;
    private Schedule schedule;

//    private List<ExplorationAnt> ants;
//    private List<AGVAgent.AGVAgent> agents;


    public IntersectionAgent(Point vertex1, Point vertex2, Point vertex3)
            throws NullPointerException {
        if (vertex1 == null || vertex2 == null || vertex3 == null) {
            throw new NullPointerException(
                    "INTERSECTION AGENT | ONE OF THE GIVEN VERTICES EQUAL TO NULL"
            );
        }

        neighbours = new ArrayList<>();
        neighbours.add(new Neighbor(vertex1));
        neighbours.add(new Neighbor(vertex2));
        neighbours.add(new Neighbor(vertex3));

//        ants = new ArrayList<>();
//        agents = new ArrayList<>();
        // TODO: change to actual capacity
//        schedule = new Schedule(1);
    }

    public IntersectionAgent(Point vertex1, Point vertex2, Point vertex3, Point vertex4) {
        this(vertex1, vertex2, vertex3);
        neighbours.add(new Neighbor(vertex4));
    }


    /*
    TYPES
     */
    @Override
    public boolean isIntersectionAgent() {
        return true;
    }

    /*
    NEIGHBOURS
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

    @Override
    public ResourceAgent getNeighbor(Point point)
            throws IllegalArgumentException {
        for (Neighbor neighbor: neighbours) {
            if (neighbor.isValidConnection(point)) {
                return neighbor.getAgent();
            }
        }
        throw new IllegalArgumentException(
                "INTERSECTION AGENT | NO NEIGHBOUR WITH GIVEN CONNECTION PRESENT"
        );
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

    public int getTravelTime() {
        return 2;
    }

    /*
    ANTS
     */
//    public ExplorationAnt getAnt(String name) {
//        ExplorationAnt result = null;
//        for (ExplorationAnt ant: ants) {
//            if (ant.getName().equals(name)) {
//                result = ant;
//            }
//        }
//        if (result == null) {
//            System.out.println("INTERSECTION AGENT | NO ANT WITH THAT NAME REGISTERED");
//        }
//        return result;
//    }
//
//    public boolean isRegistered(ExplorationAnt ant) {
//        for (ExplorationAnt registeredAnt: ants) {
//            if (registeredAnt.getCreator().equals(ant.getCreator())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public void registerAnt(ExplorationAnt ant) {
//        if (ant == null) {
//            System.out.println("INTERSECTION AGENT | REGISTERING NULL");
//        } else {
//            for (ExplorationAnt registeredAnt: ants) {
//                if (registeredAnt.getName().equals(ant.getName())) {
//                    System.out.println("INTERSECTION AGENT | ANT ALREADY REGISTERED");
//                    break;
//                }
//            }
//            ants.add(ant);
//        }
//    }
//
//    public void unregisterAnt(ExplorationAnt ant) {
//        if (ant == null) {
//            System.out.println("INTERSECTION AGENT | UNREGISTERING NULL");
//        } else {
//            boolean found = false;
//            for (ExplorationAnt registeredAnt: ants) {
//                if (registeredAnt.getName().equals(ant.getName())) {
//                    found = true;
//                    break;
//                }
//            }
//            if (found) {
//                ants.remove(ant);
//            } else {
//                System.out.println("INTERSECTION AGENT | GIVEN ANT IS NOT REGISTERED");
//            }
//        }
//    }

    /*
    Communication
     */
//    public void sendMessage(MessageContents message, Point connection) {
//        Neighbour destination = null;
//        for (Neighbour neighbour: neighbours) {
//            if(neighbour.getConnection().equals(connection)) {
//                destination = neighbour;
//                break;
//            }
//        }
//        if (destination == null) {
//            System.out.println("INTERSECTION AGENT | SENDING MESSAGE TO DESTINATION THAT HAS NO CONNECTION WITH THIS AGENT");
//        } else {
//            super.sendMessage(message, destination.getAgent());
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
//    public List<FreeSlot> getFreeSlots(Point entryPoint, Point exitPoint){
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
        String str =  "IntersectionAgent: ";
        for (Neighbor neighbor: neighbours) {
            str = str.concat(neighbor.toString() + " + ");
        }
        return str;
    }
}
