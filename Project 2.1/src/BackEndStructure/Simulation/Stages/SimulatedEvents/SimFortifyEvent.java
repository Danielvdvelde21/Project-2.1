package BackEndStructure.Simulation.Stages.SimulatedEvents;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Edge;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Vertex;

import java.util.ArrayList;
import java.util.Random;

public class SimFortifyEvent {

    public void randomFortification(Player player) {
        // Get all the owned territories for this player
        ArrayList<Vertex> ownedTerritories = player.getOwnedTerritories();

        // For all owned territories select the ones that have another adjacent owned territory and have more than 1 troop
        ArrayList<Vertex> validFroms = new ArrayList<>();
        for (Vertex v : ownedTerritories) {
            if (v.getTerritory().getNumberOfTroops() > 1) {
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
        }

        if (!validFroms.isEmpty()) {
            // Select a random territory from this list (this territory will send troops)
            Random random = new Random();
            Vertex from = validFroms.get(random.nextInt(validFroms.size()));

            // Select a random territory that is adjacent to the from territory (this territory receives troops)
            ArrayList<Edge> validTos = new ArrayList<>();
            for (Edge e : from.getEdges()) {
                if (e.getVertex().getTerritory().getOwner() == player) {
                    validTos.add(e);
                }
            }
            Vertex to = validTos.get(random.nextInt(validTos.size())).getVertex();

            // Send a random quantity of troops
            int troopsSend = (int) (Math.random() * (from.getTerritory().getNumberOfTroops() - 1)) + 1;

            // Update the troop counts in the graph
            from.getTerritory().setNumberOfTroops(from.getTerritory().getNumberOfTroops() - troopsSend);
            to.getTerritory().setNumberOfTroops(to.getTerritory().getNumberOfTroops() + troopsSend);
            // System.out.println("Player: " + player.getName() + " reinforced " + to.getTerritory().getTerritoryName() + "(-" + troopsSend + ")" + " Using " + from.getTerritory().getTerritoryName() + "(+" + troopsSend + ")");
        } else {
            // System.out.println("no valid reinforcements");
        }
    }

}
