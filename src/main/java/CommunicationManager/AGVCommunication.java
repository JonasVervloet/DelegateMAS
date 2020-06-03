package CommunicationManager;

import AGVAgent.AGVAgent;
import Ant.AntInterface;
import Ant.ExplorationAnt;
import Ant.IntentionAnt;
import DelegateMAS.ExplorationMAS;
import DelegateMAS.IntentionMAS;
import DelegateMAS.Route;
import ResourceAgent.ResourceAgent;
import com.github.rinde.rinsim.core.model.comm.CommDevice;
import com.github.rinde.rinsim.core.model.comm.MessageContents;

public class AGVCommunication extends AbstractCommunicationManager {

    /*
    The AGV agent that delegates this communication manager.
     */
    private AGVAgent agent;


    /*
    Constructor
     */
    public AGVCommunication(AGVAgent agent, CommDevice device)
            throws NullPointerException {
        super(device);

        if (agent == null) {
            throw new NullPointerException(
                    "AGV COMMUNICATION | THE GIVEN AGV AGENT IS NULL"
            );
        }
        this.agent = agent;
    }


    /*
    AGV Agent
     */
    public boolean matchesAgvId(int anId) {
        return anId == getAgvId();
    }

    public int getAgvId() {
        return agent.getAgvId();
    }

    public ResourceAgent getCurrentResource() {
        return agent.getCurrentResource();
    }

    /*
    Exploration MAS
     */
    private ExplorationMAS getExplorationMAS() {
        return agent.getExplorationMAS();
    }

    public void processExploredRoute(Route route) {
        getExplorationMAS().processExploredRoute(route);
    }

    /*
    Intention MAS
     */
    private IntentionMAS getIntentionMAS() {
        return agent.getIntentionMAS();
    }

    public void processIntentionAnt(IntentionAnt ant) {
        getIntentionMAS().processIntentionAnt(ant);
    }

    /*
    Message
     */
    @Override
    protected void handleAntMessage(AntInterface message) {
        message.visitAGVAgent(this);
    }

    public void sendToResourceAgent(MessageContents contents) {
        sendMessage(getCurrentResource(), contents);
    }
}
