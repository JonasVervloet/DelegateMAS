package Order;

import ResourceAgent.PDAgent;
import com.github.rinde.rinsim.core.Simulator;
import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import org.apache.commons.math3.random.RandomGenerator;

import java.util.ArrayList;
import java.util.List;


public class RequestManager implements TickListener {

    private Simulator sim;

    private RandomGenerator ran;

    private List<PDAgent> freeStorageAgents;

    private List<Integer> deliveryIds;

    private int nbDeliveredOrders;

    private int count;

    private int rate;

    private static RequestManager manager;

    private RequestManager(List<PDAgent> storage, List<Integer> deliveryIds, int rate, Simulator sim) {
        this.sim = sim;
        ran = sim.getRandomGenerator();
        this.freeStorageAgents = storage;
        this.deliveryIds = deliveryIds;
        this.rate = rate;
        nbDeliveredOrders = 0;
        count = 0;
    }

    public static RequestManager getRequestManager() {
        if (manager == null) {
            throw new IllegalArgumentException(
                    "REQUEST MANAGER | THE MANAGER IS NOT YET CREATED"
            );
        }
        return manager;
    }

    public static void createManager(List<PDAgent> storage, List<Integer> deliveryIds,
                                     int rate, Simulator sim) {
        manager = new RequestManager(storage, deliveryIds, rate, sim);
    }

    public void notifyDeliveredOrder(Order order) {
        order.removeOrder();
        System.out.println("Delivered Order!!");
        nbDeliveredOrders += 1;
        System.out.println("TOTAL: " + nbDeliveredOrders);
    }

    public void createOrder() {
        PDAgent start = popNextRandomFreeStorageAgent();

        System.out.println("New order at: " + start.getConnection());

        List<Integer> stops = new ArrayList<>();
        int last = -1;
        for (int i=0; i< 4; i++) {
            int curr = getNextRandomInteger(4);
            while(curr == last) {
                curr = getNextRandomInteger(4);
            }
            last = curr;
            stops.add(curr + 1);
        }

        stops.add(getNextRandomDeliveryId());

        Order order = new Order(stops);
        start.registerOrder(order);
        sim.register(order);
    }

    @Override
    public void tick(TimeLapse timeLapse) {
        if (count == 0) {
            if (freeStorageAgents.size() > 0) {
                createOrder();
            } else {
                System.out.println("All storage spaces occupied, no new order!!");
            }
        }
        count += 1;
        if (count == rate) {
            count = 0;
        }
    }

    @Override
    public void afterTick(TimeLapse timeLapse) {

    }

    /*
    Random Generator
     */
    private RandomGenerator getRandomGenerator() {
        return ran;
    }

    private int getNextRandomInteger(int integer) {
        return  getRandomGenerator().nextInt(integer);
    }

    /*
    Free Storage Agents
     */
    private int getNbOfFreeStorageAgents() {
        return getFreeStorageAgents().size();
    }

    private List<PDAgent> getFreeStorageAgents() {
        return freeStorageAgents;
    }

    private PDAgent popNextRandomFreeStorageAgent() {
        PDAgent next = getFreeStorageAgents().get(
                getNextRandomInteger(getNbOfFreeStorageAgents())
        );
        removeFreeStorageAgent(next);
        return next;
    }

    private void removeFreeStorageAgent(PDAgent agent) {
        freeStorageAgents.remove(agent);
    }

    public void addFreeStorageAgent(PDAgent storageAgent) {
        freeStorageAgents.add(storageAgent);
    }

    /*
    Delivery Ids
     */
    private int getNbDeliveryIds() {
        return getDeliveryIds().size();
    }

    private List<Integer> getDeliveryIds() {
        return deliveryIds;
    }

    private int getNextRandomDeliveryId() {
        return getDeliveryIds().get(
                getNextRandomInteger(getNbDeliveryIds())
        );
    }
}
