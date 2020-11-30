package Order;

import com.github.rinde.rinsim.core.model.DependencyProvider;
import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.core.model.time.TimeLapse;

/*
A PDP Model decorator that keeps track of how many orders
    are delivered within the model and prints that number
    every time a order is delivered.
 */
public class DeliveryLogger extends OrderManagerDecorator {

    /*
    The number of orders that where delivered within
        this PDP model.
     */
    int deliveredOrders;


    /*
    Constructor
     */
    public DeliveryLogger(OrderManager delegate) {
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
    public void pickUpOrder() {
        super.pickUpOrder();
        System.out.println("ORDER PICKUP");
    }

    @Override
    public void deliverOrder() {
        super.deliverOrder();
        incrementDeliveredOrders();
        System.out.println("DELIVERY");
        System.out.println(String.format(
                "total nb of delivered orders = %s",
                getDeliveredOrders()
        ));
    }
}
