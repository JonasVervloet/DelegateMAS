package AGVAgent;

import com.github.rinde.rinsim.core.model.pdp.Vehicle;
import com.github.rinde.rinsim.core.model.pdp.VehicleDTO;
import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import com.google.common.base.Optional;
import org.apache.commons.math3.random.RandomGenerator;


/*
A simple AGV agent. This agent chooses a destination
    at random and uses the road model to provide the
    optimal path towards the destination. When the
    destination is reached, the agent selects a new
    destination, again at random.
 */
public class SimpleAGVAgent extends Vehicle {

    /*
    Speed of the AGV agent. Currently all agents
        have the same speed.
     */
    private static double agvSpeed = 2d;

    /*
    The random generator of this agent.
        Used to generate random numbers.
     */
    private RandomGenerator randomGenerator;

    /*
    The destination of this agent.
        It is possible that the agent has no destination.
     */
    private Optional<Point> destination;


    /*
    Constructor
     */
    public SimpleAGVAgent(Point startPosition, RandomGenerator aRandomGenerator) {
        super(
                VehicleDTO.builder()
                .startPosition(startPosition)
                .speed(getAgvSpeed())
                .build()
        );

        setRandomGenerator(aRandomGenerator);
        setEmptyDestination();
    }


    /*
    AGV Speed
     */
    private static double getAgvSpeed() {
        return agvSpeed;
    }


    /*
    Random Generator
     */
    private RandomGenerator getRandomGenerator() {
        return randomGenerator;
    }

    private void setRandomGenerator(RandomGenerator aRandomGenerator) {
        this.randomGenerator = aRandomGenerator;
    }

    /*
    Destination
     */
    private boolean hasDestination() {
        return destination.isPresent();
    }

    private Point getDestination() {
        return destination.get();
    }

    private void setEmptyDestination() {
        this.destination = Optional.absent();
    }

    private void generateNewDestination(RoadModel roadModel) {
        this.destination = Optional.of(
                roadModel.getRandomPosition(
                        getRandomGenerator()
                )
        );
    }

    /*
    Tick Listener
     */
    @Override
    protected void tickImpl(TimeLapse timeLapse) {
        final RoadModel roadModel = getRoadModel();

        while (timeLapse.hasTimeLeft()) {

            if (! hasDestination()) {
                generateNewDestination(roadModel);
                System.out.print("New destination: ");
                System.out.println(getDestination());
            }
            roadModel.moveTo(
                    this, getDestination(), timeLapse
            );

            if (roadModel.containsObjectAt(this, getDestination())) {
                setEmptyDestination();
                System.out.println("Destination reached!");
            }
        }
    }
}
