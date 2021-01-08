package AI.MCTS;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Graph;

import java.util.ArrayList;
import java.util.List;

public class State {
    private final Graph graph;
    private final Player player;

    private int visitCount;
    private int winScore;

    public State(Graph g, Player player) {
        this.graph = g;
        this.player = player;
        int visitCount = 1;
    }

    // TODO Is this all the posible outcomes from a battle?
    public List<State> getAllPossibleStates() {
        return new ArrayList<State>();
    }

    public Graph getGraph() {
        return graph;
    }

    public Player getPlayer() {
        return player;
    }

    public int getWinScore() { return winScore; }

    public void setWinScore(int i) { winScore = i; }

    public void visit() {
        visitCount++;
    }

}
