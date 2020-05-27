package Environment;

import com.github.rinde.rinsim.geom.LengthData;
import com.github.rinde.rinsim.geom.ListenableGraph;
import com.github.rinde.rinsim.geom.Point;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;

public class EnvironmentCreator {

    public static Environment createEnvironment(int size, double vehicleLength) {
        final Table<Integer, Integer, Point> matrix =
                createMatrix(8 + 28 * size, 16 * size + 1, vehicleLength, new Point(0, 0));
        ListenableGraph<LengthData> graph = GraphCreator.createGraph(matrix, size, vehicleLength);
        ResourceTable resources = ResourcesCreator.getResourceTable(matrix, size);
        resources.connectResources();
        assert(resources.checkAllResourcesSet());
        return new Environment(graph, resources);
    }

    private static ImmutableTable<Integer, Integer, Point> createMatrix(
            int nbCol, int nbRow, double vehicleLength, Point offset) { ;
        final ImmutableTable.Builder<Integer, Integer, Point> builder =
                ImmutableTable.builder();
        for (int c = 0; c < nbCol; c++) {
            for (int r = 0; r < nbRow; r++) {
                builder.put(r, c, new Point(
                        offset.x + c * vehicleLength,
                        offset.y + r * vehicleLength));
            }
        }
        return builder.build();
    }
}
