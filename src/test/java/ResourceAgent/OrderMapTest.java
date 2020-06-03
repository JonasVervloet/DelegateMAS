package ResourceAgent;

import ResourceAgent.OrderMap.OrderEntry;
import ResourceAgent.OrderMap.OrderMap;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OrderMapTest {

    private List<Integer> neighborIds;
    private OrderMap map;

    @Before
    public void init() {
        neighborIds = new ArrayList<>();
        neighborIds.add(123);
        neighborIds.add(456);
        neighborIds.add(789);

        map = new OrderMap(neighborIds);
    }

    /*
    Constructor
     */
    @Test
    public void validConstructor() {
        assertEquals(3, map.getNeighborIds().size());
        assert(map.getNeighborIds().contains(123));
        assert(map.getNeighborIds().contains(456));
        assert(map.getNeighborIds().contains(789));

        assertEquals(0, map.getOrderEntriesForNeighborId(123).size());
        assertEquals(0, map.getOrderEntriesForNeighborId(456).size());
        assertEquals(0, map.getOrderEntriesForNeighborId(789).size());
    }

    @Test (expected = AssertionError.class)
    public void invalidConstructor() {
        new OrderMap(new ArrayList<Integer>());
    }

    /*
    Order Entries
     */
    @Test
    public void addValidOrderEntries() {
        map.addOrderEntry(123, 654, 5);

        assertEquals(1, map.getOrderEntriesForNeighborId(123).size());
        OrderEntry entry1 = map.getOrderEntriesForNeighborId(123).get(0);
        assert(entry1.matchesOrderId(654));
        assertEquals(5, entry1.getDistance());

        map.addOrderEntry(456, 987, 10);

        assertEquals(1, map.getOrderEntriesForNeighborId(456).size());
        OrderEntry entry2 = map.getOrderEntriesForNeighborId(456).get(0);
        assert(entry2.matchesOrderId(987));
        assertEquals(10, entry2.getDistance());

        map.addOrderEntry(123, 321, 3);
        assertEquals(2, map.getOrderEntriesForNeighborId(123).size());

        map.addOrderEntry(123, 654, 9);
        assertEquals(3, map.getOrderEntriesForNeighborId(123).size());

        map.addOrderEntry(123, 654, 5);
        assertEquals(3, map.getOrderEntriesForNeighborId(123).size());
    }

    @Test (expected = IllegalArgumentException.class)
    public void addInvalidOrderEntry() {
        map.addOrderEntry(321, 654,25);
    }

    /*
    Evaporation
     */
    @Test
    public void testEvaporation() {
        map.addOrderEntry(123, 654, 5);
        map.addOrderEntry(456, 987, 10);
        map.addOrderEntry(123, 321, 3);
        map.addOrderEntry(123, 654, 9);

        assertEquals(3, map.getOrderEntriesForNeighborId(123).size());
        assertEquals(1, map.getOrderEntriesForNeighborId(456).size());

        for (int i=1; i<OrderEntry.getLifeTime(); i++) {
            map.evaporate();
            assertEquals(3, map.getOrderEntriesForNeighborId(123).size());
            assertEquals(1, map.getOrderEntriesForNeighborId(456).size());
        }

        map.addOrderEntry(123, 654, 5);
        assertEquals(3, map.getOrderEntriesForNeighborId(123).size());
        assertEquals(1, map.getOrderEntriesForNeighborId(456).size());

        map.evaporate();
        assertEquals(1, map.getOrderEntriesForNeighborId(123).size());
        assertEquals(0, map.getOrderEntriesForNeighborId(456).size());
    }
}