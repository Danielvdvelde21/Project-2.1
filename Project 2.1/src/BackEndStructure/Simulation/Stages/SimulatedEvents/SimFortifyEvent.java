package BackEndStructure.Simulation.Stages.SimulatedEvents;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Edge;
import BackEndStructure.Graph.Vertex;

import java.util.ArrayList;
import java.util.SplittableRandom;

public class SimFortifyEvent {
    SplittableRandom splittableRandom = new SplittableRandom();

    public void randomFortification(Player player) {
        // Get all the owned territories for this player
        ArrayList<Vertex> ownedTerritories = player.getOwnedTerritories();

        // For all owned territories select the ones that have another adjacent owned territory and have more than 1 troop
        Vertex[] validFroms = new Vertex[42];
        int validFromsNo = 0;

        for (Vertex v : ownedTerritories) {
            if (v.getTerritory().getNumberOfTroops() > 1) {
                boolean twoOwnedAdjacent = false;
                Edge[] neighbours = v.getEdges();
                int neighboursNo = v.getEdgeNo();
                for (int i=0;i<neighboursNo;i++) {
                    Edge e=neighbours[i];
                    if (e.getVertex().getTerritory().getOwner() == player) {
                        twoOwnedAdjacent = true;
                        break;
                    }
                }
                if (twoOwnedAdjacent) {
                    validFroms[validFromsNo]=v;
                    validFromsNo++;
                }
            }
        }

        if (validFromsNo!=0) {
            // Select a random territory from this list (this territory will send troops)

            Vertex from = validFroms[splittableRandom.nextInt(validFromsNo)];

            // Select a random territory that is adjacent to the from territory (this territory receives troops)
            Edge[] validTos = new Edge[10];
            int validTosNo=0;
            Edge[] neighbours = from.getEdges();
            int neighboursNo = from.getEdgeNo();
            for (int i=0;i<neighboursNo;i++) {
                Edge e=neighbours[i];
                if (e.getVertex().getTerritory().getOwner() == player) {
                    validTos[validTosNo]=e;
                    validTosNo++;
                }
            }
            Vertex to = validTos[splittableRandom.nextInt(validTosNo)].getVertex();

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
