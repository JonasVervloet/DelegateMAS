package ResourceAgent;

import ResourceAgent.Schedule.Reservation;
import ResourceAgent.Schedule.Schedule;
import com.github.rinde.rinsim.geom.Point;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ScheduleTest {

    private Schedule schedule1;

    /*
    Before
     */
    @Before
    public void init() {
        schedule1 = new Schedule();
        schedule1.addReservation(
                7, 9, 123, 987
        );
        schedule1.addReservation(
                10, 14, 456, 987
        );
        schedule1.addReservation(
                13, 16, 789, 987
        );
        schedule1.addReservation(
                4, 6, 369, 987
        );
        schedule1.addReservation(
                19, 21, 369, 987
        );
    }

    /*
    Constructor
     */
    @Test
    public void validConstructor() {
        Schedule schedule = new Schedule();
        assertEquals(0, schedule.getNbOfReservations());
    }

    /*
    Reservations
     */
    @Test
    public void getReservationsForAgvId() {
        assertEquals(1, schedule1.getReservationsForAgvId(123).size());
        assertEquals(1, schedule1.getReservationsForAgvId(456).size());
        assertEquals(1, schedule1.getReservationsForAgvId(789).size());
        assertEquals(2, schedule1.getReservationsForAgvId(369).size());
        assertEquals(0, schedule1.getReservationsForAgvId(985).size());
    }

    @Test
    public void reservationsWithLeftOverlap() {
        assertEquals(1,
                schedule1.getReservationsWithLeftOverlap(5, 20, 321).size());
        assertEquals(2,
                schedule1.getReservationsWithLeftOverlap(14, 20, 321).size());
    }

    @Test
    public void reservationsWithRightOverlap() {
        assertEquals(1,
                schedule1.getReservationsWithRightOverlap(5, 20, 321).size());
        assertEquals(2,
                schedule1.getReservationsWithRightOverlap(5, 13, 321).size());
    }

    @Test
    public void reservationsWithin() {
        assertEquals(3,
                schedule1.getReservationsWithin(5, 20, 321).size());
        assertEquals(3,
                schedule1.getReservationsWithin(7, 16, 321).size());
        assertEquals(2,
                schedule1.getReservationsWithin(8, 20, 321).size());
        assertEquals(2,
                schedule1.getReservationsWithin(5, 15, 321).size());
    }

    @Test
    public void reservationsThatSpan() {
        assertEquals(1,
                schedule1.getReservationsThatSpan(11, 12, 321).size());
        assertEquals(1,
                schedule1.getReservationsThatSpan(11, 13, 321).size());
        assertEquals(1,
                schedule1.getReservationsThatSpan(14, 15, 321).size());
        assertEquals(0,
                schedule1.getReservationsThatSpan(5, 20, 321).size());
    }

    @Test
    public void findMaxEnd() {
        Reservation res1 = new Reservation(5, 12, 145, 654);
        Reservation res2 = new Reservation(6, 11, 789, 321);
        Reservation res3 = new Reservation(2, 19, 321, 852);
        List<Reservation> resList = new ArrayList<>();

        resList.add(res1);
        assertEquals(12, Schedule.findMaxEnd(resList));

        resList.add(res2);
        assertEquals(12, Schedule.findMaxEnd(resList));

        resList.add(res3);
        assertEquals(19, Schedule.findMaxEnd(resList));
    }

    @Test (expected = IllegalArgumentException.class)
    public void findMaxEndEmptyList() {
        List<Reservation> resList = new ArrayList<>();
        Schedule.findMaxEnd(resList);
    }

    @Test
    public void findMinStart() {
        Reservation res1 = new Reservation(5, 12, 145, 654);
        Reservation res2 = new Reservation(6, 11, 789, 321);
        Reservation res3 = new Reservation(2, 19, 321, 852);
        List<Reservation> resList = new ArrayList<>();

        resList.add(res1);
        assertEquals(5, Schedule.findMinStart(resList));

        resList.add(res2);
        assertEquals(5, Schedule.findMinStart(resList));

        resList.add(res3);
        assertEquals(2, Schedule.findMinStart(resList));
    }

    @Test (expected = IllegalArgumentException.class)
    public void findMinStartEmptyList() {
        List<Reservation> resList = new ArrayList<>();
        Schedule.findMinStart(resList);
    }

    /*
    Evaporation
     */
    @Test
    public void testEvaporation() {
        for (int i=1; i<Reservation.getLifeTime(); i++) {
            schedule1.evaporate();
            assertEquals(5, schedule1.getNbOfReservations());
        }

        schedule1.evaporate();
        assertEquals(0, schedule1.getNbOfReservations());
    }

    @Test
    public void testAdvanceTime() {
        schedule1.addReservation(0, 1, 123, 987);
        assertEquals(6, schedule1.getNbOfReservations());

        schedule1.advanceTime(1);
        assertEquals(6, schedule1.getNbOfReservations());

        schedule1.advanceTime(2);
        assertEquals(5, schedule1.getNbOfReservations());

        schedule1.advanceTime(3);
        assertEquals(5, schedule1.getNbOfReservations());

        schedule1.advanceTime(4);
        assertEquals(5, schedule1.getNbOfReservations());

        schedule1.advanceTime(5);
        assertEquals(5, schedule1.getNbOfReservations());

        schedule1.advanceTime(6);
        assertEquals(5, schedule1.getNbOfReservations());

        schedule1.advanceTime(7);
        assertEquals(4, schedule1.getNbOfReservations());
    }
}