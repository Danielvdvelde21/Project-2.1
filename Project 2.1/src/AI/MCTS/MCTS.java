package AI.MCTS;

import AI.BasicBot.Components.UsefulMethods;
import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Edge;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Vertex;
import BackEndStructure.Simulation.SimulatedGameLoop;

import java.util.ArrayList;

public class MCTS extends UsefulMethods {

    private Graph originalGraph;
    private int maxIterations = 1000;
    // TODO UCT stuff

    //------------------------------------------------------------------------------------------------------------------
    // Move-maker

    public Graph findNextMove(Graph g, ArrayList<Player> order) {
        // First player in the playerOrder is the MCTS bot
        Player playerMCTS = order.get(0);

        State originalState = new State(g, playerMCTS);
        Node root = new Node(originalState);
        root.setVisitCount(1); // set the root as simulated so that we can
        Tree tree = new Tree(root);

        // Time limit or iteration limit
        long time = System.currentTimeMillis();
        int end = 10000; //time limit

        int iteration=0;
        while (System.currentTimeMillis() - time < end && iteration<maxIterations) {
            Node promisingNode = selectPromisingChild(root);

            if (promisingNode.isSimulated()) {
                expansion(g, playerMCTS, promisingNode);
                promisingNode = promisingNode.getChildren().get(0);
            }

            Node simulationNode = promisingNode;

            //this is probably redundant since a promising node is always a leaf
            /*if (promisingNode.getChildren().size() > 0) {
                simulationNode = promisingNode.getRandomChildNode();
            }*/


            int playResult = playOut(simulationNode.getState().getGraph(), order);

            backProp(simulationNode, playResult);

            iteration++;
        }

        Node winner = root.getMaxScoreChild();
        tree.setRoot(winner);
        // TODO return attacker and defender vertex such that we can update visual variables in maingameloop
        return winner.getState().getGraph();
    }

    //------------------------------------------------------------------------------------------------------------------
    // Selection

    // Selection
    private Node selectPromisingChild(Node curNode) {
        while (curNode.getChildren().size() != 0) {
            curNode = UCT.findBestChildWithUCT(curNode);
        }
        return curNode;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Play-out
    // Simulates a game

    // TODO Want to set player p as the first player in the order
    public void changeOrder(ArrayList<Player> order) {

    }


    public int playOut(Graph g, ArrayList<Player> order) {
        // TODO WE NEED TO CHANGE THE ORDER SO THAT THE MCTS BOT ACTUALLY BEGINS HIS TURN
        // order has to be set somewhere, or pass player as parameter to start there
        changeOrder(order);
        SimulatedGameLoop game = new SimulatedGameLoop(g, order);
        System.out.println(game.getWinner());
        int result = 0;
        if (game.getWinner() == order.get(0)) {
            result = 10;
        }
        return result;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Expansion

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

    //------------------------------------------------------------------------------------------------------------------
    // Back propagation

    // Adds scores to nodes from this simulation
    public void backProp(Node node, int result) {
        node.addWinScore(result);
        node.visit();
        if (node.getParent() != null) {
            backProp(node.getParent(), result);
        }
    }

}
