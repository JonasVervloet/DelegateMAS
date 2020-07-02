package Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ResourceAgent.PDAgent;
import ResourceAgent.ResourceAgent;

public class ResourceTable {

    Map<Entry, ResourceAgent> table;

    public ResourceTable() {
        table = new HashMap<Entry, ResourceAgent>();
    }


    /*
    ENTRIES
     */
    public ResourceAgent getEntry(int x, int y)
            throws IllegalArgumentException {
        Entry query = new Entry(x, y);

        ResourceAgent agent = table.get(query);
        if (agent == null) {
            query.out();
            throw new IllegalArgumentException(
                    "RESOURCE TABLE | NO SUCH ENTRY STORED IN THIS TABLE"
            );
        }
        return agent;
    }

    public void setEntry(int x, int y, ResourceAgent agent)
            throws IllegalArgumentException {
        Entry query = new Entry(x, y);
        ResourceAgent test = table.get(query);
        if (test == null) {
            table.put(query, agent);
        } else {
            System.out.println(test);
            throw new IllegalArgumentException(
                    "RESOURCE TABLE | THIS TABLE CONTAINS ALREADY A RESOURCE AGENT FOR THAT ENTRY"
            );
        }
    }

    /*
    RESOURCES
     */
    public boolean checkAllResourcesSet() {
        boolean allSet = true;
        for (ResourceAgent agent: table.values()) {
            if (! agent.checkAllNeighborsSet()) {
                allSet = false;
            }
        }
        return allSet;
    }

    public List<ResourceAgent> getIntersectionAgents() {
        List<ResourceAgent> agents = new ArrayList<>();
        for (ResourceAgent agent: table.values()) {
            if (agent.isIntersectionAgent() && ! agents.contains(agent)) {
                agents.add(agent);
            }
        }
        return agents;
    }

    public List<ResourceAgent> getRoadAgents() {
        List<ResourceAgent> agents = new ArrayList<>();
        for (ResourceAgent agent: table.values()) {
            if (agent.isRoadAgent() && ! agents.contains(agent)) {
                agents.add(agent);
            }
        }
        return agents;
    }

    public List<PDAgent> getStorageAgents() {
        List<PDAgent> agents = new ArrayList<>();
        for (ResourceAgent agent: table.values()) {
            if (agent.isStorageSpace() && ! agents.contains(agent)) {
                agents.add((PDAgent) agent);
            }
        }
        return agents;
    }

    public List<ResourceAgent> getMachineAgents() {
        List<ResourceAgent> agents = new ArrayList<>();
        for (ResourceAgent agent: table.values()) {
            if (agent.isMachineAgent() && ! agents.contains(agent)) {
                agents.add(agent);
            }
        }
        return agents;
    }

    public List<Integer> getDeliveryIds() {
        List<ResourceAgent> agents = getDeliveryAgents();
        List<Integer> ids = new ArrayList<>();
        for (ResourceAgent agent: agents) {
            if (agent.isDeliveryPoint()) {
                ids.add(agent.getResourceId());
            }
        }
        return ids;
    }

    public List<ResourceAgent> getDeliveryAgents() {
        List<ResourceAgent> agents = new ArrayList<>();
        for (ResourceAgent agent: table.values()) {
            if (agent.isDeliveryPoint() && ! agents.contains(agent)) {
                agents.add(agent);
            }
        }
        return agents;
    }

    public List<ResourceAgent> getAllResourceAgents() {
        List<ResourceAgent> agents = new ArrayList<>();
        for (ResourceAgent agent: table.values()) {
            if (! agents.contains(agent)) {
                agents.add(agent);
            }
        }
        return agents;
    }

    public void connectResources() {
        for (Entry entry: table.keySet()) {
            ResourceAgent current = table.get(entry);
            try {
                table.get(entry.getEntryAvove()).connectNeighbor(current);
            } catch (NullPointerException e) {}
            try {
                table.get(entry.getEntryLeft()).connectNeighbor(current);
            } catch (NullPointerException e) {}
        }
    }

    /*
    OUT
     */
    public void out() {
        for (Entry entry: table.keySet()) {
            ResourceAgent agent = table.get(entry);
            System.out.println(
                    entry.toString() + " -- " + agent.getClass()
            );
        }
    }
}
