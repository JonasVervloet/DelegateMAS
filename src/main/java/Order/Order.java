package Order;

import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.core.model.road.RoadUser;
import com.github.rinde.rinsim.geom.Point;

import java.util.List;

public class Order implements RoadUser {

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

     */
    private int pickupAgvId;

    /*

     */
    private RoadModel roadModel;


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
    public boolean allDestinationsVisited() {
        return destinations.size() == 0;
    }

    public List<Integer> getDestinations() {
        return destinations;
    }

    public void visitDestination(int resourceId) {
        assert(destinations.get(0) == resourceId);
        destinations.remove(0);
    }

    /*
    Pick Up
     */
    public boolean hasBeenPickedUp() {
        return pickedUp;
    }

    public boolean couldPickUp(int pickupTime) {
        return pickupTime < currentPickupTime;
    }

    public void pickup() {
        if (! hasBeenPickedUp()) {
            pickedUp = true;
        } else {
            throw new IllegalStateException(
                    "ORDER | THIS ORDER HAS ALREADY BEEN PICKED UP"
            );
        }
    }

    public void reservePickup(int pickupTime, int agvId) {
        if (pickupTime < currentPickupTime) {
            currentPickupTime = pickupTime;
            pickupAgvId = agvId;
        } else {
            throw new IllegalArgumentException(
                    "ORDER | THE GIVEN PICKUP TIME IS NOT GOOD ENOUGH"
            );
        }
    }

    /*
    Road Model
     */
    private RoadModel getRoadModel() {
        if (roadModel == null) {
            throw new IllegalStateException(
                    "ORDER | THIS ORDER HAS NO ROAD MODEL YET"
            );
        }
        return roadModel;
    }

    public void showAtPosition(Point position) {
        try {
            getRoadModel().removeObject(this);
        } catch (IllegalArgumentException e) {}
        getRoadModel().addObjectAt(this, position);
    }

    public void showAtSamePosition(RoadUser user) {
        try {
            getRoadModel().removeObject(this);
        } catch (IllegalArgumentException e) {}
        getRoadModel().addObjectAtSamePosition(this, user);
    }

    @Override
    public void initRoadUser(RoadModel aModel) {
        roadModel = aModel;
    }

    public void removeOrder() {
        getRoadModel().removeObject(this);
    }
}
