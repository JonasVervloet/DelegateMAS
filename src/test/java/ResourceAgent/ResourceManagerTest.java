package ResourceAgent;

import ResourceAgent.ResourceMap.ResourceManager;
import org.apache.commons.math3.util.Pair;
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

    @Test
    public void validConstructor() {
        assertEquals(3, manager.getNeighborIds().size());
        assert(manager.getNeighborIds().contains(123));
        assert(manager.getNeighborIds().contains(456));
        assert(manager.getNeighborIds().contains(789));
    }

    @Test
    public void getMinDistances() {
        assertEquals(3, manager.getMinDistances(56).size());
        List<Integer> minDistance1 = new ArrayList<>();
        minDistance1.add(3);
        minDistance1.add(9);
        minDistance1.add(10);
        assert(minDistance1.contains(manager.getMinDistances(56).get(0).getValue()));
        assert(minDistance1.contains(manager.getMinDistances(56).get(1).getValue()));
        assert(minDistance1.contains(manager.getMinDistances(56).get(2).getValue()));

        assertEquals(1, manager.getMinDistances(78).size());
        assertEquals(2, manager.getMinDistances(89).size());
        assertEquals(1, manager.getMinDistances(87).size());
        assertEquals(1, manager.getMinDistances(15).size());
    }
}