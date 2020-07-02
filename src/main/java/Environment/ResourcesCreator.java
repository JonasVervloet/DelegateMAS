package Environment;

import ResourceAgent.IntersectionAgent;
import ResourceAgent.PDAgent;
import ResourceAgent.PDType;
import ResourceAgent.RoadAgent;
import com.github.rinde.rinsim.geom.Point;
import com.google.common.collect.Table;

public class ResourcesCreator {

    public static ResourceTable getResourceTable(Table<Integer, Integer, Point> matrix, int n) {
        ResourceTable resources = new ResourceTable();

        // CREATE DELIVERY STATIONS
        for (int i=0; i <=2*n; i++) {
            int yValue = 8 * i;
            int yEntry = 4 * i;
            PDAgent dStation = createPDAgent(
                    matrix, 0, yValue, PDType.DELIVER_POINT
            );
            resources.setEntry(0, yEntry, dStation);
            RoadAgent dRoad = createRoadAgent(
                    matrix, 0, yValue, 3, yValue
            );
            resources.setEntry(1, yEntry, dRoad);
        }

        // CREATE MACHINE WORKPLACE AGENTS
        int basex = 12;
        int basey = 8;
        int entryx = 6;
        int entryy = 4;
        for (int i=0; i<n; i++) {
            for (int j=0; j<n; j++) {
                addWorkStation(
                        matrix, basex + i * 16, basey + j * 16,
                        resources, entryx + i * 8, entryy + j * 8, j==0, n
                );
            }
        }

        // create bottom line
        basey = 2 * 8 * n;
        entryy = 2 * 4 * n;
        for (int i=0; i<2*n; i++) {
            basex = 4 + 8 * i;
            entryx = 2 + 4 * i;
            IntersectionAgent tSection = createTSectionAgentUp(matrix, basex, basey);
            resources.setEntry(entryx, entryy, tSection);

            RoadAgent rAgent = createRoadAgent(
                    matrix, basex + 1, basey, basex + 7, basey
            );
            resources.setEntry(entryx + 1, entryy, rAgent);
            resources.setEntry(entryx + 3, entryy, rAgent);
        }

        // create middle line
        basex = 4 + 2 * 8 * n;
        entryx = 2 + 2 * 4 * n;
        for (int i=0; i<2*n; i++) {
            basey = 8 * i;
            entryy = 4 * i;
            IntersectionAgent section;
            if (i == 0) {
                section = createTSectionAgentDown(matrix, basex, basey);
            } else {
                section = createCrossSectionAgent(matrix, basex, basey);
            }
            resources.setEntry(entryx, entryy, section);

            RoadAgent rAgent = createRoadAgent(
                    matrix, basex, basey + 1, basex, basey + 7
            );
            resources.setEntry(entryx, entryy + 1, rAgent);
            resources.setEntry(entryx, entryy + 3, rAgent);
        }
        IntersectionAgent tSection = createTSectionAgentUp(matrix, basex, 2 * 8 * n);
        resources.setEntry(entryx, 2 * 4 * n, tSection);

        // CREATE STORAGE SPACE
        addStoragePlace(matrix, resources, n);

        // create back line
        basex = 4 + 2 * 8 * n + 3 + 2 * 6 * n;
        entryx = 2 + 2 * 4 * n + 2 * 4 * n + 2;
        for (int i=0; i<2*n-1; i++) {
            basey = 8 + 8 * i;
            entryy = 4 + 4 * i;

            resources.setEntry(
                    entryx, entryy,
                    createTSectionAgentLeft(matrix, basex, basey)
            );
            resources.setEntry(
                    entryx - 1, entryy,
                    createRoadAgent(matrix, basex - 2, basey, basex - 1, basey)
            );

            if (! (i == 2 * n - 2)) {
                RoadAgent rAgent = createRoadAgent(
                        matrix, basex, basey + 1, basex, basey + 7
                );
                resources.setEntry(entryx, entryy + 1, rAgent);
                resources.setEntry(entryx, entryy + 3, rAgent);
            }
        }

        // Corner road top
        RoadAgent roadAgent = createRoadAgent(
                matrix, basex - 5, 0, basex, 7
        );
        resources.setEntry(entryx - 3, 0, roadAgent);
        resources.setEntry(entryx, 3, roadAgent);

        // Corner road bottom
        roadAgent = createRoadAgent(
                matrix, basex - 2, 16 * n, basex, 16 * n - 7
        );
        resources.setEntry(entryx - 1, 8 * n, roadAgent);
        resources.setEntry(entryx, 8 * n - 3, roadAgent);

        return resources;
    }


    /*
    INTERSECTION AGENTS
     */
    private static IntersectionAgent createCrossSectionAgent(
            Table<Integer, Integer, Point> matrix, int x, int y) {
        return new IntersectionAgent(
                matrix.get(y, x + 1),
                matrix.get(y - 1, x),
                matrix.get(y,x - 1),
                matrix.get(y + 1, x)
        );
    }

    private static IntersectionAgent createTSectionAgentRight(
            Table<Integer, Integer, Point> matrix, int x, int y) {
        return new IntersectionAgent(
                matrix.get(y, x + 1),
                matrix.get(y - 1, x),
                matrix.get(y + 1, x)
        );
    }

    private static IntersectionAgent createTSectionAgentLeft(
            Table<Integer, Integer, Point> matrix, int x, int y) {
        return new IntersectionAgent(
                matrix.get(y, x - 1),
                matrix.get(y - 1, x),
                matrix.get(y + 1, x)
        );
    }

    private static IntersectionAgent createTSectionAgentUp(
            Table<Integer, Integer, Point> matrix, int x, int y) {
        return new IntersectionAgent(
                matrix.get(y, x + 1),
                matrix.get(y, x - 1),
                matrix.get(y - 1, x));
    }

    private static IntersectionAgent createTSectionAgentDown(
            Table<Integer, Integer, Point> matrix, int x, int y) {
        return new IntersectionAgent(
                matrix.get(y, x + 1),
                matrix.get(y, x - 1),
                matrix.get(y + 1, x));
    }

    /*
    ROAD AGENTS
     */
    private static RoadAgent createRoadAgent(Table<Integer, Integer, Point> matrix,
                                     int x1, int y1, int x2, int y2) {
        return new RoadAgent(
                matrix.get(y1, x1),
                matrix.get(y2, x2)
        );
    }

    /*
    PD AGENTS
     */
    private static PDType getMachineType(int n, int x, int y) {
        if (x < 8 || x > 16 * n) {
            System.out.println("PD AGENT | NO VALID X");
        } else if (y < 4 || y > 16 * n - 4) {
            System.out.println("PD AGENT | NO VALID Y");
        }
        if (x - 4 > 8 * n) {
            if (y > 8 * n) {
                System.out.println("MachineB");
                return PDType.MACHINE_B;
            } else {
                System.out.println("MachineC");
                return PDType.MACHINE_C;
            }
        } else {
            if (y > 8 * n) {
                System.out.println("MachineA");
                return PDType.MACHINE_A;
            } else {
                System.out.println("MachineD");
                return PDType.MACHINE_D;
            }
        }
    }

    private static PDAgent createPDAgent(Table<Integer, Integer, Point> matrix,
                                 int x, int y, PDType type) {
        return new PDAgent(
                matrix.get(y, x), type
        );
    }

    private static PDAgent createMachineAgent(Table<Integer, Integer, Point> matrix,
                                      int n, int x, int y) {
        return new PDAgent(
                matrix.get(y, x), getMachineType(n, x, y)
        );
    }

    /*
    WORK STATIONS
     */
    private static void addWorkStationCrossRoads(Table<Integer, Integer, Point> matrix, int middlex, int middley,
                                         ResourceTable resources, int entryx, int entryy, boolean top) {
        // TOP ROW
        IntersectionAgent section1;
        IntersectionAgent section2;
        if (top) {
            // T sections when top work stations
            section1 = createTSectionAgentDown(
                    matrix, middlex - 8, middley - 8
            );
            section2 = createTSectionAgentDown(
                    matrix, middlex, middley - 8
            );
        } else {
            // Cross sections when not top work stations
            section1 = createCrossSectionAgent(
                    matrix, middlex - 8, middley - 8
            );
            section2 = createCrossSectionAgent(
                    matrix, middlex, middley - 8
            );
        }
        resources.setEntry(entryx - 4, entryy - 4, section1);
        resources.setEntry(entryx, entryy - 4, section2);

        // MIDDLE ROW
        IntersectionAgent section3 = createCrossSectionAgent(
                matrix, middlex - 8, middley
        );
        IntersectionAgent section4 = createCrossSectionAgent(
                matrix, middlex, middley
        );

        resources.setEntry(entryx - 4, entryy, section3);
        resources.setEntry(entryx, entryy, section4);

        // MACHINE T SECTIONS
        IntersectionAgent section5 = createTSectionAgentDown(
                matrix, middlex - 4, middley - 8
        );
        IntersectionAgent section6 = createTSectionAgentRight(
                matrix, middlex, middley - 4
        );
        IntersectionAgent section7 = createTSectionAgentRight(
                matrix, middlex - 8, middley + 4
        );
        IntersectionAgent section8 = createTSectionAgentDown(
                matrix, middlex + 4, middley
        );

        resources.setEntry(entryx - 2, entryy - 4, section5);
        resources.setEntry(entryx, entryy - 2, section6);
        resources.setEntry(entryx - 4, entryy + 2, section7);
        resources.setEntry(entryx + 2, entryy, section8);
    }

    private static void addWorkStationRoads(Table<Integer,Integer, Point> matrix, int middlex, int middley,
                                    ResourceTable resources, int entryx, int entryy) {
        // LONG ROADS
        // long road 1
        RoadAgent road1 = createRoadAgent(
                matrix, middlex - 8, middley - 7, middlex - 8, middley - 1
        );
        resources.setEntry(entryx - 4, entryy - 3, road1);
        resources.setEntry(entryx - 4, entryy - 1, road1);

        // long road 2
        RoadAgent road2 = createRoadAgent(
                matrix, middlex - 7, middley, middlex - 1, middley
        );
        resources.setEntry(entryx - 3, entryy, road2);
        resources.setEntry(entryx - 1, entryy, road2);

        // long road 3
        RoadAgent road3 = createRoadAgent(
                matrix, middlex, middley + 1, middlex, middley + 7
        );
        resources.setEntry(entryx, entryy + 1, road3);
        resources.setEntry(entryx, entryy + 3, road3);

        // long road 4
        RoadAgent road4 = createRoadAgent(
                matrix, middlex + 1, middley - 8, middlex + 7, middley - 8
        );
        resources.setEntry(entryx + 1, entryy - 4, road4);
        resources.setEntry(entryx + 3, entryy - 4, road4);

        // SHORT ROADS
        // short roads 1 - 2
        RoadAgent road5 = createRoadAgent(
                matrix, middlex - 7, middley - 8, middlex - 5, middley - 8
        );
        resources.setEntry(entryx - 3, entryy - 4, road5);
        RoadAgent road6 = createRoadAgent(
                matrix, middlex - 3, middley - 8, middlex - 1, middley - 8
        );
        resources.setEntry(entryx - 1, entryy - 4, road6);

        // short roads 3 - 4
        RoadAgent road7 = createRoadAgent(
                matrix, middlex, middley - 7, middlex, middley - 5
        );
        resources.setEntry(entryx, entryy - 3, road7);
        RoadAgent road8 = createRoadAgent(
                matrix, middlex, middley - 3, middlex, middley - 1
        );
        resources.setEntry(entryx, entryy - 1, road8);

        // short roads 5 - 6
        RoadAgent road9 = createRoadAgent(
                matrix, middlex - 8, middley + 1, middlex - 8, middley + 3
        );
        resources.setEntry(entryx - 4, entryy + 1, road9);
        RoadAgent road10 = createRoadAgent(
                matrix, middlex - 8, middley + 5, middlex - 8, middley + 7
        );
        resources.setEntry(entryx - 4, entryy + 3, road10);

        // short roads 7 - 8
        RoadAgent road11 = createRoadAgent(
                matrix, middlex + 1, middley, middlex + 3, middley
        );
        resources.setEntry(entryx + 1, entryy, road11);
        RoadAgent road12 = createRoadAgent(
                matrix, middlex + 5, middley, middlex + 7, middley
        );
        resources.setEntry(entryx + 3, entryy, road12);
    }

    private static void addWorkStationMachines(Table<Integer, Integer, Point> matrix, int middlex, int middley,
                                       ResourceTable resources, int entryx, int entryy, int n) {
        // MACHINE 1
        // road
        RoadAgent road13 = createRoadAgent(
                matrix, middlex - 4, middley - 7, middlex - 4, middley - 4
        );
        resources.setEntry(entryx - 2, entryy - 3, road13);
        // machine
        PDAgent mAgent1 = createMachineAgent(
                matrix, n,middlex - 4, middley - 4
        );
        resources.setEntry(entryx - 2, entryy - 2, mAgent1);

        // MACHINE 2
        // road
        RoadAgent road14 = createRoadAgent(
                matrix, middlex + 1, middley - 4, middlex + 4, middley - 4
        );
        resources.setEntry(entryx + 1, entryy - 2, road14);
        // machine
        PDAgent mAgent2 = createMachineAgent(
                matrix, n,middlex + 4, middley - 4
        );
        resources.setEntry(entryx + 2, entryy - 2, mAgent2);

        // MACHINE 3
        // road
        RoadAgent road15 = createRoadAgent(
                matrix, middlex - 7, middley + 4, middlex - 4, middley + 4
        );
        resources.setEntry(entryx - 3, entryy + 2, road15);
        // machine
        PDAgent mAgent3 = createMachineAgent(
                matrix, n,middlex - 4, middley + 4
        );
        resources.setEntry(entryx - 2, entryy + 2, mAgent3);

        // MACHINE 4
        // road
        RoadAgent road16 = createRoadAgent(
                matrix, middlex + 4, middley + 1, middlex + 4, middley + 4
        );
        resources.setEntry(entryx + 2, entryy + 1, road16);
        // machine
        PDAgent mAgent4 = createMachineAgent(
                matrix, n,middlex + 4, middley + 4
        );
        resources.setEntry(entryx + 2, entryy + 2, mAgent4);
    }

    private static void addWorkStation(Table<Integer, Integer, Point> matrix, int middlex, int middley,
                               ResourceTable resources, int entryx, int entryy,
                               boolean top, int n) {
        // CROSSROADS
        addWorkStationCrossRoads(matrix, middlex, middley, resources, entryx, entryy, top);

        // ROADS
        addWorkStationRoads(matrix, middlex,middley, resources, entryx, entryy);

        // ADD MACHINES
        addWorkStationMachines(matrix, middlex, middley, resources, entryx, entryy, n);
    }

    /*
    STORAGE SPACES
     */
    private static void addStoragePlace(Table<Integer, Integer, Point> matrix, ResourceTable resources, int n) {
        for (int i=0; i<2*n; i++) {
            for (int j=0; j<2*n; j++) {
                int basex = 4 + 2 * 8 * n + 3 + 6 * j;
                int basey = 8 * i;
                int entryx = 2 + 2 * 4 * n + 2 + 4 * j;
                int entryy = 4 * i;

                IntersectionAgent tsection1 = createTSectionAgentDown(matrix, basex, basey);
                resources.setEntry(entryx, entryy, tsection1);

                RoadAgent rAgent1 = createRoadAgent(
                        matrix, basex, basey + 1, basex, basey + 4
                );
                resources.setEntry(entryx, entryy + 1, rAgent1);

                PDAgent pdAgent1 = createPDAgent(matrix, basex, basey + 4, PDType.STORAGE);
                resources.setEntry(entryx, entryy + 2, pdAgent1);

                if (i != 0) {
                    RoadAgent rAgent2 = createRoadAgent(
                            matrix, basex - 2, basey, basex - 1, basey
                    );
                    resources.setEntry(entryx - 1, entryy, rAgent2);

                    RoadAgent rAgent3 = createRoadAgent(
                            matrix, basex + 1, basey, basex + 2, basey
                    );
                    resources.setEntry(entryx + 1, entryy, rAgent3);

                    IntersectionAgent tsection2 = createTSectionAgentUp(matrix, basex + 3, basey);
                    resources.setEntry(entryx + 2, entryy, tsection2);

                    RoadAgent rAgent4 = createRoadAgent(
                            matrix, basex + 3, basey - 1, basex + 3, basey - 4
                    );
                    resources.setEntry(entryx + 2, entryy - 1, rAgent4);

                    PDAgent pdAgent2 = createPDAgent(matrix, basex + 3, basey - 4, PDType.STORAGE);
                    resources.setEntry(entryx + 2, entryy - 2, pdAgent2);
                }
                if (i == 0 && j != 2 * n - 1) {
                    RoadAgent rAgent2 = createRoadAgent(
                            matrix, basex + 1, basey, basex + 5, basey
                    );
                    resources.setEntry(entryx + 1, entryy, rAgent2);
                    resources.setEntry(entryx + 3, entryy, rAgent2);
                }
                if (i==0 && j == 0) {
                    RoadAgent rAgent = createRoadAgent(
                            matrix, basex - 2, basey, basex - 1, basey
                    );
                    resources.setEntry(entryx - 1, entryy, rAgent);
                }
            }
        }

        int basey = 2 * 8 * n;
        int entryy = 2 * 4 * n;
        for (int i=0; i<2*n; i++) {
            int basex = 4 + 2 * 8 * n + 1 + 6 * i;
            int entryx = 2 + 2 * 4 * n + 1 + 4 * i;

            RoadAgent rAgent = createRoadAgent(
                    matrix, basex, basey, basex + 4, basey
            );
            resources.setEntry(entryx, entryy, rAgent);
            resources.setEntry(entryx + 2, entryy, rAgent);

            IntersectionAgent tSection = createTSectionAgentUp(matrix, basex + 5, basey);
            resources.setEntry(entryx + 3, entryy, tSection);

            RoadAgent rAgent2 = createRoadAgent(
                    matrix, basex + 5, basey - 1, basex + 5, basey - 4
            );
            resources.setEntry(entryx + 3, entryy - 1, rAgent2);

            PDAgent pdAgent = createPDAgent(matrix, basex + 5, basey - 4, PDType.STORAGE);
            resources.setEntry(entryx + 3, entryy - 2, pdAgent);
        }
    }
}
