package Ant;

import CommunicationManager.IntersectionCommunication;

public class ResourceFeasibilityAnt extends FeasibilityAnt {

    private int resourceId;


    /*
    Constructor
     */
    public ResourceFeasibilityAnt(int resourceId) {
        this.resourceId = resourceId;
    }

    public ResourceFeasibilityAnt(ResourceFeasibilityAnt otherAnt) {
        super(otherAnt);
        resourceId = otherAnt.getResourceId();
    }


    /*
    Resource ID
     */
    private int getResourceId() {
        return resourceId;
    }

    /*
    Feasibility Methods
     */
    @Override
    protected void notifyIntersectionAgent(IntersectionCommunication intersectionComm, int lastResourceId) {
        intersectionComm.registerResource(lastResourceId, resourceId, getDistanceTraveled());
    }

    /*
    Clone
     */
    @Override
    public ResourceFeasibilityAnt cloneAnt() {
        return new ResourceFeasibilityAnt(this);
    }
}
