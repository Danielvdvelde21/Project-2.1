package BackEndStructure;

import BackEndStructure.Cards.CardStack;
import Visual.Map;

public class Game {
    // Player object List, all the players of the game
    private Player[] players;

    // the Board
    private Map map;

    // Attacking Die
    private Dice red1;
    private Dice red2;
    private Dice red3;

    // Defending Die
    private Dice white1;
    private Dice white2;

    // Cards
    private CardStack cardStack = new CardStack();

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
        this.map = new Map();
        this.players = new Player[numberOfPlayers];

        for (int i = 0; i < numberOfPlayers; i++) {
            this.players[i] = new Player(names[i]);
        }
    }

    public Map getMap() { return this.map; }

    public Player[] getPlayers() {
        return players;
    }

    public int getStartingTroops() { return startingTroops; }
}
