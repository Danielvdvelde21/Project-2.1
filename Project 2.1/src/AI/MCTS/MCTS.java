package AI.MCTS;

import AI.BasicBot.Components.UsefulMethods;
import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Edge;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Vertex;
import BackEndStructure.Simulation.SimulatedGameLoop;

import java.util.ArrayList;
import java.util.List;

public class MCTS extends UsefulMethods {

    private Graph originalGraph;
    private ArrayList<Player> order;
    private int maxIterations = 1000;
    // TODO UCT stuff

    //------------------------------------------------------------------------------------------------------------------
    // Move-maker

    public Graph findNextMove(Graph g, Player player, int playerAmount) {
        // New tree, new root
        int iteration=0;
        State originalState = new State(g, player);
        Node root = new Node(originalState);
        root.setVisitCount(1); //set the root as simulated so that we can
        Tree tree = new Tree(root);

        int end = 10000; //time limit

        // Update time properly
        long time = System.currentTimeMillis();

        while (System.currentTimeMillis() - time < end && iteration<maxIterations) {
            Node promisingNode = selectPromisingChild(root);

            if (promisingNode.isSimulated()) {
                expansion(g, player, promisingNode);
                promisingNode = promisingNode.getChildren().get(0);
            }

            Node simulationNode = promisingNode;

            //this is probably redundant since a promising node is always a leaf
            /*if (promisingNode.getChildren().size() > 0) {
                simulationNode = promisingNode.getRandomChildNode();
            }*/

            int playResult = playOut(playerAmount, simulationNode.getState().getGraph());
            backProp(simulationNode, playResult);

            iteration++;
        }

        Node winner = root.getMaxScoreChild();
        tree.setRoot(winner);
        return winner.getState().getGraph();
    }

    //------------------------------------------------------------------------------------------------------------------
    // Selection, Play-out, Expansion, Back propagation

    // Selection
    private Node selectPromisingChild(Node curNode) {
        while (curNode.getChildren().size() != 0) {
            curNode = UCT.findBestChildWithUCT(curNode);
        }
        return curNode;
    }


    public void setPlayerOrder(ArrayList<Player> players) {
        this.order = players;
    }

    // Amount of players, board
    public int playOut(int playerNo, Graph g) {
        // order has to be set somewhere
        SimulatedGameLoop game = new SimulatedGameLoop(g, order);
        System.out.println(game.getWinner());

        // TODO placeholder value, need to evaluate result
        int result = 10;
        return result;
    }

    // Add all the first possible moves or add 1 node to the bottom of the best node
    public void expansion(Graph g, Player p, Node node) {

        originalGraph = new Graph(g.getArrayList());
        ArrayList<Vertex> owned = getOwnedVertices(originalGraph, p);
        // For all owned territories select the ones that have another adjacent owned territory
        for (Vertex v : owned) {
            for (Edge e : v.getEdges()) {
                if (e.getVertex().getTerritory().getOwner() != p) {
                    Node n = new Node(new State(originalGraph, p));
                    n.setAttacker(v);
                    n.setDefender(e.getVertex());
                    node.addChild(n);
                }
            }
        }
    }

    // Adds scores to nodes from this simulation
    public void backProp(Node node, int result) {
        node.addWinScore(result);
        node.visit();
        if (node.getParent() != null) {
            backProp(node.getParent(), result);
        }
    }

}
