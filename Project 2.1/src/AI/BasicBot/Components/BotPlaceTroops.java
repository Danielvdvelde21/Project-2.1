package AI.BasicBot.Components;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Territory;
import BackEndStructure.Graph.Vertex;
import java.util.ArrayList;

public class BotPlaceTroops extends UsefulMethods {

    // -----------------------------------------------------------------------------------------------------------------
    // Placing troops

    /**
     * Based on the current board and the amount of troops the bot can place down
     * Let the bot make a decision on where it should place troops
     *
     * @param g      This is the current board
     * @param p      This is the current player turn
     * // @param troops This is the number of troops the bot can place down
     */
    public int placeTroop(Graph g, Player p) {
        // Each owned territory is considered and given a score
        ArrayList<Vertex> ownedTerritories = getOwnedVertices(g, p);
        double[] territoryScores = new double[ownedTerritories.size()];

        for (int territory = 0; territory < ownedTerritories.size(); territory++) {
            territoryScores[territory] = ownedTerritories.get(territory).getBSR();
        }

        // Return the index of the territory with the best score
        Territory bestTerritory = ownedTerritories.get(getHighest(territoryScores)).getTerritory();
        for (int i = 0; i < g.getSize(); i++) {
            if (g.get(i).getTerritory() == bestTerritory) {
                return i;
            }
        }
        throw new IllegalArgumentException("NO VALID OPTION FOUND!");
    }

    /**
     * This method is specifically for placing troops at the start of the game
     * Based on the current board
     * Let the bot make a decision on where it should place a troop
     *
     * @param g This is the current board
     * @param p This is the current player turn
     */
    public int placementDecider(Graph g, Player p) {
        if (allTerritoriesOwned(g)) {
            return placeTroop(g, p);
        } else {
            return placeTroopsStartOfGame(g, p);
        }
    }

    public boolean allTerritoriesOwned(Graph g) {
        for (int i = 0; i < g.getSize(); i++) {
            if (g.get(i).getTerritory().getOwner() == null) {
                return false;
            }
        }
        return true;
    }

    public int placeTroopsStartOfGame(Graph g, Player p) {
        // Each owned territory is considered and given a score
        ArrayList<Vertex> unOwnedTerritories = getUnOwnedVertices(g);
        double[] territoryScores = new double[unOwnedTerritories.size()];

        for (int territory = 0; territory < unOwnedTerritories.size(); territory++) {
            // System.out.println(unOwnedTerritories.get(territory).getTerritory().getTerritoryName());
            // Continent owned modifier
            String continent = TBelongsToCont(g, unOwnedTerritories.get(territory).getTerritory().getTerritoryNumber());
            territoryScores[territory] += percentageOfContinentOwned(g,p,continent)*6; // MULTIPLIER MIGHT BE WRONG
            //System.out.println(territoryScores[territory]);
            // adjacent countries modifier
            for(int j = 0; j < g.getSize(); j++){
                if(g.get(j).getTerritory().getOwner() == p  && g.isAdjecent(g.get(j),g.get(unOwnedTerritories.get(territory).getTerritory().getTerritoryNumber()))){
                    territoryScores[territory] += 0.4;
                }
            }
            //
            // System.out.println(territoryScores[territory]);
            // anti Asia modifier
            int[] asia = new int[]{0, 6, 15, 17, 18, 19, 21, 22, 31, 32, 36, 41};
            for(int t : asia){
                if(unOwnedTerritories.get(territory).getTerritory().getTerritoryNumber() == t){
                    territoryScores[territory] -= 1;
                }
            }
            //System.out.println(territoryScores[territory]);

        }


        // Return the index of the territory with the best score
        // System.out.println("here " + getHighest(territoryScores) + "with score = "+ territoryScores[getHighest(territoryScores)]);

        Territory bestTerritory = unOwnedTerritories.get(getHighest(territoryScores)).getTerritory();


        for (int i = 0; i < g.getSize(); i++) {
            if (g.get(i).getTerritory() == bestTerritory) {
                return i;
            }
        }
        throw new IllegalArgumentException("NO VALID OPTION FOUND!");
    }
}
