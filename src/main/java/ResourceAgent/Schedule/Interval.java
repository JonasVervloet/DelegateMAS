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

    public boolean withingInterval(int time) {
        return (time >= start && time <= end);
    }

    @Override
    public String toString() {
        return "Interval: start - " + start + ", end - " + end;
    }
}
