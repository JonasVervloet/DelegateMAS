package ResourceAgent.PackageMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageMap {

    /*
    The package map keeps a list of package entries for every
        neighbor of the resource agent that manages this package
        map.
     */
    private Map<Integer, List<PackageEntry>> packageMap;


    /*
    Constructor
     */
    public PackageMap(List<Integer> neighborIds) {
        assert(neighborIds.size() > 0);
        packageMap = new HashMap<>();
        for (Integer neighborId: neighborIds) {
            packageMap.put(
                    neighborId, new ArrayList<PackageEntry>()
            );
        }
    }


    /*
    Neighbor IDs
     */
    private boolean isValidNeighborId(int neighborId) {
        return packageMap.containsKey(neighborId);
    }

    public List<Integer> getNeighborIds() {
        return new ArrayList<>(packageMap.keySet());
    }

    /*
    Package Entries
     */
    public void addPackageEntry(int neighborId, int packageId, int distance)
            throws IllegalArgumentException {
        if (! isValidNeighborId(neighborId)) {
            throw new IllegalArgumentException(
                    "PACKAGE MAP | GIVEN NEIGHBOR ID IS NOT A VALID ONE"
            );
        }

        boolean found = false;
        for (PackageEntry entry: packageMap.get(neighborId)) {
            if (entry.isEquaylEntry(packageId, distance)) {
                entry.refresh();
                found = true;
            }
        }

        if (! found) {
            packageMap.get(neighborId).add(
                    new PackageEntry(packageId, distance)
            );
        }
    }

    public List<PackageEntry> getPackageEntriesForNeighborId(int neighborId)
            throws IllegalArgumentException {
        if (! isValidNeighborId(neighborId)) {
            throw new IllegalArgumentException(
                    "PACKAGE MAP | GIVEN NEIGHBOR ID IS NOT A VALID ONE"
            );
        }

        return packageMap.get(neighborId);
    }
}
