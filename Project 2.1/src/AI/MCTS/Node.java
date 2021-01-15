package AI.MCTS;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Node {

    private final State state;
    private Node parent;
    private List<Node> children = new ArrayList<>();
    private Player player;

    private Vertex attacker;
    private Vertex defender;

    private int visitCount;
    private int winScore;

    public Node(State state, Player player) {
        this.state = state;
        this.player = player;
        this.visitCount = 0;
    }

    public void setParent(Node node) {
        this.parent = node;
    }

    public void addChild(Node node) {
        this.children.add(node);
    }

    public List<Node> getChildren() {
        return children;
    }

    public Node getRandomChildNode() {
        Random rn = new Random();
        int random = rn.nextInt(children.size());
        return children.get(random);
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

    public Node getParent() {
        return parent;
    }

    public State getState() {
        return state;
    }

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

    public int getWinScore() { return winScore; }

    public void setWinScore(int i) { winScore = i; }

    public void addWinScore(int i) { winScore += i; }

    public void visit() {
        visitCount++;
    }

    public void setVisitCount(int visits) { visitCount = visits; }

    public int getVisitCount() { return visitCount; }

    public boolean isSimulated() { return visitCount != 0;}


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
