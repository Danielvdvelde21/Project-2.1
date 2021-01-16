package AI.MCTS;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Edge;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Territories;
import BackEndStructure.Graph.Vertex;
import BackEndStructure.Simulation.SimulatedGameLoop;

import java.util.ArrayList;

public class MCTS {

    // Variables that determine the maximum time or iterations the bot has
    private final long maxTime = 10000; // 10 seconds
    private final int maxIterations = 10000;

    // Copied variables
    private Graph copiedGraph;
    private final ArrayList<Player> copiedOrder = new ArrayList<>();

    //------------------------------------------------------------------------------------------------------------------
    // Move-maker

    public Vertex[] findNextMove(Graph g, ArrayList<Player> order, Player p) {
        // Construct the root node
        State rootState = deepCopyState(g, order);
        Node root = new Node(rootState, p);
        root.setVisitCount(1); // Sets the root as simulated // TODO make sure it works!

        // The tree with assigned root
        Tree tree = new Tree(root);

        // Initialize duration variable
        long beginTimer = System.currentTimeMillis();
        int iteration = 0;

        while (System.currentTimeMillis() - beginTimer < maxTime && iteration < maxIterations) {
            // Selection
            Node promisingNode = selectPromisingChild(root);

            // Expansion
            if (promisingNode.isSimulated()) {
                expansion(promisingNode);
                promisingNode = promisingNode.getChildren().get(0);
            }

            // Play out (simulation)
            Node simulationNode = promisingNode;
            int playResult = playOut(simulationNode);

            // Backpropagation
            backProp(simulationNode, playResult);
            iteration++;
        }

        /* Time tracking code
        long ft = System.currentTimeMillis() - beginTimer;
        System.out.println("total time:" + ft);

        System.out.println("time per iteration:" + (double) ft / (double) maxIterations + "ms");
        int hz = (int) (1000.0 / ((double) ft / (double) maxIterations));
        System.out.println("iterations per second:" + hz);
         */

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
    private int playOut(Node node) {
        // Simulate game
        SimulatedGameLoop game = new SimulatedGameLoop(node.getState());
        if (game.getWinner() == node.getState().getOrder().get(0)) {
            return 10;
        }
        return 0;
    }

    private State deepCopyState(Graph g, ArrayList<Player> order) {
        // Deep copy graph
        Territories t = new Territories();
        Graph newGraph = t.getGraph();

        for (int i = 0; i < newGraph.getSize(); i++) {
            newGraph.get(i).getTerritory().setOwner(g.get(i).getTerritory().getOwner());
            newGraph.get(i).getTerritory().setNumberOfTroops(g.get(i).getTerritory().getNumberOfTroops());
        }

        // Deep copy players and update graph
        ArrayList<Player> newOrder = deepCopyPlayers(order, newGraph);

        return new State(newGraph, newOrder);
    }

    private ArrayList<Player> deepCopyPlayers(ArrayList<Player> order, Graph g) {
        ArrayList<Player> newOrder = new ArrayList<>();
        for (Player p : order) {
            Player newPlayer = p.clone();
            newOrder.add(newPlayer);
            for (Vertex v : g.getArrayList()) {
                if (v.getTerritory().getOwner() == p) {
                    v.getTerritory().setOwner(newPlayer);
                    newPlayer.getOwnedTerritories().add(v);
                }
            }
        }
        return newOrder;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Expansion

    // Add all the first possible moves or add 1 node to the bottom of the best node
    private void expansion(Node node) {
        System.out.println("expansion");
        Graph g = node.getState().getGraph();
        ArrayList<Player> order = node.getState().getOrder();

        // Move first player to back of the order
        Player temp = order.remove(0);
        order.add(temp);

        // Select bot player
        Player botPlayer = node.getPlayer();

        // pruning: defender outnumbers attack
        ArrayList<Vertex> owned = botPlayer.getOwnedTerritories();
        for (Vertex v : owned) {
            for (Edge e : v.getEdges()) {
                if (e.getVertex().getTerritory().getOwner() != node.getPlayer()) {
                    System.out.println("loop expansion");
                    addAllPossibleStates(g, v.getTerritory().getTerritoryNumber(), e.getVertex().getTerritory().getTerritoryNumber(), node, order, botPlayer);
                }
            }
        }
    }

    // Adds all possible states from a certain state given an attacker and defender and adds them as children
    private void addAllPossibleStates(Graph g, int attackerIndex, int defenderIndex, Node leaf, ArrayList<Player> order, Player player) {
        // Generate wins
        // Attacker has >= 1 troops, defender has >=1 troops and is now owned by attacker, attacker + defender troops = 2 - total attacking troops
        for (int i = 1; i < g.get(attackerIndex).getTerritory().getNumberOfTroops(); i++) {
            for (int j = g.get(attackerIndex).getTerritory().getNumberOfTroops(); j > 0; j--) {
                if (j - i > 0) {
                    // I'm not sure if this is the right way to make the copies, so check for errors
                    // New state has updated troop counts and players territoriesOwned changes
                    State newState = deepCopyState(g, order);
                    newState.getGraph().get(attackerIndex).getTerritory().setNumberOfTroops(i);
                    newState.getGraph().get(attackerIndex).getTerritory().getOwner().increaseTerritoriesOwned();
                    // TODO update ownedVertices in player
                    newState.getGraph().get(defenderIndex).getTerritory().setNumberOfTroops(j - i);
                    newState.getGraph().get(defenderIndex).getTerritory().getOwner().decreaseTerritoriesOwned();
                    Node newNode = new Node(newState, player);
                    leaf.addChild(newNode);
                    newNode.setParent(leaf);
                    //System.out.println("loop " + i + " and " + j);
                } else {
                    // when j - i is 0 or smaller, we can stop the inner loop
                    break;
                }
            }
        }

        // Generate losses
        // Attacker has only 1 troop left, defender has anywhere between 1 and total defenders left
        for (int i = 1; i < g.get(defenderIndex).getTerritory().getNumberOfTroops(); i++) {
            //Create deep copy of state
            State newState = deepCopyState(g, order);

            newState.getGraph().get(attackerIndex).getTerritory().setNumberOfTroops(1);
            newState.getGraph().get(defenderIndex).getTerritory().setNumberOfTroops(i);

            Node newNode = new Node(newState, player);
            leaf.addChild(newNode);
            newNode.setParent(leaf);
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
