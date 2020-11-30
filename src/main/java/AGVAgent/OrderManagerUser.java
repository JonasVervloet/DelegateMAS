package AGVAgent;

import Order.OrderManager;
import com.github.rinde.rinsim.core.model.pdp.Vehicle;
import com.github.rinde.rinsim.core.model.pdp.VehicleDTO;

public abstract class OrderManagerUser extends Vehicle {

    /*
    The order manager that this class provides to
        its extending children.
     */
    private static OrderManager manager;


    /*
    Constructor
     */
    protected OrderManagerUser(VehicleDTO vehicleDto) {
        super(vehicleDto);
    }


    /*
    Manager
     */
    protected static OrderManager getOrderManager() {
        return manager;
    }

    public static void setOrderManager(OrderManager orderManager) {
        manager = orderManager;
    }
}
