package AI;

import BackEndStructure.Entities.Cards.Card;
import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Edge;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Territory;
import BackEndStructure.Graph.Vertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class AiTemplate {

    private int reinforcementTroops;
    private int attackerDie;
    private final String[] continents = new String[]{"Australia", "Africa", "Asia", "North America", "South America", "Europe"};

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

    public static boolean contains(final int[] arr, final int key) {
        return Arrays.stream(arr).anyMatch(i -> i == key);
    }
    private double percentageOfContinentUnOwned(Graph g, Player p, String continent) {
        int[] territories = continentDetector(continent);
        Player tOwner;
        int counter = 0;
        for (int territory : territories) {
            tOwner=g.get(territory).getTerritory().getOwner();
                if (tOwner!=null&&tOwner!=p) {
                    counter++;
                }
        }
        return (double) counter / territories.length;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Attacking

    /**
     * Based on the current board
     * Let the bot make a decision on if and how it wants to attack
     *
     * @param g This is the current board
     * @param p This is the current player turn
     * @return A vertex array with position 0 attacker and position 1 defender
     */
    public Vertex[] attack(Graph g, Player p) {
        // Get the continent that you own the most (getting a continent)
        mostOwnedContinent(g, p);
        double[] bsr = new double[g.getSize()];
        // Bot-owned
        for (int i = 0; i < g.getSize(); i++) {
            // Only calculate BSR for bot-owned territories
            if (g.get(i).getTerritory().getOwner()==p) {
                bsr[i] = g.get(i).getBSR();
            } else {
                bsr[i] = -1;
            }
        }

        int atkIndex = getLowest(bsr);

        // Attack the neighbouring country with the least amount of troops
        LinkedList<Edge> edges = g.get(atkIndex).getEdges();
        int lowesttroops = 10000;
        int defIndex = 0;
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getVertex().getTerritory().getOwner()!=p) {
                if (edges.get(i).getVertex().getTerritory().getNumberOfTroops() < lowesttroops) {
                    lowesttroops = edges.get(i).getVertex().getTerritory().getNumberOfTroops();
                    defIndex = i;
                }
            }
        }

        attackerDie = 3;
        Vertex[] duo = {g.get(atkIndex), edges.get(defIndex).getVertex()};
        return duo;
    }


    // Evaluate when bot stops attacking
    public boolean botWantsToAttack(Graph g, Player p) {
        //
        return true;
    }

    // How many troops will be sent over when a territory is captured
    public int getTroopCarryOver() {
        // If the attacking country is now only surrounded by friendly territories; send over all but one

        // Otherwise, it'll be tougher (BST?)
        return 1;
    }

    // If a bot eliminates a player and gets his cards --> the bot needs to turn in a set
    public ArrayList<Card> attackingCard(Graph g, Player p) {
        // TODO
        return null;
    }

    public int getAttackerDie() {
        return attackerDie;
    }

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
        Vertex from = null;
        int totalTroops = 0;
        int totalTroopsMax = 0;

        //'aggressive' strategy
        for (int i = 0; i < g.getSize(); i++) {
            if (g.get(i).getTerritory().getOwner()==p) {
                totalTroops = g.get(i).getTerritory().getNumberOfTroops();
                for(int j = 0; j < g.getSize(); j++) {
                    if (g.isAdjecent(g.get(i), g.get(j))) {
                        if(g.get(j).getTerritory().getNumberOfTroops() > 1 && g.get(j).getTerritory().getOwner()==p) {
                            totalTroops += g.get(j).getTerritory().getNumberOfTroops();
                        }
                    }
                }
                if(totalTroops > totalTroopsMax) {
                    totalTroopsMax = totalTroops;
                    to = g.get(i);
                }
            }
        }

        if(to != null) {
            for (int n = 0; n < g.getSize(); n++) {
                if (g.isAdjecent(to, g.get(n))) {
                    if (g.get(n).getTerritory().getOwner()==p) {
                        if(g.get(n).getTerritory().getNumberOfTroops() > 1) {
                            reinforcementTroops = g.get(n).getTerritory().getNumberOfTroops() - 1;
                            if(g.get(n).getTerritory().getNumberOfTroops() - reinforcementTroops > 0) {
                                from = g.get(n);
                                return new Vertex[] {from, to};
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    public int getReinforcementTroops() {
        return reinforcementTroops;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Trading cards

    /**
     * Based on the bots hand (p.getHand()) and the current board
     * Let the bot make a decision on if and how it wants to return its cards
     *
     * @param g This is the current board
     * @param p This is the current player turn
     * @return a list of cards that the bot wants to turn in
     */
    public ArrayList<Card> cards(Graph g, Player p) {
        // No sets available if the bot has less than 3 cards
        if (p.getHand().size() < 3) {
            return null;
        }

        // Get all territories owned
        ArrayList<Territory> territoriesOwned = getOwnedTerritories(g, p);
        int h = p.getHand().size();

        // Evaluate what territories owned match with the cards
        ArrayList<Card> preferredCards = new ArrayList<>();
        for (Card c : p.getHand()) {
            for (Territory t : territoriesOwned) {
                if (c.getCardName().equals(t.getTerritoryName())) {
                    preferredCards.add(c);
                }
            }
        }

        // Create each possible set of cards
        ArrayList<Card> bestSet = new ArrayList<>();
        int maxPrefSetsUsed = 0;

        for (int i = 1; i < h; i++) {
            for (int j = i + 1; j < h; j++) {
                for (int k = j + 1; k < h; k++) {
                    ArrayList<Card> tempSet = new ArrayList<>(Arrays.asList(p.getHand().get(i), p.getHand().get(j), p.getHand().get(k)));
                    // Check if the tempSet is a valid set
                    if (isSet(tempSet)) {
                        // Check how many preferredCards the set is using
                        int prefCardsUsed = -1;
                        for (Card c : tempSet) {
                            for (Card prefCard : preferredCards) {
                                if (c == prefCard) {
                                    prefCardsUsed++;
                                }
                            }
                        }
                        if (prefCardsUsed > maxPrefSetsUsed) {
                            bestSet = tempSet;
                            maxPrefSetsUsed = prefCardsUsed;
                        }
                    }
                }
            }
        }
        return bestSet;
    }

    // Sets are always pairs of 3 cards
    private boolean isSet(ArrayList<Card> cards) {
        int countInfantry = 0;
        int countCavalry = 0;
        int countArtillery = 0;

        for (Card c : cards) {
            String cardType = c.getCardType();
            switch (cardType) {
                case "WILDCARD":
                    return true;
                case "Infantry":
                    countInfantry++;
                    break;
                case "Cavalry":
                    countCavalry++;
                    break;
                case "Artillery":
                    countArtillery++;
            }
        }
        return countInfantry == 3 || countCavalry == 3 || countArtillery == 3 || (countInfantry == 1 && countCavalry == 1 && countArtillery == 1);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Extra methods

    // Find index of lowest value in double[]
    private int getLowest(double[] list) {
        double lowest = list[0];
        int indexLowestScore = 0;
        for (int i = 1; i < list.length; i++) {
            if (list[i] != -1) {
                if (lowest == 0 || lowest > list[i]) {
                    lowest = list[i];
                    indexLowestScore = i;
                }
            }
        }
        return indexLowestScore;
    }

    // Returns the name of the player that is the biggest threat
    private Player biggestThreat(Graph g) {
        // Determine how many players are in the game based on Graph
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < g.getSize(); i++) {
            if (!players.contains(g.get(i).getTerritory().getOwner())) {
                players.add(g.get(i).getTerritory().getOwner());
            }
        }

        // Estimate the threat for each player
        double biggestThreat = 0;
        Player biggestThreatPlayer=players.get(0);

        for (Player player : players) {
            if (biggestThreat > estimateThreat(g, player)) {
                biggestThreat = estimateThreat(g, player);
                biggestThreatPlayer = player;
            }
        }

        return biggestThreatPlayer;
    }

    // Estimates how big of a threat a given player is
    private double estimateThreat(Graph g, Player playerName) {
        // Calculate how many territories player has
        int territoriesOwned = 0;
        for (int i = 0; i < g.getSize(); i++) {
            if (g.get(i).getTerritory().getOwner()==playerName) {
                territoriesOwned++;
            }
        }
        // Calculate what percentage of territories player has
        double territoryOwnedRatio = (double) territoriesOwned / g.getSize();

        // Calculate how many troops player has
        int troopsOwned = 0;
        int totalTroopsOnBoard = 0;
        for (int i = 0; i < g.getSize(); i++) {
            if (g.get(i).getTerritory().getOwner()==playerName) {
                troopsOwned += g.get(i).getTerritory().getNumberOfTroops();
            }
            totalTroopsOnBoard += g.get(i).getTerritory().getNumberOfTroops();
        }

        // Calculate what percentage of troops player has
        double troopsOwnedRatio = (double) troopsOwned / totalTroopsOnBoard;

        return (territoryOwnedRatio + troopsOwnedRatio) / 2;
    }

    // Detects which continent is selected and returns its corresponding territories
    private int[] continentDetector(String continent) {
        switch (continent) {
            case "Australia":
                // 9, 38, 23, 16
                return new int[]{9, 16, 23, 38};

            case "Europe":
                // 12, 14, 26, 30, 34, 35, 39
                return new int[]{12, 14, 26, 30, 34, 39};
            case "North America":
                // 1, 2, 5, 10, 13, 25, 27, 29, 40
                return new int[]{1, 2, 5, 10, 13, 25, 27, 29, 40};

            case "South America":
                // 3, 4, 28, 37
                return new int[]{3, 4, 28, 37};

            case "Africa":
                // 7, 8, 11, 20, 24, 33
                return new int[]{7, 8, 11, 20, 24, 33};

            case "Asia":
                // 0, 6, 15, 17, 18, 19, 21, 22, 31, 32, 36, 41
                return new int[]{0, 6, 15, 17, 18, 19, 21, 22, 31, 32, 36, 41};
        }
        throw new IllegalArgumentException("INVALID CONTINENT SELECTED!");
    }

    // For a continent, calculate what percentage is owned by a player
    private double percentageOfContinentOwned(Graph g, Player p, String continent) {
        int[] territories = continentDetector(continent);

        int counter = 0;
        for (int territory : territories) {
            if (g.get(territory).getTerritory().getOwner()==p) {
                counter++;
            }
        }

        return (double) counter / territories.length;
    }

    private String mostOwnedContinent(Graph g, Player p) {
        double percentage = 0.0;
        String highest = "";
        for (String s : continents) {
            if (percentageOfContinentOwned(g, p, s) > percentage && percentageOfContinentOwned(g, p, s) < 0.95) {
                percentage = percentageOfContinentOwned(g, p, s);
                highest = s;
            }
        }
        return highest;
    }

    // For a continent get the number of troops on it
    private int getTroopsOnContinent(Graph g, String continent) {
        int[] territories = continentDetector(continent);

        int totalTroops = 0;
        for (int t : territories) {
            totalTroops += g.get(t).getTerritory().getNumberOfTroops();
        }
        return totalTroops;
    }

    // For a continent get the number of troops on it for a given player
    private int getTroopsOnContinent(Graph g, Player p, String continent) {
        int[] territories = continentDetector(continent);

        int totalTroops = 0;
        for (int t : territories) {
            if (g.get(t).getTerritory().getOwner()==p) {
                totalTroops += g.get(t).getTerritory().getNumberOfTroops();
            }
        }
        return totalTroops;
    }

    // Get all owned territories for a player
    private ArrayList<Territory> getOwnedTerritories(Graph g, Player p) {
        ArrayList<Territory> territoriesOwned = new ArrayList<>();

        for (int i = 0; i < g.getSize(); i++) {
            if (g.get(i).getTerritory().getOwner()==p) {
                territoriesOwned.add(g.get(i).getTerritory());
            }
        }

        return territoriesOwned;
    }

}
