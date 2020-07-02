package Ant;

import CommunicationManager.AGVCommunication;
import CommunicationManager.IntersectionCommunication;
import CommunicationManager.PDCommunication;
import CommunicationManager.RoadCommunication;
import DelegateMAS.Route;
import ResourceAgent.Schedule.PossibleReservation;
import com.google.common.base.Optional;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;
import java.util.ArrayList;
import java.util.List;


//public abstract class ExplorationAnt implements MessageContents {
//
//    private String creator;
//    private String parentName;
//    private String name;
//
//    private int depth;
//
//    private Point start;
//    private List<Route> routes;
//    private List<Route> validRoutes;
//
//
//    public ExplorationAnt(String creator, String parentName, String name, int depth) {
//        this.creator = creator;
//        this.parentName = parentName;
//        this.name = name;
//
//        this.depth = depth;
//        if (depth < 0) {
//            System.out.println("EXPLORATION ANT | RECURSION DEPTH SMALLER THAN ZERO");
//        }
//
//        routes = new ArrayList<>();
//        validRoutes = new ArrayList<>();
//    }
//
//    public String getCreator() {
//        return creator;
//    }
//
//    public String getParentName() {
//        return parentName;
//    }
//
//    public String getName() {
//        return parentName + name;
//    }
//
//    public Point getStartPoint() {
//        return start;
//    }
//
//    public int getDepth() {
//        return depth;
//    }
//
//    public void reduceDepth() {
//        depth -= 1;
//    }
//
//    public void setStartPoint(Point start) {
//        this.start = start;
//    }
//
//    public boolean hasRoutes() {
//        return (! routes.isEmpty());
//    }
//
//    public Point getEntryPoint() {
//        if (! hasRoutes()) {
//            return start;
//        }
//        return routes.get(0).getLastExit();
//    }
//
//    public List<Route> getRoutes() {
//        return routes;
//    }
//
//    public Route getBestRoute() {
//        Route best = null;
//        int earliest = Integer.MAX_VALUE;
//        for (Route route: getRoutes()) {
//            if (route.getEarliestArrival() < earliest) {
//                best = route;
//                earliest = route.getEarliestArrival();
//            }
//        }
//        if (best == null) {
//            System.out.println("RANDOM EXPLORATION ANT | NO BEST ROUTE FOUND");
//        }
//        return best;
//    }
//
//    public int getNbValidRoutes() {
//        return validRoutes.size();
//    }
//
//    public List<Route> getValidRoutes() {
//        return validRoutes;
//    }
//
//    public Path getPathWithEarliestArrival() {
//        if (validRoutes.size() == 0) {
//            System.out.println("RANDOM EXPLORATION ANT | NO VALID ROUTES PRESENT");
//            return null;
//        } else {
//            int earliestArrival = Integer.MAX_VALUE;
//            Route bestRoute = null;
//            for (Route validroute: validRoutes) {
//                int arrival = validroute.getEarliestArrival();
//                if (arrival < earliestArrival) {
//                    earliestArrival = arrival;
//                    bestRoute = validroute;
//                }
//            }
//            return bestRoute.transformToPath();
//        }
//    }
//
//    public void addValidRoute(Route validRoute) {
//        if (validRoute == null) {
//            System.out.println("EXPLORATION ANT | ADDING NULL ROUTE AS VALID ROUTE");
//        } else {
//            validRoutes.add(validRoute);
//        }
//    }
//
//    public List<Route> addFreeSlotsToRoutes(List<FreeSlot> freeslots, Point entry, Point exit, int traveltime) {
//        List<Route> newroutes = new ArrayList<>();
//        if (routes.size() > 0) {
//            for (FreeSlot slot : freeslots) {
//                for (Route route : routes) {
//                    if (route.isConsistent(slot, traveltime)) {
//                        Route copy = route.copy();
//                        copy.addEntry(slot, traveltime, entry, exit);
//                        newroutes.add(copy);
//                    }
//                }
//            }
//        } else {
//            for (FreeSlot slot: freeslots) {
//                Route newroute = new Route();
//                newroute.addEntry(slot, traveltime, entry, exit);
//                newroutes.add(newroute);
//            }
//        }
//        return newroutes;
//    }
//
//    public void setRoutes(List<Route> routes) {
//        this.routes = routes;
//    }
//
//    public abstract void inspectPDAgent(PDAgent agent);
//
//    public abstract void inspectRoadAgent(RoadAgent agent);
//
//    public abstract void inspectIntersectionAgent(IntersectionAgent agent);
//
//    public void inspectAgent(ResourceAgent agent) {
//        if (MASProject.DEBUG) {
//            System.out.println(creator + " " + parentName + " " + name);
//            System.out.println("depth: " + depth);
//            System.out.println(agent.getClass());
//        }
//        if (agent.getClass() == PDAgent.class) {
//            inspectPDAgent((PDAgent) agent);
//        } else if (agent.getClass() == RoadAgent.class) {
//            inspectRoadAgent((RoadAgent) agent);
//        } else if (agent.getClass() == IntersectionAgent.class) {
//            inspectIntersectionAgent((IntersectionAgent) agent);
//        } else {
//            System.out.println("RANDOM EXPLORATION AND | AGENT DIFFERENT THAN PD, ROAD OR INTERSECTION");
//        }
//    }
//}

public class ExplorationAnt extends AbstractAnt {

    /*
    The look ahead time of exploration ants.
        Exploration ants wil explore routes
        until there route exceeds this time.
     */
    private static int lookAheadTime = 40;

    /*
    The route that this exploration ant has explored.
        The route is a list of pairs that link the id
        of a resource agent to the possible reservation
        that could be made at that resource agent.
     */
    private Route route;

    /*
    The ID of the AGV Agent that created this
        exploration ant.
     */
    private int agvId;

    /*
    The destinations that this exploration agent
        should try to visit. When the exploration
        ant searches for a packages, the optional
        will be empty.
     */
    private Optional<List<Integer>> orderDestinations;

    /*
    Random generator, used to generate random numbers.
     */
    private RandomGenerator randomGenerator;

    /*
    Boolean reflecting if the ant is going forward on
        exploration or going backward to the AGV agent
        that created this ant.
     */
    private boolean goingForward;

    /*
    The time when the ant was created.
     */
    private int startTime;


    /*
    Constructor
     */
    public ExplorationAnt(int agvId, RandomGenerator rgn, int currentTime) {
        this.route = new Route();
        this.agvId = agvId;
        this.orderDestinations = Optional.absent();
        this.randomGenerator = rgn;
        this.goingForward = true;
        this.startTime = currentTime;
    }

    public ExplorationAnt(int agvId, List<Integer> orderDestinations, RandomGenerator rgn, int currentTime) {
        this.route = new Route();
        this.agvId = agvId;
        this.orderDestinations = Optional.of(orderDestinations);
        this.randomGenerator = rgn;
        this.goingForward = true;
        this.startTime = currentTime;
    }

    public ExplorationAnt(ExplorationAnt other) {
        super(other);
        this.route = other.getRoute().copyRoute();
        this.agvId = other.getAgvId();
        if (other.isSearchingPackage()) {
            this.orderDestinations = Optional.absent();
        } else {
            List<Integer> destinations = new ArrayList<>(other.getOrderDestinations());
            this.orderDestinations = Optional.of(destinations);
        }
        this.randomGenerator = other.getRandomGenerator();
        this.goingForward = true;
        this.startTime = other.startTime;
    }


    /*
    Look Ahead Time
     */
    private static boolean isValidLookAheadTime(int time) {
        return time > 0;
    }

    public static int getLookAheadTime() {
        return lookAheadTime;
    }

    public int getExplorationTime() {
        return startTime + getLookAheadTime();
    }

    public static void setLookAheadTime(int time)
            throws IllegalArgumentException {
        if (! isValidLookAheadTime(time)) {
            throw new IllegalArgumentException(
                    "EXPLORATION ANT | THE GIVEN LOOK AHEAD TIME IS NOT A VALID ONE"
            );
        }
        lookAheadTime = time;
    }

    /*
    AGV ID
     */
    public int getAgvId() {
        return agvId;
    }

    /*
    Route
     */
    private int getNextEarliestArrival() {
        if (route.getRouteLength() == 0) {
            return startTime;
        } else {
            return route.getNextEarliestArrival();
        }
    }

    protected Route getRoute() {
        return route;
    }

    protected void addRouteSlot(int resourceId, PossibleReservation possRes) {
        route.addRouteSlot(resourceId, possRes);
    }

    private void filterPossibleReservations(List<PossibleReservation> possibleReservations) {
        route.filterPossibleReservations(possibleReservations);
    }

    private void addCheckpointVisitToRoute(int arrivalTime) {
        route.addCheckpointVisit(arrivalTime);
    }

    /*
    Order Destinations
     */
    protected boolean isSearchingPackage() {
        return ! orderDestinations.isPresent();
    }

    private int getNumberOfOrderDestinations() {
        return getOrderDestinations().size();
    }

    protected List<Integer> getOrderDestinations() {
        return orderDestinations.get();
    }

    private int getNextDestinationId() {
        return getOrderDestinations().get(0);
    }

    private void visitOrderDestination(int destination)
            throws IllegalArgumentException {
        if (getNextDestinationId() != destination) {
            throw new IllegalArgumentException(
                    "EXPLORATION ANT | THE VISITED DESTINATION IS NOT THE RIGHT ONE"
            );
        }
        addCheckpointVisitToRoute(getNextEarliestArrival());
        getOrderDestinations().remove(0);
        if (getNumberOfOrderDestinations() == 0) {
            orderDestinations = Optional.absent();
        }
    }

    private void setOrderDestinations(List<Integer> newDestinations) {
        orderDestinations = Optional.of(newDestinations);
    }

    /*
    Random Generator
     */
    protected RandomGenerator getRandomGenerator() {
        return randomGenerator;
    }

    private float nextRandomNumber() {
        return randomGenerator.nextFloat();
    }

    /*
    Going Forward
     */
    public boolean isGoingForward() {
        return goingForward;
    }

    private void changeToGoingBackwards()
            throws IllegalStateException {
        if (! goingForward) {
            throw new IllegalStateException(
                    "EXPLORATION ANT | THE ANT IS ALREADY GOING BACKWARDS"
            );
        }
        System.out.println("Exploration ant changing direction: agvId " + getAgvId());
        System.out.println("Nb of visited resources: " + getNbOfVisitedResource());
        goingForward = false;
    }

    /*
    Road Agent
     */
    @Override
    public void visitRoadAgent(RoadCommunication roadCommunication) {
        System.out.println("Exploration ant -- road agent: agv id " + getAgvId());
        if (isGoingForward()) {
            visitResource(roadCommunication.getResourceId());

            List<PossibleReservation> possReservations = roadCommunication.getPossibleReservations(
                    getNextEarliestArrival(), getExplorationTime(), getAgvId(), lastVisitedResourceId()
            );
            filterPossibleReservations(possReservations);
            if (possReservations.size() > 0) {
                for (PossibleReservation possRes: possReservations) {
                    ExplorationAnt clone = cloneAnt();
                    clone.addRouteSlot(roadCommunication.getResourceId(), possRes);
                    roadCommunication.sendToOtherNeighbor(lastVisitedResourceId(), clone);
                }
            } else {
                changeToGoingBackwards();
                roadCommunication.sendToNeighbor(
                        getPreviousResourceId(roadCommunication.getResourceId()), this
                );
            }
        } else {
            roadCommunication.sendToNeighbor(
                    getPreviousResourceId(roadCommunication.getResourceId()), this
            );
        }
    }

    /*
    Intersection Agent
     */
    @Override
    public void visitIntersectionAgent(IntersectionCommunication intersectionCommunication) {
        System.out.println("Exploration ant -- intersection agent: agv id " + getAgvId());
        if (isGoingForward()) {
            visitResource(intersectionCommunication.getResourceId());

            List<Pair<Integer, Integer>> minDistances;
            if (isSearchingPackage()) {
                System.out.println("searching package");
                minDistances = intersectionCommunication.getMinDistancesToOrder(lastVisitedResourceId());
            } else {
                System.out.println("searching pd location");
                minDistances = intersectionCommunication.getMinDistancesToResource(
                        getNextDestinationId(), lastVisitedResourceId()
                );
            }

            boolean possReservationsFounded = false;
            List<Pair<Integer, Float>> probabilities;
            if (minDistances.size() == 0) {
                List<Integer> ids = intersectionCommunication.
                        getOtherNeighborIds(lastVisitedResourceId());
                probabilities = new ArrayList<>();
                for (Integer id: ids) {
                    probabilities.add(new Pair(id, (float) 1/ids.size()));
                }
            } else {
                probabilities = computeProbabilitiesForCloning(minDistances);
            }
            for (Pair<Integer, Float> pair: probabilities) {
                List<PossibleReservation> possReservations = intersectionCommunication.getPossibleReservations(
                        getNextEarliestArrival(), getExplorationTime(), getAgvId(), pair.getKey()
                );
                filterPossibleReservations(possReservations);
                if (possReservations.size() > 0) {
                    possReservationsFounded = true;
                }
                for (PossibleReservation possRes: possReservations) {
                    if (nextRandomNumber() <= pair.getValue()) {
                        System.out.println("clone ant");
                        ExplorationAnt clone = cloneAnt();
                        clone.addRouteSlot(intersectionCommunication.getResourceId(), possRes);
                        intersectionCommunication.sendMessageToNeighbor(pair.getKey(), cloneAnt());
                    }
                }
            }

            if (! possReservationsFounded) {
                changeToGoingBackwards();
                intersectionCommunication.sendMessageToNeighbor(
                        getPreviousResourceId(intersectionCommunication.getResourceId()), this
                );
            }
        } else {
            intersectionCommunication.sendMessageToNeighbor(
                    getPreviousResourceId(intersectionCommunication.getResourceId()), this
            );
        }
    }

    private List<Pair<Integer, Float>> computeProbabilitiesForCloning(
            List<Pair<Integer, Integer>> minDistances) {
        List<Pair<Integer, Float>> probabilities = new ArrayList<>();

        int minDistance = getMinDistance(minDistances);
        int difference = getMaxDistance(minDistances) - minDistance;
        float minProb = (float) 1/minDistances.size();

        for (Pair<Integer, Integer> pair: minDistances) {
            float prob = minProb;
            if (difference > 0) {
                prob += ((pair.getValue() - minDistance) / (float) difference) * (1-minProb);
            }
            probabilities.add(new Pair(pair.getKey(), prob));
        }
        return probabilities;
    }

    private int getMinDistance(List<Pair<Integer, Integer>> minDistances) {
        int minDistance = Integer.MAX_VALUE;
        for(Pair<Integer, Integer> pair: minDistances) {
            if(pair.getValue() < minDistance) {
                minDistance = pair.getValue();
            }
        }
        return minDistance;
    }

    private int getMaxDistance(List<Pair<Integer, Integer>> minDistances) {
        int maxDistance = 0;
        for(Pair<Integer, Integer>pair: minDistances) {
            if (pair.getValue() < maxDistance) {
                maxDistance = pair.getValue();
            }
        }
        return maxDistance;
    }

    /*
    PD Agent
     */
    @Override
    public void visitPDAgent(PDCommunication pdCommunication) {
        System.out.println("Exploration ant -- PD agent: agv id " + getAgvId());
        if (isGoingForward()) {
            visitResource(pdCommunication.getResourceId());

            if (isSearchingPackage()) {
                if (pdCommunication.harborsOrder()
                        && pdCommunication.couldPickupOrder(getNextEarliestArrival())) {
                    List<PossibleReservation>  possReservations =
                            pdCommunication.getPossibleReservations(
                                    getNextEarliestArrival(), getExplorationTime(), getAgvId()
                            );
                    filterPossibleReservations(possReservations);
                    if (possReservations.size() > 0) {
                        addCheckpointVisitToRoute(getNextEarliestArrival());
                        setOrderDestinations(pdCommunication.getOrderDestinations());
                        for (PossibleReservation possRes: possReservations) {
                            ExplorationAnt clone = cloneAnt();
                            clone.addRouteSlot(pdCommunication.getResourceId(), possRes);
                            pdCommunication.sendMessageToNeighbor(clone);
                        }
                    } else {
                        changeToGoingBackwards();
                        pdCommunication.sendMessageToNeighbor(this);
                    }
                }
            } else {
                if (pdCommunication.matchesResourceId(getNextDestinationId())) {
                    List<PossibleReservation> possReservations =
                            pdCommunication.getPossibleReservations(
                                    getNextEarliestArrival(), getLookAheadTime(), getAgvId()
                            );
                    filterPossibleReservations(possReservations);
                    if (possReservations.size() > 0) {
                        visitOrderDestination(pdCommunication.getResourceId());

                        for (PossibleReservation possRes: possReservations) {
                            ExplorationAnt clone = cloneAnt();
                            clone.addRouteSlot(pdCommunication.getResourceId(), possRes);
                            pdCommunication.sendMessageToNeighbor(clone);
                        }
                    } else {
                        changeToGoingBackwards();
                        pdCommunication.sendMessageToNeighbor(this);
                    }
                }
            }
        } else {
            pdCommunication.sendMessageToAGVAgent(getAgvId(), this);
        }
    }

    /*
    AGV Agent
     */
    @Override
    public void visitAGVAgent(AGVCommunication agvCommunication) {
        if (agvCommunication.matchesAgvId(getAgvId())) {
            agvCommunication.processExploredRoute(getRoute());
        } else {
            throw new IllegalStateException(
                    "EXPLORATION ANT | THE AGV AGENT DID NOT CREATE THIS ANT"
            );
        }
    }

    /*
    Abstract Ant
     */
    @Override
    public ExplorationAnt cloneAnt() {
        return new ExplorationAnt(this);
    }
}
