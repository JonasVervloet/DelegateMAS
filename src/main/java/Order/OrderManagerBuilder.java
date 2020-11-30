package Order;

public class OrderManagerBuilder {

    OrderManager orderManager;


    /*
    Constructor
     */
    public OrderManagerBuilder() {
        setOrderManager(
                new SimpleOrderManager()
        );
    }


    /*
    Order Manager
     */
    private OrderManager getOrderManager() {
        return orderManager;
    }

    private void setOrderManager(OrderManager anOrderManager) {
        orderManager = anOrderManager;
    }

    /*
    Builder
     */
    public OrderManager build() {
        return getOrderManager();
    }

    public OrderManagerBuilder addDeliveryLogger() {
        setOrderManager(new DeliveryLogger(
                getOrderManager()
        ));
        return this;
    }
}
