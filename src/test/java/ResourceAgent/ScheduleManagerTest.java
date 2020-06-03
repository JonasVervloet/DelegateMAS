package ResourceAgent;

import ResourceAgent.Schedule.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ScheduleManagerTest {

    ScheduleManager manager1;
    ScheduleManager manager2;
    ScheduleRequest request;

    /*
    Before
     */
    @Before
    public void init() {
        manager1 = new ScheduleManager(5, 3);
        manager2 = new ScheduleManager(2, 3);
        request = new ScheduleRequest(
                5, 20, 145, 987
        );
    }

    /*
    Constructor
     */
    @Test
    public void validConstructor() {
        assertEquals(5, manager1.getCapacity());
        assertEquals(3, manager1.getTraversalTime());
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidCapacity() {
        new ScheduleManager(-1, 3);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidTraversalTime() {
        new ScheduleManager(5, 0);
    }

    /*
    AGV Register
     */
    @Test
    public void isValidToRegister() {
        assertFalse(manager1.isValidToRegister(123));
        manager1.makeReservation(new ScheduleRequest(5,  9,  123, 987));
        assertFalse(manager1.isValidToRegister(123));
        manager1.makeReservation(new ScheduleRequest(0, 4, 123, 987));
        assertTrue(manager1.isValidToRegister(123));
    }

    /*
    Possible Reservations
     */
    @Test
    public void possibleReservationsEmptyInInterval() {
        manager1.makeReservation(
                new ScheduleRequest(1, 3, 123, 987)
        );
        manager1.makeReservation(
                new ScheduleRequest(21, 26, 789, 987)
        );
        List<PossibleReservation> possRes =
                manager1.getPossibleReservations(request);

        assertEquals(1, possRes.size());
        PossibleReservation possRes1 = possRes.get(0);
        assertEquals(5, possRes1.getArrivalStart());
        assertEquals(18, possRes1.getArrivalEnd());
        assertEquals(7, possRes1.getDepartureStart());
        assertEquals(20, possRes1.getDepartureEnd());
    }

    @Test
    public void possibleReservationsSpan() {
        manager1.makeReservation(
                new ScheduleRequest(3, 23, 123, 987)
        );

        List<PossibleReservation> possRes =
                manager1.getPossibleReservations(request);
        assertEquals(0, possRes.size());
    }

    @Test
    public void possibleReservationsOnlyLeftOverlap() {
        manager1.makeReservation(
                new ScheduleRequest(1, 5, 123, 987)
        );
        manager1.makeReservation(
                new ScheduleRequest(3, 6, 789, 987)
        );
        manager1.makeReservation(
                new ScheduleRequest(4, 8, 654, 987)
        );

        List<PossibleReservation> possRes =
                manager1.getPossibleReservations(request);
        assertEquals(1, possRes.size());

        PossibleReservation possRes1 = possRes.get(0);
        assertEquals(5, possRes1.getArrivalStart());
        assertEquals(18, possRes1.getArrivalEnd());
        assertEquals(9, possRes1.getDepartureStart());
        assertEquals(20, possRes1.getDepartureEnd());
    }

    @Test
    public void possibleReservationsOnlyRightOverlap() {
        manager1.makeReservation(
                new ScheduleRequest(16, 21, 123, 987)
        );
        manager1.makeReservation(
                new ScheduleRequest(18, 23, 789, 987)
        );
        manager1.makeReservation(
                new ScheduleRequest(20, 24, 654, 987)
        );

        List<PossibleReservation> possRes =
                manager1.getPossibleReservations(request);
        assertEquals(1, possRes.size());

        PossibleReservation possRes1 = possRes.get(0);
        assertEquals(5, possRes1.getArrivalStart());
        assertEquals(15, possRes1.getArrivalEnd());
        assertEquals(7, possRes1.getDepartureStart());
        assertEquals(20, possRes1.getDepartureEnd());
    }

    @Test
    public void possibleReservationsOnlyWithin() {
        manager1.makeReservation(
                new ScheduleRequest(8, 12, 123, 987)
        );
        manager1.makeReservation(
                new ScheduleRequest(10, 14, 789, 987)
        );
        manager1.makeReservation(
                new ScheduleRequest(14, 17, 654, 987)
        );
        List<PossibleReservation> possRes =
                manager1.getPossibleReservations(request);
        assertEquals(4, possRes.size());

        PossibleReservation possRes1 = possRes.get(0);
        assertEquals(5, possRes1.getArrivalStart());
        assertEquals(7, possRes1.getArrivalEnd());
        assertEquals(7, possRes1.getDepartureStart());
        assertEquals(11, possRes1.getDepartureEnd());

        PossibleReservation possRes2 = possRes.get(1);
        assertEquals(9, possRes2.getArrivalStart());
        assertEquals(9, possRes2.getArrivalEnd());
        assertEquals(13, possRes2.getDepartureStart());
        assertEquals(13, possRes2.getDepartureEnd());

        PossibleReservation possRes3 = possRes.get(2);
        assertEquals(11, possRes3.getArrivalStart());
        assertEquals(13, possRes3.getArrivalEnd());
        assertEquals(15, possRes3.getDepartureStart());
        assertEquals(16, possRes3.getDepartureEnd());

        PossibleReservation possRes4 = possRes.get(3);
        assertEquals(15, possRes4.getArrivalStart());
        assertEquals(18, possRes4.getArrivalEnd());
        assertEquals(18, possRes4.getDepartureStart());
        assertEquals(20, possRes4.getDepartureEnd());
    }

    @Test
    public void possibleReservationsLeftAndRightOverlap() {
        manager1.makeReservation(
                new ScheduleRequest(2, 7, 123, 987)
        );
        manager1.makeReservation(
                new ScheduleRequest(17, 24, 654, 987)
        );

        List<PossibleReservation> possRes =
                manager1.getPossibleReservations(request);
        assertEquals(1, possRes.size());

        PossibleReservation possRes1 = possRes.get(0);
        assertEquals(5, possRes1.getArrivalStart());
        assertEquals(16, possRes1.getArrivalEnd());
        assertEquals(8, possRes1.getDepartureStart());
        assertEquals(20, possRes1.getDepartureEnd());
    }

    @Test
    public void possibleReservationsLeftOverlapAndWithin() {
        manager1.makeReservation(
                new ScheduleRequest(2, 7, 123, 987)
        );
        manager1.makeReservation(
                new ScheduleRequest(8, 12, 789, 987)
        );
        manager1.makeReservation(
                new ScheduleRequest(10, 14, 654, 987)
        );

        List<PossibleReservation> possRes =
                manager1.getPossibleReservations(request);
        assertEquals(3, possRes.size());

        PossibleReservation possRes1 = possRes.get(0);
        assertEquals(5, possRes1.getArrivalStart());
        assertEquals(7, possRes1.getArrivalEnd());
        assertEquals(8, possRes1.getDepartureStart());
        assertEquals(11, possRes1.getDepartureEnd());

        PossibleReservation possRes2 = possRes.get(1);
        assertEquals(9, possRes2.getArrivalStart());
        assertEquals(9, possRes2.getArrivalEnd());
        assertEquals(13, possRes2.getDepartureStart());
        assertEquals(13, possRes2.getDepartureEnd());

        PossibleReservation possRes3 = possRes.get(2);
        assertEquals(11, possRes3.getArrivalStart());
        assertEquals(18, possRes3.getArrivalEnd());
        assertEquals(15, possRes3.getDepartureStart());
        assertEquals(20, possRes3.getDepartureEnd());
    }

    @Test
    public void possibleReservationsRightOverlapAndWithin() {
        manager1.makeReservation(
                new ScheduleRequest(16, 24, 123, 987)
        );
        manager1.makeReservation(
                new ScheduleRequest(8, 12, 789, 987)
        );
        manager1.makeReservation(
                new ScheduleRequest(14, 17, 654, 987)
        );

        List<PossibleReservation> possRes =
                manager1.getPossibleReservations(request);
        assertEquals(3, possRes.size());

        PossibleReservation possRes1 = possRes.get(0);
        assertEquals(5, possRes1.getArrivalStart());
        assertEquals(7, possRes1.getArrivalEnd());
        assertEquals(7, possRes1.getDepartureStart());
        assertEquals(11, possRes1.getDepartureEnd());

        PossibleReservation possRes2 = possRes.get(1);
        assertEquals(9, possRes2.getArrivalStart());
        assertEquals(13, possRes2.getArrivalEnd());
        assertEquals(13, possRes2.getDepartureStart());
        assertEquals(16, possRes2.getDepartureEnd());

        PossibleReservation possRes3 = possRes.get(2);
        assertEquals(15, possRes3.getArrivalStart());
        assertEquals(15, possRes3.getArrivalEnd());
        assertEquals(18, possRes3.getDepartureStart());
        assertEquals(20, possRes3.getDepartureEnd());
    }

    @Test
    public void possibleReservationsAll() {
        manager1.makeReservation(
                new ScheduleRequest(2, 7, 123, 987)
        );
        manager1.makeReservation(
                new ScheduleRequest(8, 12, 789, 987)
        );
        manager1.makeReservation(
                new ScheduleRequest(10, 14, 654, 987)
        );
        manager1.makeReservation(
                new ScheduleRequest(16, 24, 123, 987)
        );

        List<PossibleReservation> possRes =
                manager1.getPossibleReservations(request);
        assertEquals(3, possRes.size());

        PossibleReservation possRes1 = possRes.get(0);
        assertEquals(5, possRes1.getArrivalStart());
        assertEquals(7, possRes1.getArrivalEnd());
        assertEquals(8, possRes1.getDepartureStart());
        assertEquals(11, possRes1.getDepartureEnd());

        PossibleReservation possRes2 = possRes.get(1);
        assertEquals(9, possRes2.getArrivalStart());
        assertEquals(9, possRes2.getArrivalEnd());
        assertEquals(13, possRes2.getDepartureStart());
        assertEquals(13, possRes2.getDepartureEnd());

        PossibleReservation possRes3 = possRes.get(2);
        assertEquals(11, possRes3.getArrivalStart());
        assertEquals(15, possRes3.getArrivalEnd());
        assertEquals(15, possRes3.getDepartureStart());
        assertEquals(20, possRes3.getDepartureEnd());
    }

    @Test
    public void possibleReservationsInvalidArrivalInterval() {
        manager1.makeReservation(
                new ScheduleRequest(8, 10, 123, 987)
        );
        manager1.makeReservation(
                new ScheduleRequest(9, 12, 789, 987)
        );

        List<PossibleReservation> possRes =
                manager1.getPossibleReservations(request);
        assertEquals(2, possRes.size());

        PossibleReservation possRes1 = possRes.get(0);
        assertEquals(5, possRes1.getArrivalStart());
        assertEquals(7, possRes1.getArrivalEnd());
        assertEquals(7, possRes1.getDepartureStart());
        assertEquals(9, possRes1.getDepartureEnd());

        PossibleReservation possRes2 = possRes.get(1);
        assertEquals(10, possRes2.getArrivalStart());
        assertEquals(18, possRes2.getArrivalEnd());
        assertEquals(13, possRes2.getDepartureStart());
        assertEquals(20, possRes2.getDepartureEnd());
    }

    @Test
    public void possibleReservationsInvalidDepartureInterval() {
        manager1.makeReservation(
                new ScheduleRequest(8, 11, 123, 987)
        );
        manager1.makeReservation(
                new ScheduleRequest(10, 12, 789, 987)
        );

        List<PossibleReservation> possRes =
                manager1.getPossibleReservations(request);
        assertEquals(2, possRes.size());

        PossibleReservation possRes1 = possRes.get(0);
        assertEquals(5, possRes1.getArrivalStart());
        assertEquals(7, possRes1.getArrivalEnd());
        assertEquals(7, possRes1.getDepartureStart());
        assertEquals(10, possRes1.getDepartureEnd());

        PossibleReservation possRes2 = possRes.get(1);
        assertEquals(11, possRes2.getArrivalStart());
        assertEquals(18, possRes2.getArrivalEnd());
        assertEquals(13, possRes2.getDepartureStart());
        assertEquals(20, possRes2.getDepartureEnd());
    }

    @Test
    public void possibleReservationsInvalidIntervals() {
        manager1.makeReservation(
                new ScheduleRequest(8, 11, 123, 987)
        );
        manager1.makeReservation(
                new ScheduleRequest(9, 12, 789, 987)
        );

        List<PossibleReservation> possRes =
                manager1.getPossibleReservations(request);
        assertEquals(2, possRes.size());

        PossibleReservation possRes1 = possRes.get(0);
        assertEquals(5, possRes1.getArrivalStart());
        assertEquals(7, possRes1.getArrivalEnd());
        assertEquals(7, possRes1.getDepartureStart());
        assertEquals(10, possRes1.getDepartureEnd());

        PossibleReservation possRes2 = possRes.get(1);
        assertEquals(10, possRes2.getArrivalStart());
        assertEquals(18, possRes2.getArrivalEnd());
        assertEquals(13, possRes2.getDepartureStart());
        assertEquals(20, possRes2.getDepartureEnd());
    }

    @Test
    public void possibleReservationsCapacityOverflow() {
        manager2.makeReservation(
                new ScheduleRequest(9, 12, 123, 987)
        );
        manager2.makeReservation(
                new ScheduleRequest(11, 14, 789, 987)
        );

        List<PossibleReservation> possRes =
                manager2.getPossibleReservations(request);
        assertEquals(2, possRes.size());

        PossibleReservation possRes1 = possRes.get(0);
        assertEquals(5, possRes1.getArrivalStart());
        assertEquals(8, possRes1.getArrivalEnd());
        assertEquals(7, possRes1.getDepartureStart());
        assertEquals(10, possRes1.getDepartureEnd());

        PossibleReservation possRes2 = possRes.get(1);
        assertEquals(13, possRes2.getArrivalStart());
        assertEquals(18, possRes2.getArrivalEnd());
        assertEquals(15, possRes2.getDepartureStart());
        assertEquals(20, possRes2.getDepartureEnd());
    }

    @Test
    public void possibleReservationsCapacityOverflow2() {
        manager2.makeReservation(
                new ScheduleRequest(3, 8, 123, 987)
        );
        manager2.makeReservation(
                new ScheduleRequest(7, 12, 789, 987)
        );
        manager2.makeReservation(
                new ScheduleRequest(10, 13, 789, 987)
        );
        manager2.makeReservation(
                new ScheduleRequest(15, 19, 789, 987)
        );
        manager2.makeReservation(
                new ScheduleRequest(18, 23, 789, 987)
        );


        List<PossibleReservation> possRes =
                manager2.getPossibleReservations(request);
        assertEquals(1, possRes.size());

        PossibleReservation possRes1 = possRes.get(0);
        assertEquals(13, possRes1.getArrivalStart());
        assertEquals(14, possRes1.getArrivalEnd());
        assertEquals(15, possRes1.getDepartureStart());
        assertEquals(17, possRes1.getDepartureEnd());
    }

    /*
    Time
     */
    @Test
    public void testTick() {
        manager1.makeReservation(
                new ScheduleRequest(1, 3, 123, 987)
        );
        manager1.makeReservation(
                new ScheduleRequest(21, 26, 789, 987)
        );
        assertEquals(2, manager1.getNbOfReservations());

        for (int i=1; i < Reservation.getLifeTime(); i++) {
            manager1.tick();
            assertEquals(2, manager1.getNbOfReservations());
        }

        manager1.tick();
        assertEquals(0, manager1.getNbOfReservations());
    }

    @Test
    public void testAdvanceTime() {
        manager1.makeReservation(
                new ScheduleRequest(1, 3, 123, 987)
        );
        manager1.makeReservation(
                new ScheduleRequest(21, 26, 789, 987)
        );
        assertEquals(2, manager1.getNbOfReservations());

        manager1.advanceTime(1);
        assertEquals(2, manager1.getNbOfReservations());

        manager1.advanceTime(2);
        assertEquals(2, manager1.getNbOfReservations());

        manager1.advanceTime(3);
        assertEquals(2, manager1.getNbOfReservations());

        manager1.advanceTime(4);
        assertEquals(1, manager1.getNbOfReservations());
    }
}