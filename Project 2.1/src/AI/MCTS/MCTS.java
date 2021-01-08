package AI.MCTS;

import AI.BasicBot.Components.UsefulMethods;
import BackEndStructure.Entities.Player;
import BackEndStructure.Game.MainGameLoop;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Vertex;

import java.util.ArrayList;

public class MCTS extends UsefulMethods {

    public Graph findNextMove(Graph g, Player player) {
        State originalState = new State(g, player);
        Node root = new Node(originalState);
        Tree tree = new Tree(root);

        int end = 30000; //time limit

        while (System.currentTimeMillis() < end) {
            Node promisingNode = selectPromisingNode(root);
            boolean expand = false; // should create a condition for when to expand
            if(expand) {
                expansion(promisingNode);
            }
            Node explorationNode = promisingNode;
            if (promisingNode.getChildren().size() > 0) {
                explorationNode = promisingNode.getRandomChildNode();
            }
            int playResult = playOut(1);
            backProp(explorationNode, playResult);
        }

        Node winner = root.getMaxScoreChild();
        tree.setRoot(winner);
        return winner.getState().getGraph();
    }

    // Selection
    private Node selectPromisingNode(Node rootNode) {
        Node node = rootNode;
        while (node.getChildren().size() != 0) {
            node = UCT.findBestNodeWithUCT(node);
        }
        return node;
    }

    // Adds all the possible attacks on the current graph to the parent node
    public void addPossibleAttacks(Graph g, Player p, Node parent) {
        ArrayList<Vertex> owned = getOwnedVertices(g, p);
        for (Vertex v : owned) {
            Node x = new Node(new State(g, p));
            parent.addChild(x);
        }
    }

    // TODO Play-out
    public int playOut(int playerNo) {
        String[] names = new String[playerNo];
        boolean[] bots = new boolean[playerNo];
        for (int i = 0; i < playerNo; i++) {
            names[i] = "" + i;
            bots[i] = true;
        }
        MainGameLoop game = new MainGameLoop(playerNo, names, bots, true);
        System.out.println(game.getWinner());

        // TODO placeholder value, need to evaluate result
        int result = 10;
        return result;
    }

    // TODO Expansion
    public void expansion(Node node) {
        // Use UCT for choosing which node to expand
    }

    // TODO back-propagation
    public void backProp(Node node, int result) {

    }


}
