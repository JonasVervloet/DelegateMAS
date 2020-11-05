package Order;

import DelegateMAS.SimpleOrderBroadcastMessage;
import com.github.rinde.rinsim.core.model.comm.CommDevice;
import com.github.rinde.rinsim.core.model.comm.CommDeviceBuilder;
import com.github.rinde.rinsim.core.model.comm.CommUser;
import com.github.rinde.rinsim.core.model.pdp.PDPModel;
import com.github.rinde.rinsim.core.model.pdp.Parcel;
import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import com.google.common.base.Optional;

import javax.swing.text.html.Option;

/*
A simple order. This order has a destination that it
    should reach. An AGV agent will have to pick up
    the package and deliver it at the destination.
 */
public class SimpleOrder extends Parcel implements CommUser, TickListener {

    /*
    The communication device of this order.
        The order will use this device to broadcast
        message. AGV agents that receive those messages
        will then know where to find the order.
     */
    private Optional<CommDevice> communicationDevice;


    /*
    Constructor
     */
    public SimpleOrder(Point startPosition, Point destination) {
        super(
                Parcel.builder(
                        startPosition, destination
                ).serviceDuration(20)
                .buildDTO()
        );
        setEmptyCommunicationDevice();
    }

    /*
    Parcel
     */
    private boolean isInAvailableState() {
        return getOrderState() == PDPModel.ParcelState.AVAILABLE;
    }

    private PDPModel.ParcelState getOrderState() {
        return getPDPModel().getParcelState(this);
    }


    /*
    Communication User
     */
    private boolean hasCommunicationDevice() {
        return communicationDevice.isPresent();
    }

    @Override
    public Optional<Point> getPosition() {
        if (isInAvailableState()) {
            return Optional.of(
                    getRoadModel().getPosition(this)
            );
        } else {
            return Optional.absent();
        }
    }

    private CommDevice getCommunicationDevice() {
        return communicationDevice.get();
    }

    private void setEmptyCommunicationDevice() {
        this.communicationDevice = Optional.absent();
    }

    @Override
    public void setCommDevice(CommDeviceBuilder commDeviceBuilder) {
        this.communicationDevice = Optional.of(
                commDeviceBuilder
                        .setMaxRange(10)
                        .build()
        );
    }

    private void sendBroadcastMessage() {
        if (hasCommunicationDevice()) {
            getCommunicationDevice().broadcast(
                    new SimpleOrderBroadcastMessage(
                            getPickupLocation()
                    )
            );
        }
    }

    /*
    Tick Listener
     */
    @Override
    public void tick(TimeLapse timeLapse) {
        if (isInAvailableState()) {
            sendBroadcastMessage();
        }
    }

    @Override
    public void afterTick(TimeLapse timeLapse) {

    }
}
