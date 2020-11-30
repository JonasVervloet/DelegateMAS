package Order;

public abstract class OrderManagerDecorator implements OrderManager {

    /*
    The delegate order manager of this order manager
        decorator.
     */
    private OrderManager delegate;


    /*
    Constructor
     */
    public OrderManagerDecorator(OrderManager delegate) {
        setDelegate(delegate);
    }


    /*
    Delegate
     */
    private OrderManager getDelegate() {
        return delegate;
    }

    private void setDelegate(OrderManager aDelegate) {
        delegate = aDelegate;
    }

    /*
    Order Manager
     */
    @Override
    public void pickUpOrder() {
        getDelegate().pickUpOrder();
    }

    @Override
    public void deliverOrder() {
        getDelegate().deliverOrder();
    }
}
