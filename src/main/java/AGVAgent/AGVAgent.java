package AGVAgent;

import Ant.ExplorationAnt;
import CommunicationManager.AGVCommunication;
import DelegateMAS.ExplorationMAS;
import DelegateMAS.IntentionEntry;
import DelegateMAS.IntentionMAS;
import Order.Order;
import ResourceAgent.ResourceAgent;
import ResourceAgent.PDAgent;
import com.github.rinde.rinsim.core.model.comm.CommDeviceBuilder;
import com.github.rinde.rinsim.core.model.comm.CommUser;
import com.github.rinde.rinsim.core.model.pdp.Vehicle;
import com.github.rinde.rinsim.core.model.pdp.VehicleDTO;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import com.google.common.base.Optional;
import org.apache.commons.math3.random.RandomGenerator;

import java.util.List;

//import com.github.rinde.rinsim.core.model.comm.MessageContents;
//import ResourceAgent.PDAgent;
//import com.github.rinde.rinsim.core.model.pdp.Vehicle;
//import com.github.rinde.rinsim.core.model.pdp.VehicleDTO;
//import com.github.rinde.rinsim.core.model.road.RoadModel;
//import com.github.rinde.rinsim.core.model.pdp.VehicleDTO;
//import com.github.rinde.rinsim.core.model.time.TimeLapse;
//import com.github.rinde.rinsim.geom.Point;
//import java.util.ArrayList;
//import java.util.List;
//
//import Ants.*;
//import ResourceAgent.PDAgent;
//import ResourceAgent.ResourceAgent;

//public class AGVAgent.AGVAgent extends Vehicle {
//
//    private static double SPEED = 2d;
//
//    private String name;
//
//    private ResourceAgent resource;
//
//    private List<Route> possibleRoutes;
//    private Path path;
//
//    private Order load;
//
//
//    AGVAgent.AGVAgent(String name, PDAgent start) {
//        super(VehicleDTO.builder()
//                .startPosition(start.getConnection())
//                .speed(SPEED)
//                .build());
//        this.name = name;
//        this.resource = start;
//        this.possibleRoutes = new ArrayList<>();
//        start.registerAGVAgent(this);
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    private boolean hasGoal() {
//        return (path != null);
//    }
//
//    private boolean hasLoad() {
//        return (load != null);
//    }
//
//    private Route getRouteWithEarliestArrival() {
//        if (possibleRoutes.isEmpty()) {
//            System.out.println("AGV AGENT | NO POSSIBLE ROUTES TO SELECT ONE FROM");
//            return null;
//        } else {
//            Route best = null;
//            int earliestArrival = Integer.MAX_VALUE;
//            for (Route possRoute: possibleRoutes) {
//                if (possRoute.getEarliestArrival() < earliestArrival) {
//                    best = possRoute;
//                    earliestArrival = possRoute.getEarliestArrival();
//                }
//            }
//            return best;
//        }
//    }
//
//    private void removeRouteWithEarliestArrival() {
//        Route best = getRouteWithEarliestArrival();
//        possibleRoutes.remove(best);
//    }
//
//    private void retractPickups(List<Route> routes) {
//        for (Route route: routes) {
//            RetractInteractionMsg msg = new RetractInteractionMsg(
//                    route.getEarliestArrival(), name, route.transformToPath());
//            if (resource.getClass() == PDAgent.class) {
//                ((PDAgent) resource).sendMessageToNeighbour(msg);
//            } else {
//                System.out.println("AGV AGENT | RETRACTING ROUTES FROM AGENT OTHER THEN PD AGENT");
//            }
//        }
//    }
//
//    public void receiveRandomExplorationAnt(RandomExplorationAnt ant) {
//        if (ant.hasOrderFound()) {
//            System.out.println("Order found!");
//            if (ant.getNbValidRoutes() > 0) {
//                possibleRoutes.addAll(ant.getValidRoutes());
//            } else {
//                System.out.println("Immediately picking up load");
//                pickupLoad();
//            }
//        } else {
//            System.out.println("NO ORDER FOUND");
//        }
//    }
//
//    public void receiveTypeExplorationAnt(TypeExplorationAnt ant) {
//        if (ant.hasTypeFound()) {
//            System.out.println("machine of wanted type found!!");
//            possibleRoutes.addAll(ant.getValidRoutes());
//        } else {
//            System.out.println("NO machine of wanted type found");
//            System.out.println(ant.getType());
//        }
//    }
//
//    public void receivePointExplorationAnt(PointExplorationAnt ant) {
//        if (ant.hasPointFound()) {
//            System.out.println("PD Agent with connection found");
//            possibleRoutes.addAll(ant.getValidRoutes());
//        } else {
//            System.out.println("NO PD Agent with connection found");
//            System.out.println(load.getDeliveryLocation().getConnection());
//        }
//    }
//
//    public void receiveIntentionAnt(IntentionAnt ant) {
//        if (ant.hasSucceeded()) {
//            System.out.println("Intention ant has succeeded!");
//            path = ant.getPath();
//            removeRouteWithEarliestArrival();
//            retractPickups(possibleRoutes);
//            possibleRoutes = new ArrayList<>();
//            if (resource.getClass() == PDAgent.class) {
//                PDAgent current = (PDAgent) resource;
//                ResourceAgent newResource = current.getNeighbour();
//                current.unregisterAGVAgent(this);
//                newResource.registerAGVAgent(this);
//                resource = newResource;
//            } else {
//                System.out.println("AGV AGENT | STARTING PATH FROM OTHER AGENT THAN PD AGENT");
//            }
//        } else {
//            System.out.println("Intention ant did not succeed");
//            if (! ant.isRejected()) {
//                System.out.println("not rejected, sending retract msg");
//                RetractInteractionMsg msg = new RetractAcceptedInteractionMsg(ant.getPath().getArrivalTime(),
//                        name, ant.getPath());
//                ((PDAgent) resource).sendMessageToNeighbour(msg);
//            }
//        }
//    }
//
//    public void receiveMessage(MessageContents message) {
//        if (message.getClass() == RandomExplorationAnt.class) {
//            receiveRandomExplorationAnt((RandomExplorationAnt) message);
//        } else if (message.getClass() == IntentionAnt.class) {
//            receiveIntentionAnt((IntentionAnt) message);
//        } else if (message.getClass() == TypeExplorationAnt.class){
//            receiveTypeExplorationAnt((TypeExplorationAnt) message);
//        } else if (message.getClass() == PointExplorationAnt.class) {
//            receivePointExplorationAnt((PointExplorationAnt) message);
//        } else if (message.getClass() == RequestAfterTickMsg.class) {
//            handleAfterTick();
//        } else {
//            System.out.println("AGV AGENT | RECEIVED MESSAGE DIFFERENT FROM KNOWN ANTS");
//        }
//    }
//
//    private void sendRandomExplorationAnt() {
//        System.out.println("sending random exploration ant");
//        if (resource.getClass() == PDAgent.class) {
//            PDAgent pd = (PDAgent) resource;
//            RandomExplorationAnt ant = new RandomExplorationAnt(name, Integer.toString(0));
//            pd.receiveMessage(ant);
//        } else {
//            System.out.println("AGV AGENT | SENDING EXPLORATION ANT TO AGENT OTHER THAN PD AGENT: NOT YET IMPLEMENTED...");
//            // TODO: implement
//        }
//    }
//
//    private void sendTypeExplorationAnt() {
//        System.out.println("sending type exploration ant");
//        if (resource.getClass() == PDAgent.class) {
//            PDAgent pd = (PDAgent) resource;
//            TypeExplorationAnt ant = new TypeExplorationAnt(name, "Type" + 0, load.getNextStop());
//            pd.receiveMessage(ant);
//        } else {
//            System.out.println("AGV AGENT | SENDING EXPLORATION ANT TO AGENT OTHER THAN PD AGENT: NOT YET IMPLEMENTED...");
//            // TODO: implement
//        }
//    }
//
//    private void sendPointExplorationAnt(Point point) {
//        System.out.println("sending point exploration ant");
//        if (resource.getClass() == PDAgent.class) {
//            PDAgent pd = (PDAgent) resource;
//            PointExplorationAnt ant = new PointExplorationAnt(name, "Point" + 0, point);
//            pd.receiveMessage(ant);
//        } else {
//            System.out.println("AGV AGENT | SENDING EXPLORATION ANT TO AGENT OTHER THAN PD AGENT: NOT YET IMPLEMENTED...");
//            // TODO: implement
//        }
//    }
//
//    private void sendIntentionAnt(Path possiblePath, boolean pickup) {
//        System.out.println(name + ": sending intention ant");
//        if (resource.getClass() == PDAgent.class) {
//            PDAgent pd = (PDAgent) resource;
//            IntentionAnt ant = new IntentionAnt(name, possiblePath, pickup);
//            pd.receiveMessage(ant);
//        } else {
//            System.out.println("AGV AGENT | SENDING INTENTION ANT TO AGENT OTHER THAN PD AGENT: NOT YET IMPLEMENTED...");
//            // TODO: implement
//        }
//    }
//
//    private void deliverLoad() {
//        System.out.println("delivering load");
//        if( resource.getClass() == PDAgent.class) {
//            ((PDAgent) resource).deliverOrder(load);
//            load = null;
//        } else {
//            System.out.println("AGV AGENT | NO PD AGENT TO DELIVER THE LOAD TO");
//        }
//    }
//
//    private void pickupLoad() {
//        System.out.println("picking up load");
//        if( resource.getClass() == PDAgent.class) {
//            load = ((PDAgent) resource).pickupOrder();
//            if (load.isDeliveryNextStop()) {
//                sendPointExplorationAnt(load.getDeliveryLocation().getConnection());
//            } else {
//                sendTypeExplorationAnt();
//            }
//        } else {
//            System.out.println("AGV AGENT | NO PD AGENT TO PICK UP THE LOAD FROM");
//        }
//    }
//
//    @Override
//    protected void tickImpl(TimeLapse time) {
///*        System.out.println(name + " handle tick");*/
//        RoadModel rm = getRoadModel();
//        if (! hasGoal()) {
//            System.out.println("No goal");
//            if (! hasLoad()) {
//                sendRandomExplorationAnt();
//            } else {
//                if (load.isDeliveryNextStop()) {
//                    sendPointExplorationAnt(load.getDeliveryLocation().getConnection());
//                } else {
//                    sendTypeExplorationAnt();
//                }
//            }
//        } else {
//            path.evolveTime();
//            Point dest = path.getNextDestination();
//            rm.moveTo(this, dest, time);
//            if (rm.getPosition(this).equals(dest)
//                    && path.readyToProceed()) {
//                resource.unregisterAGVAgent(this);
//                path.checkpointReached();
//                resource = resource.getNeighbour(dest);
//                resource.registerAGVAgent(this);
//                if (path.isEmpty()) {
//                    path = null;
//                    if (hasLoad()) {
//                        deliverLoad();
//                    } else {
//                        pickupLoad();
//                    }
//                }
//            }
//        }
///*        System.out.println();*/
//    }
//
//    private void handleAfterTick() {
///*        System.out.println(name + " handle after tick");*/
//        if (!hasGoal()) {
//            boolean pickup = ! hasLoad();
//            if (! possibleRoutes.isEmpty()) {
//                while (! possibleRoutes.isEmpty()) {
//                    Route earliest = getRouteWithEarliestArrival();
//                    Path path = earliest.transformToPath();
//                    sendIntentionAnt(path, pickup);
//                    if (! possibleRoutes.isEmpty()) {
//                        if (possibleRoutes.contains(earliest)) {
//                            possibleRoutes.remove(earliest);
//                        } else {
//                            System.out.println("AGV AGENT | POSSIBLE ROUTES NOT EMPTY BUT EARLIEST ROUTE NO LONGER PRESENT");
//                        }
//                    }
//                }
//            } else {
//                System.out.println("AGV AGENT | NO GOAL BUT NO POSSIBLE ROUTES");
//            }
//        }
///*        System.out.println();*/
//    }
//
//    @Override
//    public void afterTick(TimeLapse time) {
//        handleAfterTick();
//    }
//}

public class AGVAgent extends Vehicle implements CommUser {

    private static double agvSpeed = 2d;

    /*
    Counter that is used to give every resource a
        unique ID.
     */
    private static int idCounter = 0;

    /*
    A unique ID among AGV agents.
     */
    private int agvId;

    /*
    The resource agent that this agent is
        currently traversing.
     */
    private ResourceAgent currentResource;

    /*
    The communication manager of this AGV agent.
        The manager handles all the contact with
        other entities in the world.
     */
    private AGVCommunication commManager;

    /*
    The exploration MAS module of this AGV agent.
        The module is used to regularly update the
        beliefs about the state of the world and
        to change the desires of this agent accordingly.
     */
    private ExplorationMAS explorationMAS;

    /*
    The intention MAS module of this AGV agent.
        The module is used to regularly consider
        to change intention based on changing beliefs
        from the exploration MAS module.
     */
    private IntentionMAS intentionMAS;

    /*
    The order that this AGV agent might carry.
     */
    private Optional<Order> order;

    /*
    The random generator of this agv agent.
        Used to generate random numbers.
     */
    private RandomGenerator randomGenerator;

    /*
    The number of ticks before one time unit passes.
     */
    private static int timeAdvancementFrequency = 100;

    /*
    Counter that counts the number of ticks.
     */
    private int tickCounter;

    /*
    The current destination of this agv agent.
     */
    private Point currentDestination;


    /*
    Constructor
     */
    public AGVAgent(PDAgent firstResource, RandomGenerator randomGenerator) {
        super(VehicleDTO.builder()
                .startPosition(firstResource.getConnection())
                .speed(getAgvSpeed())
                .build());

        this.agvId = next();
        this.explorationMAS = new ExplorationMAS(this);
        this.order = Optional.absent();
        this.currentResource = firstResource;
        firstResource.getCommunicationManager().makeInitialReservation(
                getCurrentTime(), ExplorationAnt.getLookAheadTime(),
                getAgvId());
        firstResource.registerAgvAgent(this);
        this.intentionMAS = new IntentionMAS(this,
                new IntentionEntry(firstResource.getResourceId(),
                        getCurrentTime(), ExplorationAnt.getLookAheadTime()));
        this.currentDestination = firstResource.getConnection();
        this.randomGenerator = randomGenerator;
        this.tickCounter = 0;
    }


    /*
    AGV Speed
     */
    private static double getAgvSpeed() {
        return agvSpeed;
    }

    /*
    AGV ID
     */
    private static int next() {
        idCounter += 1;
        return idCounter;
    }

    public int getAgvId() {
        return agvId;
    }

    /*
    Communication Manager
     */
    @Override
    public Optional<Point> getPosition() {
        return Optional.of(getRoadModel().getPosition(this));
    }

    @Override
    public void setCommDevice(CommDeviceBuilder commDeviceBuilder) {
        commManager = new AGVCommunication(this, commDeviceBuilder.build());
    }

    public AGVCommunication getCommunicationManager() {
        return commManager;
    }

    /*
    Exploration MAS
     */
    public ExplorationMAS getExplorationMAS() {
        return explorationMAS;
    }

    /*
    Intention MAS
     */
    public IntentionMAS getIntentionMAS() {
        return intentionMAS;
    }

    /*
    Order
     */
    public boolean carriesOrder() {
        return order.isPresent();
    }

    public Order getOrder() {
        if (! carriesOrder()) {
            throw new IllegalStateException(
                    "AGV AGENT | THIS AGENT DOES NOT CARRY ANY ORDER"
            );
        }
        return order.get();
    }

    private Order deliverOrder() {
        Order current = getOrder();
        order = Optional.absent();
        return current;
    }

    public List<Integer> getOrderDestinations() {
        return getOrder().getDestinations();
    }

    private void visitCheckPoint(ResourceAgent agent) {
        assert(agent.isPDAgent());
        PDAgent pdAgent = (PDAgent) agent;
        if (pdAgent.isStorageSpace()) {
            assert(! carriesOrder());
            pickupOrder(pdAgent.pickupOrder());
        }
        getOrder().visitDestination(agent.getResourceId());
        if (pdAgent.isDeliveryPoint()) {
            assert(getOrder().allDestinationsVisited());
            pdAgent.acceptOrderDelivery(deliverOrder());
        }
    }

    private void pickupOrder(Order newOrder) {
        if (carriesOrder()) {
            throw new IllegalStateException(
                    "AGV AGENT | THIS AGENT IS ALREADY CARRYING A ORDER"
            );
        }
        order = Optional.of(newOrder);
    }

    private void showOrder() {
        if (carriesOrder()) {
            getOrder().showAtSamePosition(this);
        }
    }

    /*
    Resource Agent
     */
    public ResourceAgent getCurrentResource() {
        return currentResource;
    }

    private void setCurrentResource(ResourceAgent agent) {
        currentResource = agent;
    }

    /*
    Random Generator
     */
    public RandomGenerator getRandomGenerator() {
        return randomGenerator;
    }

    /*
    Tick
     */
    private boolean tickCounterEqualsFrequency() {
        return getTickCounter() % getTimeAdvancementFrequency() == 0;
    }

    private static int getTimeAdvancementFrequency() {
        return timeAdvancementFrequency;
    }

    private int getTickCounter() {
        return tickCounter;
    }

    public int getCurrentTime() {
        return (getTickCounter() / getTimeAdvancementFrequency());
    }

    private void incrementTickCounter() {
        tickCounter += 1;
    }

    @Override
    protected void tickImpl(TimeLapse timeLapse) {
        showOrder();
        explorationMAS.tick();
        intentionMAS.tick();
        incrementTickCounter();

        System.out.println("AGV " + getAgvId() + " move to" + getCurrentDestination());
        getRoadModel().moveTo(this, getCurrentDestination(), timeLapse);
        if (tickCounterEqualsFrequency() &&  readyToChangeToNextDestination()) {
            changeToNextDestination();
        }
    }

    private boolean readyToChangeToNextDestination() {
        return (atNextDestination() &&
                getIntentionMAS().readyToChangeToNextEntry(getCurrentTime()));
    }

    /*
    Current Destination
     */
    private boolean atNextDestination() {
        return getCurrentDestination().equals(getRoadModel().getPosition(this));
    }

    private Point getCurrentDestination() {
        return currentDestination;
    }

    private void changeToNextDestination() {
        getIntentionMAS().changeToNextDestination();

        if (getIntentionMAS().getNbOfEntries() == 0) {
            throw new IllegalStateException(
                    "AGV AGENT | CURRENT INTENTION HAS NO MORE ENTRIES"
            );
        } else {
            // change resource agent
            int firstId = getIntentionMAS().getIdOfFirstEntry();
            currentResource.unregisterAgvAgent(getAgvId());
            setCurrentResource(
                    getCurrentResource().getNeighborAgent(firstId)
            );
            currentResource.registerAgvAgent(this);

            // if resource agent is pd agent, handle checkpoint visit
            if (currentResource.isPDAgent()) {
                visitCheckPoint(currentResource);
            }

            // change destination
            int secondId = getIntentionMAS().getIdOfSecondEntry();
            setNextDestination(
                    getCurrentResource().getConnectionWithNeighbor(secondId)
            );
        }
    }

    private void setNextDestination(Point next) {
        currentDestination = next;
    }
}
