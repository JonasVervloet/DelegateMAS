package AGVAgent;

import Order.SimpleOrder;
import com.github.rinde.rinsim.core.model.pdp.PDPModel;
import com.github.rinde.rinsim.core.model.pdp.Vehicle;
import com.github.rinde.rinsim.core.model.pdp.VehicleDTO;
import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import com.google.common.base.Optional;
import org.apache.commons.math3.random.RandomGenerator;

import java.util.Set;


/*
A simple AGV agent. This agent chooses a destination
    at random and uses the road model to provide the
    optimal path towards the destination. When the
    destination is reached, the agent selects a new
    destination, again at random.
 */
public class SimpleAGVAgent extends Vehicle {

    /*
    Speed of the AGV agent. Currently all agents
        have the same speed.
     */
    private static double agvSpeed = 2d;

    /*
    The random generator of this agent.
        Used to generate random numbers.
     */
    private RandomGenerator randomGenerator;

    /*
    The destination of this agent.
        It is possible that the agent has no destination.
     */
    private Optional<Point> destination;

    /*
    The order of this agent.
        It is possible that the agent has no order.
     */
    private Optional<SimpleOrder> order;


    /*
    Constructor
     */
    public SimpleAGVAgent(Point startPosition, RandomGenerator aRandomGenerator) {
        super(
                VehicleDTO.builder()
                .startPosition(startPosition)
                .speed(getAgvSpeed())
                .build()
        );

        setRandomGenerator(aRandomGenerator);
        setEmptyDestination();
        setEmptyOrder();
    }


    /*
    AGV Speed
     */
    private static double getAgvSpeed() {
        return agvSpeed;
    }


    /*
    Random Generator
     */
    private RandomGenerator getRandomGenerator() {
        return randomGenerator;
    }

    private void setRandomGenerator(RandomGenerator aRandomGenerator) {
        this.randomGenerator = aRandomGenerator;
    }

    /*
    Destination
     */
    private boolean hasDestination() {
        return destination.isPresent();
    }

    private Point getDestination() {
        return destination.get();
    }

    private void setEmptyDestination() {
        this.destination = Optional.absent();
    }

    private void generateNewDestination(RoadModel roadModel) {
        setDestination(
                roadModel.getRandomPosition(
                        getRandomGenerator()
                )
        );
    }

    private void setDestination(Point aDestination) {
        this.destination = Optional.of(aDestination);
    }

    /*
    Order
     */
    private boolean hasOrder() {
        return order.isPresent();
    }

    private SimpleOrder getOrder() {
        return order.get();
    }

    private SimpleOrder selectOrder(Set<SimpleOrder> orders) {
        SimpleOrder result = null;
        for (SimpleOrder order: orders) {
            result = order;
            break;
        }
        return result;
    }

    private void setEmptyOrder() {
        this.order = Optional.absent();
    }

    private void setOrder(SimpleOrder anOrder) {
        this.order = Optional.of(anOrder);
    }

    /*
    Tick Listener
     */
    @Override
    protected void tickImpl(TimeLapse timeLapse) {
        final RoadModel roadModel = getRoadModel();
        final PDPModel pdpModel = getPDPModel();

        while (timeLapse.hasTimeLeft()) {

            if (! hasOrder()) {
                Set<SimpleOrder> ordersAtLocation = roadModel.getObjectsAt(this, SimpleOrder.class);
                if (ordersAtLocation.size() > 0) {
                    System.out.println("Pickup new order!");
                    SimpleOrder newOrder = selectOrder(ordersAtLocation);
                    pdpModel.pickup(this, newOrder, timeLapse);
                    setOrder(newOrder);
                    setDestination(
                            getOrder().getDeliveryLocation()
                    );
                } else {
                    if (! hasDestination()) {
                        System.out.println("Generate new random destination!");
                        generateNewDestination(roadModel);
                    } else {
                        if (! roadModel.containsObjectAt(this, getDestination())) {
                            roadModel.moveTo(
                                    this, getDestination(), timeLapse
                            );
                        } else {
                            System.out.println("Setting empty destination!");
                            setEmptyDestination();
                        }
                    }
                }
            } else {
                if (! roadModel.containsObjectAt(this, getDestination())) {
                    roadModel.moveTo(
                            this, getDestination(), timeLapse
                    );
                } else {
                    System.out.println("Deliver order!");
                    pdpModel.deliver(this, getOrder(), timeLapse);
                    setEmptyOrder();
                    setEmptyDestination();
                }
            }
        }
    }
}