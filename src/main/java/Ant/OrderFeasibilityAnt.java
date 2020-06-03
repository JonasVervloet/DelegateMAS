package Ant;

import CommunicationManager.IntersectionCommunication;

public class OrderFeasibilityAnt extends FeasibilityAnt {

    private int packageId;


    /*
    Constructor
     */
    public OrderFeasibilityAnt(int packageId) {
        this.packageId = packageId;
    }

    public OrderFeasibilityAnt(OrderFeasibilityAnt otherAnt) {
        super(otherAnt);
        packageId = otherAnt.getPackageId();
    }


    /*
    Package ID
     */
    private int getPackageId() {
        return packageId;
    }

    /*
    Feasibility Methods
     */
    @Override
    protected void notifyIntersectionAgent(IntersectionCommunication intersectionComm, int lastResourceId) {
        intersectionComm.registerOrder(lastResourceId, packageId, getDistanceTraveled());
    }

    /*
    Clone
     */
    @Override
    public OrderFeasibilityAnt cloneAnt() {
        return new OrderFeasibilityAnt(this);
    }
}
