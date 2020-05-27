package ResourceAgent.Schedule;

public class PossibleReservation {

    /*
    The arrival interval of this possible reservation.
        The eventual actual reservation should arrive in between this interval.
     */
    private Interval arrivalInterval;

    /*
    The departure interval of this possible reservation.
        The eventual actual reservation should depart in between this interval.
     */
    private Interval departureInterval;

    public PossibleReservation(Interval arrivalInterval, Interval departureInterval)
            throws IllegalArgumentException {
        if (! isValidArrivalInterval(arrivalInterval)) {
            throw new IllegalArgumentException(
                    "POSSIBLE RESERVATION | INVALID ARRIVAL INTERVAL"
            );
        }
        this.arrivalInterval = arrivalInterval;

        if (! isValidDepartureInterval(departureInterval)) {
            throw new IllegalArgumentException(
                    "POSSIBLE RESERVATION | INVALID DEPARTURE INTERVAL"
            );
        }
        this.departureInterval = departureInterval;
    }

    /*
    Arrival interval
     */
    public boolean isValidArrivalInterval(Interval arrivalInterval) {
        return true;
    }

    public int getArrivalStart() {
        return arrivalInterval.getStart();
    }

    public int getArrivalEnd() {
        return arrivalInterval.getEnd();
    }

    /*
    Departure interval
     */
    public boolean isValidDepartureInterval(Interval departureInterval) {
        return true;
    }

    public int getDepartureStart() {
        return departureInterval.getStart();
    }

    public int getDepartureEnd() {
        return departureInterval.getEnd();
    }


    /*
    Interval interaction
     */
    private boolean overlap(Interval interval) {
        return !(interval.getEnd() < getArrivalStart() ||
                interval.getStart() > getDepartureEnd());
    }

    private boolean isLeftAdjustable(Interval interval) {
        return (getArrivalStart() < interval.getStart() &&
                getDepartureStart() < interval.getStart());
    }

    private boolean isRightAdjustable(Interval interval) {
        return (getArrivalEnd() > interval.getEnd() &&
                getDepartureEnd() > interval.getEnd());
    }

    public void adjustToInterval(Interval interval)
            throws IllegalStateException {
        if (overlap(interval)) {
            if (isLeftAdjustable(interval) && isRightAdjustable(interval)) {
                System.out.println("POSSIBLE RESERVATION | LEFT AND RIGTH ADJUSTABLE!");
            } else if (isLeftAdjustable(interval)) {
                adjustToIntervalLeft(interval);
            } else if (isRightAdjustable(interval)) {
                adjustToIntervalRight(interval);
            } else {
                throw new IllegalStateException(
                        "POSSIBLE RESERVATION | NOT POSSIBLE TO ADJUST TO INTERVAL"
                );
            }
        }
    }

    private void adjustToIntervalLeft(Interval interval) {
        arrivalInterval = new Interval(
                getArrivalStart(),
                Math.min(getArrivalEnd(), interval.getStart() - 1)
        );

        departureInterval = new Interval(
                getDepartureStart(),
                Math.min(getDepartureEnd(), interval.getStart() - 1)
        );
    }

    private void adjustToIntervalRight(Interval interval) {
        arrivalInterval = new Interval(
                Math.max(getArrivalStart(), interval.getEnd() + 1),
                getArrivalEnd()
        );

        departureInterval = new Interval(
                Math.max(getDepartureStart(), interval.getEnd() + 1),
                getDepartureEnd()
        );
    }

    public void adjustToTraversalTime(int traversalTime)
            throws IllegalStateException {
        int minStart = arrivalInterval.getStart();
        int maxStart = arrivalInterval.getEnd();

        int minEnd = departureInterval.getStart();
        int maxEnd = departureInterval.getEnd();

        try {
            arrivalInterval = new Interval(
                    minStart, Math.min(maxStart, maxEnd - (traversalTime - 1))
            );
            departureInterval = new Interval(
                    Math.max(minStart + (traversalTime - 1), minEnd), maxEnd
            );
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                    "POSSIBLE RESERVATION | NOT POSSIBLE TO ADJUST TO GIVEN TRAVERSAL TIME"
            );
        }
    }
}
