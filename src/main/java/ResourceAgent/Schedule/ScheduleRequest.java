package ResourceAgent.Schedule;

import com.github.rinde.rinsim.geom.Point;

public class ScheduleRequest {

    /*
    The start time of this schedule request.
     */
    private int start;

    /*
    The end time of this schedule request.
     */
    private int end;

    /*
    The ID of the AGV Agent that makes this
        request.
     */
    private int agvId;

    /*
    The ID of the resource agent that the AGV
        agent will visit after this resource
        agent.
     */
    private int destinationId;


    /*
    Constructor
     */
    public ScheduleRequest(int start, int end, int agvId, int destinationId)
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
        if (! isValidDestinationId(destinationId)) {
            throw new IllegalArgumentException(
                    "SCHEDULE REQUEST | GIVEN DESTINATION ID IS NOT A VALID ONE"
            );
        }
        this.destinationId = destinationId;
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
    public boolean isValidDestinationId(int destinationId) {
        return destinationId > 0;
    }

    public int getDestinationId() {
        return destinationId;
    }
}
