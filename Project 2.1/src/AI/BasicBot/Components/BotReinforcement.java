package AI.BasicBot.Components;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Vertex;

public class BotReinforcement extends UsefulMethods {

    private int reinforcementTroops;

    // -----------------------------------------------------------------------------------------------------------------
    // Fortifying

    /**
     * Based on the current board
     * Let the AI make a decision on if and how it wants to reinforce its position
     *
     * @param g This is the current board
     * @param p This is the current player turn
     * @return A vertex array with position 0 from and position 1 to
     */
    public Vertex[] reinforce(Graph g, Player p) {
        // TODO

        reinforcementTroops = 0;
        Vertex to = null;
        Vertex from;
        int totalTroops;
        int totalTroopsMax = 0;

        //'aggressive' strategy
        for (int i = 0; i < g.getSize(); i++) {
            if (g.get(i).getTerritory().getOwner()==p) {    //checks territories owned by player
                totalTroops = g.get(i).getTerritory().getNumberOfTroops();
                for(int j = 0; j < g.getSize(); j++) {
                    if (g.isAdjecent(g.get(i), g.get(j)) && g.get(j).getTerritory().getNumberOfTroops() > 1 && g.get(j).getTerritory().getOwner()==p) {
                        //checks all adjacent territories owned by player with > 1 troops
                        totalTroops += g.get(j).getTerritory().getNumberOfTroops(); //total adjacent troops of the territory
                    }
                }
                if(totalTroops > totalTroopsMax) {  //chooses the 'to' territory with the most total adjacent troops
                    totalTroopsMax = totalTroops;
                    to = g.get(i);
                }
            }
        }

        //determining 'from' territory
        if(to != null) {
            for (int n = 0; n < g.getSize(); n++) {
                if (g.isAdjecent(to, g.get(n)) && g.get(n).getTerritory().getNumberOfTroops() > 1 && g.get(n).getTerritory().getOwner()==p) {
                    reinforcementTroops = g.get(n).getTerritory().getNumberOfTroops() - 1;
                    if(g.get(n).getTerritory().getNumberOfTroops() - reinforcementTroops > 0) {
                        from = g.get(n);
                        return new Vertex[] {from, to};
                    }
                }
            }
        }

        return null;
    }

    public int getReinforcementTroops() {
        return reinforcementTroops;
    }
}
