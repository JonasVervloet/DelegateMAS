package ResourceAgent.OrderMap;

import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class OrderManager {

    /*
    The package map that this package manager manages.
     */
    private OrderMap orderMap;


    /*
    Constructor
     */
    public OrderManager(List<Integer> neighborIds) {
        orderMap = new OrderMap(neighborIds);
    }


    /*
    Order Map
     */
    private boolean isValidArrivalId(int arrivalId) {
        return getNeighborIds().contains(arrivalId);
    }

    public List<Integer> getNeighborIds() {
        return orderMap.getNeighborIds();
    }

    public List<Pair<Integer, Integer>> getMinDistancesToOrder(int arrivalId)
            throws IllegalArgumentException {
        if (! isValidArrivalId(arrivalId)) {
            throw new IllegalArgumentException(
                    "ORDER MANAGER | THE GIVEN ARRIVAL ID IS NOT A VALID ONE"
            );
        }

        List<Pair<Integer, Integer>> distances = new ArrayList<>();
        for (Integer neighborId: getNeighborIds()) {
            if (neighborId != arrivalId) {
                try {
                    int distance = getMinDistanceToOrder(neighborId);
                    distances.add(new Pair<>(neighborId, distance));
                } catch (IllegalArgumentException e) {}
            }
        }

        return distances;
    }

    public void registerOrder(int neighborId, int orderId, int distance) {
        orderMap.addOrderEntry(neighborId, orderId, distance);
    }

    private int getMinDistanceToOrder(int neighborId)
            throws IllegalArgumentException {
        if (orderMap.getOrderEntriesForNeighborId(neighborId).size() == 0) {
            throw new IllegalArgumentException(
                    "PACKAGE MANAGER | NO PACKAGE FOUND IN THE DIRECTION OF THE GIVEN NEIGHBOR ID"
            );
        }

        int minDistance = Integer.MAX_VALUE;
        for (OrderEntry entry:
                orderMap.getOrderEntriesForNeighborId(neighborId)) {
            if (entry.getDistance() < minDistance) {
                minDistance = entry.getDistance();
            }
        }

        return minDistance;
    }

    /*
    Tick
     */
    public void tick() {
        orderMap.evaporate();
    }
}
