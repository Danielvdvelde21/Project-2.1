package BackEndStructure.Game;

import AI.AiTemplate;
import BackEndStructure.Entities.Cards.CardStack;
import BackEndStructure.Entities.AttackingHandler;
import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Territories;
import Visualisation.Map.Components.CardInventory;
import Visualisation.Map.Components.DicePanel;
import Visualisation.Map.Components.PlayerTurn;
import Visualisation.Map.Map;
import Visualisation.Map.Components.Narrator;

import java.awt.*;
import java.util.ArrayList;

public class Game {
    // Player object List, all the players of the game
    private ArrayList<Player> players = new ArrayList<>();
    private final Color[] colors = {Color.red, Color.blue, Color.green, Color.orange, Color.yellow, Color.CYAN};

    // the Board
    private final Map map;

    // AI (bot)
    private final AiTemplate ai = new AiTemplate();

    // Visual variables
    private final Narrator narrator = new Narrator();
    private final PlayerTurn playerTurn = new PlayerTurn();
    private final CardInventory cardInventory = new CardInventory();
    private final DicePanel dicePanel = new DicePanel();

    // All the territories in a graph (with edges)
    private final Graph graph;

    // Attacking Die
    private final AttackingHandler attackingHandler;

    // Cards
    private final CardStack cardStack = new CardStack();

    // Number of troops players start with
    private int startingTroops;

    // Troops you receive for sets owned
    private final int[] setValues = new int[]{0,4,6,8,10,12,15};

    public Game(int numberOfPlayers, String[] names, boolean[] bots) {
        // How many troops each player gets from the start
        switch (numberOfPlayers) {
            case 2:
                startingTroops = 40;
                break;
            case 3:
                startingTroops = 35;
                break;
            case 4:
                startingTroops = 30;
                break;
            case 5:
                startingTroops = 25;
                break;
            case 6:
                startingTroops = 20;
                break;
            default:
                System.out.println("Insert 2,3,4,5 or 6");
                break;
        }
        // Create a new map
        this.map = new Map();
        map.createMap();

        // Instantiate Graph
        this.graph = new Territories().getGraph();

        // Instantiate Dice
        this.attackingHandler = new AttackingHandler();

        // Instantiate players
        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(new Player(names[i], colors[i], bots[i]));
        }
    }

    public Map getMap() {
        return this.map;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public String getPlayerOrder() {
        String order = "";
        for (Player p : players) {
            order += p.getName() + " --> ";
        }
        return order.substring(0, order.length()-4);
    }

    public void setPlayerOrder(ArrayList<Player> order) {
        players = order;
    }

    public Graph getGraph() { return graph; }

    public int getStartingTroops() { return startingTroops; }

    public CardStack getCardStack() {
        return cardStack;
    }

    public AttackingHandler getAttackingHandeler() {
        return attackingHandler;
    }

    public Narrator getNarrator() { return narrator; }

    public CardInventory getCardInventory() { return cardInventory; }

    public PlayerTurn getPlayerTurn() { return playerTurn; }

    public DicePanel getDicePanel() { return dicePanel; }

    public AiTemplate getAi() { return ai; }

    // Checks how many continents a player has
    public void hasContinents(Player player) {

        // Australia
        // 9, 38, 23, 16
        int[] ausArr = new int[]{9, 16, 23, 38};
        checkContinent(player, ausArr, "Australia");

        // Europe
        // 12, 14, 26, 30, 34, 35, 39
        int[] euArr = new int[]{12, 14, 26, 30, 34, 39};
        checkContinent(player, euArr, "Europe");

        // North America
        // 1, 2, 5, 10, 13, 25, 27, 29, 40
        int[] naArr = new int[]{1, 2, 5, 10, 13, 25, 27, 29, 40};
        checkContinent(player, naArr, "North America");

        // South America
        // 3, 4, 28, 37
        int[] saArr = new int[]{3, 4, 28, 37};
        checkContinent(player, saArr, "South America");

        // Africa
        // 7, 8, 11, 20, 24, 33
        int[] afArr = new int[]{7, 8, 11, 20, 24, 33};
        checkContinent(player, afArr, "Africa");

        // Asia
        // 0, 6, 15, 17, 18, 19, 21, 22, 31, 32, 36, 41
        int[] asArr = new int[]{0, 6, 15, 17, 18, 19, 21, 22, 31, 32, 36, 41};
        checkContinent(player, asArr, "Asia");
    }

    // Checks whether a player owns all countries in a continent based on its territories
    public void checkContinent(Player player, int[] terr, String continent) {
        String n = player.getName();
        int counter = 0;
        for (int i = 0; i < terr.length ; i++) {
            if (graph.get(terr[i]).getTerritory().getOwner().equals(n)) {
                counter++;
            }
        }
        boolean check = false;
        if (counter == terr.length) {
            // Making sure no duplicates are added to continents owned
            for (int i = 0; i < player.getContinentsOwned().size(); i++) {
                if (player.getContinentsOwned().get(i).equals(continent)) {
                    check = true;
                    break;
                }
            }
            if (!check) {
                player.addContinent(continent);
            }
        }
        else {
            for (int i = 0; i < player.getContinentsOwned().size(); i++) {
                if (player.getContinentsOwned().get(i).equals(continent)) {
                    player.getContinentsOwned().remove(i);
                    // Can break as there shouldn't be any duplicates and we don't want to run into bugs
                    break;
                }
            }
        }
    }

    // Checks how money points a player gets for the amount of continents he owns
    public int getValueOfContinentsOwned(ArrayList<String> list) {
        int value = 0;
        for (String c : list) {
            switch (c) {
                case "Asia":
                    value += 7;
                    break;
                case "Europe":
                case "North America":
                    value += 5;
                    break;
                case "Australia":
                case "South America":
                    value += 2;
                    break;
                case "Africa":
                    value += 3;
            }
        }
        return value;
    }

    public int getSetValue(int sets) {
        if (sets > 6) {
            return 15 + (sets-6)*5;
        } else {
            return setValues[sets];
        }
    }

}
