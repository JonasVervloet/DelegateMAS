package ResourceAgent.OrderMap;

public class OrderEntry {

    /*
    The life time of a order entry.
        If the entry is not refreshed
        the entry will be deleted after
        this time.
     */
    private static int LIFETIME = 100;

    /*
    The order id of the package to which this
        entry points to.
     */
    private int orderId;

    /*
    The distance to the package to which this
        entry points to.
     */
    private int distance;

    /*
    The time to live for this package entry. When this
        time runs out, the entry will be removed from the map.
     */
    private int timeToLive;


    /*
    Constructor
     */
    public OrderEntry(int orderId, int distance)
            throws IllegalArgumentException {
        if (! isValidOrderId(orderId)) {
            throw new IllegalArgumentException(
                    "PACKAGE ENTRY | THE GIVEN PACKAGE ID IS NOT A VALID ONE"
            );
        }
        this.orderId = orderId;

        if (! isValidDistance(distance)) {
            throw new IllegalArgumentException(
                    "PACKAGE ENTRY | THE GIVEN DISTANCE IS NOT A VALID ONE"
            );
        }
        this.distance = distance;

        timeToLive = LIFETIME;
    }


    /*
    Order ID
     */
    private boolean isValidOrderId(int orderId) {
        return orderId > 0;
    }

    public boolean matchesOrderId(int otherId) {
        return orderId == otherId;
    }

    /*
    Distance
     */
    private boolean isValidDistance(int distance) {
        return distance > 0;
    }

    public boolean hadEqualDistance(int aDistance) {
        return distance == aDistance;
    }

    public int getDistance() {
        return distance;
    }

    /*
    Life Time
     */
    private static boolean isValidLifeTime(int lifeTime) {
        return lifeTime > 0;
    }

    public static int getLifeTime() {
        return OrderEntry.LIFETIME;
    }

    public static void setLifeTime(int lifeTime)
            throws IllegalArgumentException {
        if (! isValidLifeTime(lifeTime)) {
            throw new IllegalArgumentException(
                    "PACKAGE ENTRY | THE GIVEN LIFE TIME IS NOT A VALID ONE"
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
        timeToLive = OrderEntry.LIFETIME;
    }

    /*
    Equal
     */
    public boolean isEqualEntry(int aOrderId, int aDistance) {
        return matchesOrderId(aOrderId) && hadEqualDistance(aDistance);
    }
}
