package Environment;

import com.github.rinde.rinsim.geom.*;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;

import ResourceAgent.*;

public class GraphCreator {

    GraphCreator() {}

    private static void connectHorizontal(Graph<LengthData> graph, Table<Integer, Integer, Point> matrix,
                                  int indexy, int indexx1, int indexx2) {
        for (int i = indexx1; i < indexx2; i++) {
            Graphs.addBiPath(graph, matrix.get(indexy, i),
                    matrix.get(indexy, i + 1));
        }
    }

    private static void connectVertical(Graph<LengthData> graph, Table<Integer, Integer, Point> matrix,
                                  int indexx, int indexy1, int indexy2) {
        for (int i = indexy1; i < indexy2; i++) {
            Graphs.addBiPath(graph, matrix.get(i, indexx),
                    matrix.get(i + 1, indexx));
        }
    }

    private static void connect(Graph<LengthData> graph, Table<Integer, Integer, Point> matrix,
                              int index1x, int index1y, int index2x, int index2y)
            throws IllegalArgumentException {
        if (index1y == index2y) {
            if (index1x < index2x) {
                connectHorizontal(graph, matrix, index1y, index1x, index2x);
            } else if (index2x < index1x) {
                connectHorizontal(graph, matrix, index1y, index2x, index1x);
            } else {
                throw new IllegalArgumentException("Connecting Two the same points! Not useful...");
            }
        } else if (index1x == index2x) {
            if (index1y < index2y) {
                connectVertical(graph, matrix, index1x, index1y, index2y);
            } else {
                connectVertical(graph, matrix, index1x, index2y, index1y);
            }
        } else {
            throw new IllegalArgumentException("Connecting diagonal lines! Not possible...");
        }
    }

    private static void addMachineAccess(Graph<LengthData> graph, Table<Integer, Integer, Point> matrix,
                                 int x, int y) {
        connect(graph, matrix, x + 4, y, x + 4, y + 4);
        connect(graph, matrix, x - 8, y + 4, x - 4, y + 4);
        connect(graph, matrix, x - 4, y - 8, x - 4, y - 4);
        connect(graph, matrix, x, y - 4, x + 4, y - 4);
    }

    private static Graph<LengthData> createTableGraph(Table<Integer, Integer, Point> matrix, int n) {
        final Graph<LengthData> g = new TableGraph<>();

        int originx = 4 + 16 * n;
        int originy = 8 * n;

        // CREATE MACHINE WORK PLACE
        // horizontal and vertical lines
        for (int i = 0; i <= 2*n; i++) {
            // vertical lines
            connect(g, matrix, originx - 8 * i, originy + 8 * n, originx - 8 * i, originy - 8 * n);
            // horizontal lines
            connect(g, matrix, originx - 16 * n, originy - 8 * (i-n), originx, originy - 8 * (i-n));
        }
        // machines
        for (int i = 0; i < n; i ++) {
            for (int j = 0; j < n; j++) {
                int x = originx - 16 * i - 8;
                int y = originy + 8 * (n - 1) - 16 * j;
                addMachineAccess(g, matrix, x, y);
            }
        }

        // CREATE STORAGE PLACE
        // storages places
        for (int i = 0; i <= 2*n; i++) {
            int y_value = originy + 8 * (i - n);
            int x_value1 = originx + 6;
            int x_value2 = originx + 3;
            // horizontal line
            connect(g, matrix, originx, y_value, originx + 3 + 12 * n, y_value);
            for (int j = 0; j < 2*n; j++) {
                // vertical lines up: they are not needed in the top line
                if (i != 0) {
                    connect(
                            g, matrix,
                            x_value1 + 6 * j, y_value,
                            x_value1 + 6 * j, y_value - 4
                    );
                }
                // vertical lines down: they are not needed in the bottom line
                if (i != 2 * n) {
                    connect(
                            g, matrix,
                            x_value2 + 6 * j, y_value,
                            x_value2+ 6 * j, y_value + 4);
                }
            }
        }
        // final vertical line
        connect(g, matrix, originx + 3 + 12 * n, originy - 8 * n, originx + 3 + 12 * n, originy + 8 * n);

        // CREATE DELIVER PLACES
        for (int i = 0; i <= 2*n; i++) {
            connect(g, matrix, originx - 4 - 16*n, originy + 8*(i-n), originx - 16*n, originy + 8*(i-n));
        }

        return g;
    }

    public static ListenableGraph<LengthData> createGraph(
            Table<Integer, Integer, Point> matrix, int size, double vehicleLength) {
        final Graph<LengthData> g = createTableGraph(matrix, size);
        return new ListenableGraph<>(g);
    }
}
