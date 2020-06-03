package ResourceAgent;

import ResourceAgent.OrderMap.OrderEntry;
import ResourceAgent.OrderMap.OrderManager;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OrderManagerTest {

    private List<Integer> neighborIds;
    private OrderManager manager;

    @Before
    public void init() {
        neighborIds = new ArrayList<>();
        neighborIds.add(123);
        neighborIds.add(456);
        neighborIds.add(789);

        manager = new OrderManager(neighborIds);

        manager.registerOrder(123, 321, 5);
        manager.registerOrder(123, 654, 8);
        manager.registerOrder(456, 321, 7);
        manager.registerOrder(456, 987, 21);
    }

    /*
    Constructor
     */
    @Test
    public void validConstructor() {
        assertEquals(3, manager.getNeighborIds().size());
        assert(manager.getNeighborIds().contains(123));
        assert(manager.getNeighborIds().contains(456));
        assert(manager.getNeighborIds().contains(789));
    }

    /*
    Order Map
     */
    @Test
    public void getMinDistancesToPackage() {
        assertEquals(2, manager.getMinDistancesToOrder(789).size());
        List<Integer> minDistances1 = new ArrayList<>();
        minDistances1.add(5);
        minDistances1.add(7);

        assert(minDistances1.contains(manager.getMinDistancesToOrder(789).get(0).getValue()));
        assert(minDistances1.contains(manager.getMinDistancesToOrder(789).get(1).getValue()));

        manager.registerOrder(789, 321, 12);
        assertEquals(2, manager.getMinDistancesToOrder(789).size());

        assert(minDistances1.contains(manager.getMinDistancesToOrder(789).get(0).getValue()));
        assert(minDistances1.contains(manager.getMinDistancesToOrder(789).get(1).getValue()));

        manager.registerOrder(123, 852, 2);
        assertEquals(2, manager.getMinDistancesToOrder(789).size());
        List<Integer> minDistances2 = new ArrayList<>();
        minDistances2.add(2);
        minDistances2.add(7);

        assert(minDistances2.contains(manager.getMinDistancesToOrder(789).get(0).getValue()));
        assert(minDistances2.contains(manager.getMinDistancesToOrder(789).get(1).getValue()));
    }

    @Test (expected = IllegalArgumentException.class)
    public void getMidDistancesToPackageFail() {
        manager.getMinDistancesToOrder(987);
    }

    /*
    Tick
     */
    @Test
    public void testTick() {
        assertEquals(2, manager.getMinDistancesToOrder(789).size());

        for (int i=1; i< OrderEntry.getLifeTime(); i++) {
            manager.tick();
            assertEquals(2, manager.getMinDistancesToOrder(789).size());
        }

        manager.registerOrder(123, 654, 8);
        assertEquals(2, manager.getMinDistancesToOrder(789).size());

        manager.tick();
        assertEquals(1, manager.getMinDistancesToOrder(789).size());
    }
}