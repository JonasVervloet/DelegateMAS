package DelegateMAS;

import AGVAgent.AGVAgent;
import Ant.ExplorationAnt;
import CommunicationManager.AGVCommunication;
import ResourceAgent.Schedule.ScheduleManager;
import org.apache.commons.math3.random.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

public class ExplorationMAS extends DelegateMAS {

    /*
    The action rate for exploration delegate MAS
        modules.
     */
    private static int explorationActionRate = 20;

    /*
    The AGVAgent that delegates this Exploration
        MAS module.
     */
    private AGVAgent agent;

    /*
    A list of routes that the AGV agent can follow.
     */
    private List<Route> routes;


    /*
    Constructor
     */
    public ExplorationMAS(AGVAgent agent)
            throws NullPointerException {
        super(explorationActionRate);

        if (agent == null) {
            throw new NullPointerException(
                    "EXPLORATION MAS | GIVEN AGV AGENT IS NULL"
            );
        }
        this.agent = agent;

        routes = new ArrayList<>();
    }


    /*
    Exploration Action Rate
     */
    private static boolean isValidActionRate(int actionRate) {
        return actionRate > 0;
    }

    public static void setExplorationActionRate(int actionRate)
            throws IllegalArgumentException {
        if (! isValidActionRate(actionRate)) {
            throw new IllegalArgumentException(
                    "EXPLORATION MAS | THE GIVEN ACTION RATE IS INVALID"
            );
        }
        explorationActionRate = actionRate;
    }

    /*
    AGV Agent
     */
    private boolean carriesOrder() {
        return agent.carriesOrder();
    }

    private int getAgvId() {
        return agent.getAgvId();
    }

    private int getCurrentTime() {
        return agent.getCurrentTime();
    }

    private List<Integer> getOrderDestinations() {
        return agent.getOrderDestinations();
    }

    private RandomGenerator getRandomGenerator() {
        return agent.getRandomGenerator();
    }

    /*
    Delegate MASS
     */
    @Override
    protected void takeAction() {
        if (carriesOrder()) {
            sendExplorationAnt(
                    new ExplorationAnt(getAgvId(), getOrderDestinations(),
                            getRandomGenerator(), getCurrentTime())
            );
        } else {
            sendExplorationAnt(
                    new ExplorationAnt(getAgvId(),
                            getRandomGenerator(), getCurrentTime())
            );
        }
    }

    @Override
    public void tick() {
        super.tick();
        evaporateRoutes();
    }

    /*
    Communication Manager
     */
    private AGVCommunication getCommunicationManager() {
        return agent.getCommunicationManager();
    }

    private void sendExplorationAnt(ExplorationAnt ant) {
        getCommunicationManager().sendToResourceAgent(ant);
    }

    /*
    Routes
     */
    public int getNumberOfRoutes() {
        return routes.size();
    }

    public Route getBestRoute() {
        Route best = null;
        for (Route route: routes) {
            if (best == null) {
                best = route;
            } else if (isBetterRoute(best, route)) {
                best = route;
            }
        }

        return best;
    }

    private static boolean isBetterRoute(Route current, Route newRoute) {
        if (current.getNbOfCheckPoints() < newRoute.getNbOfCheckPoints()) {
            return true;
        } else if (current.getNbOfCheckPoints() == newRoute.getNbOfCheckPoints()) {
            if (current.getArrivalTime() < newRoute.getArrivalTime()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void evaporateRoutes() {
        List<Route> toRemove = new ArrayList<>();
        for (Route route: routes) {
            route.evaporate();
            if (! route.hasTimeToLive()) {
                toRemove.add(route);
            }
        }

        routes.removeAll(toRemove);
    }

    public void processExploredRoute(Route route) {
        route.cleanRoute();
        routes.add(route);
    }

    public void invalidateRoute(Route aRoute) {
        routes.remove(aRoute);
    }
}
