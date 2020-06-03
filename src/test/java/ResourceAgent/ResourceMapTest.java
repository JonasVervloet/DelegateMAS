package ResourceAgent;

import ResourceAgent.ResourceMap.ResourceEntry;
import ResourceAgent.ResourceMap.ResourceMap;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ResourceMapTest {

    private List<Integer> neighborIds;
    private ResourceMap map;

    @Before
    public void init() {
        neighborIds = new ArrayList<>();
        neighborIds.add(123);
        neighborIds.add(546);
        neighborIds.add(789);
        map = new ResourceMap(neighborIds);
    }

    /*
    Constructor
     */
    @Test
    public void validConstructor() {
        assertEquals(3, map.getNeighborIds().size());
        assert(map.getNeighborIds().contains(123));
        assert(map.getNeighborIds().contains(546));
        assert(map.getNeighborIds().contains(789));
    }

    @Test (expected  = AssertionError.class)
    public void invalidConstructor() {
        new ResourceMap(new ArrayList<Integer>());
    }

    /*
    Resource Entries
     */
    @Test
    public void addValidResourceEntries() {
        map.addResourceEntry(
                123, 654, 5
        );

        assertEquals(1, map.getResourceEntriesForNeighborId(123).size());
        ResourceEntry entry1 = map.getResourceEntriesForNeighborId(123).get(0);
        assert(entry1.matchesResourceId(654));
        assertEquals(5, entry1.getDistance());

        assertEquals(0, map.getResourceEntriesForNeighborId(546).size());
        assertEquals(0, map.getResourceEntriesForNeighborId(789).size());

        map.addResourceEntry(
                546, 639, 10
        );

        assertEquals(1, map.getResourceEntriesForNeighborId(546).size());
        ResourceEntry entry2 = map.getResourceEntriesForNeighborId(546).get(0);
        assert(entry2.matchesResourceId(639));
        assertEquals(10, entry2.getDistance());

        assertEquals(1, map.getResourceEntriesForNeighborId(123).size());
        assertEquals(0, map.getResourceEntriesForNeighborId(789).size());

        map.addResourceEntry(123, 654, 8);
        assertEquals(2, map.getResourceEntriesForNeighborId(123).size());

        map.addResourceEntry(123, 654, 5);
        assertEquals(2, map.getResourceEntriesForNeighborId(123).size());
    }

    @Test (expected = IllegalArgumentException.class)
    public void addInvalidResourceEntry() {
        map.addResourceEntry(
                852, 634, 3
        );
    }

    /*
    Evaporation
     */
    @Test
    public void testEvaporation() {
        map.addResourceEntry(123, 654, 5);
        map.addResourceEntry(546, 639, 10);
        map.addResourceEntry(123, 654, 8);
        assertEquals(2, map.getResourceEntriesForNeighborId(123).size());
        assertEquals(1, map.getResourceEntriesForNeighborId(546).size());

        for (int i=1; i < ResourceEntry.getLifeTime(); i++) {
            map.evaporate();
            assertEquals(2, map.getResourceEntriesForNeighborId(123).size());
            assertEquals(1, map.getResourceEntriesForNeighborId(546).size());
        }

        map.addResourceEntry(123, 654, 5);
        assertEquals(2, map.getResourceEntriesForNeighborId(123).size());
        assertEquals(1, map.getResourceEntriesForNeighborId(546).size());

        map.evaporate();
        assertEquals(1, map.getResourceEntriesForNeighborId(123).size());
        assertEquals(0, map.getResourceEntriesForNeighborId(546).size());
    }
}