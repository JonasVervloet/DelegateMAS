package ResourceAgent;

import com.github.rinde.rinsim.geom.Point;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PDAgentTest {

    Point connection;
    Point connection2;
    PDAgent agent1;
    PDAgent agent2;
    PDAgent agent3;
    PDAgent agent4;
    PDAgent agent5;
    PDAgent agent6;

    @Before
    public void inti() {
        connection = new Point(5, 6);
        connection2 = new Point(3, 4);
        agent1 = new PDAgent(connection, PDType.STORAGE);
        agent2 = new PDAgent(connection, PDType.MACHINE_A);
        agent3 = new PDAgent(connection, PDType.MACHINE_B);
        agent4 = new PDAgent(connection, PDType.MACHINE_C);
        agent5 = new PDAgent(connection, PDType.MACHINE_D);
        agent6 = new PDAgent(connection2, PDType.DELIVER_POINT);
    }

    /*
    Constructor
     */
    @Test
    public void testValidConstructor() {
        assertNotEquals(agent1.getResourceId(), agent6.getResourceId());
        assertNotEquals(1, agent1.getResourceId());
        assertNotEquals(2, agent1.getResourceId());
        assertNotEquals(3, agent1.getResourceId());
        assertNotEquals(2, agent1.getResourceId());


        assertEquals(1, agent2.getResourceId());
        assertEquals(2, agent3.getResourceId());
        assertEquals(3, agent4.getResourceId());
        assertEquals(4, agent5.getResourceId());

        assertNotEquals(1, agent6.getResourceId());
        assertNotEquals(2, agent6.getResourceId());
        assertNotEquals(3, agent6.getResourceId());
        assertNotEquals(2, agent6.getResourceId());

        assertEquals(1, agent1.getScheduleManager().getCapacity());
        assertEquals(4, agent1.getScheduleManager().getTraversalTime());

        assertFalse(agent1.checkAllNeighborsSet());
        assertFalse(agent2.checkAllNeighborsSet());
        assertFalse(agent3.checkAllNeighborsSet());
        assertFalse(agent4.checkAllNeighborsSet());
        assertFalse(agent5.checkAllNeighborsSet());
        assertFalse(agent6.checkAllNeighborsSet());
    }

    /*
    Types
     */
    @Test
    public void testTypes() {
        assert(agent1.isStorageSpace());
        assertFalse(agent1.isMachineAgent());
        assertFalse(agent1.isDeliveryPoint());

        assertFalse(agent2.isStorageSpace());
        assert(agent2.isMachineAgent());
        assertFalse(agent2.isDeliveryPoint());

        assertFalse(agent3.isStorageSpace());
        assert(agent3.isMachineAgent());
        assertFalse(agent3.isDeliveryPoint());

        assertFalse(agent4.isStorageSpace());
        assert(agent4.isMachineAgent());
        assertFalse(agent4.isDeliveryPoint());

        assertFalse(agent5.isStorageSpace());
        assert(agent5.isMachineAgent());
        assertFalse(agent5.isDeliveryPoint());

        assertFalse(agent6.isStorageSpace());
        assertFalse(agent6.isMachineAgent());
        assert(agent6.isDeliveryPoint());
    }

    /*
    Neighbors
     */
    @Test
    public void testConnectNeighbor() {
        agent1.connectNeighbor(agent2);
        assert(agent1.checkAllNeighborsSet());
        assert(agent2.checkAllNeighborsSet());

        assertEquals(agent2, agent1.getNeighborAgent());
        assertEquals(agent1, agent2.getNeighborAgent());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConnectNeighborWrongConnection() {
        agent1.connectNeighbor(agent6);
    }

    @Test (expected = IllegalStateException.class)
    public void testConnectNeighborTwice() {
        agent1.connectNeighbor(agent2);
        agent1.connectNeighbor(agent3);
    }


    /*
    Communication Manager
     */
    @Test
    public void testGetPosition() {
        assertEquals(connection, agent1.getPosition().get());
        assertEquals(connection, agent2.getPosition().get());
        assertEquals(connection, agent3.getPosition().get());
        assertEquals(connection, agent4.getPosition().get());
        assertEquals(connection, agent5.getPosition().get());
        assertEquals(connection2, agent6.getPosition().get());
    }

    /*
    String
     */
    @Test
    public void testToString() {
        assertEquals("PD agent: Neighbor (no agent set): " + connection.toString(), agent1.toString());
    }
}