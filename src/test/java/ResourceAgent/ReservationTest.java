package ResourceAgent;

import ResourceAgent.Schedule.Reservation;
import com.github.rinde.rinsim.geom.Point;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReservationTest {

    private Reservation reservation;

    /*
    Before
     */
    @Before
    public void init() {
        reservation = new Reservation(5, 12, 156, 987);
    }

    /*
    Constructor
     */
    @Test
    public void validConstructor() {
        assertEquals(5, reservation.getStartTime());
        assertEquals(12, reservation.getEndTime());
        assertEquals(156, reservation.getAgvId());
        assertEquals(987, reservation.getDestinationId());
        assertEquals(Reservation.getLifeTime(), reservation.getTimeToLive());
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidStartTime() {
        new Reservation(-2, 12, 156, 987);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidEndTime() {
        new Reservation(5, 5, 156, 987);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidAgvId() {
        new Reservation(5, 12, -156, 987);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidDestinationId() {
        new Reservation(5, 12, 156, -2);
    }

    /*
    Overlap
     */
    @Test
    public void overlap() {
        Reservation res1 = new Reservation(5, 12, 156, 987);
        Reservation res2 = new Reservation(6, 11, 178, 654);
        Reservation res3 = new Reservation (4, 8, 456, 852);
        Reservation res4 = new Reservation(1, 5, 123, 963);

        assert(res1.overlap(res2));
        assert(res2.overlap(res1));
        assert(res1.overlap(res3));
        assert(res3.overlap(res1));
        assert(res1.overlap(res4));
        assert(res4.overlap(res1));
    }

    @Test
    public void noOverlap() {
        Reservation res1  = new Reservation(5, 12, 156, 987);
        Reservation res2 = new Reservation(1, 4, 178, 654);
        Reservation res3 = new Reservation (13, 16, 456, 321);

        assertFalse(res1.overlap(res2));
        assertFalse(res2.overlap(res1));
        assertFalse(res1.overlap(res3));
        assertFalse(res3.overlap(res1));
    }

    @Test
    public void leftOverlapWithOther() {
        assert(reservation.leftOverlapWithOther(12, 15));
        assert(reservation.leftOverlapWithOther(7, 15));
        assert(reservation.leftOverlapWithOther(7, 12));
    }

    @Test
    public void noLeftOverlapWithOther() {
        assertFalse(reservation.leftOverlapWithOther(13, 17));
        assertFalse(reservation.leftOverlapWithOther(6, 11));
        assertFalse(reservation.leftOverlapWithOther(4, 13));
        assertFalse(reservation.leftOverlapWithOther(4, 11));
        assertFalse(reservation.leftOverlapWithOther(1, 3));
    }

    @Test
    public void fallsWithinOther() {
        assert(reservation.fallsWithinOther(4, 13));
        assert(reservation.fallsWithinOther(5, 13));
        assert(reservation.fallsWithinOther(4, 12));
        assert(reservation.fallsWithinOther(5, 12));
    }

    @Test
    public void doesNotFallWithinOther() {
        assertFalse(reservation.fallsWithinOther(6, 11));
        assertFalse(reservation.fallsWithinOther(4, 8));
        assertFalse(reservation.fallsWithinOther(8, 15));
    }

    @Test
    public void rightOverlapWithOther() {
        assert(reservation.rightOverlapWithOther(5, 9));
        assert(reservation.rightOverlapWithOther(3, 10));
        assert(reservation.rightOverlapWithOther(2, 5));
    }

    @Test
    public void noRightOverlapWithOther() {
        assertFalse(reservation.rightOverlapWithOther(6, 11));
        assertFalse(reservation.rightOverlapWithOther(4, 13));
        assertFalse(reservation.rightOverlapWithOther(2, 4));
        assertFalse(reservation.rightOverlapWithOther(13, 18));
        assertFalse(reservation.rightOverlapWithOther(6, 13));
    }

    @Test
    public void spansOther() {
        assert(reservation.spansOther(6, 11));
        assert(reservation.spansOther(8, 10));
    }

    @Test
    public void doesNotSpanOther() {
        assertFalse(reservation.spansOther(13, 18));
        assertFalse(reservation.spansOther(6, 12));
        assertFalse(reservation.spansOther(5, 12));
        assertFalse(reservation.spansOther(4, 11));
        assertFalse(reservation.spansOther(2, 4));
    }


    /*
    AgvId
     */
    @Test
     public void agvId() {

        Reservation res1  = new Reservation(5, 12, 156, 987);
        Reservation res2 = new Reservation(1, 4, 156, 654);
        Reservation res3 = new Reservation (13, 16, 456, 321);

        assert(res1.hasEqualAgvId(res2));
        assert(res2.hasEqualAgvId(res1));
        assertFalse(res1.hasEqualAgvId(res3));
        assertFalse(res3.hasEqualAgvId(res1));

        assert(res1.matchesAgvId(156));
        assert(res2.matchesAgvId(156));
        assert(res3.matchesAgvId(456));
        assertFalse(res1.matchesAgvId(456));
        assertFalse(res2.matchesAgvId(456));
        assertFalse(res3.matchesAgvId(156));
    }

    /*
    Destination ID
     */
    @Test
    public void matchingDestination() {
        Reservation reservation = new Reservation(2, 5, 123, 987);

        assert(reservation.matchingDestination(987));
        assertFalse(reservation.matchingDestination(321));
    }

    /*
    Time to life
     */
    @Test
    public void evaporation() {
        Reservation.setLifeTime(4);
        Reservation res1  = new Reservation(2, 3, 156, 987);
        assertEquals(Reservation.getLifeTime(), res1.getTimeToLive());
        assertEquals(2, res1.getStartTime());
        assertEquals(3, res1.getEndTime());
        assert(res1.hasTimeToLive());

        res1.evaporate();
        assertEquals(Reservation.getLifeTime() - 1, res1.getTimeToLive());
        assertEquals(2, res1.getStartTime());
        assertEquals(3, res1.getEndTime());
        assert(res1.hasTimeToLive());


        res1.evaporate();
        assertEquals(Reservation.getLifeTime() - 2, res1.getTimeToLive());
        assertEquals(2, res1.getStartTime());
        assertEquals(3, res1.getEndTime());
        assert(res1.hasTimeToLive());
    }

    @Test (expected = IllegalStateException.class)
    public void evaporationIllegalLifeTime() {
        Reservation res1  = new Reservation(1, Reservation.getLifeTime() + 2, 156, 987);

        for (int i=0; i < Reservation.getLifeTime(); i++) {
            assert(res1.hasTimeToLive());
            res1.evaporate();
        }
        assertFalse(res1.hasTimeToLive());
        res1.evaporate();
    }

    @Test
    public void refresh() {
        Reservation res1  = new Reservation(5, 12, 156, 987);

        assertEquals(Reservation.getLifeTime(), res1.getTimeToLive());
        res1.evaporate();
        assertEquals(Reservation.getLifeTime() - 1, res1.getTimeToLive());
        res1.evaporate();
        assertEquals(Reservation.getLifeTime() - 2, res1.getTimeToLive());
        res1.refresh();
        assertEquals(Reservation.getLifeTime(), res1.getTimeToLive());
    }

    /*
    To String
     */
    @Test
    public void toStringTest() {
        Reservation res1  = new Reservation(5, 12, 156, 987);
        assertEquals("Reservation: start - 5, end - 12, agv ID - 156, destination ID - " + 987,
                res1.toString());
    }

    /*
    Compare To
     */
    @Test
    public void testCompareTo() {
        Reservation res1  = new Reservation(5, 12, 156, 987);
        Reservation res2  = new Reservation(5, 12, 145, 654);
        Reservation res3  = new Reservation(2, 12, 156, 987);
        Reservation res4  = new Reservation(5, 13, 156, 987);

        assertEquals(0, res1.compareTo(res2));
        assertEquals(0, res2.compareTo(res1));

        assertEquals(1, res1.compareTo(res3));
        assertEquals(-1, res3.compareTo(res1));

        assertEquals(-1, res1.compareTo(res4));
        assertEquals(1, res4.compareTo(res1));
    }
}