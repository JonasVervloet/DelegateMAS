package ResourceAgent.PackageMap;

import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class PackageManager {

    /*
    The package map that this package manager manages.
     */
    private PackageMap packageMap;


    /*
    Constructor
     */
    public PackageManager(List<Integer> neighborIds) {
        packageMap = new PackageMap(neighborIds);
    }


    /*
    Methods
     */
    public List<Integer> getNeighborIds() {
        return packageMap.getNeighborIds();
    }

    public List<Pair<Integer, Integer>> getMinDistancesToPackage() {
        List<Pair<Integer, Integer>> distances = new ArrayList<>();
        for (Integer neighborId: getNeighborIds()) {
            try {
                int distance = getMinDistanceToPackage(neighborId);
                distances.add(new Pair<Integer, Integer>(
                        neighborId, distance
                ));
            } catch (IllegalArgumentException e) {}
        }

        return distances;
    }

    public void registerPackage(int neighborId, int packageId, int distance) {
        packageMap.addPackageEntry(neighborId, packageId, distance);
    }

    private int getMinDistanceToPackage(int neighborId)
            throws IllegalArgumentException {
        if (packageMap.getPackageEntriesForNeighborId(neighborId).size() == 0) {
            throw new IllegalArgumentException(
                    "PACKAGE MANAGER | NO PACKAGE FOUND IN THE DIRECTION OF THE GIVEN NEIGHBOR ID"
            );
        }

        int minDistance = Integer.MAX_VALUE;
        for (PackageEntry entry:
                packageMap.getPackageEntriesForNeighborId(neighborId)) {
            if (entry.getDistance() < minDistance) {
                minDistance = entry.getDistance();
            }
        }

        return minDistance;
    }
}
