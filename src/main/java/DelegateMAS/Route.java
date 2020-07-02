package DelegateMAS;

import ResourceAgent.Schedule.PossibleReservation;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Route {

    /*
    A list containing the IDs of all the resource agents on the route.
        For every resource agent, a possible reservation is stored.
        This possible reservation can be used to eventually create
        a plan.
     */
    private List<Pair<Integer, PossibleReservation>> route;

    /*
    The number of checkpoints that the route reaches.
        Finding a order or reaching a destination of
        a order are possible checkpoints.
     */
    private int nbOfCheckPoints;

    /*
    The arrival time at the last checkpoint.
        This time will be null until a checkpoint
        has been reached.
     */
    private int arrivalTime;

    /*
    The life time that every route is given.
        After that time, the route will be
        considered outdated.
     */
    private static int lifeTime = 100;

    /*
    The time to live for this route.
        After this time expires, the route will be
        considered outdated.
     */
    private int timeToLive;


    /*
    Constructor
     */
    public Route() {
        this.route = new ArrayList<>();
        this.nbOfCheckPoints = 0;
    }

    public Route(Route aRoute) {
        this.route = aRoute.copyRouteSlots();
        this.nbOfCheckPoints = aRoute.getNbOfCheckPoints();
        this.arrivalTime = aRoute.getArrivalTime();
    }


    /*
    Route
     */
    private boolean isValidNextSlot(PossibleReservation next) {
        if (getRouteLength() == 0) {
            return true;
        } else {
            return getLastPossibleReservation().overlapWithNextPossReservation(next);
        }
    }

    public int getRouteLength() {
        return route.size();
    }

    public int getNextEarliestArrival() {
        return getLastPossibleReservation().getDepartureStart() + 1;
    }

    private PossibleReservation getLastPossibleReservation() {
        return route.get(route.size() - 1).getValue();
    }

    protected List<Pair<Integer, PossibleReservation>> copyRouteSlots() {
        List<Pair<Integer, PossibleReservation>> slots = new ArrayList<>();
        for (Pair<Integer, PossibleReservation>slot: route) {
            slots.add(
                    new Pair(slot.getKey(), slot.getValue().copyReservation())
            );
        }

        return slots;
    }

    public void addRouteSlot(int resourceId, PossibleReservation possRes)
            throws IllegalArgumentException {
        System.out.println(possRes);
        if (! isValidNextSlot(possRes)) {
            throw new IllegalArgumentException(
                    "ROUTE | THE GIVEN POSSIBLE RESERVATION IS NOT A VALID ONE"
            );
        }
        route.add(new Pair<>(resourceId, possRes));
    }

    public void filterPossibleReservations(List<PossibleReservation> possibleReservations) {
        if (getRouteLength() != 0) {
            List<PossibleReservation> toRemove = new ArrayList<>();
            PossibleReservation last = getLastPossibleReservation();

            for (PossibleReservation next: possibleReservations) {
                if (! last.overlapWithNextPossReservation(next)) {
                    toRemove.add(next);
                }
            }

            possibleReservations.removeAll(toRemove);
        }
    }

    public void cleanRoute() {
        int routeSize = route.size();
        for (int i=1; i <= routeSize; i++) {
            PossibleReservation first = route.get(routeSize - (i + 1)).getValue();
            PossibleReservation next = route.get(routeSize - i).getValue();
            first.adjustToNextPossibleReservation(next);
        }
    }

    public Intention convertToIntention() {
        Intention intention = new Intention(
                getNbOfCheckPoints(), getArrivalTime()
        );
        for (Pair<Integer, PossibleReservation> pair: route) {
            PossibleReservation possRes = pair.getValue();
            intention.addEntry(
                    pair.getKey(), possRes.getArrivalStart(),
                    possRes.getDepartureStart()
            );
        }

        return intention;
    }

    /*
    Number of checkpoints
     */
    public boolean hasReachedAnyCheckpoint() {
        return nbOfCheckPoints > 0;
    }

    public int getNbOfCheckPoints() {
        return nbOfCheckPoints;
    }

    private void incrementNbOfCheckpoints() {
        nbOfCheckPoints += 1;
    }

    /*
    Arrival Time
     */
    private boolean isValidArrivalTime(int newTime) {
        if (hasReachedAnyCheckpoint()) {
            return newTime > arrivalTime;
        } else {
            return newTime >= 0;
        }
    }

    public int getArrivalTime()
            throws IllegalStateException {
        if (!hasReachedAnyCheckpoint()) {
            throw new IllegalStateException(
                    "ROUTE | THIS ROUTE HAS NOT REACHED ANY CHECKPOINT"
            );
        }
        return arrivalTime;
    }

    private void setArrivalTime(int newTime) {
        if (! isValidArrivalTime(arrivalTime)) {
            throw new IllegalArgumentException(
                    "ROUTE | THE GIVEN ARRIVAL TIME IS NOT A VALID ONE"
            );
        }
        arrivalTime = newTime;
    }

    public void addCheckpointVisit(int arrivalTime)
            throws IllegalArgumentException {
        setArrivalTime(arrivalTime);
        incrementNbOfCheckpoints();
    }

    /*
    Time To Live
     */
    private static boolean isValidLifeTime(int lifeTime) {
        return lifeTime > 0;
    }

    public static void setLifeTime(int aTime)
            throws IllegalArgumentException {
        if (! isValidLifeTime(aTime)) {
            throw new IllegalArgumentException(
                    "ROUTE | THE GIVE LIFE TIME IS NOT A VALID ONE"
            );
        }
        lifeTime = aTime;
    }

    public boolean hasTimeToLive() {
        return timeToLive > 0;
    }

    public static int getLifeTime() {
        return Route.lifeTime;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public void evaporate()
            throws IllegalStateException {
        if (getTimeToLive() == 0) {
            throw new IllegalStateException(
                    "ROUTE | EVAPORATING WHEN TIME TO LIVE EQUALS ZEROS"
            );
        }

        timeToLive -= 1;
    }

    public void refresh() {
        timeToLive = getLifeTime();
    }

    /*
    Utils
     */
    public Route copyRoute() {
        return new Route(this);
    }
}
