package DelegateMAS;

import com.github.rinde.rinsim.core.model.comm.MessageContents;
import com.github.rinde.rinsim.geom.Point;

public class SimpleOrderBroadcastMessage implements MessageContents {

    /*
    The point of the order that created this broadcast
        message.
     */
    Point pickupPoint;


    /*
    Constructor
     */
    public SimpleOrderBroadcastMessage(Point pickupPoint) {
        setPickupPoint(pickupPoint);
    }


    /*
    Pick up Point
     */
    public Point getPickupPoint() {
        return pickupPoint;
    }

    private void setPickupPoint(Point aPickupPoint) {
        this.pickupPoint = aPickupPoint;
    }
}
