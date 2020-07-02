package DelegateMAS;

import ResourceAgent.ResourceMap.ResourceEntry;

import java.util.ArrayList;
import java.util.List;

public class Intention {

    /*
    The intention entries of this intention.
     */
    private List<IntentionEntry> entries;

    /*
    The number of checkpoints that are reached
        when following this intention.
     */
    private int nbOfCheckpoints;

    /*
    The arrival time at the last checkpoint when
        following this intention.
     */
    private int arrivalTime;


    /*
    Constructor
     */
    public Intention(int nbOfCheckpoints, int arrivalTime) {
        this.entries = new ArrayList<>();
    }

    public Intention(int nbOfCheckpoints, int arrivalTime, IntentionEntry entry) {
        this(nbOfCheckpoints, arrivalTime);
        this.entries.add(entry);
    }


    /*
    Entries
     */
    public int getNbOfEntries() {
        return entries.size();
    }

    public int getIdOfEntry(int index) {
        return entries.get(index).getResourceId();
    }

    public IntentionEntry getEntry(int index) {
        return entries.get(index);
    }

    public void addEntry(int resourceId, int start, int end) {
        entries.add(new IntentionEntry(resourceId, start, end));
    }

    public void changToNextDestination() {
        entries.remove(0 );
    }

    /*
    Number of Checkpoints
     */
    public int getNbOfCheckpoints() {
        return nbOfCheckpoints;
    }

    /*
    Arrival Time
     */
    public int getArrivalTime() {
        return arrivalTime;
    }
}
