package BackEndStructure.Simulation.Stages.SimulatedEvents;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Edge;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Vertex;

import java.util.ArrayList;
import java.util.Random;

public class SimFortifyEvent {

    private final Graph graph;

    public SimFortifyEvent(Graph g) {
        this.graph = g;
    }

    public void randomFortification(Player player) {
        // Get all the owned territories for this player that have more than 1 troop
        ArrayList<Vertex> ownedTerritories = new ArrayList<>();
        for (Vertex v : graph.getArrayList()) {
            if (v.getTerritory().getOwner() == player && v.getTerritory().getNumberOfTroops() > 1) {
                ownedTerritories.add(v);
            }
        }

        // For all owned territories select the ones that have another adjacent owned territory
        ArrayList<Vertex> validFroms = new ArrayList<>();
        for (Vertex v : ownedTerritories) {
            boolean twoOwnedAdjacent = false;
            for (Edge e : v.getEdges()) {
                if (e.getVertex().getTerritory().getOwner() == player) {
                    twoOwnedAdjacent = true;
                    break;
                }
            }
            if (twoOwnedAdjacent) {
                validFroms.add(v);
            }
        }

        if (!validFroms.isEmpty()) {
            // Select a random territory from this list (this territory will send troops)
            Random random = new Random();
            Vertex from = validFroms.get(random.nextInt(validFroms.size()));

            // Select a random territory that is adjacent to the from territory (this territory receives troops)
            Vertex to = from.getEdges().get(random.nextInt(from.getEdges().size())).getVertex();

            // Send a random quantity of troops
            int troopsSend = (int) (Math.random() * (from.getTerritory().getNumberOfTroops() - 1)) + 1;

            // Update the troop counts in the graph
            from.getTerritory().setNumberOfTroops(from.getTerritory().getNumberOfTroops() - troopsSend);
            to.getTerritory().setNumberOfTroops(to.getTerritory().getNumberOfTroops() + troopsSend);
        } else {
            System.out.println("no valid reinforcements");
        }
    }

}
