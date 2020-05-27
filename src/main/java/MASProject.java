import ResourceAgent.ResourceAgent;
import com.github.rinde.rinsim.core.model.comm.CommModel;
import com.github.rinde.rinsim.core.Simulator;
import com.github.rinde.rinsim.core.model.pdp.DefaultPDPModel;
import com.github.rinde.rinsim.core.model.pdp.PDPModel;
import com.github.rinde.rinsim.core.model.road.CollisionGraphRoadModelImpl;
import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.core.model.road.RoadModelBuilders;
import com.github.rinde.rinsim.ui.View;
import com.github.rinde.rinsim.ui.renderers.AGVRenderer;
import com.github.rinde.rinsim.ui.renderers.RoadUserRenderer;
import com.github.rinde.rinsim.ui.renderers.WarehouseRenderer;
import org.apache.commons.math3.random.RandomGenerator;
import javax.measure.unit.SI;

import Environment.Environment;
import Environment.EnvironmentCreator;

import java.util.List;
//import ResourceAgent.PDAgent;
//import ResourceAgent.ResourceAgent;

/**
 * Example showcasing the {@link CollisionGraphRoadModelImpl} with a
 * {@link WarehouseRenderer} and {@link AGVRenderer}.
 * @author Rinde van Lon
 */
public class MASProject {

    public static final double VEHICLE_LENGTH = 2d;

    private static final int NUM_PROD = 2;

    private static final long SERVICE_DURATION = 60000;

    private static int modelsize = 2;

    private static int nbAGVs = 3;

    private static int orderrate = 30;

    public static int MACHINE_WORKTIME = 5;

    public static boolean DEBUG = false;


    private MASProject() {}

    /**
     * Runs the project.
     * @param arguments     This is ignored.
     */
    public static void main(String[] arguments) {
        run(false);
    }

    public static void run(boolean testing) {
        View.Builder viewBuilder = View.builder()
                .withTitleAppendix("MAS Project")
                .with(WarehouseRenderer.builder()
                    .withMargin(VEHICLE_LENGTH))
                .with(AGVRenderer.builder())
                .with(RoadUserRenderer.builder());

        Environment env = EnvironmentCreator.createEnvironment(modelsize, VEHICLE_LENGTH);
        assert(env.checkAllNeighborsSet());

        final Simulator sim = Simulator.builder()
                .addModel(
                        RoadModelBuilders.dynamicGraph(env.getGraph())
                        .withCollisionAvoidance()
                        .withDistanceUnit(SI.METER)
                        .withVehicleLength(VEHICLE_LENGTH)
                        .withSpeedUnit(SI.METERS_PER_SECOND)
                )
                .addModel(DefaultPDPModel.builder())
                .addModel(viewBuilder)
                .addModel(CommModel.builder())
                .setTimeUnit(SI.MILLI(SI.SECOND))
                .setTickLength(1000)
                .build();

        final RoadModel roadModel = sim.getModelProvider().getModel(
                RoadModel.class);
        final PDPModel pdpModel = sim.getModelProvider().getModel(
                PDPModel.class
        );
        final RandomGenerator ran = sim.getRandomGenerator();

        List<ResourceAgent> storageAgents = env.getStorageAgents();

//        List<ResourceAgent> agents = env.getAllResourceAgents();
//        for (ResourceAgent agent: agents) {
//            sim.register(agent);
//        }

//        OrderManager mgr = new OrderManager(storageAgents, env.getDeliveryAgents(),
//                orderrate, sim);
//        sim.register(mgr);

//        for (int i = 0; i < nbAGVs; i++) {
//            PDAgent start = storageAgents.get(ran.nextInt(storageAgents.size()));
//            System.out.println(start.getConnection());
//            sim.register(new AGVAgent.AGVAgent("AGV" + i, start));
//        }

        sim.start();
    }
}
