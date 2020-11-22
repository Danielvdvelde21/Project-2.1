package AI.BasicBot;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Territory;

import java.util.ArrayList;
import java.util.Arrays;

public class UsefulMethods {

    public final String[] continents = new String[]{"Australia", "Africa", "Asia", "North America", "South America", "Europe"};

    public static boolean contains(final int[] arr, final int key) {
        return Arrays.stream(arr).anyMatch(i -> i == key);
    }

    public double percentageOfContinentUnOwned(Graph g, Player p, String continent) { //TODO Sam
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

    // Find index of lowest value in double[]
    public int getLowest(double[] list) {
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

    public int getLowestWithoutZero(double[] list) {
        double lowest = 100.0;
        int indexLowestScore = 0;
        for (int i = 1; i < list.length; i++) {
            if (list[i] != -1) {
                if (lowest > list[i] && list[i] > 0) {
                    lowest = list[i];
                    indexLowestScore = i;
                }
            }
        }
        return indexLowestScore;
    }

    // Find index of lowest value in double[] and in a specific continent
    public int getLowestInCont(double[] list, String c) {
        // Territories in continent
        int[] terrInCont = continentDetector(c);
        double lowest = 999.0;
        int indexLowestScore = terrInCont[0];

        for (int i = 1; i < terrInCont.length; i++) {
            if (list[terrInCont[i]] != -1) {
                if (lowest > list[terrInCont[i]] && list[terrInCont[i]] > 0) {
                    lowest = list[terrInCont[i]];
                    indexLowestScore = terrInCont[i];
                }
            }
        }
        return indexLowestScore;
    }

    // Returns the name of the player that is the biggest threat
    public Player biggestThreat(Graph g) {
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
    public double estimateThreat(Graph g, Player playerName) {
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
    public int[] continentDetector(String continent) {
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
    public double percentageOfContinentOwned(Graph g, Player p, String continent) {
        int[] territories = continentDetector(continent);

        int counter = 0;
        for (int territory : territories) {
            if (g.get(territory).getTerritory().getOwner()==p) {
                counter++;
            }
        }

        return (double) counter / territories.length;
    }

    public String mostOwnedContinent(Graph g, Player p) {
        double percentage = 0.0;
        String highest = "";
        for (String s : continents) {
            if (percentageOfContinentOwned(g, p, s) > percentage && percentageOfContinentOwned(g, p, s) < 0.9) {
                percentage = percentageOfContinentOwned(g, p, s);
                highest = s;
            }
        }
        return highest;
    }

    // For a continent get the number of troops on it
    public int getTroopsOnContinent(Graph g, String continent) {
        int[] territories = continentDetector(continent);

        int totalTroops = 0;
        for (int t : territories) {
            totalTroops += g.get(t).getTerritory().getNumberOfTroops();
        }
        return totalTroops;
    }

    // For a continent get the number of troops on it for a given player
    public int getTroopsOnContinent(Graph g, Player p, String continent) {
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
    public ArrayList<Territory> getOwnedTerritories(Graph g, Player p) {
        ArrayList<Territory> territoriesOwned = new ArrayList<>();

        for (int i = 0; i < g.getSize(); i++) {
            if (g.get(i).getTerritory().getOwner()==p) {
                territoriesOwned.add(g.get(i).getTerritory());
            }
        }

        return territoriesOwned;
    }

}
