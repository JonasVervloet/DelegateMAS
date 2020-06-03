package Order;

import java.util.List;

public class Order {

    /*
    The ID Counter ensures that every packages
        receives an unique ID.
     */
    private static int idCounter = 0;

    /*
    The ID of this package.
        This ID is unique amongst all packages.
     */
    private int orderId;

    /*
    The list IDs of the destinations that this
        order should visit.
     */
    private List<Integer> destinations;

    /*
    Boolean reflecting whether or not this order
        as already been picked up by an AGV agent.
     */
    private boolean pickedUp;

    /*
    ...
     */
    private int currentPickupTime;


    /*
    Constructor
     */
    public Order(List<Integer> destinations) {
        this.orderId = next();
        this.destinations = destinations;
        this.pickedUp = false;
        this.currentPickupTime = Integer.MAX_VALUE;
    }


    /*
    Package ID
     */
    private static int next() {
        idCounter += 1;
        return idCounter;
    }

    public int getOrderId() {
        return orderId;
    }

    /*
    Destinations
     */
    public List<Integer> getDestinations() {
        return destinations;
    }

    /*
    Pick Up
     */
    public boolean couldPickUp(int pickupTime) {
        return pickupTime < currentPickupTime;
    }

    public void pickup() {
        if (! pickedUp) {
            pickedUp = true;
        } else {
            throw new IllegalStateException(
                    "ORDER | THIS ORDER HAS ALREADY BEEN PICKED UP"
            );
        }
    }
}
