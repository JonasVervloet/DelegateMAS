package DelegateMAS;

import AGVAgent.AGVAgent;
import Ant.IntentionAnt;
import CommunicationManager.AGVCommunication;
import ResourceAgent.ResourceAgent;
import com.google.common.base.Optional;

public class IntentionMAS extends DelegateMAS {

    /*
    The action rate for intention delegate MAS
        modules.
     */
    private static int intentionActionRate = 20;

    /*
    The AGV agent that delegates this intention
        MAS module.
     */
    private AGVAgent agent;

    /*
    The current intention of this module.
        The module will consider to change
        this intention on a regular basis,
        based on the new beliefs of the
        exploration MAS module.
     */
    private Intention currentIntention;

    private int currentIntentionAntId;

    private boolean currentAntValidated;

    private Optional<Route> newIntention;

    private Optional<Integer> newIntentionAntId;


    /*
    Constructor
     */
    public IntentionMAS(AGVAgent agent, IntentionEntry entry)
            throws NullPointerException {
        super(intentionActionRate);

        if (agent == null) {
            throw new NullPointerException(
                    "INTENTION MAS | GIVEN AGV AGENT IS NULL"
            );
        }
        this.agent = agent;

        this.currentIntention = new Intention(0, 0, entry);
        this.currentAntValidated = true;
        this.newIntention = Optional.absent();
        this.newIntentionAntId = Optional.absent();
    }


    /*
    Delegate MAS
     */
    @Override
    protected void takeAction() {
        if (! currentAntValidated) {
            throw new IllegalStateException(
                    "INTENTION MAS | THE CURRENT ANT IS NOT YET VALIDATED"
            );
        }
        considerChangingIntention();

        IntentionAnt currentAnt = new IntentionAnt(getAgvId(), currentIntention);
        currentIntentionAntId = currentAnt.getId();
        sendIntentionAnt(currentAnt);
        currentAntValidated = false;

        if (newIntention.isPresent()) {
            IntentionAnt newAnt = new IntentionAnt(getAgvId(), convertNewIntention());
            newIntentionAntId = Optional.of(newAnt.getId());
            sendIntentionAnt(newAnt);
        }
    }

    /*
    AGV Agent
     */
    private int getAgvId() {
        return agent.getAgvId();
    }

    /*
    Communication Manager
     */
    private AGVCommunication getCommunicationManager() {
        return agent.getCommunicationManager();
    }

    private void sendIntentionAnt(IntentionAnt ant) {
        getCommunicationManager().sendToResourceAgent(ant);
    }

    public void processIntentionAnt(IntentionAnt ant) {
        if (ant.getId() == currentIntentionAntId) {
            if (ant.hasSucceeded()) {
                currentAntValidated = true;
            } else {
                throw new IllegalStateException(
                        "INTENTION ANT | THE CURRENT INTENTION DID NOT SUCCEED"
                );
            }
        } else if (ant.getId() == newIntentionAntId.get()) {
            if (ant.hasSucceeded()) {
                currentIntention = convertNewIntention();
                currentIntentionAntId = newIntentionAntId.get();
                currentAntValidated = true;
            } else {
                invalidateRoute(getNewIntentionRoute());
            }
            newIntention = Optional.absent();
            newIntentionAntId = Optional.absent();
        } else {
            throw new IllegalArgumentException(
                    "INTENTION ANT | LOST ANT"
            );
        }
    }

    /*
    Exploration MAS
     */
    private boolean shouldChangeIntention(Route route) {
        if (route.getNbOfCheckPoints() >
                currentIntention.getNbOfCheckpoints()) {
            return true;
        } else if (route.getNbOfCheckPoints() ==
                currentIntention.getNbOfCheckpoints()) {
            if (route.getArrivalTime() <
                    currentIntention.getArrivalTime()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private ExplorationMAS getExplorationMAS() {
        return agent.getExplorationMAS();
    }

    public void considerChangingIntention() {
        Route newBest = getExplorationMAS().getBestRoute();

        if (newBest != null && shouldChangeIntention(newBest)) {
            newIntention = Optional.of(newBest);
        }
    }

    private void invalidateRoute(Route route) {
        getExplorationMAS().invalidateRoute(route);
    }

    /*
    Intention
     */
    public boolean readyToChangeToNextEntry(int currentTime) {
        return currentTime == currentIntention.getEntry(1).getStartTime();
    }

    public int getNbOfEntries() {
        return currentIntention.getNbOfEntries();
    }

    public int getIdOfFirstEntry() {
        return currentIntention.getIdOfEntry(0);
    }

    public int getIdOfSecondEntry() {
        return currentIntention.getIdOfEntry(1);
    }

    public void changeToNextDestination() {
        currentIntention.changToNextDestination();
    }

    public Route getNewIntentionRoute() {
        return newIntention.get();
    }

    public Intention convertNewIntention() {
        return getNewIntentionRoute().convertToIntention();
    }
}
