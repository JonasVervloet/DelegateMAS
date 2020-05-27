package ResourceAgent;

import ResourceAgent.PackageMap.PackageEntry;
import ResourceAgent.PackageMap.PackageMap;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PackageMapTest {

    private List<Integer> neighborIds;
    private PackageMap map;

    @Before
    public void init() {
        neighborIds = new ArrayList<>();
        neighborIds.add(123);
        neighborIds.add(456);
        neighborIds.add(789);

        map = new PackageMap(neighborIds);
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

        assertEquals(0, map.getPackageEntriesForNeighborId(123).size());
        assertEquals(0, map.getPackageEntriesForNeighborId(456).size());
        assertEquals(0, map.getPackageEntriesForNeighborId(789).size());
    }

    @Test (expected = AssertionError.class)
    public void invalidConstructor() {
        new PackageMap(new ArrayList<Integer>());
    }

    /*
    Package Entries
     */
    @Test
    public void addValidPackageEntries() {
        map.addPackageEntry(123, 654, 5);

        assertEquals(1, map.getPackageEntriesForNeighborId(123).size());
        PackageEntry entry1 = map.getPackageEntriesForNeighborId(123).get(0);
        assert(entry1.matchesPackageId(654));
        assertEquals(5, entry1.getDistance());

        map.addPackageEntry(456, 987, 10);

        assertEquals(1, map.getPackageEntriesForNeighborId(456).size());
        PackageEntry entry2 = map.getPackageEntriesForNeighborId(456).get(0);
        assert(entry2.matchesPackageId(987));
        assertEquals(10, entry2.getDistance());

        map.addPackageEntry(123, 321, 3);
        assertEquals(2, map.getPackageEntriesForNeighborId(123).size());

        map.addPackageEntry(123, 654, 9);
        assertEquals(3, map.getPackageEntriesForNeighborId(123).size());

        map.addPackageEntry(123, 654, 5);
        assertEquals(3, map.getPackageEntriesForNeighborId(123).size());
    }

    @Test (expected = IllegalArgumentException.class)
    public void addInvalidPackageEntry() {
        map.addPackageEntry(321, 654,25);
    }
}