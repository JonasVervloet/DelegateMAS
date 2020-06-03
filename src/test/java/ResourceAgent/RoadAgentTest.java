package ResourceAgent;

import com.github.rinde.rinsim.geom.Point;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RoadAgentTest {

    Point connection1;
    Point connection2;
    RoadAgent agent1;
    PDAgent agent2;
    PDAgent agent3;

    @Before
    public void init() {
        connection1 = new Point(2, 4);
        connection2 = new Point(6, 10);
        agent1 = new RoadAgent(connection1, connection2);
        agent2 = new PDAgent(connection1, PDType.DELIVER_POINT);
        agent3 = new PDAgent(connection2, PDType.STORAGE);
    }

    /*
    Constructor
     */
    @Test
    public void testValidConstructor() {
        assert(agent1.isRoadAgent());
        assertFalse(agent1.isStorageSpace());
        assertFalse(agent1.isDeliveryPoint());
        assertFalse(agent1.isMachineAgent());
        assertFalse(agent1.isIntersectionAgent());

        assertFalse(agent1.checkAllNeighborsSet());

        assertEquals(5, agent1.getScheduleManager().getTraversalTime());
        assertEquals(5, agent1.getScheduleManager().getCapacity());

        assertEquals(new Point(4, 7), agent1.getPosition().get());
    }

    /*
    Neighbors
     */
    @Test
    public void testAddNeighbor() {
        agent1.addNeighbor(connection1, agent2);
        agent1.addNeighbor(connection2, agent3);

        agent1.checkAllNeighborsSet();
    }

    @Test (expected = IllegalArgumentException.class)
    public void testAddNeighborInvalidConnection() {
        agent1.addNeighbor(new Point(3, 8), agent2);
    }

    @Test (expected = IllegalStateException.class)
    public void testAddNeighborSameConnectionTwiceNeighbor1() {
        agent1.addNeighbor(connection1, agent2);
        agent1.addNeighbor(connection1, agent3);
    }

    @Test (expected = IllegalStateException.class)
    public void testAddNeighborSameConnectionTwiceNeighbor2() {
        agent1.addNeighbor(connection2, agent2);
        agent1.addNeighbor(connection2, agent3);
    }

    @Test
    public void testConnectNeighbors() {
        agent1.connectNeighbor(agent2);
        assertFalse(agent1.checkAllNeighborsSet());

        agent1.connectNeighbor(agent3);
        assert(agent1.checkAllNeighborsSet());

        assertEquals(agent2.getResourceId(),
                agent1.getOtherNeighborId(agent3.getResourceId()));
        assertEquals(agent3.getResourceId(),
                agent1.getOtherNeighborId(agent2.getResourceId()));

        assertEquals(agent2, agent1.getOtherNeighborAgent(agent3.getResourceId()));
        assertEquals(agent3, agent1.getOtherNeighborAgent(agent2.getResourceId()));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConnectNeighborsWithoutValidConnection() {
        PDAgent agent4 = new PDAgent(new Point(8, 9), PDType.STORAGE);
        agent1.connectNeighbor(agent4);
    }

    /*
    String
     */
    @Test
    public void testToString() {
        assertEquals("RoadAgent: Neighbor (no agent set): " + connection1.toString() +
                " + Neighbor (no agent set): " + connection2.toString(),
                agent1.toString());
    }
}