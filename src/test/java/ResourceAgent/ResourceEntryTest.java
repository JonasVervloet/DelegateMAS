package ResourceAgent;

import ResourceAgent.ResourceMap.ResourceEntry;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import static org.junit.Assert.*;

public class ResourceEntryTest {

    ResourceEntry entry;

    @Before
    public void init() {
        entry = new ResourceEntry(123, 5);
    }

    /*
    Constructor
     */
    @Test
    public void validConstructor() {
        assert(entry.matchesResourceId(123));
        assertEquals(5, entry.getDistance());
        assertEquals(ResourceEntry.getLifeTime(), entry.getTimeToLive());
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidResourceIdConcstructor() {
        new ResourceEntry(-12, 5);
    }

    @Test (expected =  IllegalArgumentException.class)
    public void invalidDistanceConstructor() {
        new ResourceEntry(123, -2);
    }

    /*
    Time to live
     */
    @Test
    public void testEvaporate() {
        assertEquals(ResourceEntry.getLifeTime(), entry.getTimeToLive());
        entry.evaporate();
        assertEquals(ResourceEntry.getLifeTime() - 1, entry.getTimeToLive());
        entry.evaporate();
        assertEquals(ResourceEntry.getLifeTime() - 2, entry.getTimeToLive());
    }

    @Test
    public void testEvaporateJustBeforeFail() {
        for (int i = 0; i < ResourceEntry.getLifeTime(); i++) {
            entry.evaporate();
        }
        assertEquals(0, entry.getTimeToLive());
    }

    @Test (expected = IllegalStateException.class)
    public void testEvaporateFail() {
        for (int i = 0; i <= ResourceEntry.getLifeTime(); i++) {
            entry.evaporate();
        }
    }

    @Test
    public void refresh() {
        entry.evaporate();
        entry.evaporate();
        entry.evaporate();

        assertEquals(ResourceEntry.getLifeTime() - 3,
                entry.getTimeToLive());

        entry.refresh();
        assertEquals(ResourceEntry.getLifeTime(), entry.getTimeToLive());
    }
}