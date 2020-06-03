package ResourceAgent.ResourceMap;

public class ResourceEntry {

    /*
    The life time of a resource entry.
        If the entry is not refreshed
        the entry will be deleted after
        this time.
     */
    private static int LIFETIME = 100;

    /*
    The resource id of the resource agent that this
        entry points to.
     */
    private int resourceId;

    /*
    The distance to the resource agent with the
        above id.
     */
    private int distance;

    /*
    The time to live for this resource entry. When this
        time runs out, the entry will be removed from the
        map.
     */
    private int timeToLive;


    /*
    Constructor
     */
    public ResourceEntry(int resourceId, int distance)
            throws IllegalArgumentException {
        if (! isValidResourceId(resourceId)) {
            throw new IllegalArgumentException(
                    "RESOURCE ENTRY | GIVEN RESOURCE ID IS NOT A VALID ONE"
            );
        }
        this.resourceId = resourceId;
        if (! isValidDistance(distance)) {
            throw new IllegalArgumentException(
                    "RESOURCE ENTRY | GIVEN DISTANCE IS NOT A VALID ONE"
            );
        }
        this.distance = distance;

        this.timeToLive = LIFETIME;
    }


    /*
    Resource ID
     */
    private boolean isValidResourceId(int resourceId) {
        return resourceId > 0;
    }

    public boolean matchesResourceId(int otherId) {
        return resourceId == otherId;
    }

    /*
    Distance
     */
    private boolean isValidDistance(int distance) {
        return distance > 0;
    }

    public boolean hasEqualDistance(int aDistance) {
        return distance == aDistance;
    }

    public int getDistance() {
        return distance;
    }

    /*
    Lifetime
     */
    public static int getLifeTime() {
        return ResourceEntry.LIFETIME;
    }

    private static boolean isValidLifeTime(int lifeTime) {
        return lifeTime > 0;
    }

    public static void setLifeTime(int lifeTime)
            throws IllegalArgumentException {
        if (! isValidLifeTime(lifeTime)) {
            throw new IllegalArgumentException(
                    "RESOURCE ENTRY | THE GIVEN LIFE TIME IS NOT A VALID ONE"
            );
        }

        LIFETIME = lifeTime;
    }

    /*
    Time To Live
     */

    public boolean isDone() {
        return timeToLive == 0;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public void evaporate()
            throws IllegalStateException {
        if (timeToLive == 0) {
            throw new IllegalStateException(
                    "RESOURCE ENTRY | EVAPORATING WHEN TIME TO LIVE EQUALS ZERO"
            );
        }
        timeToLive -= 1;
    }

    public void refresh() {
        timeToLive = ResourceEntry.LIFETIME;
    }

    /*
    Equal
     */
    public boolean isEqualEntry(int aResourceId, int aDistance) {
        return matchesResourceId(aResourceId) && hasEqualDistance(aDistance);
    }
}
