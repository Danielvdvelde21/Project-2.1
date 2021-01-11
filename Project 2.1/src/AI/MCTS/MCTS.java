package AI.MCTS;

import AI.BasicBot.Components.UsefulMethods;
import BackEndStructure.Entities.Player;
import BackEndStructure.Game.MainGameLoop;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Vertex;
import BackEndStructure.Simulation.SimulatedGameLoop;

import java.util.ArrayList;

public class MCTS extends UsefulMethods {

    private boolean isRoot = true;
    private Graph originalGraph;
    private ArrayList<Player> order;

    //------------------------------------------------------------------------------------------------------------------
    // Move-maker

    public Graph findNextMove(Graph g, Player player, int playerAmount) {
        State originalState = new State(g, player);
        Node root = new Node(originalState);
        Tree tree = new Tree(root);

        int end = 10000; //time limit

        while (System.currentTimeMillis() < end) {
            Node promisingNode = selectPromisingNode(root);

            // First iteration the node will be the root
            // Use UCT for choosing which node to expand
            expansion(g, player, promisingNode, isRoot);
            isRoot = false; // Next iteration won't consist of root node

            Node simulationNode = promisingNode;
            if (promisingNode.getChildren().size() > 0) {
                simulationNode = promisingNode.getRandomChildNode();
            }
            int playResult = playOut(playerAmount, simulationNode.getState().getGraph());
            backProp(simulationNode, playResult);
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

    public void setPlayerOrder(ArrayList<Player> players) {
        this.order = players;
    }

    // TODO Play-out
    // Amount of players, board
    public int playOut(int playerNo, Graph g) {
        // order has to be set somewhere
        SimulatedGameLoop game = new SimulatedGameLoop(g, order);
        System.out.println(game.getWinner());

        // TODO placeholder value, need to evaluate result
        int result = 10;
        return result;
    }

    // TODO
    // Add all the first possible moves or add 1 node to the bottom of the best node
    public void expansion(Graph g, Player p, Node node, boolean isRoot) {
        if (isRoot) {
            originalGraph = new Graph(g.getArrayList());
            ArrayList<Vertex> owned = getOwnedVertices(g, p);
            for (Vertex v : owned) {
                // TODO attack
                // TODO update graph
                Node x = new Node(new State(g, p));
                node.addChild(x);
            }
        } else {
            // TODO random attack
            // TODO update graph
            node.addChild(new Node(new State(g,p)));
        }
    }

    // TODO back-propagation
    public void backProp(Node node, int result) {

    }

    //------------------------------------------------------------------------------------------------------------------
    // Extra methods

}
