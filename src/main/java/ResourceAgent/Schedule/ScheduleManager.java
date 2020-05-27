package ResourceAgent.Schedule;
import java.util.ArrayList;
import java.util.List;

public class ScheduleManager {

    /*
    Capacity of the resource agent that delegates this manager.
     */
    private int capacity;

    /*
    The least amount of time it takes a AGV agent to cross
        the resource agent that delegates this manager
     */
    private int traversalTime;

    /*
    The schedule that this schedule manager manages.
     */
    private Schedule schedule;


    /*
    Constructor
     */
    public ScheduleManager(int capacity, int traversalTime)
            throws IllegalArgumentException {
        if (! isValidCapacity(capacity)) {
            throw new IllegalArgumentException(
                    "SCHEDULE MANAGER | GIVEN CAPACITY IS NOT A VALID ONE"
            );
        }
        this.capacity = capacity;
        if (! isValidTraversalTime(traversalTime)) {
            throw new IllegalArgumentException(
                    "SCHEDULE MANAGER | GIVEN TRAVERSAL TIME IS NOT A VALID ONE"
            );
        }
        this.traversalTime = traversalTime;
        this.schedule = new Schedule();
    }


    /*
    Capacity
     */
    private boolean isValidCapacity(int newCapacity) {
        return newCapacity > 0;
    }

    public int getCapacity() {
        return this.capacity;
    }

    /*
    Traversal Time
     */
    private boolean isValidTraversalTime(int newTraversalTime) {
        return newTraversalTime > 0;
    }

    public int getTraversalTime() {
        return this.traversalTime;
    }

    /*
    Schedule
     */
    public boolean isValidToRegister(int agvId) {
        for (Reservation reservation: schedule.getReservationsForAgvId(agvId)) {
            if (reservation.getStartTime() == 0) {
                return true;
            }
        }
        return false;
    }

    public List<PossibleReservation> getPossibleReservations(ScheduleRequest request) {
        int start = request.getStart();
        int end = request.getEnd();

        List<Reservation> span = schedule.getReservationsThatSpan(start, end);
        if (span.size() > 0) {
            return new ArrayList<>();
        }

        List<PossibleReservation> possibleReservations = createPossibleReservations(request);
        adjustPossibleReservationsToCapacity(possibleReservations, request);
        adjustPossibleReservationsToTraversalTime(possibleReservations, request);

        return possibleReservations;
    }

    private List<PossibleReservation> createPossibleReservations(ScheduleRequest request) {
        int start = request.getStart();
        int end = request.getEnd();

        List<Reservation> leftOverlap = schedule.getReservationsWithLeftOverlap(start, end);
        List<Reservation> within = schedule.getReservationsWithin(start, end);
        List<Reservation> rightOverlap = schedule.getReservationsWithRightOverlap(start, end);

        List<PossibleReservation> possibleReservations = new ArrayList<>();

        // first slot
        int minStart = start;
        int maxStart = end;
        int minEnd = start;
        int maxEnd = end;
        if (leftOverlap.size() > 0) {
            minEnd = Schedule.findMaxEnd(leftOverlap) + 1;
        }
        if (within.size() > 0) {
            maxStart = within.get(0).getStartTime() - 1;
            maxEnd = within.get(0).getEndTime() - 1;
        } else if (rightOverlap.size() > 0) {
            maxStart = Schedule.findMinStart(rightOverlap) - 1;
        }
        try {
            possibleReservations.add(
                    createPossibleReservation(minStart, maxStart, minEnd, maxEnd)
            );
        } catch (IllegalArgumentException e) {}

        //  middle slots
        for (int i=1; i< within.size(); i++) {
            minStart = maxStart + 2;
            minEnd = maxEnd + 2;

            maxStart = within.get(i).getStartTime() -1;
            maxEnd = within.get(i).getEndTime() - 1;

            try {
                possibleReservations.add(
                        createPossibleReservation(minStart, maxStart, minEnd, maxEnd)
                );
            } catch (IllegalArgumentException e) {}
        }

        // last slot
        //      Only when reservations fall within interval, otherwise this is already dealt with
        //      when creating first slot
        if (within.size() > 0) {
            minStart = maxStart + 2;
            minEnd = maxEnd + 2;
            maxStart = end;
            maxEnd = end;
            if (rightOverlap.size() > 0) {
                maxStart = Schedule.findMinStart(rightOverlap) -1;
            }
            try {
                possibleReservations.add(
                        createPossibleReservation(minStart, maxStart, minEnd, maxEnd)
                );
            } catch (IllegalArgumentException e) {}
        }

        return possibleReservations;
    }

    private PossibleReservation createPossibleReservation(int minStart, int maxStart, int minEnd, int maxEnd) {
        return new PossibleReservation(
                new Interval(minStart, maxStart), new Interval(minEnd, maxEnd)
        );
    }

    private void adjustPossibleReservationsToCapacity(
            List<PossibleReservation> possibleReservations, ScheduleRequest request) {
        int start = request.getStart();
        int end = request.getEnd();

        List<Interval> occupiedIntervals = getOccupiedIntervals(request);

        List<PossibleReservation> toRemove = new ArrayList<>();
        for (PossibleReservation possRes: possibleReservations) {
            for (Interval occupiedInterval: occupiedIntervals) {
                try {
                    possRes.adjustToInterval(occupiedInterval);
                } catch (IllegalStateException e) {
                    toRemove.add(possRes);
                    break;
                }
            }
        }

        possibleReservations.removeAll(toRemove);
    }

    private List<Interval> getOccupiedIntervals(ScheduleRequest request) {
        int start = request.getStart();
        int end = request.getEnd();

        List<Reservation> leftOverlap = schedule.getReservationsWithLeftOverlap(start, end);
        List<Reservation> within = schedule.getReservationsWithin(start, end);
        List<Reservation> rightOverlap = schedule.getReservationsWithRightOverlap(start, end);

        List<Integer> capacitySlots = createCapacitySlots(request);
        adjustCapacitySlotsList(capacitySlots, leftOverlap, request);
        adjustCapacitySlotsList(capacitySlots, within, request);
        adjustCapacitySlotsList(capacitySlots, rightOverlap, request);

        return createOccupiedIntervals(capacitySlots, request);
    }

    private List<Integer> createCapacitySlots(ScheduleRequest request) {
        List<Integer> slots = new ArrayList<>();
        int nb = request.getEnd() - request.getStart();
        for (int i=0; i<=nb; i++) {
            slots.add(capacity);
        }
        return slots;
    }

    private static void adjustCapacitySlotsList(
            List<Integer>capacitySlots, List<Reservation> reservations, ScheduleRequest request) {
        for (Reservation reservation: reservations) {
            adjustCapacitySlots(capacitySlots, reservation, request);
        }
    }

    private static void adjustCapacitySlots(
            List<Integer>capacitySlots, Reservation reservation, ScheduleRequest request) {
        assert(capacitySlots.size() == request.getEnd() - request.getStart() + 1);

        int maxStart = Math.max(request.getStart(), reservation.getStartTime()) - request.getStart();
        int minEnd = Math.min(request.getEnd(), reservation.getEndTime()) - request.getStart();

        for (int i=maxStart; i<=minEnd; i++) {
            int capacity = capacitySlots.get(i);
            capacitySlots.remove(i);
            if (reservation.matchingDestination(request.getDestination())) {
                capacitySlots.add(i, capacity - 1);
            } else {
                capacitySlots.add(i, 0);
            }
        }
    }

    private List<Interval> createOccupiedIntervals(List<Integer>capacitySlots, ScheduleRequest request)
            throws IllegalArgumentException {
        int start = request.getStart();
        List<Interval> occupiedIntervals = new ArrayList<>();

        boolean currentlyInOccupiedSlot = false;
        int startTime = start;
        int endTime = start;
        for (int i=0; i<capacitySlots.size(); i++) {
            if (capacitySlots.get(i) == 0) {
                if (! currentlyInOccupiedSlot) {
                    currentlyInOccupiedSlot = true;
                    startTime = start + i;
                }
                endTime = start + i;
            } else if (capacitySlots.get(i) > 0) {
                if (currentlyInOccupiedSlot) {
                    currentlyInOccupiedSlot = false;
                    occupiedIntervals.add(
                            new Interval(startTime, endTime)
                    );
                }
            } else {
                throw new IllegalArgumentException("SCHEDULE | SLOTS GO BELOW ZERO");
            }
        }
        if (currentlyInOccupiedSlot) {
            occupiedIntervals.add(new Interval(startTime, endTime));
        }

        return occupiedIntervals;
    }

    private void adjustPossibleReservationsToTraversalTime(
            List<PossibleReservation> possibleReservations, ScheduleRequest request) {
        List<PossibleReservation> toRemove = new ArrayList<>();
        for (PossibleReservation possRes: possibleReservations) {
            try {
                possRes.adjustToTraversalTime(traversalTime);
            } catch (IllegalStateException e) {
                toRemove.add(possRes);
            }
        }

        possibleReservations.removeAll(toRemove);
    }

    public void makeReservation(ScheduleRequest request)
            throws IllegalArgumentException {
        schedule.addReservation(
                request.getStart(), request.getEnd(),
                request.getAgvId(), request.getDestination()
        );
    }
}
