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
    private final long maxTime = 5000; // Milliseconds
    private final int maxIterations = 100000; // Attacks

    //------------------------------------------------------------------------------------------------------------------
    // Move-maker

    public Vertex[] findNextMove(Graph g, ArrayList<Player> order, Player p) {
        // Construct the root node
        State rootState = deepCopyState(g, order, p, true);
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
                if (promisingNode == root) {
                    System.out.println("does it?");
                    prunedExpansion(promisingNode);
                }
                else {
                    expansion(promisingNode);
                }
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
        System.out.println(attackerTerritoryName + " attacks " + defenderTerritoryName);

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
        root=null;
        rootState=null;
        System.gc();
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
        // ArrayList<Player> order = expandNode.getState().getOrder();
        // Player currentMovePlayer = order.remove(0);
        // order.add(currentMovePlayer);
        Player currentMovePlayer = expandNode.getState().getPlayerMCTS();

        // pruning: defender outnumbers attack
        ArrayList<Vertex> owned = currentMovePlayer.getOwnedTerritories();
        boolean noNewStates = true;
        for (Vertex v : owned) {
            if (v.getTerritory().getNumberOfTroops() > 1) {
                for (Edge e : v.getEdges()) {
                    if (e.getVertex().getTerritory().getOwner() != currentMovePlayer) {
                        noNewStates = false;
                        addAllPossibleStates(v.getTerritory().getTerritoryNumber(), e.getVertex().getTerritory().getTerritoryNumber(), expandNode);
                    }
                }
            }
        }
        if (noNewStates) {
            State newState = deepCopyState(expandNode.getState().getGraph(), expandNode.getState().getOrder(), expandNode.getState().getPlayerMCTS(), false);
            Node newNode = new Node(newState);

            expandNode.addChild(newNode);
            newNode.setParent(expandNode);
        }
    }

    // Add all the first possible moves or add 1 node to the bottom of the best node
    private void prunedExpansion(Node expandNode) {
        // Move first player to back of the order
        // ArrayList<Player> order = expandNode.getState().getOrder();
        // Player currentMovePlayer = order.remove(0);
        // order.add(currentMovePlayer);
        Player currentMovePlayer = expandNode.getState().getPlayerMCTS();

        // pruning: defender outnumbers attack
        ArrayList<Vertex> owned = currentMovePlayer.getOwnedTerritories();
        boolean noNewStates = true;
        for (Vertex v : owned) {
            if (v.getTerritory().getNumberOfTroops() > 1) {
                for (Edge e : v.getEdges()) {
                    if (e.getVertex().getTerritory().getOwner() != currentMovePlayer && e.getVertex().getTerritory().getNumberOfTroops() < v.getTerritory().getNumberOfTroops()) {
                        noNewStates = false;
                        addPositivePrunedStates(v.getTerritory().getTerritoryNumber(), e.getVertex().getTerritory().getTerritoryNumber(), expandNode);
                    }
                }
            }
        }
        if (noNewStates) {
            State newState = deepCopyState(expandNode.getState().getGraph(), expandNode.getState().getOrder(), expandNode.getState().getPlayerMCTS(), false);
            Node newNode = new Node(newState);

            expandNode.addChild(newNode);
            newNode.setParent(expandNode);
        }
    }



    // Assumes every dice roll ends in 1 lost troop for attackers, 1 lost troop for defenders
    private void addPositivePrunedStates(int attackerIndex, int defenderIndex, Node expandNode) {
        Graph g = expandNode.getState().getGraph();
        ArrayList<Player> order = expandNode.getState().getOrder();
        Player botPlayer = expandNode.getState().getPlayerMCTS();

        State newState = deepCopyState(g, order, botPlayer, false);

        Player attacker = newState.getGraph().get(attackerIndex).getTerritory().getOwner();
        Player defender = newState.getGraph().get(defenderIndex).getTerritory().getOwner();
        Vertex attackingVertex = newState.getGraph().get(attackerIndex);
        Vertex defendingVertex = newState.getGraph().get(defenderIndex);
        int temp = 0;
        while (defendingVertex.getTerritory().getNumberOfTroops() > 0 && attackingVertex.getTerritory().getNumberOfTroops() > 2) {
            if (temp % 2 == 0) {
                attackingVertex.getTerritory().setNumberOfTroops(attackingVertex.getTerritory().getNumberOfTroops() - 1);
            }
            else {
                attackingVertex.getTerritory().setNumberOfTroops(attackingVertex.getTerritory().getNumberOfTroops() - 2);
            }
            defendingVertex.getTerritory().setNumberOfTroops(defendingVertex.getTerritory().getNumberOfTroops() - 1);
        }
        State secondState = deepCopyState(newState.getGraph(),newState.getOrder(), newState.getPlayerMCTS(), false);

        // Update attacker
        attackingVertex.getTerritory().setNumberOfTroops(attackingVertex.getTerritory().getNumberOfTroops() - 1);
        defendingVertex.getTerritory().setOwner(attacker);
        attacker.increaseTerritoriesOwned();
        attacker.getOwnedTerritories().add(defendingVertex);
        // Update defender
        defendingVertex.getTerritory().setNumberOfTroops(1);
        defender.decreaseTerritoriesOwned();
        defender.getOwnedTerritories().remove(defendingVertex);

        // Add to tree
        Node newNode = new Node(newState);
        newNode.setAttacker(attackingVertex);
        newNode.setDefender(defendingVertex);

        expandNode.addChild(newNode);
        newNode.setParent(expandNode);

        // Second one
        Player attacker2 = secondState.getGraph().get(attackerIndex).getTerritory().getOwner();
        Player defender2 = secondState.getGraph().get(defenderIndex).getTerritory().getOwner();
        Vertex attackingVertex2 = secondState.getGraph().get(attackerIndex);
        Vertex defendingVertex2 = secondState.getGraph().get(defenderIndex);
        // Update attacker
        int atkTroops = attackingVertex2.getTerritory().getNumberOfTroops();
        attackingVertex2.getTerritory().setNumberOfTroops(1);
        defendingVertex2.getTerritory().setOwner(attacker2);
        attacker2.increaseTerritoriesOwned();
        attacker2.getOwnedTerritories().add(defendingVertex2);
        // Update defender
        defendingVertex2.getTerritory().setNumberOfTroops((atkTroops-1));
        defender2.decreaseTerritoriesOwned();
        defender2.getOwnedTerritories().remove(defendingVertex2);

        // Add to tree
        Node secondNode = new Node(secondState);
        secondNode.setAttacker(attackingVertex2);
        secondNode.setDefender(defendingVertex2);

        expandNode.addChild(secondNode);
        secondNode.setParent(expandNode);

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
            Player newPlayer = new Player(p.getName(), p.getColor(), p.isBot(), p.isMCTSBot(), p.getTerritoriesOwned(), p.getContinentsOwned());
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
                    newMctsPlayer = newOrder.get(order.indexOf(p));
                    break;
                }
            }
        }
        if (newMctsPlayer == null) {
            throw new RuntimeException("Player not updated properly");
        }
        if (!newMctsPlayer.getName().equals(mctsPlayer.getName())) {
            throw new RuntimeException("MCTS player updated incorrectly");
        }
        return new State(newGraph, newOrder, newMctsPlayer);
    }

}
