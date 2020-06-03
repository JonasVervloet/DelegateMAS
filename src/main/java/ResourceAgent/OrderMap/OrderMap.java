package ResourceAgent.OrderMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderMap {

    /*
    The package map keeps a list of package entries for every
        neighbor of the resource agent that manages this package
        map.
     */
    private Map<Integer, List<OrderEntry>> orderMap;


    /*
    Constructor
     */
    public OrderMap(List<Integer> neighborIds) {
        assert(neighborIds.size() > 0);
        orderMap = new HashMap<>();
        for (Integer neighborId: neighborIds) {
            orderMap.put(
                    neighborId, new ArrayList<OrderEntry>()
            );
        }
    }


    /*
    Neighbor IDs
     */
    private boolean isValidNeighborId(int neighborId) {
        return orderMap.containsKey(neighborId);
    }

    public List<Integer> getNeighborIds() {
        return new ArrayList<>(orderMap.keySet());
    }

    /*
    Order Entries
     */
    public List<OrderEntry> getOrderEntriesForNeighborId(int neighborId)
            throws IllegalArgumentException {
        if (! isValidNeighborId(neighborId)) {
            throw new IllegalArgumentException(
                    "PACKAGE MAP | GIVEN NEIGHBOR ID IS NOT A VALID ONE"
            );
        }

        return orderMap.get(neighborId);
    }

    public void addOrderEntry(int neighborId, int packageId, int distance)
            throws IllegalArgumentException {
        if (! isValidNeighborId(neighborId)) {
            throw new IllegalArgumentException(
                    "PACKAGE MAP | GIVEN NEIGHBOR ID IS NOT A VALID ONE"
            );
        }

        boolean found = false;
        for (OrderEntry entry: orderMap.get(neighborId)) {
            if (entry.isEqualEntry(packageId, distance)) {
                entry.refresh();
                found = true;
            }
        }

        if (! found) {
            orderMap.get(neighborId).add(
                    new OrderEntry(packageId, distance)
            );
        }
    }

    /*
    Evaporation
     */
    public void evaporate() {
        for (List<OrderEntry> list: orderMap.values()) {
            List<OrderEntry> toRemove = new ArrayList<>();
            for (OrderEntry entry: list) {
                entry.evaporate();
                if (entry.isDone()) {
                    toRemove.add(entry);
                }
            }

            list.removeAll(toRemove);
        }
    }
}
