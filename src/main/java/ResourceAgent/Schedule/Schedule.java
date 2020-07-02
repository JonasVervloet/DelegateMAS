package ResourceAgent.Schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Schedule {

    /*
    List of all reservations that are part of
        this schedule.
     */
    private List<Reservation> reservations;


    /*
    Constructor
     */
    public Schedule() {
        reservations = new ArrayList<>();
    }


    /*
    Static methods
     */
    public static int findMaxEnd(List<Reservation> reservationList)
            throws IllegalArgumentException {
        if (reservationList.size() == 0) {
            throw new IllegalArgumentException(
                    "SCHEDULE | FIND MAX END OF EMPTY LIST"
            );
        }
        int maxEnd = 0;
        for (Reservation res: reservationList) {
            maxEnd = Math.max(maxEnd, res.getEndTime());
        }
        return maxEnd;
    }

    public static int findMinStart(List<Reservation> reservationList) {
        if (reservationList.size() == 0) {
            throw new IllegalArgumentException(
                    "SCHEDULE | FIND MAX END OF EMPTY LIST"
            );
        }
        int minStart = Integer.MAX_VALUE;
        for (Reservation res: reservationList) {
            minStart = Math.min(minStart, res.getStartTime());
        }
        return minStart;
    }

    /*
    Reservations
     */
    public int getNbOfReservations() {
        return reservations.size();
    }

    public List<Reservation> getReservationsForAgvId(int agvId) {
        List<Reservation> validReservations = new ArrayList<>();

        for (Reservation reservation: reservations) {
            if (reservation.matchesAgvId(agvId)) {
                validReservations.add(reservation);
            }
        }

        return validReservations;
    }

    public List<Reservation> getReservationsWithLeftOverlap(int start, int end, int agvId) {
        List<Reservation> validReservations = new ArrayList<>();
        for (Reservation reservation: reservations) {
            if (! reservation.matchesAgvId(agvId) &&
                    reservation.leftOverlapWithOther(start, end)) {
                validReservations.add(reservation);
            }
        }

        return validReservations;
    }

    public List<Reservation> getReservationsWithRightOverlap(int start, int end, int agvId) {
        List<Reservation> validReservations = new ArrayList<>();
        for (Reservation reservation: reservations) {
            if (! reservation.matchesAgvId(agvId) &&
                    reservation.rightOverlapWithOther(start, end)) {
                validReservations.add(reservation);
            }
        }

        return validReservations;
    }

    public List<Reservation> getReservationsWithin(int start, int end, int agvId) {
        List<Reservation> validReservations = new ArrayList<>();
        for (Reservation reservation: reservations) {
            if (! reservation.matchesAgvId(agvId) &&
                    reservation.fallsWithinOther(start, end)) {
                validReservations.add(reservation);
            }
        }

        return validReservations;
    }

    public List<Reservation> getReservationsThatSpan(int start, int end, int agvId) {
        List<Reservation> validReservations = new ArrayList<>();
        for (Reservation reservation: reservations) {
            if (! reservation.matchesAgvId(agvId) &&
                    reservation.spansOther(start, end)) {
                validReservations.add(reservation);
            }
        }

        return validReservations;
    }

    public void addReservation(int start, int end, int agvId, int destinationId) {
        reservations.add(new Reservation(start, end, agvId, destinationId));
        Collections.sort(reservations);
    }

    /*
    Evaporation
     */
    public void evaporate() {
        List<Reservation> toRemove = new ArrayList<>();
        for (Reservation reservation: reservations) {
            reservation.evaporate();
            if (! reservation.hasTimeToLive()) {
                toRemove.add(reservation);
            }
        }

        reservations.removeAll(toRemove);
    }

    public void advanceTime(int currentTime) {
        List<Reservation> toRemove = new ArrayList<>();
        for (Reservation reservation: reservations) {
            if (reservation.endTimePassed(currentTime)) {
                toRemove.add(reservation);
            }
        }

        reservations.removeAll(toRemove);
    }

    public void output() {
        System.out.println("Schedule");
        for (Reservation reservation: reservations) {
            System.out.println(reservation);
        }
    }
}
