package ResourceAgent;

import ResourceAgent.ResourceMap.ResourceEntry;
import ResourceAgent.ResourceMap.ResourceManager;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ResourceManagerTest {

    private List<Integer> neighborIds;
    private ResourceManager manager;

    @Before
    public void init() {
        neighborIds = new ArrayList<>();
        neighborIds.add(123);
        neighborIds.add(456);
        neighborIds.add(789);

        manager = new ResourceManager(neighborIds);

        manager.registerResource(123, 56, 3);
        manager.registerResource(123, 56, 12);
        manager.registerResource(123, 78, 5);
        manager.registerResource(456, 56, 9);
        manager.registerResource(456, 89, 6);
        manager.registerResource(456, 87, 45);
        manager.registerResource(789, 56, 10);
        manager.registerResource(789, 89, 5);
        manager.registerResource(789, 15, 15);
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
    Resource Map
     */
    @Test
    public void getMinDistances() {
        assertEquals(2, manager.getMinDistances(56, 789).size());
        List<Integer> minDistance1 = new ArrayList<>();
        minDistance1.add(3);
        minDistance1.add(9);
        assert(minDistance1.contains(manager.getMinDistances(56, 789).get(0).getValue()));
        assert(minDistance1.contains(manager.getMinDistances(56, 789).get(1).getValue()));

        assertEquals(0, manager.getMinDistances(78, 123).size());
        assertEquals(1, manager.getMinDistances(78, 456).size());
        assertEquals(1, manager.getMinDistances(78, 789).size());

        assertEquals(2, manager.getMinDistances(89, 123).size());
        assertEquals(1, manager.getMinDistances(89, 456).size());
        assertEquals(1, manager.getMinDistances(89, 789).size());

        assertEquals(1, manager.getMinDistances(87, 123).size());
        assertEquals(0, manager.getMinDistances(87, 456).size());
        assertEquals(1, manager.getMinDistances(87, 789).size());

        assertEquals(1, manager.getMinDistances(15, 123).size());
        assertEquals(1, manager.getMinDistances(15, 456).size());
        assertEquals(0, manager.getMinDistances(15, 789).size());
    }

    /*
    Tick
     */
    @Test
    public void testTick() {
        assertEquals(2, manager.getMinDistances(56, 456).size());

        for (int i=1; i < ResourceEntry.getLifeTime(); i++) {
            manager.tick();
            assertEquals(2, manager.getMinDistances(56, 456).size());
        }

        manager.registerResource(123, 56, 3);
        assertEquals(2, manager.getMinDistances(56, 456).size());

        manager.tick();
        assertEquals(1, manager.getMinDistances(56, 456).size());
    }
}