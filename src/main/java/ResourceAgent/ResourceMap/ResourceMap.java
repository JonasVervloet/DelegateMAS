package ResourceAgent.ResourceMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceMap {

    /*
    The resource map keeps a list of resource entries for every
        neighbor of the resource agent that manages this resource
        map.
     */
    private Map<Integer, List<ResourceEntry>> resourceMap;


    /*
    Constructor
     */
    public ResourceMap(List<Integer> neighborIds) {
        assert(neighborIds.size() > 0);
        resourceMap = new HashMap<>();
        for (Integer neighborId: neighborIds) {
            resourceMap.put(
                    neighborId, new ArrayList<ResourceEntry>()
            );
        }
    }


    /*
    Neighbor Ids
     */
    private boolean isValidNeighborId(int neighborId) {
        return resourceMap.containsKey(neighborId);
    }

    public List<Integer> getNeighborIds() {
        return new ArrayList<>(resourceMap.keySet());
    }


    /*
    Resource Entries
     */
    public void addResourceEntry(int neighborId, int resourceId, int distance)
            throws IllegalArgumentException {
        if (! isValidNeighborId(neighborId)) {
            throw new IllegalArgumentException(
                    "RESOURCE MAP | GIVEN NEIGHBOR ID IS NOT A VALID ONE"
            );
        }

        boolean found = false;
        for (ResourceEntry entry: resourceMap.get(neighborId)) {
            if (entry.isEqualEntry(resourceId, distance)) {
                entry.refresh();
                found = true;
            }
        }

        if (! found) {
            resourceMap.get(neighborId).add(
                    new ResourceEntry(resourceId, distance)
            );
        }
    }

    public List<ResourceEntry> getResourceEntryForNeighborId(int neighborId)
            throws IllegalArgumentException {
        if (! isValidNeighborId(neighborId)) {
            throw new IllegalArgumentException(
                    "RESOURCE MAP | GIVEN NEIGHBOR ID IS NOT A VALID ONE"
            );
        }
        return resourceMap.get(neighborId);
    }
}
