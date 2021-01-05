package AI.MCTS;

import BackEndStructure.Graph.Graph;

import java.util.ArrayList;
import java.util.List;

public class State {
    Graph board;
    int playerNo;
    int visitCount;
    double winScore;

    //TODO

    // copy constructor, getters, and setters

    public List<State> getAllPossibleStates() {

        return new ArrayList<State>();
    }
    public void randomPlay() {
        /* get a list of all possible positions on the board and
           play a random move */
    }
}
