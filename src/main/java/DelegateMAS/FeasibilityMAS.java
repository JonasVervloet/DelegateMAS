package DelegateMAS;

import Ant.FeasibilityAnt;
import Ant.OrderFeasibilityAnt;
import Ant.ResourceFeasibilityAnt;
import ResourceAgent.PDAgent;
import Order.Order;
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
        try {
            order.get();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
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

    public void registerOrder(Order anOrder)
            throws IllegalStateException {
        if (harborsOrder()) {
            throw new IllegalStateException(
                    "FEASIBILITY MAS | THIS MODULE ALREADY HARBORS A ORDER"
            );
        }
        order = Optional.of(anOrder);
    }

    /*
    PD Agent
     */
    private int getResourceId() {
        return agent.getResourceId();
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
        return new OrderFeasibilityAnt(getOrderId());
    }
}
