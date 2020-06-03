package Ant;

import CommunicationManager.AGVCommunication;
import CommunicationManager.IntersectionCommunication;
import CommunicationManager.PDCommunication;
import CommunicationManager.RoadCommunication;
import DelegateMAS.Intention;
import DelegateMAS.IntentionEntry;


//public class IntentionAnt implements MessageContents {
//
//    private String creator;
//    private Path path;
//    private int index;
//
//    private boolean pickup;
//
//    private boolean succeeded;
//    private boolean rejected;
//
//    private boolean forward;
//
//
//    public IntentionAnt(String creator, Path path, boolean pickup) {
//        this.creator = creator;
//        this.path = path;
//        index = 0;
//        this.pickup = pickup;
//        succeeded = false;
//        forward = true;
//        rejected = false;
//    }
//
//    public Path getPath() {
//        return path;
//    }
//
//    public boolean isPickup() {
//        return pickup;
//    }
//
//    public boolean isRejected() {
//        return rejected;
//    }
//
//    private void inspectPDAgent(PDAgent agent) {
//        if (forward) {
//            if (index == 0) {
//                agent.sendMessageToNeighbour(this);
//            } else if (index == path.getLength()) {
//                System.out.println("PD agent reached");
//                if (pickup) {
//                    succeeded = agent.acceptPickup(path.getArrivalTime(), creator);
//                } else {
//                    succeeded = agent.acceptUsage(path.getArrivalTime(), creator);
//                }
//                if (! succeeded) {
//                    System.out.println("Intention ant rejected");
//                    rejected = true;
//                } else {
//                    System.out.println("Intention ant accepted");
//                }
//                forward = false;
//                index -= 1;
//                agent.sendMessageToNeighbour(this);
//            } else {
//                System.out.println("INTENTION ANT | INSPECT PD AGENT IN MIDDLE OF PATH");
//                System.out.println(index);
//                System.out.println(path.getLength());
//            }
//        } else {
//            agent.sendMessageToAGV(this, creator);
//        }
//    }
//
//    private void inspectRoadAgent(RoadAgent agent) {
//        PathEntry entry = path.getEntry(index);
//        if (forward) {
//            index += 1;
//            agent.sendMessage(this, entry.getExitPoint());
//        } else {
//            if (succeeded) {
//                succeeded = agent.makeReservation(entry.getStartTime() , entry.getEndTime(),
//                        creator, entry.getEntryPoint(), entry.getExitPoint());
//            }
//            index -= 1;
//            agent.sendMessage(this, entry.getEntryPoint());
//        }
//    }
//
//    private void inspectIntersectionAgent(IntersectionAgent agent) {
//        PathEntry entry = path.getEntry(index);
//        if (forward) {
//            index += 1;
//            agent.sendMessage(this, entry.getExitPoint());
//        } else {
//            if (succeeded) {
//                succeeded = agent.makeReservation(entry.getStartTime(), entry.getEndTime(),
//                        creator, entry.getEntryPoint(), entry.getExitPoint());
//            }
//            index -= 1;
//            agent.sendMessage(this, entry.getEntryPoint());
//        }
//    }
//
//    public void inspectAgent(ResourceAgent agent) {
//        if (MASProject.DEBUG) {
//            System.out.println(creator);
//            System.out.println(index);
//            System.out.println(agent.getClass());
//        }
//        if (agent.getClass() == PDAgent.class) {
//            inspectPDAgent((PDAgent) agent);
//        } else if (agent.getClass() == RoadAgent.class) {
//            inspectRoadAgent((RoadAgent) agent);
//        } else if (agent.getClass() == IntersectionAgent.class) {
//            inspectIntersectionAgent((IntersectionAgent) agent);
//        } else {
//            System.out.println("RANDOM EXPLORATION AND | AGENT DIFFERENT THAN PD, ROAD OR INTERSECTION");
//        }
//    }
//
//    public boolean hasSucceeded() {
//        return succeeded;
//    }
//}

public class IntentionAnt extends AbstractAnt {

    private final int agvId;

    private Intention intention;

    private int index;

    private boolean goingForward;

    private boolean success;


    /*
    Constructor
     */
    public IntentionAnt(int agvId, Intention intention)
            throws IllegalArgumentException, NullPointerException {
        if (! isValidAgvId(agvId)) {
            throw new IllegalArgumentException(
                    "INTENTION ANT | THE GIVEN AGV ID IS NOT A VALID ONE"
            );
        }
        this.agvId = agvId;

        if (intention == null) {
            throw new NullPointerException(
                    "INTENTION ANT | THE GIVEN INTENTION IS EQUAL TO NULL"
            );
        }
        this.intention = intention;
        this.index = 0;

        this.goingForward = true;
        this.success = false;
    }


    /*
    AGV ID
     */
    private boolean isValidAgvId(int agvId) {
        return agvId > 0;
    }

    private int getAgvId() {
        return agvId;
    }

    /*
    Intention
     */
    private int getNbOfEntries() {
        return getIntention().getNbOfEntries();
    }

    private Intention getIntention() {
        return intention;
    }

    private int getIdOfCurrentEntry() {
        return getIntention().getIdOfEntry(getIndex());
    }

    private IntentionEntry getCurrentEntry() {
        return getIntention().getEntry(getIndex());
    }

    /*
    Index
     */
    private boolean finalDestinationReached() {
        return getIndex() == getNbOfEntries();
    }

    private int getIndex() {
        return index;
    }

    private void incrementIndex() {
        index += 1;
    }

    /*
    Going Forward
     */
    private boolean isGoingForward() {
        return goingForward;
    }

    private void changeToGoingBackwards()
            throws IllegalStateException {
        if (! isGoingForward()) {
            throw new IllegalStateException(
                    "INTENTION ANT | THE ANT IS ALREADY GOING BACKWARDS"
            );
        }
        goingForward = false;
    }

    /*
    Success
     */
    public boolean hasSucceeded() {
        return success;
    }

    private void succeed() {
        if (hasSucceeded()) {
            throw new IllegalStateException(
                    "INTENTION ANT | THIS ANT DID ALREADY SUCCEED"
            );
        }
        success = true;
    }

    /*
    Road Agent
     */
    @Override
    public void visitRoadAgent(RoadCommunication roadCommunication) {
        assert(roadCommunication.getResourceId() == getIdOfCurrentEntry());

        if (isGoingForward()) {
            visitResource(roadCommunication.getResourceId());
            IntentionEntry entry = getCurrentEntry();
            try {
                roadCommunication.makeReservation(
                        entry.getStartTime(), entry.getEndTime(), getAgvId(), lastVisitedResourceId()
                );
                incrementIndex();
                if (! finalDestinationReached()) {
                    roadCommunication.sendToOtherNeighbor(lastVisitedResourceId(), this);
                } else {
                    succeed();
                    changeToGoingBackwards();
                    roadCommunication.sendToNeighbor(
                            getPreviousResourceId(roadCommunication.getResourceId()), this
                    );
                }
            } catch (IllegalArgumentException e) {
                changeToGoingBackwards();
                roadCommunication.sendToNeighbor(
                        getPreviousResourceId(roadCommunication.getResourceId()), this
                );
            }
        } else {
            roadCommunication.sendToNeighbor(
                    getPreviousResourceId(roadCommunication.getResourceId()), this
            );
        }
    }

    /*
    Intention Agent
     */
    @Override
    public void visitIntersectionAgent(IntersectionCommunication intersectionCommunication) {
        assert(intersectionCommunication.getResourceId() == getIdOfCurrentEntry());

        if (isGoingForward()) {
            visitResource(intersectionCommunication.getResourceId());
            IntentionEntry entry = getCurrentEntry();
            try {
                intersectionCommunication.makeReservation(
                        entry.getStartTime(), entry.getEndTime(), getAgvId(), lastVisitedResourceId()
                );
                incrementIndex();
                if (! finalDestinationReached()) {
                    intersectionCommunication.sendMessageToNeighbor(
                            getIdOfCurrentEntry(), this
                    );
                } else {
                    succeed();
                    changeToGoingBackwards();
                    intersectionCommunication.sendMessageToNeighbor(
                            getPreviousResourceId(intersectionCommunication.getResourceId()), this
                    );
                }
            } catch (IllegalArgumentException e) {
                changeToGoingBackwards();
                intersectionCommunication.sendMessageToNeighbor(
                        getPreviousResourceId(intersectionCommunication.getResourceId()), this
                );
            }
        } else {
            intersectionCommunication.sendMessageToNeighbor(
                    getPreviousResourceId(intersectionCommunication.getResourceId()), this
            );
        }
    }

    /*
    PD Agent
     */
    @Override
    public void visitPDAgent(PDCommunication pdCommunication) {
        assert(pdCommunication.getResourceId() == getIdOfCurrentEntry());

        if (isGoingForward()) {
            visitResource(pdCommunication.getResourceId());
            IntentionEntry entry = getCurrentEntry();
            try {
                pdCommunication.makeReservation(
                        entry.getStartTime(), entry.getEndTime(), getAgvId()
                );
                incrementIndex();
                if (! finalDestinationReached()) {
                    pdCommunication.sendMessageToNeighbor(this);
                } else {
                    succeed();
                    changeToGoingBackwards();
                    pdCommunication.sendMessageToNeighbor(this);
                }
            } catch (IllegalArgumentException e) {
                changeToGoingBackwards();
                pdCommunication.sendMessageToNeighbor(this);
            }
        } else {
            pdCommunication.sendMessageToAGVAgent(getAgvId(), this);
        }
    }

    /*
    AGV Agent
     */
    @Override
    public void visitAGVAgent(AGVCommunication agvCommunication) {
        assert(agvCommunication.matchesAgvId(getAgvId()));
    }

    @Override
    public AntInterface cloneAnt()
            throws IllegalStateException {
        throw new IllegalStateException(
                "INTENTION ANT | AN INTENTION ANT SHOULD NEVER BE CLONED"
        );
    }
}
