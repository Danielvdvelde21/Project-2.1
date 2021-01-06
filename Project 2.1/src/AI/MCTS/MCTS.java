package AI.MCTS;

import BackEndStructure.Graph.Graph;

import java.util.List;


// source of base structure: https://www.baeldung.com/java-monte-carlo-tree-search

public class MCTS {
    final int WIN_SCORE = 10; //value given to a win
    int level;
    int opponent;

    public Graph findNextMove(Graph graph, int playerNo) {
        // need to define an endtime, either a depth or time in seconds
        // for now, we'll make it time
        int end = 20000; // in ms

        opponent = 3 - playerNo; // Needs to be adjusted, basically needs to be the player whose turn is next
        Tree tree = new Tree();
        Node root = tree.getRoot();
        root.getState().setGraph(graph);
        root.getState().setPlayerNo(opponent);

        while (System.currentTimeMillis() < end) {
            Node promisingNode = selectPromisingNode(root);
            // this boolean is to check whether we need to expand
            boolean expand = false;
            if (expand) {
                expandNode(promisingNode);
            }
            Node exploreNode = promisingNode;
            // You always want to simulate from a leaf node, so go deeper
            // Might have to be a while loop for deeper trees?
            if (promisingNode.getChildren().size() > 0) {
                exploreNode = promisingNode.getRandomChildNode();
            }
            int playoutResult = simulateRandomPlayout(exploreNode);
            backPropogate(exploreNode, playoutResult);
        }
        Node winnerNode = root.getMaxScoreChild();
        tree.setRoot(winnerNode);
        return winnerNode.getState().getGraph();
    }

    private Node selectPromisingNode(Node root) {
        Node node = root;
        while (node.getChildren().size() != 0) {
            node = UCT.findBestNodeWithUCT(node);
        }
        return node;
    }

    // TODO: current expansion would add all possible states to the tree
    // want to find something else
    private void expandNode(Node node) {
        List<State> possibleStates = node.getState().getAllPossibleStates();
        possibleStates.forEach(state -> {
            int opponent = 2; // this needs to be the next player whose turn it is
            Node newNode = new Node(state);
            newNode.setParent(node);
            newNode.getState().setPlayerNo(opponent);
            node.getChildren().add(newNode);
        });
    }

    // TODO backpropogation
    private void backPropogate(Node nodeToExplore, int playerNo) {
        Node tempNode = nodeToExplore;
        while (tempNode != null) {
            tempNode.getState().incrementVisit();
            if (tempNode.getState().getPlayerNo() == playerNo) {
                tempNode.getState().addScore(WIN_SCORE);
            }
            tempNode = tempNode.getParent();
        }
    }

    // TODO simulation
    private int simulateRandomPlayout(Node node) {
        Node tempNode = new Node(node);
        State tempState = tempNode.getState();
        // TODO finish simulation
        return 0; // placeholder return statement
    }
}
