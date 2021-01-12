package AI.MCTS;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Graph;

import java.util.ArrayList;
import java.util.List;

public class State {
    private final Graph graph;
    private final Player player;

    public State(Graph g, Player player) {
        this.graph = g;
        this.player = player;
    }

    public Graph getGraph() {
        return graph;
    }

    public Player getPlayer() {
        return player;
    }
}
