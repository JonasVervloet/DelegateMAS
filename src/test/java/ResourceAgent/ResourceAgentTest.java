package ResourceAgent;

import com.github.rinde.rinsim.geom.Point;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResourceAgentTest {

    Point connection1;
    PDAgent agent1;
    PDAgent agent2;

    @Before
    public void init() {
        connection1 = new Point(5, 6);
        agent1 = new PDAgent(connection1, PDType.STORAGE);
        agent2 = new PDAgent(connection1, PDType.STORAGE);
    }

    /*
    Constructor
     */
    @Test
    public void testValidConstructor() {
        assertEquals(1, agent1.getScheduleManager().getCapacity());
        assertEquals(4, agent1.getScheduleManager().getTraversalTime());

        assertEquals(1, agent2.getScheduleManager().getCapacity());
        assertEquals(4, agent2.getScheduleManager().getTraversalTime());

        assertNotEquals(agent1.getResourceId(), agent2.getResourceId());

        assertEquals(0, agent1.getAgvManager().getNbOfAgvAgents());
    }
}