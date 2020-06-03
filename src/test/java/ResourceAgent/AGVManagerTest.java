package ResourceAgent;

import AGVAgent.AGVAgent;
import ResourceAgent.Schedule.ScheduleManager;
import ResourceAgent.Schedule.ScheduleRequest;
import com.github.rinde.rinsim.geom.Point;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AGVManagerTest {

//    private AGVAgent agvAgent1;
//    private AGVAgent agvAgent2;
//    private ScheduleManager scheduleManager;
//    private AGVManager agvManager;
//
//
//    @Before
//    public void init() {
//        agvAgent1 = new AGVAgent();
//        agvAgent2 = new AGVAgent();
//
//        scheduleManager = new ScheduleManager(2, 3);
//        scheduleManager.makeReservation(
//                new ScheduleRequest(0, 4, agvAgent1.getAgvId(), 987)
//        );
//        scheduleManager.makeReservation(
//                new ScheduleRequest(2, 8, agvAgent2.getAgvId(), 654)
//        );
//
//        agvManager = new AGVManager();
//        agvManager.setScheduleManager(scheduleManager);
//    }
//
//
//    /*
//    Register AGV Agent
//     */
//    @Test
//    public void registerValidAGVAgent() {
//        agvManager.registerAGVAgent(agvAgent1);
//
//        assertEquals(1, agvManager.getNbOfAgvAgents());
//        assertEquals(agvAgent1, agvManager.getAGVAgent(agvAgent1.getAgvId()));
//    }
//
//    @Test (expected = IllegalArgumentException.class)
//    public void registerInvalidAGVAgent() {
//        agvManager.registerAGVAgent(agvAgent2);
//    }
//
//    /*
//    Unregister AGV Agent
//     */
//    @Test
//    public void unregisterValidAGVAgent() {
//        agvManager.registerAGVAgent(agvAgent1);
//        assertEquals(1, agvManager.getNbOfAgvAgents());
//
//        agvManager.unregisterAGVAgent(agvAgent1.getAgvId());
//        assertEquals(0, agvManager.getNbOfAgvAgents());
//    }
//
//    @Test (expected = IllegalArgumentException.class)
//    public void unregisterInvalidAGVAgent() {
//        agvManager.registerAGVAgent(agvAgent1);
//        assertEquals(1, agvManager.getNbOfAgvAgents());
//
//        agvManager.unregisterAGVAgent(agvAgent2.getAgvId());
//    }
}