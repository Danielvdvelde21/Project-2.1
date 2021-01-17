package AI.MCTS;

import BackEndStructure.Graph.Vertex;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private final State state;

    private Node parent;
    private final List<Node> children = new ArrayList<>();

    private Vertex attacker;
    private Vertex defender;

    private int visitCount = 0;
    private int winScore = 0;

    //------------------------------------------------------------------------------------------------------------------
    // Constructor
    public Node(State state) {
        this.state = state;
    }

    //------------------------------------------------------------------------------------------------------------------
    // State
    public State getState() {
        return state;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Parents
    public void setParent(Node node) {
        this.parent = node;
    }

    public Node getParent() {
        return parent;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Child
    public void addChild(Node node) {
        this.children.add(node);
    }

    public List<Node> getChildren() {
        return children;
    }

    public Node getMaxScoreChild() {
        int maxscore = 0;
        Node maxNode = children.get(0);
        for (Node n : children) {
            if (n.getWinScore() > maxscore) {
                maxNode = n;
                maxscore = n.getWinScore();
            }
        }
        return maxNode;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Attacker and defender

    public void setAttacker(Vertex attacker) {
        this.attacker = attacker;
    }

    public void setDefender(Vertex defender) {
        this.defender = defender;
    }

    public Vertex getAttacker() {
        assert(attacker != null);
        return attacker;
    }

    public Vertex getDefender() {
        assert(defender != null);
        return defender;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Scores
    public void addWinScore(int i) { winScore += i; }

    public int getWinScore() { return winScore; }

    //------------------------------------------------------------------------------------------------------------------
    // Visiting

    public void visit() {
        visitCount++;
    }

    public int getVisitCount() { return visitCount; }

    public boolean isSimulated() { return visitCount != 0;}

}
