package Environment;

import com.github.rinde.rinsim.geom.LengthData;
import com.github.rinde.rinsim.geom.ListenableGraph;

import ResourceAgent.ResourceAgent;

import java.util.List;


public class Environment {

    private ListenableGraph<LengthData> graph;
    private ResourceTable resources;


    public Environment(ListenableGraph<LengthData> graph, ResourceTable table) {
        this.graph = graph;
        this.resources = table;
    }

    public ListenableGraph<LengthData> getGraph() {
        return graph;
    }

    public boolean checkAllNeighborsSet() {
        return resources.checkAllResourcesSet();
    }

    public List<ResourceAgent> getStorageAgents() {
        return resources.getStorageAgents();
    }

    public List<ResourceAgent> getDeliveryAgents() {
        return resources.getDeliveryAgents();
    }

    public List<ResourceAgent> getAllResourceAgents() {
        return resources.getAllResourceAgents();
    }
}
