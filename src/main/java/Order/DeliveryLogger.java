package Order;

import com.github.rinde.rinsim.core.model.pdp.ForwardingPDPModel;
import com.github.rinde.rinsim.core.model.pdp.PDPModel;
import com.github.rinde.rinsim.core.model.pdp.Parcel;
import com.github.rinde.rinsim.core.model.pdp.Vehicle;
import com.github.rinde.rinsim.core.model.time.TimeLapse;

/*
A PDP Model decorator that keeps track of how many orders
    are delivered within the model and prints that number
    every time a order is delivered.
 */
public class DeliveryLogger extends ForwardingPDPModel {

    /*
    The number of orders that where deliverd within
        this PDP model.
     */
    int deliveredOrders;


    /*
    Constructor
     */
    public DeliveryLogger(PDPModel delegate) {
        super(delegate);
        deliveredOrders = 0;
    }


    /*
    Delivered orders
     */
    private int getDeliveredOrders() {
        return deliveredOrders;
    }

    private void incrementDeliveredOrders() {
        deliveredOrders += 1;
    }

    /*
    PDP Model
     */
    @Override
    public void deliver(Vehicle vehicle, Parcel parcel, TimeLapse time) {
        super.deliver(vehicle, parcel, time);
        incrementDeliveredOrders();
        System.out.println("DELIVERY");
        System.out.println(String.format(
                "total nb of delivered orders = %s",
                getDeliveredOrders()
        ));
    }
}
