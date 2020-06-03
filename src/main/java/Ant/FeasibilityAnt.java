package Ant;

import CommunicationManager.AGVCommunication;
import CommunicationManager.IntersectionCommunication;
import CommunicationManager.PDCommunication;
import CommunicationManager.RoadCommunication;

public abstract class FeasibilityAnt extends AbstractAnt {

    /*
    The max hop counts a feasibility agent will make
        before it stops traveling through the infrastructure.
     */
    private static int maxHopCount = 20;

    /*
    The current number of resource agents that this
        agent has visited.
     */
    private int hopCount;

    /*
    The current distance that this feasibility agent has
        traveled.
     */
    private int distanceTraveled;


    /*
    Constructor
     */
    public FeasibilityAnt() {
        hopCount = 0;
        distanceTraveled = 0;
    }

    public FeasibilityAnt(FeasibilityAnt otherAnt) {
        super(otherAnt);
        hopCount = otherAnt.getHopCount();
        distanceTraveled = otherAnt.getDistanceTraveled();
    }


    /*
    Abstract Ant
     */
    @Override
    public void visitResource(int resourceId)
            throws IllegalArgumentException {
        if (alreadyVisited(resourceId)) {
            throw new IllegalArgumentException(
                    "ABSTRACT ANT | THE GIVEN RESOURCE AGENT HAS ALREADY BEEN VISITED"
            );
        }
        super.visitResource(resourceId);
    }

    /*
    Hop Count
     */
    private boolean maxHopCountReached() {
        return !(hopCount < maxHopCount);
    }

    private int getHopCount() {
        return hopCount;
    }

    private void incrementHopCount() {
        hopCount += 1;
    }

    /*
    Traveled distance
     */
    protected int getDistanceTraveled() {
        return distanceTraveled;
    }

    private void increaseDistance(int length) {
        distanceTraveled += length;
    }

    /*
    Road Agent
     */
    @Override
    public void visitRoadAgent(RoadCommunication roadComm) {
        try {
            visitResource(roadComm.getResourceId());
            incrementHopCount();
            increaseDistance(roadComm.getLength());

            if (! maxHopCountReached()) {
                roadComm.sendToOtherNeighbor(lastVisitedResourceId(), this);
            }
        } catch (IllegalArgumentException e) {}
    }

    /*
    Intersection Agent
     */
    @Override
    public void visitIntersectionAgent(IntersectionCommunication intersectionComm) {
        try {
            visitResource(intersectionComm.getResourceId());
            notifyIntersectionAgent(intersectionComm, lastVisitedResourceId());
            incrementHopCount();
            increaseDistance(intersectionComm.getLength());

            if (! maxHopCountReached()) {
                intersectionComm.sendMessageToOtherNeighbors(lastVisitedResourceId(), this);
            }
        } catch (IllegalArgumentException e) {}
    }

    protected abstract void notifyIntersectionAgent(IntersectionCommunication intersectionComm, int lastResourceId);

    /*
    PD Agent
     */
    @Override
    public void visitPDAgent(PDCommunication pdComm) {}

    /*
    AGV Agent
     */
    @Override
    public void visitAGVAgent(AGVCommunication agvComm)
            throws IllegalStateException {
        throw new IllegalStateException(
                "FEASIBILITY AGENT | A FEASIBILITY AGENT SHOULD NOT VISIT AGV AGENTS"
        );
    }
}
