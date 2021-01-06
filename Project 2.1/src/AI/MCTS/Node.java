package AI.MCTS;

import java.util.List;
import java.util.Random;

public class Node {
    State state;
    Node parent;
    List<Node> children;

    public Node(State state) {
        this.state = state;
    }
    public Node() {  this.state = new State(); }
    public Node(Node node) {
        this.state = node.getState();
        this.parent = node.getParent();
        this.children = node.getChildren();
    }

    public void setParent(Node node) {
        this.parent = node;
    }

    public void addChild(Node node) {
        children.add(node);
    }

    public Node getRandomChildNode() {
        Random rn = new Random();
        int random = rn.nextInt(children.size());
        return children.get(random);
    }

    public Node getMaxScoreChild() {
        int maxScore = 0;
        Node bestNode = children.get(0);
        for (Node n : children) {
            if (n.getState().getWinScore() > maxScore) {
                bestNode = n;
            }
        }
        return bestNode;
    }

    public List<Node> getChildren() {
        return children;
    }

    public Node getParent() {
        return parent;
    }

    public State getState() {  return state; }
}
