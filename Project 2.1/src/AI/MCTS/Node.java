package AI.MCTS;

import java.util.List;
import java.util.Random;

public class Node {
    private final State state;
    private Node parent;
    private List<Node> children;

    public Node(State state) {
        this.state = state;
    }

    public void setParent(Node node) {
        this.parent = node;
    }

    public void addChild(Node node) {
        children.add(node);
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
            if (n.getState().getWinScore() > maxscore) {
                maxNode = n;
                maxscore = n.getState().getWinScore();
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
}
