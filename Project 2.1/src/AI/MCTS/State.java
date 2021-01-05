package AI.MCTS;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Graph;

import java.util.ArrayList;
import java.util.List;

public class State {
    private final Graph graph;
    private final int playerNo;
    private final Player player;

    private int visitCount;
    private double winScore;

    public State(Graph g, int playerNo, Player player) {
        this.graph = g;
        this.playerNo = playerNo;
        this.player = player;
        int visitCount = 1;
    }

    public List<State> getAllPossibleStates() {

        return new ArrayList<State>();
    }

    public Graph getGraph() {
        return graph;
    }

    public int getPlayerNo() {
        return playerNo;
    }

    public Player getPlayer() {
        return player;
    }

    public double getWinScore() {
        return winScore;
    }

    public void setWinScore(int i) {
        winScore = i;
    }

}
