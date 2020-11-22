package AI.BasicBot.Components;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Graph;

public class BotPlaceTroops extends UsefulMethods {

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

    public void placeTroop(Graph g, Player p, int troops) {
        // Each territory gets a bsr score
        double[] bsr = new double[g.getSize()];

        // For each vertex (territory) that is owned by the bot calculate its BSR
        for (int i = 0; i < g.getSize(); i++) {
            // Only calculate BSR for bot-owned territories
            if (g.get(i).getTerritory().getOwner()==p) {
                bsr[i] = g.get(i).getBSR();
            } else {
                bsr[i] = -1;
            }
        }
        // Get the index of the lowest BSR in bsr[]
        int indexLowestBSR = getLowest(bsr);

        // Put the troops on the territory with the highest score
        g.get(indexLowestBSR).getTerritory().setNumberOfTroops(g.get(indexLowestBSR).getTerritory().getNumberOfTroops() + troops);
    }

    /**
     * This method is specifically for placing troops at the start of the game
     * Based on the current board
     * Let the bot make a decision on where it should place a troop
     *
     * @param g This is the current board
     * @param p This is the current player turn
     */
    public int placementDecider(Graph g, Player p){
        Boolean territoryFull = true;
        int country =0;
        for(int i =0; i < g.getSize(); i++){
            if(g.get(i).getTerritory().getOwner()==null){
                territoryFull = false;
            }
        }
        if(territoryFull){
            return placeTroopsStartOfGame(g,p);
        }
        else{
            return chooseLandStartOfGame(g,p);
        }
    }

    public int chooseLandStartOfGame(Graph g, Player p) {
        String[] continents = new String[]{"Europe","Australia","North America","South America","Africa","Asia"};
        double[] countries = new double[42];

        for(int i =0; i < countries.length; i++){

            if(g.get(i).getTerritory().getOwner()==null){
                countries[i] = 1;
            }
            else{
                countries[i] = -100;
            }
            for(String continent : continents){
                int[] cont = continentDetector(continent);
                if(contains(cont, i)){
                    countries[i] += percentageOfContinentOwned(g,p,continent);
                    countries[i] += percentageOfContinentUnOwned(g,p,continent);
                }
            }
            for(int j = 0; j < 42; j++){
                if(g.get(j).getTerritory().getOwner()==p && g.isAdjecent(g.get(j),g.get(i))){
                    countries[i] += 0.4;
                }
            }
        }
        int bestPick = 0;
        double bestGrade = 0;
        for(int i =0; i < 42; i++){
            if(countries[i] > bestGrade){
                bestPick = i;
                bestGrade = countries[i];
            }
        }
        System.out.println(bestGrade);
        return bestPick;

    }

    public int placeTroopsStartOfGame(Graph g, Player p){
        // Each territory gets a bsr score
        double[] bsr = new double[g.getSize()];

        // For each vertex (territory) that is owned by the bot calculate its BSR
        for (int i = 0; i < g.getSize(); i++) {
            // Only calculate BSR for bot-owned territories
            if (g.get(i).getTerritory().getOwner()==p) {
                bsr[i] = g.get(i).getBSR();
            } else {
                bsr[i] = -1;
            }
        }
        // Get the index of the lowest BSR in bsr[]
        int indexLowestBSR = getLowest(bsr);

        return indexLowestBSR;
    }
}
