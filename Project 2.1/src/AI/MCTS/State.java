package AI.MCTS;

import BackEndStructure.Graph.Graph;

import java.util.ArrayList;
import java.util.List;

public class State {
    Graph graph;
    int playerNo;
    int visitCount;
    double winScore;

    // copy constructor, getters, and setters
    public State(Graph graph, int player) {
        this.graph = graph;
        this.playerNo = player;
        // When constructed, it hasn't been visited yet, so it hasn't resulted in a win yet either
        visitCount = 0;
        winScore = 0;
    }

    public State() {
        // When constructed, it hasn't been visited yet, so it hasn't resulted in a win yet either
        visitCount = 0;
        winScore = 0;
    }

    public Graph getGraph() {
        return graph;
    }

    public double getWinScore() {
        return winScore;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public int getPlayerNo() {
        return playerNo;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void setPlayerNo(int playerNo) {
        this.playerNo = playerNo;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public void setWinScore(double winScore) {
        this.winScore = winScore;
    }

    public void incrementVisit() {
        this.visitCount++;
    }

    public void addScore(int points) {
        this.winScore += points;
    }

    public List<State> getAllPossibleStates() {
        //TODO
        // Not sure if this will really be efficient, as the list of states will be immensely big
        // Since this would mainly be used for expansion, maybe just choose a random state to expand to?
        // Possible heuristics
        return new ArrayList<State>();
    }

    public void randomPlay() {
        // This is used for the simulation phase; choose a random move from all possible moves
    }

}
