package BackEndStructure.Game;

import AI.BasicBot.AIMain;
import AI.MCTS.MCTS;
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
    // -----------------------------------------------------------------------------------------------------------------
    // Global variables

    // Player object List, all the players of the game
    private ArrayList<Player> players = new ArrayList<>();
    private final Color[] colors = {Color.red, Color.blue, Color.green, Color.orange, Color.yellow, Color.CYAN};

    // the Board
    private final Map map;

    // Rule based AI
    private final AIMain ai = new AIMain();

    // MCTS Ai
    private final MCTS AIMCTS = new MCTS();

    // Visual variables
    private final Narrator narrator = new Narrator();
    private final PlayerTurn playerTurn = new PlayerTurn();
    private final CardInventory cardInventory = new CardInventory();
    private final DicePanel dicePanel = new DicePanel();

    // All the territories in a graph (with edges)
    private Graph graph;

    // Attacking Die
    private final AttackingHandler attackingHandler;

    // Cards
    private final CardStack cardStack = new CardStack();

    // Number of troops players start with
    private int startingTroops;

    // Troops you receive for sets owned
    private final int[] setValues = new int[]{0,4,6,8,10,12,15};

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors

    // For playing a regular game
    public Game(int numberOfPlayers, String[] names, boolean[] basicBots, boolean[] MCTSBots) {
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

        // Instantiate Graph
        this.graph = new Territories().getGraph();

        // Instantiate Dice
        this.attackingHandler = new AttackingHandler();

        // Instantiate players
        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(new Player(names[i], colors[i], basicBots[i], MCTSBots[i]));
        }

        // Create a new map
        this.map = new Map();
        map.createMap();
    }

    // For playing a simulated game
    public Game(Graph g, ArrayList<Player> players) {
        // Instantiate Graph
        // TODO make sure that vertices are deep copied as well
        this.graph = new Graph(g.getArrayList());

        // Instantiate players
        this.players.addAll(players);

        // Instantiate Dice
        this.attackingHandler = new AttackingHandler();

        // We only instantiate the map class here, there wont be an actual map
        this.map = new Map();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Players

    // Returns all players in their respective order
    public ArrayList<Player> getPlayers() {
        return players;
    }

    // Sets all players in their RESPECTIVE ORDER!
    public void setPlayers(ArrayList<Player> order) { players = order; }

    public String getPlayerOrderToString() {
        StringBuilder order = new StringBuilder();
        for (Player p : players) {
            order.append(p.getName()).append(" --> ");
        }
        return order.substring(0, order.length()-4);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Graph

    public void setGraph(Graph g) { this.graph = g; }

    public Graph getGraph() { return graph; }

    // -----------------------------------------------------------------------------------------------------------------
    // Starting troops

    public int getStartingTroops() { return startingTroops; }

    // -----------------------------------------------------------------------------------------------------------------
    // Cards

    public CardStack getCardStack() {
        return cardStack;
    }

    // -----------------------------------------------------------------------------------------------------------------

    // Attacking logic

    public AttackingHandler getAttackingHandler() {
        return attackingHandler;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Graphics getters

    public Narrator getNarrator() { return narrator; }

    public CardInventory getCardInventory() { return cardInventory; }

    public PlayerTurn getPlayerTurn() { return playerTurn; }

    public DicePanel getDicePanel() { return dicePanel; }

    public Map getMap() {
        return this.map;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // AI

    // Rule Based AI
    public AIMain getAi() { return ai; }

    // MCTS AI
    public MCTS getAIMCTS() { return AIMCTS; }

    // -----------------------------------------------------------------------------------------------------------------
    // Continents

    // Checks how many continents a player has
    public void hasContinents(Player player) {

        // Australia
        // 9, 38, 23, 16
        int[] ausArr = new int[]{9, 16, 23, 38};
        checkContinent(player, ausArr, 0);

        // Europe
        // 12, 14, 26, 30, 34, 35, 39
        int[] euArr = new int[]{12, 14, 26, 30, 34, 39};
        checkContinent(player, euArr, 1);

        // North America
        // 1, 2, 5, 10, 13, 25, 27, 29, 40
        int[] naArr = new int[]{1, 2, 5, 10, 13, 25, 27, 29, 40};
        checkContinent(player, naArr, 2);

        // South America
        // 3, 4, 28, 37
        int[] saArr = new int[]{3, 4, 28, 37};
        checkContinent(player, saArr, 3);

        // Africa
        // 7, 8, 11, 20, 24, 33
        int[] afArr = new int[]{7, 8, 11, 20, 24, 33};
        checkContinent(player, afArr, 4);

        // Asia
        // 0, 6, 15, 17, 18, 19, 21, 22, 31, 32, 36, 41
        int[] asArr = new int[]{0, 6, 15, 17, 18, 19, 21, 22, 31, 32, 36, 41};
        checkContinent(player, asArr, 5);
    }

    // Checks whether a player owns all countries in a continent based on its territories
    public void checkContinent(Player player, int[] terr, int contIndex) {
        int counter = 0;
        for (int i = 0; i < terr.length ; i++) {
            if (graph.get(terr[i]).getTerritory().getOwner() == player) {
                counter++;
            }
        }
        if (counter == terr.length) {
            player.addContinent(contIndex);
        }
        else {
            player.removeContinent(contIndex);
        }
    }

    // Checks how money points a player gets for the amount of continents he owns
    public int getValueOfContinentsOwned(boolean[] continents) {
        int value = 0;
        // Australia
        if (continents[0]) {
            value += 2;
        }
        // Europe
        if (continents[1]) {
            value += 5;
        }
        // North America
        if (continents[2]) {
            value += 5;
        }
        // South America
        if (continents[3]) {
            value += 2;
        }
        // Africa
        if (continents[4]) {
            value += 3;
        }
        // Asia
        if (continents[5]) {
            value += 7;
        }
        return value;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Sets

    public int getSetValue(int sets) {
        if (sets > 6) {
            return 15 + (sets-6)*5;
        } else {
            return setValues[sets];
        }
    }

}
