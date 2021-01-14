package AI.MCTS;

import AI.BasicBot.Components.UsefulMethods;
import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Edge;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Vertex;
import BackEndStructure.Simulation.SimulatedGameLoop;

import java.awt.*;
import java.util.ArrayList;

public class MCTS extends UsefulMethods {

    private final int maxIterations = 1000;
    private ArrayList<Player> copiedOrder = new ArrayList<>();

    //------------------------------------------------------------------------------------------------------------------
    // Move-maker

    public Vertex[] findNextMove(Graph g, ArrayList<Player> order) {
        // Deep copy player order
        for (Player p : order) {
            copiedOrder.add(new Player(p.getName(), p.getColor(), p.isBot(),p.isMCTSBot()));
        }

        // First player in the copiedOrder is the MCTS bot
        Player playerMCTS = copiedOrder.get(0);

        // Construct the root node
        Node root = new Node(new State(g, playerMCTS));
        root.setVisitCount(1); // Sets the root as simulated

        Tree tree = new Tree(root);

        // Time limit or iteration limit
        long time = System.currentTimeMillis();
        int end = 2500; // Time limit

        int iteration=0;
        while (System.currentTimeMillis() - time < end && iteration<maxIterations) {
            // Selection
            Node promisingNode = selectPromisingChild(root);

            // Expansion
            if (promisingNode.isSimulated()) {
                expansion(g, playerMCTS, promisingNode);
                promisingNode = promisingNode.getChildren().get(0);
            }

            // Play out (simulation
            Node simulationNode = promisingNode;
            int playResult = playOut(simulationNode.getState().getGraph(), order, simulationNode);

            // Backpropagation
            backProp(simulationNode, playResult);
            iteration++;
        }

        Node winner = root.getMaxScoreChild();
        tree.setRoot(winner);

        return new Vertex[]{winner.getAttacker(), winner.getDefender()};
    }

    //------------------------------------------------------------------------------------------------------------------
    // Selection

    // Selection considers all leaves
    private Node selectPromisingChild(Node curNode) {
        while (curNode.getChildren().size() != 0) {
            curNode = UCT.findBestChildWithUCT(curNode);
        }
        return curNode;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Play-out

    // Simulates a game
    private int playOut(Graph g, ArrayList<Player> order, Node node) {
        // Fix player order for simulated game
        ArrayList<Player> simulatedPlayerOrder = changeOrder(copiedOrder, node);

        // Simulate game
        SimulatedGameLoop game = new SimulatedGameLoop(g, simulatedPlayerOrder, new Vertex[]{node.getAttacker(), node.getDefender()});
        // TODO return winner
        return analyzeGame(game, node.getState().getPlayer());
    }

    // Change order for simulated game such that MCTS bot in node starts
    private ArrayList<Player> changeOrder(ArrayList<Player> order, Node node) {
        while(!(order.get(0) == node.getState().getPlayer())) {
            Player temp = order.get(0);
            order.remove(temp);
            order.add(temp);
        }
        return order;
    }

    // Evaluate the current graph and assigns points to the node
    private int analyzeGame(SimulatedGameLoop game, Player player) {
        int score = 0;
        if (game.getWinner() == player) {
            score += 100;
        }
        score += player.getTerritoriesOwned();
        score += player.getContinentsOwned().size() * 5;
        // score += troopsOwned * 0.1;
        return score;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Expansion

    // Add all the first possible moves or add 1 node to the bottom of the best node
    private void expansion(Graph g, Player p, Node node) {
        Graph copiedGraph = new Graph(g.getArrayList());
        ArrayList<Vertex> owned = getOwnedVertices(copiedGraph, p);

        // For all owned territories select the ones that have another adjacent owned territory
        for (Vertex v : owned) {
            for (Edge e : v.getEdges()) {
                if (e.getVertex().getTerritory().getOwner() != p) {
                    Node n = new Node(new State(copiedGraph, p));
                    n.setAttacker(v);
                    n.setDefender(e.getVertex());
                    node.addChild(n);
                }
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // Back propagation

    // Adds scores to nodes from this simulation
    private void backProp(Node node, int result) {
        node.addWinScore(result);
        node.visit();
        if (node.getParent() != null) {
            backProp(node.getParent(), result);
        }
    }

}
