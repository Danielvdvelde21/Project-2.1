package AI.MCTS;

public class Tree {
    Node root;

    public Tree(Node node) {
        this.root = node;
    }

    public Tree() {
        this.root = new Node();
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }
}
