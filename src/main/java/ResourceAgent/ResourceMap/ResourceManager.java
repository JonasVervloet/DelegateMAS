package ResourceAgent.ResourceMap;

import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ResourceManager {

    /*
    The resource map that this resource manager manages.
     */
    private ResourceMap resourceMap;


    /*
    Constructor
     */
    public ResourceManager(List<Integer> neighborIds) {
        resourceMap = new ResourceMap(neighborIds);
    }


    /*
    Methods
     */
    public List<Integer> getNeighborIds() {
        return resourceMap.getNeighborIds();
    }

    public List<Pair<Integer, Integer>> getMinDistances(int resourceId) {
        List<Pair<Integer, Integer>> distances = new ArrayList<>();
        for (Integer neighborId: getNeighborIds()) {
            try {
                int distance  = getMinDistanceToResource(resourceId, neighborId);
                distances.add(new Pair<Integer, Integer>(neighborId, distance) {
                });
            } catch (IllegalArgumentException e) {}
        }

        return distances;
    }

    public void registerResource(int neighborId, int resourceId, int distance) {
        resourceMap.addResourceEntry(neighborId, resourceId, distance);
    }

    private int getMinDistanceToResource(int resourceId, int neighborId)
            throws IllegalArgumentException {
        int minDistance = Integer.MAX_VALUE;
        boolean distanceFounded = false;
        for (ResourceEntry entry:
                resourceMap.getResourceEntryForNeighborId(neighborId)) {
            if (entry.matchesResourceId(resourceId)) {
                distanceFounded = true;
                if (entry.getDistance() < minDistance) {
                    minDistance = entry.getDistance();
                }
            }
        }

        if (distanceFounded) {
            return minDistance;
        } else {
            throw new IllegalArgumentException(
                    "RESOURCE MANAGER | NO DISTANCE FOUND FOR GIVEN RESOURCE ID AND EDGE POINT"
            );
        }
    }
}
