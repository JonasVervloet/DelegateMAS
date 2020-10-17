import Order.RequestManager;
import ResourceAgent.ResourceAgent;
import ResourceAgent.PDAgent;
import AGVAgent.AGVAgent;
import AGVAgent.SimpleAGVAgent;

import com.github.rinde.rinsim.core.model.comm.CommModel;
import com.github.rinde.rinsim.core.Simulator;
import com.github.rinde.rinsim.core.model.pdp.DefaultPDPModel;
import com.github.rinde.rinsim.core.model.pdp.PDPModel;
import com.github.rinde.rinsim.core.model.road.CollisionGraphRoadModelImpl;
import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.core.model.road.RoadModelBuilders;
import com.github.rinde.rinsim.geom.Point;
import com.github.rinde.rinsim.ui.View;
import com.github.rinde.rinsim.ui.renderers.AGVRenderer;
import com.github.rinde.rinsim.ui.renderers.RoadUserRenderer;
import com.github.rinde.rinsim.ui.renderers.WarehouseRenderer;
import org.apache.commons.math3.random.RandomGenerator;
import javax.measure.unit.SI;

import Environment.Environment;
import Environment.EnvironmentCreator;

import java.util.List;

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

    private static int nbAGVs = 1;

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
                .setTickLength(10)
                .build();

        final RoadModel roadModel = sim.getModelProvider().getModel(
                RoadModel.class);
//        final PDPModel pdpModel = sim.getModelProvider().getModel(
//                PDPModel.class
//        );
        final RandomGenerator ran = sim.getRandomGenerator();
//
//        List<ResourceAgent> agents = env.getAllResourceAgents();
//        for (ResourceAgent agent: agents) {
//            sim.register(agent);
//        }
//
//        List<PDAgent> storageAgents = env.getStorageAgents();
//        RequestManager.createManager(storageAgents, env.getDeliveryIds(),
//                orderrate, sim);
//        sim.register(RequestManager.getRequestManager());

//        for (int i = 0; i < nbAGVs; i++) {
//            PDAgent start = storageAgents.get(ran.nextInt(storageAgents.size()));
//            System.out.println(start.getConnection());
//            AGVAgent agvAgent = new AGVAgent(start, ran);
//            System.out.println("AGV agent: " + agvAgent.getAgvId());
//            sim.register(agvAgent);
//        }

        for (int i = 0; i < nbAGVs; i++) {
            Point startPosition = roadModel.getRandomPosition(ran);
            SimpleAGVAgent agent = new SimpleAGVAgent(startPosition, ran);
            sim.register(agent);
        }

        sim.start();
    }
}
