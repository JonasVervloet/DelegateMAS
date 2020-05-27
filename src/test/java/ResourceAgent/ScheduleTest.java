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
                7, 9, 123, new Point(5, 6)
        );
        schedule1.addReservation(
                10, 14, 456, new Point(5, 6)
        );
        schedule1.addReservation(
                13, 16, 789, new Point(5, 6)
        );
        schedule1.addReservation(
                4, 6, 369, new Point(5, 6)
        );
        schedule1.addReservation(
                19, 21, 369, new Point(5, 6)
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
                schedule1.getReservationsWithLeftOverlap(5, 20).size());
        assertEquals(2,
                schedule1.getReservationsWithLeftOverlap(14, 20).size());
    }

    @Test
    public void reservationsWithRightOverlap() {
        assertEquals(1,
                schedule1.getReservationsWithRightOverlap(5, 20).size());
        assertEquals(2,
                schedule1.getReservationsWithRightOverlap(5, 13).size());
    }

    @Test
    public void reservationsWithin() {
        assertEquals(3,
                schedule1.getReservationsWithin(5, 20).size());
        assertEquals(3,
                schedule1.getReservationsWithin(7, 16).size());
        assertEquals(2,
                schedule1.getReservationsWithin(8, 20).size());
        assertEquals(2,
                schedule1.getReservationsWithin(5, 15).size());
    }

    @Test
    public void reservationsThatSpan() {
        assertEquals(1,
                schedule1.getReservationsThatSpan(11, 12).size());
        assertEquals(1,
                schedule1.getReservationsThatSpan(11, 13).size());
        assertEquals(1,
                schedule1.getReservationsThatSpan(14, 15).size());
        assertEquals(0,
                schedule1.getReservationsThatSpan(5, 20).size());
    }

    @Test
    public void findMaxEnd() {
        Reservation res1 = new Reservation(5, 12, 145, new Point(4, 5));
        Reservation res2 = new Reservation(6, 11, 789, new Point(8, 9));
        Reservation res3 = new Reservation(2, 19, 321, new Point(8, 2));
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
        Reservation res1 = new Reservation(5, 12, 145, new Point(4, 5));
        Reservation res2 = new Reservation(6, 11, 789, new Point(8, 9));
        Reservation res3 = new Reservation(2, 19, 321, new Point(8, 2));
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
}