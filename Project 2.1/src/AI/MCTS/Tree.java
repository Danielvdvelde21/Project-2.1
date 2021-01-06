package AI.MCTS;

public class Tree {
    private Node root;

    public Tree(Node node) {
        root = node;
    }

    public Node getRoot() {
        return root;
    }
}
