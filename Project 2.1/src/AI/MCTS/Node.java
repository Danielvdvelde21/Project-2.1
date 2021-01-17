package AI.MCTS;

import BackEndStructure.Graph.Vertex;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private final State state;

    private Node parent;

    private Node [] children = new Node [25];
    private int childrenSize = 0;

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
        Node[]children2 = new Node[0];
        if(childrenSize==children.length-10){
            children2=new Node[children.length*2];
            copyAintoB(children,children2);
            children=children2;
        }
        children[childrenSize]=node;
        childrenSize++;
    }

    private void copyAintoB(Node[]a,Node[]b){
        for (int i=0;i<a.length;i++) {
            b[i]=a[i];
        }
    }

    public Node[] getChildren() {
        return children;
    }

    public Node getMaxScoreChild() {
        int maxscore = 0;
        Node maxNode = children[0];
        for (int i=0;i<childrenSize;i++) {
            Node n=children[i];
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

    public int getChildrenSize() {
        return childrenSize;
    }

    public void setChildrenSize(int childrenSize) {
        this.childrenSize = childrenSize;
    }
}
