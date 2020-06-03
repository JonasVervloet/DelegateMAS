package ResourceAgent;

import com.github.rinde.rinsim.geom.Point;

public class Neighbor {

    /*
    The point that connects the resource agent that
        manages this neighbor object with the resource
        agent that is stored in this neighbor object.
     */
    private Point connection;

    /*
    The resource agent that is connected with the
        resource agent that manages this neighbor
        object through the point that is stored by
        this neighbor object.
     */
    private ResourceAgent agent;


    /*
    Constructor
     */
    public Neighbor(Point connection)
            throws NullPointerException {
        if (connection == null) {
            throw new NullPointerException(
                    "NEIGHBOR | GIVEN CONNECTION EQUALS TO NULL"
            );
        }
        this.connection = connection;
    }

    /*
    Connection
     */
    public boolean isValidConnection(Point aConnection) {
        return connection.equals(aConnection);
    }

    public Point getConnection() {
        return connection;
    }


    /*
    Agent
     */
    public boolean agentSet() {
        return (agent != null);
    }

    public boolean matchesResourceId(int aResourceId)
            throws IllegalStateException {
        if (! agentSet()) {
            throw new IllegalStateException(
                    "NEIGHBOR | THIS NEIGHBOR DOES NOT YET CONTAIN A RESOURCE AGENT"
            );
        }
        return getResourceId() == aResourceId;
    }

    public int getResourceId() throws IllegalStateException {
        if (! agentSet()) {
            throw new IllegalStateException(
                    "NEIGHBOR | THIS NEIGHBOR DOES NOT YET CONTAIN A RESOURCE AGENT"
            );
        }
        return agent.getResourceId();
    }

    public ResourceAgent getAgent()
            throws IllegalStateException {
        if (! agentSet()) {
            throw new IllegalStateException(
                    "NEIGHBOR |THIS NEIGHBOR DOES NOT YET CONTAIN A RESOURCE AGENT"
            );
        }
        return agent;
    }

    public void setAgent(ResourceAgent agent)
            throws NullPointerException, IllegalStateException {
        if (agent == null) {
            throw new NullPointerException(
                    "NEIGHBOR | THE GIVEN RESOURCE AGENT EQUALS NULL"
            );
        } else if (this.agent != null) {
            throw new IllegalStateException(
                    "NEIGHBOR | THIS NEIGHBORS RESOURCE AGENT IS ALREADY SET"
            );
        }
        this.agent = agent;
    }

    /*
    String
     */
    @Override
    public String toString() {
        if (agentSet()) {
            return "Neighbor " + getAgent().getClass() +": "+ getConnection().toString();
        } else {
            return "Neighbor (no agent set): " + getConnection().toString();
        }
    }
}
