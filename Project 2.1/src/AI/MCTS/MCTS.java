package AI.MCTS;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Edge;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Vertex;
import BackEndStructure.Simulation.SimulatedGameLoop;

import java.util.ArrayList;

public class MCTS {

    private final int maxIterations = 1000;

    private Graph copiedGraph;
    private final ArrayList<Player> copiedOrder = new ArrayList<>();

    private Player MCTSPlayer;

    //------------------------------------------------------------------------------------------------------------------
    // Move-maker

    public Vertex[] findNextMove(Graph g, ArrayList<Player> order) {
        // Deep copy graph, players and update graph
        deepCopyState(g,order);

        // Assign MCTS player, must be first player in order!
        this.MCTSPlayer = copiedOrder.get(0);

        // Construct the root node
        Node root = new Node(new State(g, MCTSPlayer));
        root.setVisitCount(1); // Sets the root as simulated

        Tree tree = new Tree(root);

        // Time limit or iteration limit
        long time = System.currentTimeMillis();
        int end = 2500; // Time limit

        int iteration = 0;
        while (System.currentTimeMillis() - time < end && iteration < maxIterations) {
            // Selection
            Node promisingNode = selectPromisingChild(root);

            // Expansion
            if (promisingNode.isSimulated()) {
                expansion(promisingNode);
                promisingNode = root; // TODO TEMP REMOVE ME
                // promisingNode = promisingNode.getChildren().get(0);
            }

            // Play out (simulation)
            Node simulationNode = promisingNode;
            int playResult = playOut(simulationNode.getState().getGraph(), simulationNode);

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

    private void deepCopyState(Graph g, ArrayList<Player> order){
        this.copiedGraph = g.clone();
        
        // Deep copy players and update graph
        for (Player p : order) {
            Player newPlayer = new Player(p.getName(), p.getColor(), p.isBot(), p.isMCTSBot());
            newPlayer.setTerritoriesOwned(p.getTerritoriesOwned());
            newPlayer.setContinentsOwned(p.getContinentsOwned());
            this.copiedOrder.add(newPlayer);
            // Update the players to the copied players in the graph
            for(Vertex v : copiedGraph.getArrayList()) {
                if (v.getTerritory().getOwner() == p) {
                    v.getTerritory().setOwner(newPlayer);
                }
            }
        }

        // Update territories owned for simulation only
        for (Vertex v : copiedGraph.getArrayList()) {
            for (Player p : copiedOrder) {
                if (p == v.getTerritory().getOwner()){
                    p.getOwnedTerritories().add(v);
                    break;
                }
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // Play-out

    // Simulates a game
    private int playOut(Graph g, Node node) {
        // Fix player order for simulated game
        ArrayList<Player> simulatedPlayerOrder = changeOrder(node);

        // Simulate game
        System.out.println("Simulation starts");
        long startTime = System.currentTimeMillis();
        SimulatedGameLoop game = new SimulatedGameLoop(g, simulatedPlayerOrder);
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Time: " + estimatedTime);
        System.out.println("Simulation over!");
        System.exit(0);

        // TODO return winner
        return analyzeGame(game, node.getState().getPlayer());
    }

    // Change order for simulated game such that MCTS bot in node starts
    private ArrayList<Player> changeOrder(Node node) {
        while (!(copiedOrder.get(0) == node.getState().getPlayer())) {
            Player temp = copiedOrder.get(0);
            copiedOrder.remove(temp);
            copiedOrder.add(temp);
        }
        return copiedOrder;
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
    private void expansion(Node node) {
        Graph g = node.getState().getGraph();
        int playerIndex = 0;

        for (int i = 0; i < copiedOrder.size(); i++) {
            if (copiedOrder.get(i) == node.getState().getPlayer()) {
                playerIndex = i;
            }
        }
        Player nextPlayer;
        if (playerIndex == (copiedOrder.size() - 1)) {
            nextPlayer = copiedOrder.get(0);
        }
        else {
            nextPlayer = copiedOrder.get(playerIndex + 1);
        }
        //TODO
        /*
        int[][] matrix = new matrix[2][42];
        for (int i = 0; i < matrix[0].length; i++) {
            if (matrix[0][i] == playerId) {
                for (Edge e: copiedGraph.get(i).getEdges()) {
                    if (matrix[0][e.getVertex().getTerritory().getTerritoryNumber()] != playerId) {
                        addAllPossibleStates(matrix, i, e.getVertex().getTerritory().getTerritoryNumber(), node, nextPlayer);
                    }
                }
            }
        }*/
    }

    // Adds all possible states from a certain state given an attacker and defender and adds them as children
    private void addAllPossibleStates(Graph g, int attackerIndex, int defenderIndex, Node leaf, Player nextPlayer) {
        Graph copy;
        // Generate wins
        // Attacker has >= 1 troops, defender has >=1 troops and is now owned by attacker, attacker + defender troops = 2 - total attacking troops
        for (int i = 1; i < g.get(attackerIndex).getTerritory().getNumberOfTroops(); i++) {
            copy = g.clone();
            // TODO
        }

        // Generate losses
        // Attacker has only 1 troop left, defender has anywhere between 1 and total defenders left
        for (int i = 1; i < g.get(defenderIndex).getTerritory().getNumberOfTroops(); i++) {
            //Create deep copy of graph
            copy = g.clone();
            copy.get(attackerIndex).getTerritory().setNumberOfTroops(1);
            copy.get(defenderIndex).getTerritory().setNumberOfTroops(i);
            leaf.addChild(new Node(new State(copy, nextPlayer)));
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

    // Get all owned vertices for a player
    public ArrayList<Vertex> getOwnedVertices(Graph g, Player p) {
        ArrayList<Vertex> verticesOwned = new ArrayList<>();

        for (int i = 0; i < g.getSize(); i++) {
            if (g.get(i).getTerritory().getOwner()==p) {
                verticesOwned.add(g.get(i));
            }
        }
        return verticesOwned;
    }

}
