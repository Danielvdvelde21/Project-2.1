package AI.MCTS;

import java.util.List;

public class Node {
    State state;
    Node parent;
    List<Node> children;

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

    public Node getParent() {
        return parent;
    }
}
