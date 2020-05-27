package ResourceAgent.Schedule;

import com.github.rinde.rinsim.geom.Point;

public class ScheduleRequest {

    private int start;

    private int end;

    private int agvId;

    private Point destination;


    public ScheduleRequest(int start, int end, int agvId, Point destination)
            throws IllegalArgumentException{
        if (! isValidStart(start)) {
            throw new IllegalArgumentException(
                    "SCHEDULE REQUEST | GIVEN START IS NOT A VALID ONE"
            );
        }
        this.start = start;
        if (! isValidEnd(end)) {
            throw new IllegalArgumentException(
                    "SCHEDULE REQUEST | GIVEN END IS NOT A VALID ONE"
            );
        }
        this.end = end;
        if (! isValidAgvId(agvId)) {
            throw new IllegalArgumentException(
                    "SCHEDULE REQUEST | GIVEN AGV ID IS NOT A VALID ONE"
            );
        }
        this.agvId = agvId;
        if (! isValidDestination(destination)) {
            throw new IllegalArgumentException(
                    "SCHEDULE REQUEST | GIVEN DESTINATION IS NOT A VALID ONE"
            );
        }
        this.destination = destination;
    }


    /*
    Start
     */
    public boolean isValidStart(int start) {
        return start >= 0;
    }

    public int getStart() {
        return start;
    }

    /*
    End
     */
    public boolean isValidEnd(int end) {
        return end >= start;
    }

    public int getEnd() {
        return end;
    }

    /*
    Agv ID
     */
    public boolean isValidAgvId(int agvId) {
        return agvId >= 0;
    }

    public int getAgvId() {
        return agvId;
    }

    /*
    Destination
     */
    public boolean isValidDestination(Point destination) {
        return !( destination == null);
    }

    public Point getDestination() {
        return destination;
    }
}
