package ResourceAgent;

import com.github.rinde.rinsim.geom.Point;

public class Neighbor {

    private Point connection;
    private ResourceAgent agent;


    public Neighbor(Point connection)
            throws NullPointerException {
        if (connection == null) {
            throw new NullPointerException(
                    "NEIGHBOR | GIVEN CONNECTION EQUAL TO NULL"
            );
        }
        this.connection = connection;
    }

    /*
    CONNECTION
     */
    public boolean isValidConnection(Point aConnection) {
        return connection.equals(aConnection);
    }

    public Point getConnection() {
        return connection;
    }


    /*
    AGENT
     */
    public boolean agentSet() {
        return (agent != null);
    }

    public ResourceAgent getAgent() {
        if (agent == null) {
            System.out.println("NEIGHBOUR | REQUESTING AGENT THAT IS NOT BEEN SET YET");
            System.out.println(connection);
        }
        return agent;
    }

    public void setAgent(ResourceAgent agent) {
        if (agent == null) {
            System.out.println("SETTING AGENT TO NULL");
        } else if (this.agent != null) {
            System.out.println("NEIGHBOUR | SETTING AGENT WHILE NEIGHBOUR IS ALREADY SET");
            System.out.println(connection);
        }
        this.agent = agent;
    }

    @Override
    public String toString() {
        if (agent != null) {
            return "Neighbor " + agent.getClass() +" :"+ connection.toString();
        } else {
            return "Neighbor (no agent set): " + connection.toString();
        }
    }
}
