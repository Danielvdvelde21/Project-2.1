package AI.MCTS;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Edge;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Territories;
import BackEndStructure.Graph.Vertex;
import BackEndStructure.Simulation.SimulatedGameLoop;

import java.util.ArrayList;

public class MCTS {

    private final int maxIterations = 10000;

    private Graph copiedGraph;
    private final ArrayList<Player> copiedOrder = new ArrayList<>();

    private Player MCTSPlayer;

    //------------------------------------------------------------------------------------------------------------------
    // Move-maker

    public Vertex[] findNextMove(Graph g, ArrayList<Player> order, Player p) {
        // Deep copy graph, players and update graph
        State rootState = deepCopyState(g, order);
        State duplicate = deepCopyState(g,order);

        // Assign MCTS player, must be first player in order!
        //this.MCTSPlayer = copiedOrder.get(0);

        // Construct the root node
        Node root = new Node(rootState, p);
        root.setVisitCount(1); // Sets the root as simulated

        Tree tree = new Tree(root);

        // Time limit or iteration limit
        long time = System.currentTimeMillis();
        int end = 2500; // Time limit

        int iteration = 0;
        long start = System.currentTimeMillis();
        while (iteration < maxIterations) {
             duplicate = deepCopyState(rootState.getGraph(),rootState.getOrder());
             root = new Node(duplicate, p);
            // Selection
            Node promisingNode = root;

            // Expansion
            /*if (promisingNode.isSimulated()) {
                expansion(promisingNode);
                promisingNode = promisingNode.getChildren().get(0);
            }*/

            // Play out (simulation)
            Node simulationNode = promisingNode;

            int playResult = playOut(simulationNode.getState().getGraph(), simulationNode);

            // Backpropagation
            //backProp(simulationNode, playResult);
            iteration++;
            if (iteration%100 == 0) {
                System.out.println(iteration);
            }
        }
        long ft=System.currentTimeMillis()-start;
        System.out.println("total time:" +ft);

        System.out.println("time per iteration:" +(double)ft/(double)maxIterations+"ms");
        int hz=(int)(1000.0/((double)ft/(double)maxIterations));
        System.out.println("iterations per second:" +hz);

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
    private int playOut(Graph g, Node node) {
        // Fix player order for simulated game
        //ArrayList<Player> simulatedPlayerOrder = changeOrder(node);

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

    // Change order for simulated game such that MCTS bot in node starts
    /*private ArrayList<Player> changeOrder(Node node) {
        while (!(copiedOrder.get(0) == node.getState().getPlayer())) {
            Player temp = copiedOrder.get(0);
            copiedOrder.remove(temp);
            copiedOrder.add(temp);
        }
        return copiedOrder;
    }*/

    //------------------------------------------------------------------------------------------------------------------
    // Expansion

    // Add all the first possible moves or add 1 node to the bottom of the best node
    private void expansion(Node node) {
        System.out.println("expansion");
        Graph g = node.getState().getGraph();
        ArrayList<Player> newOrder = deepCopyPlayers(node.getState().getOrder(), g);
        // Move first player to back of the order
        Player temp = newOrder.remove(0);
        newOrder.add(temp);
        Player botPlayer = node.getPlayer();

        // pruning: defender outnumbers attack
        ArrayList<Vertex> owned = getOwnedVertices(g, node.getPlayer());
        for (Vertex v : owned) {
            for (Edge e : v.getEdges()) {
                if (e.getVertex().getTerritory().getOwner() != node.getPlayer()) {
                    System.out.println("loop expansion");
                    addAllPossibleStates(g, v.getTerritory().getTerritoryNumber(), e.getVertex().getTerritory().getTerritoryNumber(), node, newOrder, botPlayer);
                }
            }
        }
    }

    // Adds all possible states from a certain state given an attacker and defender and adds them as children
    private void addAllPossibleStates(Graph g, int attackerIndex, int defenderIndex, Node leaf, ArrayList<Player> order, Player player) {
        State newState;
        // Generate wins
        // Attacker has >= 1 troops, defender has >=1 troops and is now owned by attacker, attacker + defender troops = 2 - total attacking troops
        for (int i = 1; i < g.get(attackerIndex).getTerritory().getNumberOfTroops(); i++) {
            for (int j = g.get(attackerIndex).getTerritory().getNumberOfTroops(); j > 0; j--) {
                if (j - i > 0) {
                    // I'm not sure if this is the right way to make the copies, so check for errors
                    // New state has updated troop counts and players territoriesOwned changes
                    newState = deepCopyState(g, order);
                    newState.getGraph().get(attackerIndex).getTerritory().setNumberOfTroops(i);
                    newState.getGraph().get(attackerIndex).getTerritory().getOwner().increaseTerritoriesOwned();

                    newState.getGraph().get(defenderIndex).getTerritory().setNumberOfTroops(j - i);
                    newState.getGraph().get(defenderIndex).getTerritory().getOwner().decreaseTerritoriesOwned();

                    leaf.addChild(new Node(newState, player));
                    //System.out.println("loop " + i + " and " + j);
                }
                else {
                    // when j - i is 0 or smaller, we can stop the inner loop
                    break;
                }
            }
        }

        // Generate losses
        // Attacker has only 1 troop left, defender has anywhere between 1 and total defenders left
        for (int i = 1; i < g.get(defenderIndex).getTerritory().getNumberOfTroops(); i++) {
            //Create deep copy of state
            newState = deepCopyState(g, order);

            newState.getGraph().get(attackerIndex).getTerritory().setNumberOfTroops(1);
            newState.getGraph().get(defenderIndex).getTerritory().setNumberOfTroops(i);

            leaf.addChild(new Node(newState, player));
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

    //------------------------------------------------------------------------------------------------------------------
    // Extra methods

    // Get all owned vertices for a player
    public ArrayList<Vertex> getOwnedVertices(Graph g, Player p) {
        ArrayList<Vertex> verticesOwned = new ArrayList<>();

        for (int i = 0; i < g.getSize(); i++) {
            if (g.get(i).getTerritory().getOwner() == p) {
                verticesOwned.add(g.get(i));
            }
        }
        return verticesOwned;
    }

}
