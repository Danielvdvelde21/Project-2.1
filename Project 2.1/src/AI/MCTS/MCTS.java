package AI.MCTS;

import AI.BasicBot.Components.UsefulMethods;
import BackEndStructure.Entities.Player;
import BackEndStructure.Game.MainGameLoop;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Vertex;

import java.util.ArrayList;

public class MCTS extends UsefulMethods {

    private boolean isRoot = true;

    //------------------------------------------------------------------------------------------------------------------
    // Move-maker

    public Graph findNextMove(Graph g, Player player) {
        State originalState = new State(g, player);
        Node root = new Node(originalState);
        Tree tree = new Tree(root);

        int end = 30000; //time limit

        while (System.currentTimeMillis() < end) {
            Node promisingNode = selectPromisingNode(root);

            // First iteration the node will be the root
            // Use UCT for choosing which node to expand
            expansion(g, player, promisingNode, isRoot);
            isRoot = false; // Next iteration won't consist of root node

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

    //------------------------------------------------------------------------------------------------------------------
    // Selection, Play-out, Expansion, Back propagation

    // Selection
    private Node selectPromisingNode(Node rootNode) {
        Node node = rootNode;
        while (node.getChildren().size() != 0) {
            node = UCT.findBestNodeWithUCT(node);
        }
        return node;
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

    // TODO IDK IF THIS WORKS KEKW
    // Add all the first possible moves or add 1 node to the bottom of the best node
    public void expansion(Graph g, Player p, Node node, boolean isRoot) {
        if (isRoot) {
            ArrayList<Vertex> owned = getOwnedVertices(g, p);
            for (Vertex v : owned) {
                Node x = new Node(new State(g, p));
                node.addChild(x);
            }
        } else {
            node.addChild(new Node(new State(g,p)));
        }
    }

    // TODO back-propagation
    public void backProp(Node node, int result) {

    }

    //------------------------------------------------------------------------------------------------------------------
    // Extra methods

}
