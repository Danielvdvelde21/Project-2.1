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
    private final long maxTime = 20000; // Milliseconds
    private final int maxIterations = 100000; // Attacks

    //------------------------------------------------------------------------------------------------------------------
    // Move-maker

    public Vertex[] findNextMove(Graph g, ArrayList<Player> order, Player p) {
        // Construct the root node
        State rootState = deepCopyState(g, order, p, false);
        Node root = new Node(rootState);
        root.visit(); // Sets the root as simulated

        // Initialize duration variable
        long beginTimer = System.currentTimeMillis();
        int iteration = 0;

        while (System.currentTimeMillis() - beginTimer < maxTime && iteration < maxIterations) {
            // Selection
            Node promisingNode = selectPromisingChild(root);

            // Expansion
            if (promisingNode.isSimulated()) {
                expansion(promisingNode);
                if (promisingNode.getChildren().size() == 0) {
                    throw new RuntimeException("Expansion error! children not added!");
                }
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

        // The vertices from the winner node are not the same objects as the vertices in the original graph
        // So we do some name detection and make sure we return vertices that are also in the original graph
        Node winner = root.getMaxScoreChild();

        Vertex[] returner = new Vertex[2];
        String attackerTerritoryName = winner.getAttacker().getTerritory().getTerritoryName();
        String defenderTerritoryName = winner.getDefender().getTerritory().getTerritoryName();

        for (int i = 0; i < g.getSize(); i++) {
            if (g.get(i).getTerritory().getTerritoryName().equals(attackerTerritoryName)) {
                returner[0] = g.get(i);
            }
            if (g.get(i).getTerritory().getTerritoryName().equals(defenderTerritoryName)) {
                returner[1] = g.get(i);
            }
        }
        // Make sure the territories are the same
        if (!g.getArrayList().contains(returner[0]) || !g.getArrayList().contains(returner[1])) {
            throw new RuntimeException("Duplicated vertices are returned");
        }
        return returner;
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
        Node simNode = new Node(deepCopyState(node.getState().getGraph(), node.getState().getOrder(), node.getState().getPlayerMCTS(), true));
        SimulatedGameLoop game = new SimulatedGameLoop(simNode.getState());
        if (game.getWinner() == simNode.getState().getPlayerMCTS()) {
            return 10;
        }
        return 0;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Expansion

    // Add all the first possible moves or add 1 node to the bottom of the best node
    private void expansion(Node expandNode) {
        // Move first player to back of the order
        ArrayList<Player> order = expandNode.getState().getOrder();
        Player currentMovePlayer = order.remove(0);
        order.add(currentMovePlayer);

        // pruning: defender outnumbers attack
        ArrayList<Vertex> owned = currentMovePlayer.getOwnedTerritories();
        for (Vertex v : owned) {
            if (v.getTerritory().getNumberOfTroops() > 1) {
                for (Edge e : v.getEdges()) {
                    if (e.getVertex().getTerritory().getOwner() != currentMovePlayer) {
                        addAllPossibleStates(v.getTerritory().getTerritoryNumber(), e.getVertex().getTerritory().getTerritoryNumber(), expandNode);
                    }
                }
            }
        }
    }

    // Adds all possible states from a certain state given an attacker and defender and adds them as children
    private void addAllPossibleStates(int attackerIndex, int defenderIndex, Node expandNode) {
        Graph g = expandNode.getState().getGraph();
        ArrayList<Player> order = expandNode.getState().getOrder();
        Player botPlayer = expandNode.getState().getPlayerMCTS();

        // Generate wins
        // Attacker has >= 1 troops, defender has >=1 troops and is now owned by attacker, attacker + defender troops = 2 - total attacking troops
        for (int i = 1; i < g.get(attackerIndex).getTerritory().getNumberOfTroops(); i++) {
            for (int j = g.get(attackerIndex).getTerritory().getNumberOfTroops(); j > 0; j--) {
                if (j - i > 0) {
                    // Deep copy state
                    State newState = deepCopyState(g, order, botPlayer, false);

                    Player attacker = newState.getGraph().get(attackerIndex).getTerritory().getOwner();
                    Player defender = newState.getGraph().get(defenderIndex).getTerritory().getOwner();
                    Vertex attackingVertex = newState.getGraph().get(attackerIndex);
                    Vertex defendingVertex = newState.getGraph().get(defenderIndex);

                    // Update territory attacker
                    attackingVertex.getTerritory().setNumberOfTroops(i);
                    // Update attacker
                    defendingVertex.getTerritory().setOwner(attacker);
                    attacker.increaseTerritoriesOwned();
                    attacker.getOwnedTerritories().add(defendingVertex);
                    // Update territory defender
                    defendingVertex.getTerritory().setNumberOfTroops(j - i);
                    // Update defender
                    defender.decreaseTerritoriesOwned();
                    defender.getOwnedTerritories().remove(defendingVertex);

                    // Add to tree
                    Node newNode = new Node(newState);
                    newNode.setAttacker(attackingVertex);
                    newNode.setDefender(defendingVertex);

                    expandNode.addChild(newNode);
                    newNode.setParent(expandNode);
                } else {
                    // when j - i is 0 or smaller, we can stop the inner loop
                    break;
                }
            }
        }

        // Generate losses
        // Attacker has only 1 troop left, defender has anywhere between 1 and total defenders left
        for (int i = 1; i < g.get(defenderIndex).getTerritory().getNumberOfTroops(); i++) {
            // Deep copy state
            State newState = deepCopyState(g, order, botPlayer, false);

            newState.getGraph().get(attackerIndex).getTerritory().setNumberOfTroops(1);
            newState.getGraph().get(defenderIndex).getTerritory().setNumberOfTroops(i);

            // Add to tree
            Node newNode = new Node(newState);
            newNode.setAttacker(newState.getGraph().get(attackerIndex));
            newNode.setDefender(newState.getGraph().get(defenderIndex));

            expandNode.addChild(newNode);
            newNode.setParent(expandNode);
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
    // Copying States
    private State deepCopyState(Graph g, ArrayList<Player> order, Player mctsPlayer, boolean simulation) {
        // Deep copy graph
        Territories t = new Territories();
        Graph newGraph = t.getGraph();

        for (int i = 0; i < newGraph.getSize(); i++) {
            newGraph.get(i).getTerritory().setOwner(g.get(i).getTerritory().getOwner());
            newGraph.get(i).getTerritory().setNumberOfTroops(g.get(i).getTerritory().getNumberOfTroops());
        }

        // Deep copy players and update graph
        ArrayList<Player> newOrder = new ArrayList<>();
        for (Player p : order) {
            Player newPlayer = p.clone();
            newOrder.add(newPlayer);
            for (Vertex v : newGraph.getArrayList()) {
                if (v.getTerritory().getOwner() == p) {
                    v.getTerritory().setOwner(newPlayer);
                    newPlayer.getOwnedTerritories().add(v);
                }
            }
        }

        Player newMctsPlayer = null;
        // If the game is being simulated don't rotate the mcts player
        if (simulation) {
            for (Player p : order) {
                if (p == mctsPlayer) {
                    newMctsPlayer = newOrder.get(order.indexOf(p));
                    break;
                }
            }
        } else {
            for (Player p : order) {
                if (p == mctsPlayer) {
                    if (order.indexOf(p) + 1 == order.size()) {
                        newMctsPlayer = newOrder.get(0);
                    } else {
                        newMctsPlayer = newOrder.get(order.indexOf(p) + 1);
                    }
                    break;
                }
            }
        }
        if (newMctsPlayer == null) {
            throw new RuntimeException("Player not updated properly");
        }
        return new State(newGraph, newOrder, newMctsPlayer);
    }

}
