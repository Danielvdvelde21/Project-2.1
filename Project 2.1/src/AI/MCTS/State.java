package AI.MCTS;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Graph;

import java.util.ArrayList;

public class State {

    private Graph graph;
    ArrayList<Player> order;

    public State(Graph g, ArrayList<Player> order) {
        this.graph = g;
        this.order = order;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public ArrayList<Player> getOrder() {
        return order;
    }

    public void setOrder(ArrayList<Player> order) {
        this.order = order;
    }
}
