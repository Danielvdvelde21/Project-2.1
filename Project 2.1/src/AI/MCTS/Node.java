package AI.MCTS;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Vertex;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private final State state;

    private Node parent;

    private Node[] children = new Node[25];
    private int childrenSize = 0;

    private Vertex attacker;
    private Vertex defender;

    private int visitCount = 0;
    private int winScore = 0;

    private boolean isTerminal = false;

    // Only used for getMaxChild
    public ArrayList<ArrayList<Node>> childrenForAverage = new ArrayList<>();

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
        if (childrenSize == children.length - 1) {
            Node[] children2 = new Node[children.length * 2];
            copyAintoB(children, children2);
            children = children2;
        }
        children[childrenSize] = node;
        childrenSize++;
    }

    private void copyAintoB(Node[] a, Node[] b) {
        for (int i = 0; i < a.length; i++) {
            b[i] = a[i];
        }
    }

    public Node[] getChildren() {
        return children;
    }

    public Node getMaxScoreChild() {
        double[] averages = new double[childrenForAverage.size()];
        int iterator = 0;
        for (ArrayList<Node> n : childrenForAverage) {
            int sum = 0;

            for (Node n1 : n) {
                sum += n1.winScore;
            }
            double average = (double) sum / (double) n.size();
            averages[iterator] = average;
            iterator++;
        }

        double maxAverage = averages[0];
        int indexMaxAverage = 0;
        for (int i = 0; i < averages.length; i++) {
            if (averages[i] > maxAverage) {
                maxAverage = averages[i];
                indexMaxAverage = i;
            }
        }

        return childrenForAverage.get(indexMaxAverage).get(0);
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
        assert (attacker != null);
        return attacker;
    }

    public Vertex getDefender() {
        assert (defender != null);
        return defender;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Scores
    public void addWinScore(int i) {
        winScore += i;
    }

    public int getWinScore() {
        return winScore;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Visiting

    public void visit() {
        visitCount++;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public boolean isSimulated() {
        return visitCount != 0;
    }

    public int getChildrenSize() {
        return childrenSize;
    }

    public void setChildrenSize(int childrenSize) {
        this.childrenSize = childrenSize;
    }

    // Terminating
    public void setTerminal() {
        isTerminal = true;
    }

    public boolean isTerminal() {
        return isTerminal;
    }
}
