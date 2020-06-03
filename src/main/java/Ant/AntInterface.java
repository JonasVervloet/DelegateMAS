package Ant;

import CommunicationManager.AGVCommunication;
import CommunicationManager.IntersectionCommunication;
import CommunicationManager.PDCommunication;
import CommunicationManager.RoadCommunication;
import com.github.rinde.rinsim.core.model.comm.MessageContents;

public interface AntInterface extends MessageContents {

    /*
    The functionality to visit road agents.
     */
    void visitRoadAgent(RoadCommunication roadCommunication);

    /*
    The functionality to visit intersection agents.
     */
    void visitIntersectionAgent(IntersectionCommunication intersectionCommunication);

    /*
    The functionality to visit PD agents.
     */
    void visitPDAgent(PDCommunication pdCommunication);

    /*
    The functionality to visit AGV agents.
     */
    void visitAGVAgent(AGVCommunication agvCommunication);

    /*
    Clone function
     */
    AntInterface cloneAnt();
}
