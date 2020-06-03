package CommunicationManager;

import Ant.AntInterface;
import com.github.rinde.rinsim.core.model.comm.CommDevice;
import com.github.rinde.rinsim.core.model.comm.CommUser;
import com.github.rinde.rinsim.core.model.comm.Message;
import com.github.rinde.rinsim.core.model.comm.MessageContents;

public abstract class AbstractCommunicationManager implements CommunicationManagerInterface{

    /*
    The communication device of the communication manager.
        The communication device is used to send and receive
        messages.
     */
    private CommDevice device;


    /*
    Constructor
     */
    public AbstractCommunicationManager(CommDevice device) {
        this.device = device;
    }


    /*
    Check messages
     */
    @Override
    public void checkMessages() throws IllegalStateException {
        for (Message message: device.getUnreadMessages()) {
            if (message instanceof AntInterface) {
                this.handleAntMessage((AntInterface) message);
            } else {
                throw new IllegalStateException(
                        "ABSTRACT COMMUNICATION MANAGER | UNKNOWN MESSAGE, DOES NOT IMPLEMENT ANT INTERFACE"
                );
            }
        }
    }

    protected abstract void handleAntMessage(AntInterface message);

    protected void sendMessage(CommUser receiver, MessageContents contents) {
        device.send(contents, receiver);
    }
}
