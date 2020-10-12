package BackEndStructure.Game;

import BackEndStructure.Entities.Cards.CardStack;
import BackEndStructure.Entities.Dice;
import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Territories;
import BackEndStructure.Graph.Territory;
import Visualisation.Map;

import java.awt.*;

public class Game {
    // Player object List, all the players of the game
    private final Player[] players;
    private final Color[] colors = {Color.red, Color.blue, Color.green, Color.orange, Color.yellow, Color.CYAN};

    // the Board
    private final Map map;

    // All the territories in a graph (with edges)
    private final Graph graph;

    // Attacking Die
    private Dice red1;
    private Dice red2;
    private Dice red3;

    // Defending Die
    private Dice white1;
    private Dice white2;

    // Cards
    private final CardStack cardStack = new CardStack();

    // Number of troops players start with
    private int startingTroops;

    public Game(int numberOfPlayers, String[] names) {
        // How manny troops each player gets from the start
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

        // Instantiate players
        this.players = new Player[numberOfPlayers];

        for (int i = 0; i < numberOfPlayers; i++) {
            this.players[i] = new Player(names[i], colors[i]);
        }
    }

    public Map getMap() { return this.map; }

    public  Player[] getPlayers() { return players; }

    public Graph getGraph() { return graph; }

    public int getStartingTroops() { return startingTroops; }
}