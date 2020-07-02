package Ant;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAnt implements AntInterface {

    /*
    Id Counter, ensuring that every ant receives a unique id.
     */
    private static int idCounter = 0;

    /*
    The ID of this ant. The ID can be used by delegate MAS
        modules to keep track of their messages.
     */
    private int antId;

    /*
    A list of Resource Agent Ids of resource agents that this
        agent already visited. This list will prevent agents to
        make loops.
     */
    private List<Integer> visitedResourceIds;


    /*
    Constructor
     */
    public AbstractAnt() {
        antId = next();
        visitedResourceIds = new ArrayList<>();
    }

    public AbstractAnt(AbstractAnt otherAnt) {
        antId = next();
        visitedResourceIds = new ArrayList<>(
                otherAnt.getVisitedResourceIds()
        );
    }


    /*
    Ant ID
     */
    private static int next() {
        idCounter += 1;
        return idCounter;
    }

    public int getId() {
        return antId;
    }

    /*
    Resource Visits
     */
    protected int getNbOfVisitedResource() {
        return visitedResourceIds.size();
    }

    public boolean alreadyVisited(int resourceId) {
        return visitedResourceIds.contains(resourceId);
    }

    public int lastVisitedResourceId() {
        return visitedResourceIds.get(visitedResourceIds.size() - 2);
    }

    public int firstVisitedResourceId() {
        return visitedResourceIds.get(0);
    }

    public int getPreviousResourceId(int resourceId)
            throws IllegalArgumentException {
        for (int i=0; i < visitedResourceIds.size(); i++) {
            if (visitedResourceIds.get(i) == resourceId) {
                if (i == 0) {
                    throw new IllegalArgumentException(
                            "ABSTRACT ANT | THE GIVEN RESOURCE ID BELONGS TO THE FIRST VISITED RESOURCE AGENT"
                    );
                }
                return visitedResourceIds.get(i - 1);
            }
        }
        throw new IllegalArgumentException(
                "ABSTRACT ANT | THIS ANT DID NOT VISIT A RESOURCE AGENT WITH GIVEN ID"
        );
    }

    private List<Integer> getVisitedResourceIds() {
        return visitedResourceIds;
    }

    public void visitResource(int resourceId) {
        visitedResourceIds.add(resourceId);
    }
}
