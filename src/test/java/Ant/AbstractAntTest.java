package Ant;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractAntTest {

    private OrderFeasibilityAnt ant1;
    private OrderFeasibilityAnt ant2;

    @Before
    public void init() {
        ant1 = new OrderFeasibilityAnt(123, 222);
        ant2 = new OrderFeasibilityAnt(123, 22);
    }


    /*
    Constructor
     */
    @Test
    public void testConstructor() {
        assertNotEquals(ant1.getId(), ant2.getId());
    }

    @Test
    public void testAntConstructor() {
        ant1.visitResource(987);
        ant1.visitResource(654);
        OrderFeasibilityAnt ant3 = new OrderFeasibilityAnt(ant1);

        assertNotEquals(ant1.getId(), ant3.getId());
        assertNotEquals(ant2.getId(), ant3.getId());

        assert(ant3.alreadyVisited(987));
        assert(ant3.alreadyVisited(654));
        assertEquals(987, ant3.firstVisitedResourceId());
        assertEquals(654, ant3.lastVisitedResourceId());

        ant3.visitResource(321);
        assert(ant3.alreadyVisited(321));
        assertFalse(ant1.alreadyVisited(321));
        assertEquals(321, ant3.lastVisitedResourceId());
        assertEquals(654, ant1.lastVisitedResourceId());
    }

    /*
    Resource visits
     */
    @Test
    public void visitResource() {
        ant1.visitResource(987);
        assert(ant1.alreadyVisited(987));
        assertEquals(987, ant1.firstVisitedResourceId());
        assertEquals(987, ant1.lastVisitedResourceId());

        ant1.visitResource(654);
        assert(ant1.alreadyVisited(987));
        assert(ant1.alreadyVisited(654));
        assertEquals(987, ant1.firstVisitedResourceId());
        assertEquals(654, ant1.lastVisitedResourceId());

        ant1.visitResource(321);
        assert(ant1.alreadyVisited(987));
        assert(ant1.alreadyVisited(654));
        assert(ant1.alreadyVisited(321));
        assertEquals(987, ant1.firstVisitedResourceId());
        assertEquals(321, ant1.lastVisitedResourceId());
    }

    @Test (expected = IllegalArgumentException.class)
    public void visitResourceFailure() {
        ant1.visitResource(987);
        ant1.visitResource(654);
        ant1.visitResource(321);
        ant1.visitResource(987);
    }

    @Test
    public void getPreviousResourceId() {
        ant1.visitResource(987);
        ant1.visitResource(654);
        ant1.visitResource(321);

        assertEquals(654, ant1.getPreviousResourceId(321));
        assertEquals(987, ant1.getPreviousResourceId(654));
    }

    @Test (expected = IllegalArgumentException.class)
    public void getPreviousResourceIdNotInList() {
        ant1.visitResource(987);
        ant1.visitResource(654);
        ant1.visitResource(321);

        ant1.getPreviousResourceId(852);
    }

    @Test (expected = IllegalArgumentException.class)
    public void getPreviousResourceIdFirstId() {
        ant1.visitResource(987);
        ant1.visitResource(654);
        ant1.visitResource(321);

        ant1.getPreviousResourceId(987);
    }
}