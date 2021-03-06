package DelegateMAS;

import Ant.FeasibilityAnt;
import Ant.OrderFeasibilityAnt;
import Ant.ResourceFeasibilityAnt;
import ResourceAgent.PDAgent;
import Order.Order;
import ResourceAgent.ResourceAgent;
import com.github.rinde.rinsim.geom.Point;
import com.google.common.base.Optional;

import java.util.List;
import java.util.NoSuchElementException;

public class FeasibilityMAS extends DelegateMAS {

    /*
    The action rate for feasibility delegate
        MAS modules.
     */
    private static int feasibilityActionRate = 20;

    /*
    PD agent that delegates this
        Feasibility MAS.
     */
    private PDAgent agent;

    /*
    The order that this feasibility MAS module
        might harbor.
     */
    private Optional<Order> order;


    /*
    Constructor
     */
    public FeasibilityMAS(PDAgent agent)
            throws NullPointerException {
        super(feasibilityActionRate);

        if (agent == null) {
            throw new NullPointerException(
                    "FEASIBILITY MAS | GIVEN PD AGENT IS NULL"
            );
        }
        this.agent = agent;
        this.order = Optional.absent();
    }


    /*
    Feasibility Action Rate
     */
    private static boolean isValidActionRate(int actionRate) {
        return actionRate > 0;
    }

    public static void setFeasibilityActionRate(int actionRate)
            throws IllegalArgumentException {
        if (! isValidActionRate(actionRate)) {
            throw new IllegalArgumentException(
                    "FEASIBILITY MAS | THE GIVEN ACTION RATE IS INVALID"
            );
        }
        feasibilityActionRate = actionRate;
    }
    
    /*
    Order
     */
    public boolean harborsOrder() {
        return order.isPresent();
    }

    public boolean couldPickupOrder(int pickupTime) {
        if (! harborsOrder()) {
            throw new IllegalStateException(
                    "FEASIBILITY MAS | THIS FEASIBILITY MAS DOES NOT HARBOR A ORDER"
            );
        }
        return getOrder().couldPickUp(pickupTime);
    }

    private int getOrderId() {
        return order.get().getOrderId();
    }

    private Order getOrder() {
        return order.get();
    }

    public List<Integer> getOrderDestinations()
            throws IllegalStateException {
        if (! harborsOrder()) {
            throw new IllegalStateException(
                    "FEASIBILITY MAS | THIS FEASIBILITY MAS DOES NOT HARBOR ANY ORDER"
            );
        }
        return getOrder().getDestinations();
    }

    public Order pickupOrder() {
        Order current = getOrder();
        order = Optional.absent();
        current.pickup();
        return current;
    }

    public void registerOrder(Order anOrder)
            throws IllegalStateException {
        if (harborsOrder()) {
            throw new IllegalStateException(
                    "FEASIBILITY MAS | THIS MODULE ALREADY HARBORS A ORDER"
            );
        }
        order = Optional.of(anOrder);
    }

    public void reservePickup(int pickupTime, int agvId) {
        assert(harborsOrder());
        getOrder().reservePickup(pickupTime, agvId);
    }

    public void showOrder() {
        if (harborsOrder()) {
            getOrder().showAtPosition(getPosition());
        }
    }

    /*
    PD Agent
     */
    private int getResourceId() {
        return agent.getResourceId();
    }

    private Point getPosition() {
        return getResourceAgent().getPosition().get();
    }

    private ResourceAgent getResourceAgent() {
        return agent;
    }

    private void sendAnt(FeasibilityAnt ant) {
        agent.getCommunicationManager().sendMessageToNeighbor(ant);
    }

    /*
    Delegate MAS
     */
    @Override
    protected void takeAction() {
        sendAnt(createResourceAnt());
        if (harborsOrder()) {
            sendAnt(createPackageAnt());
        }
    }

    private ResourceFeasibilityAnt createResourceAnt() {
        return new ResourceFeasibilityAnt(getResourceId());
    }

    private OrderFeasibilityAnt createPackageAnt()
            throws IllegalStateException {
        if (! harborsOrder()) {
            throw new IllegalStateException(
                    "FEASIBILITY MAS | CAN NOT SEND ORDER ANT WHEN NOT HARBORING ORDER"
            );
        }
        return new OrderFeasibilityAnt(getOrderId(), getResourceId());
    }
}
