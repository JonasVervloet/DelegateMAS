package ResourceAgent;

import com.github.rinde.rinsim.geom.Point;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NeighborTest {

    private Point connection;
    private Point connection2;
    private Point connection3;
    private Neighbor neighbor;
    private PDAgent pdAgent1;
    private PDAgent pdAgent2;


    /*
    Initialization
     */
    @Before
    public void init() {
        connection = new Point(5, 6);
        connection2 = new Point(5, 6);
        connection3 = new Point(3, 4);
        neighbor = new Neighbor(connection);
        pdAgent1 = new PDAgent(connection, PDType.MACHINE_D);
        pdAgent2 = new PDAgent(connection, PDType.MACHINE_C);
    }

    /*
    Constructor
     */
    @Test
    public void validConstructor() {
        assertEquals(connection, neighbor.getConnection());
        assertFalse(neighbor.agentSet());
    }

    @Test (expected = NullPointerException.class)
    public void invalidConstructor() {
        new Neighbor(null);
    }

    /*
    Connection
     */
    @Test
    public void testIsValidConnection() {
        assert(neighbor.isValidConnection(connection2));
        assertFalse(neighbor.isValidConnection(connection3));
    }

    /*
    Agent
     */
    @Test (expected = IllegalStateException.class)
    public void matchesResourceIdNoAgentSet() {
        neighbor.matchesResourceId(123);
    }

    @Test (expected = IllegalStateException.class)
    public void getResourceIdNoAgentSet() {
        neighbor.getResourceId();
    }

    @Test(expected = IllegalStateException.class)
    public void getAgentNoAgentSet() {
        neighbor.getAgent();
    }

    @Test
    public void testSetAgent() {
        neighbor.setAgent(pdAgent1);
        assert(neighbor.agentSet());
        assert(neighbor.matchesResourceId(pdAgent1.getResourceId()));
        assertEquals(pdAgent1.getResourceId(), neighbor.getResourceId());
        assertEquals(pdAgent1, neighbor.getAgent());
    }

    @Test (expected = NullPointerException.class)
    public void testSetAgentNull() {
        neighbor.setAgent(null);
    }

    @Test (expected = IllegalStateException.class)
    public void testSetAgentTwice() {
        neighbor.setAgent(pdAgent1);
        neighbor.setAgent(pdAgent2);
    }

    /*
    String
     */
    @Test
    public void testToString() {
        assertEquals("Neighbor (no agent set): " + connection.toString(),
                neighbor.toString());
        neighbor.setAgent(pdAgent1);
        assertEquals("Neighbor " + pdAgent1.getClass() + ": " + connection.toString(),
                neighbor.toString());
    }
}