package ResourceAgent;

import ResourceAgent.OrderMap.OrderEntry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrderEntryTest {

    OrderEntry entry;

    @Before
    public void init() {
        entry = new OrderEntry(152, 5);
    }

    /*
    Constructor
     */
    @Test
    public void validConstructor() {
        assert(entry.matchesOrderId(152));

        assert(entry.hadEqualDistance(5));
        assertEquals(5, entry.getDistance());

        assertEquals(OrderEntry.getLifeTime(), entry.getTimeToLive());

        assert(entry.isEqualEntry(152, 5));
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidOrderIdConstructor() {
        new OrderEntry(-1, 20);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidDistanceConstructor() {
        new OrderEntry(258, -1);
    }

    /*
    Time to live
     */
    @Test
    public void testEvaporate() {
        assertEquals(OrderEntry.getLifeTime(), entry.getTimeToLive());
        entry.evaporate();
        assertEquals(OrderEntry.getLifeTime() - 1, entry.getTimeToLive());
        entry.evaporate();
        assertEquals(OrderEntry.getLifeTime() - 2, entry.getTimeToLive());
    }

    @Test
    public void testEvaporateJustBeforeFail() {
        for (int i = 0; i < OrderEntry.getLifeTime(); i++) {
            entry.evaporate();
            assertFalse(entry.isDone());
        }
        assertEquals(0, entry.getTimeToLive());
        assert(entry.isDone());
    }

    @Test (expected = IllegalStateException.class)
    public void testEvaporateUntilFail() {
        for (int i = 0; i <= OrderEntry.getLifeTime(); i++) {
            entry.evaporate();
        }
    }

    @Test
    public void testRefresh() {
        entry.evaporate();
        entry.evaporate();
        entry.evaporate();
        assertEquals(OrderEntry.getLifeTime() - 3, entry.getTimeToLive());
        entry.refresh();
        assertEquals(OrderEntry.getLifeTime(), entry.getTimeToLive());
    }
}