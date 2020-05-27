package ResourceAgent;

import ResourceAgent.PackageMap.PackageManager;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PackageManagerTest {

    private List<Integer> neighborIds;
    private PackageManager manager;

    @Before
    public void init() {
        neighborIds = new ArrayList<>();
        neighborIds.add(123);
        neighborIds.add(456);
        neighborIds.add(789);

        manager = new PackageManager(neighborIds);

        manager.registerPackage(123, 321, 5);
        manager.registerPackage(123, 654, 8);
        manager.registerPackage(456, 321, 7);
        manager.registerPackage(456, 987, 21);
    }

    @Test
    public void validConstructor() {
        assertEquals(3, manager.getNeighborIds().size());
        assert(manager.getNeighborIds().contains(123));
        assert(manager.getNeighborIds().contains(456));
        assert(manager.getNeighborIds().contains(789));
    }

    @Test
    public void getMinDistancesToPackage() {
        assertEquals(2, manager.getMinDistancesToPackage().size());
        List<Integer> minDistances1 = new ArrayList<>();
        minDistances1.add(5);
        minDistances1.add(7);

        assert(minDistances1.contains(manager.getMinDistancesToPackage().get(0).getValue()));
        assert(minDistances1.contains(manager.getMinDistancesToPackage().get(1).getValue()));

        manager.registerPackage(789, 321, 12);
        assertEquals(3, manager.getMinDistancesToPackage().size());
        minDistances1.add(12);

        assert(minDistances1.contains(manager.getMinDistancesToPackage().get(0).getValue()));
        assert(minDistances1.contains(manager.getMinDistancesToPackage().get(1).getValue()));
        assert(minDistances1.contains(manager.getMinDistancesToPackage().get(2).getValue()));

        manager.registerPackage(123, 852, 2);
        assertEquals(3, manager.getMinDistancesToPackage().size());
        List<Integer> minDistances2 = new ArrayList<>();
        minDistances2.add(2);
        minDistances2.add(7);
        minDistances2.add(12);

        assert(minDistances2.contains(manager.getMinDistancesToPackage().get(0).getValue()));
        assert(minDistances2.contains(manager.getMinDistancesToPackage().get(1).getValue()));
        assert(minDistances2.contains(manager.getMinDistancesToPackage().get(2).getValue()));
    }
}