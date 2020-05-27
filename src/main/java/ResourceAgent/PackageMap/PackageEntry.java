package ResourceAgent.PackageMap;

public class PackageEntry {

    private static int LIFETIME = 100;

    /*
    The package id of the package to which this
        entry points to.
     */
    private int packageId;

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
    public PackageEntry(int packageId, int distance)
            throws IllegalArgumentException {
        if (! isValidPackageId(packageId)) {
            throw new IllegalArgumentException(
                    "PACKAGE ENTRY | THE GIVEN PACKAGE ID IS NOT A VALID ONE"
            );
        }
        this.packageId = packageId;

        if (! isValidDistance(distance)) {
            throw new IllegalArgumentException(
                    "PACKAGE ENTRY | THE GIVEN DISTANCE IS NOT A VALID ONE"
            );
        }
        this.distance = distance;

        timeToLive = LIFETIME;
    }


    /*
    Resource ID
     */
    private boolean isValidPackageId(int packageId) {
        return packageId > 0;
    }

    public boolean matchesPackageId(int otherId) {
        return packageId == otherId;
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
    Time to life
     */
    public static int getLifeTime() {
        return PackageEntry.LIFETIME;
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
        timeToLive = PackageEntry.LIFETIME;
    }

    /*
    Equal
     */
    public boolean isEquaylEntry(int aPackageId, int aDistance) {
        return matchesPackageId(aPackageId) && hadEqualDistance(aDistance);
    }
}
