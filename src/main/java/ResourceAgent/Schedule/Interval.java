package ResourceAgent.Schedule;

public class Interval {

    /*
    The start time of this interval
     */
    final int start;

    /*
    The end time of this interval
     */
    final int end;

    /*
    Constructor
     */
    public Interval(int start, int end)
            throws IllegalArgumentException {
        if (! isValidStartTime(start)) {
            throw new IllegalArgumentException(
                    "RESERVATION | INVALID START TIME"
            );
        }
        this.start = start;

        if (! isValidEndTime(end)) {
            throw new IllegalArgumentException(
                    "RESERVATION | INVALID END TIME"
            );
        }
        this.end = end;
    }

    public Interval(Interval other) {
        this.start = other.getStart();
        this.end = other.getEnd();
    }


    /*
    Start
     */
    public boolean isValidStartTime(int startTime) {
        return startTime >= 0;
    }

    public int getStart() {
        return this.start;
    }

    /*
    End
     */
    public boolean isValidEndTime(int endTime) {
        return endTime >= start;
    }

    public int getEnd() {
        return this.end;
    }

    /*
    Overlap
     */
    public boolean withingInterval(int time) {
        return (time >= start && time <= end);
    }

    public boolean overlap(int otherStart, int otherEnd) {
        return (! (otherEnd < start || otherStart > end));
    }

    public boolean overlapInterval(Interval other) {
        return overlap(other.getStart(), other.getEnd());
    }

    public Interval incrementInterval() {
        return new Interval(getStart() + 1, getEnd() + 1);
    }

    public Interval decrementInterval() {
        return new Interval(getStart() - 1, getEnd() - 1);
    }

    public Interval getOverlappingInterval(Interval other) {
        if (! overlapInterval(other)) {
            throw new IllegalArgumentException(
                    "INTERVAL | THE TWO INTERVALS DO NO OVERLAP"
            );
        }
        return new Interval(
                Math.max(getStart(), other.getStart()),
                Math.min(getEnd(), other.getEnd())
        );
    }

    /*
    Utils
     */
    @Override
    public String toString() {
        return "Interval: start - " + start + ", end - " + end;
    }

    public Interval copyInterval() {
        return new Interval(this);
    }
}
