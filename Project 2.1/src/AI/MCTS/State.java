package AI.MCTS;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Graph;

import java.util.ArrayList;

public class State {

    private final Graph graph;
    private final ArrayList<Player> order;
    private final Player playerMCTS;

    public State(Graph g, ArrayList<Player> order, Player player) {
        this.graph = g;
        this.order = order;
        this.playerMCTS = player;
    }

    public Graph getGraph() {
        return graph;
    }

    public ArrayList<Player> getOrder() {
        return order;
    }

    public Player getPlayerMCTS() { return playerMCTS; }

    public void printState() {
        System.out.println("Printing state: ");
        System.out.println("Players");
        System.out.println(order.toString());
        System.out.println("PlayerMCTS");
        System.out.println(playerMCTS);
        for (Player p : order) {
            System.out.println("Player " + p.getName() + " owns:");
            System.out.println(p.getOwnedTerritories());
        }
        System.out.println();
    }
}
