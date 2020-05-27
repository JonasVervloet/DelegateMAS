package ResourceAgent.Schedule;

import com.github.rinde.rinsim.geom.Point;
import org.jetbrains.annotations.NotNull;

public class Reservation implements Comparable {

    private static int LIFETIME = 100;

    /*
    Start time of the reservation
     */
    private int start;

    /*
    End time of the reservation
     */
    private int end;

    /*
    Id of the AGV Agent that made the reservation
     */
    private int agvId;

    /*
    The destination point of the AGV agent within the resource agent
        that manages this reservation.
     */
    private Point destination;

    /*
    The time to live for this reservation. After this time expires, it
        will be removed from the schedule.
     */
    private int timeToLive;

    public Reservation(int start, int end, int agvId,
                       Point destination) throws IllegalArgumentException {
        if (! isValidStartTime(start)) {
            throw new IllegalArgumentException("RESERVATION | NO VALID START TIME");
        }
        this.start = start;

        if (! isValidEndTime(end)) {
            throw new IllegalArgumentException("RESERVATION | NO VALID END TIME");
        }
        this.end = end;

        if (! isValidAgvId(agvId)) {
            throw new IllegalArgumentException("RESERVATION | NO VALID AGV ID");
        }
        this.agvId = agvId;
        this.destination = destination;

        this.timeToLive = LIFETIME;
    }


    /*
    Start/End
     */
    private boolean isValidStartTime(int startTime) {
        return startTime >= 0;
    }

    private boolean isValidEndTime(int endTime) {
        return endTime > start;
    }

    public int getStartTime() {
        return this.start;
    }

    public int getEndTime() {
        return this.end;
    }

    public boolean overlap(int otherStart, int otherEnd) {
        return !(end < otherStart || start > otherEnd);
    }

    public boolean overlap(Reservation other) {
        return overlap(other.start, other.end);
    }

    public boolean leftOverlapWithOther(int otherStart, int otherEnd) {;
        assert(otherEnd >= otherStart);
        return  (start < otherStart && end >= otherStart &&
                end <= otherEnd);
    }

    public boolean fallsWithinOther(int otherStart, int otherEnd) {
        assert(otherEnd >= otherStart);
        return (start >= otherStart && start <= otherEnd
                && end >= otherStart && end <= otherEnd);
    }

    public boolean rightOverlapWithOther(int otherStart, int otherEnd) {
        assert(otherEnd >= otherStart);
        return (start <= otherEnd && end > otherEnd &&
                start >= otherStart);
    }

    public boolean spansOther(int otherStart, int otherEnd) {
        assert(otherEnd >= otherStart);
        return (start < otherStart && end > otherEnd);
    }


    /*
    AgvId
     */
    private boolean isValidAgvId(int agvId) {
        return agvId >= 0;
    }

    public int getAgvId() {
        return this.agvId;
    }

    public boolean matchesAgvId(int otherId) {
        return agvId == otherId;
    }

    public boolean hasEqualAgvId(Reservation other) {
        return other.getAgvId() == agvId;
    }

    /*
    Destination
     */
    public boolean matchingDestination(Point aDestination) {
        return destination.equals(aDestination);
    }

    public Point getDestination() {
        return this.destination;
    }

    /*
    Time To Live
     */
    public static int getLifeTime() {
        return Reservation.LIFETIME;
    }

    public int getTimeToLive() {
        return this.timeToLive;
    }

    public void evaporate()
            throws IllegalStateException {
        if (timeToLive == 0) {
            throw new IllegalStateException("RESERVATION | EVAPORATING WHEN TIME TO LIVE EQUALS ZERO");
        }
        if (end == 0) {
            throw new IllegalStateException("RESERVATION | EVAPORATION WHEN END TIME EQUALS ZERO");
        }

        timeToLive -= 1;
        if (start != 0) {
            start -= 1;
        }
        end -= 1;
    }

    public void refresh() {
        timeToLive = LIFETIME;
    }


    @Override
    public String toString() {
        return "Reservation: start - " + this.start + ", end - " + this.end
                + ", agv ID - " + this.agvId + ", destination - " + this.destination.toString();
    }

    @Override
    public int compareTo(@NotNull Object o) {
        Reservation other = (Reservation) o;

        if (other.getStartTime() > getStartTime()) {
            return -1;
        } else if (other.getStartTime() < getStartTime()) {
            return 1;
        } else {
             if (other.getEndTime() > getEndTime()) {
                 return -1;
             } else if (other.getEndTime() < getEndTime()) {
                 return 1;
             } else {
                 return 0;
            }
        }
    }
}
