package AI.BasicBot.Components;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Territory;
import BackEndStructure.Graph.Vertex;
import java.util.ArrayList;

public class BotPlaceTroops extends UsefulMethods {

    private boolean allTerritoriesOwned = false;

    // -----------------------------------------------------------------------------------------------------------------
    // Placing troops

    /**
     * Based on the current board and the amount of troops the bot can place down
     * Let the bot make a decision on where it should place troops
     *
     * @param g      This is the current board
     * @param p      This is the current player turn
     * @param troops This is the number of troops the bot can place down
     */
    public int placeTroop(Graph g, Player p, int troops) {
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
        if (allTerritoriesOwned) {
            return placeTroopsStartOfGame(g, p);
        } else {
            allTerritoriesOwned(g);
            return placeTroop(g, p, 1);
        }
    }

    public void allTerritoriesOwned(Graph g) {
        for (int i = 0; i < g.getSize(); i++) {
            if (g.get(i).getTerritory().getOwner() == null) {
                allTerritoriesOwned = false;
            }
        }
    }

    public int placeTroopsStartOfGame(Graph g, Player p) {
        double[] countries = new double[42];

        for (int i = 0; i < countries.length; i++) {

            if (g.get(i).getTerritory().getOwner() == null) {
                countries[i] = 1;
            } else {
                countries[i] = -100;
            }
            for (String continent : continents) {
                int[] cont = continentDetector(continent);
                if (contains(cont, i)) {
                    countries[i] += percentageOfContinentOwned(g, p, continent);
                    countries[i] += percentageOfContinentUnOwned(g, p, continent);
                }
            }
            for (int j = 0; j < 42; j++) {
                if (g.get(j).getTerritory().getOwner() == p && g.isAdjecent(g.get(j), g.get(i))) {
                    countries[i] += 0.4;
                }
            }
        }
        int bestPick = 0;
        double bestGrade = 0;
        for (int i = 0; i < 42; i++) {
            if (countries[i] > bestGrade) {
                bestPick = i;
                bestGrade = countries[i];
            }
        }
        return bestPick;
    }

}
