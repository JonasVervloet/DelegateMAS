package DelegateMAS;

public class IntentionEntry {

    /*
    The resource ID of this intention entry.
     */
    private final int resourceId;

    /*
    The start time of this intention entry.
     */
    private final int start;

    /*
    The end time of this intention entry
     */
    private final int end;


    /*
    Constructor
     */
    public IntentionEntry(int resourceId, int start, int end)
            throws IllegalArgumentException {
        if (! isValidResourceId(resourceId)) {
            throw new IllegalArgumentException(
                    "INTENTION ENTRY | THE GIVEN RESOURCE ID IS NOT A VALID ONE"
            );
        }
        this.resourceId = resourceId;

        if (! isValidStart(start)) {
            throw new IllegalArgumentException(
                    "INTENTION ENTRY | THE GIVEN START TIME IS NOT A VALID ONE"
            );
        }
        this.start = start;

        if (! isValidEnd(end)) {
            throw new IllegalArgumentException(
                    "INTENTION ENTRY | THE GIVEN END TIME IS NOT A VALID ONE"
            );
        }
        this.end = end;
    }


    /*
    Resource ID
     */
    private boolean isValidResourceId(int resourceId) {
        return resourceId > 0;
    }

    public int getResourceId() {
        return resourceId;
    }

    /*
    Start
     */
    private boolean isValidStart(int start) {
        return start > 0;
    }

    public int getStartTime() {
        return start;
    }

    /*
    End
     */
    private boolean isValidEnd(int end) {
        return end > 0;
    }

    public int getEndTime() {
        return end;
    }
}
