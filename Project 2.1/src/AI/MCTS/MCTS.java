package AI.MCTS;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Edge;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Territories;
import BackEndStructure.Graph.Vertex;
import BackEndStructure.Simulation.SimulatedGameLoop;

import java.util.ArrayList;
import java.util.Collections;

public class MCTS {

    // Variables that determine the maximum time or iterations the bot has
    private final long maxTime = 100; // Milliseconds
    private final int maxIterations = 100000; // Attacks
    public static ArrayList<Integer> childrenNo;

    //------------------------------------------------------------------------------------------------------------------
    // Move-maker

    public Vertex[] findNextMove(Graph g, ArrayList<Player> order, Player p) {
        childrenNo = new ArrayList<Integer>();
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
                expansion(promisingNode);
                if (promisingNode.isTerminal()) {
                    continue;
                } else {
                    childrenNo.add(promisingNode.getChildrenSize());
                }

                promisingNode = promisingNode.getChildren()[0];
            }

            // Play out (simulation)
            Node simulationNode = promisingNode;
            int playResult = playOut(simulationNode);

            // Backpropagation
            backProp(simulationNode, playResult);
            iteration++;
        }

        // Time tracking code
        System.out.println("iterations: " + iteration);
        long ft = System.currentTimeMillis() - beginTimer;
        int hz = (int) (1000.0 / ((double) ft / (double) iteration));
        System.out.println("iterations per second:" + hz);


        // The vertices from the winner node are not the same objects as the vertices in the original graph
        // So we do some name detection and make sure we return vertices that are also in the original graph
        Node returner = root.getMaxScoreChild();

        Vertex attacker = returner.getAttacker();
        Vertex defender = returner.getDefender();
        String attackerTerritoryName = attacker.getTerritory().getTerritoryName();
        String defenderTerritoryName = defender.getTerritory().getTerritoryName();

        for (int i = 0; i < g.getSize(); i++) {
            if (g.get(i).getTerritory().getTerritoryName().equals(attackerTerritoryName)) {
                attacker = g.get(i);
            }
            if (g.get(i).getTerritory().getTerritoryName().equals(defenderTerritoryName)) {
                defender = g.get(i);
            }
        }
        // Make sure the territories are the same
        /*if (!g.getArrayList().contains(returner[0]) || !g.getArrayList().contains(returner[1])) {
            throw new RuntimeException("Duplicated vertices are returned");
        }*/
        
        System.out.println("number of expansions: " + childrenNo.size());
        System.out.println("max expansion size: " + Collections.max(childrenNo));
        System.out.println("min expansion size: " + Collections.min(childrenNo));
        System.out.println("average: " + calculateAverage(childrenNo));

        root = null;
        rootState = null;
        System.gc();
        return new Vertex[]{attacker, defender};
    }

    private double calculateAverage(ArrayList<Integer> marks) {//thanks StackOverflow
        Integer sum = 0;
        if (!marks.isEmpty()) {
            for (Integer mark : marks) {
                sum += mark;
            }
            return sum.doubleValue() / marks.size();
        }
        return sum;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Selection

    // Selection considers all leaves
    private Node selectPromisingChild(Node curNode) {
        while (curNode.getChildrenSize() != 0) {
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
                Edge[] neighbours = v.getEdges();
                int neighboursNo = v.getEdgeNo();
                for (int i = 0; i < neighboursNo; i++) {
                    Edge e = neighbours[i];
                    if (e.getVertex().getTerritory().getOwner() != currentMovePlayer) {
                        if (v.getTerritory().getNumberOfTroops() > e.getVertex().getTerritory().getNumberOfTroops()) {
                            noNewStates = false;
                            addAllPossibleStates(v.getTerritory().getTerritoryNumber(), e.getVertex().getTerritory().getTerritoryNumber(), expandNode);
                        }
                    }
                }
            }
        }
        if (noNewStates) {
            expandNode.setTerminal();
        }
    }

    // Adds all possible states from a certain state given an attacker and defender and adds them as children
    private void addAllPossibleStates(int attackerIndex, int defenderIndex, Node expandNode) {
        Graph g = expandNode.getState().getGraph();
        ArrayList<Player> order = expandNode.getState().getOrder();
        Player botPlayer = expandNode.getState().getPlayerMCTS();

        ArrayList<Node> adc = new ArrayList<>();
        expandNode.childrenForAverage.add(adc);

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

                    adc.add(newNode);
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

            adc.add(newNode);
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