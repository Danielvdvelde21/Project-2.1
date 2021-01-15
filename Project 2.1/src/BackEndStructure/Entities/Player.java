package BackEndStructure.Entities;

import BackEndStructure.Entities.Cards.Card;
import BackEndStructure.Graph.Vertex;

import java.awt.*;
import java.util.ArrayList;

public class Player {
    // Player data
    private final String playerName;
    private final Color playerColor;
    private final boolean isBot;
    private final boolean isMCTSBot;

    // Territories and Continents
    private int territoriesOwned;
    private final ArrayList<String> continentsOwned = new ArrayList<>();

    // Cards
    private ArrayList<Card> hand = new ArrayList<>();
    private int setsTurnedIn;

    //------------------------------------------------------------------------------------------------------------------
    // Constructors

    public Player(String name, Color col, boolean isBot) {
        this.playerName = name;
        this.playerColor = col;
        this.isBot = isBot;
        this.isMCTSBot = false;
    }

    public Player(String name, Color col, boolean isBot, boolean MCTSBot) {
        this.playerName = name;
        this.playerColor = col;
        this.isBot = isBot;
        this.isMCTSBot = MCTSBot;
        assert (!(isBot()) && isMCTSBot());
    }

    // Get Player data
    public String getName() {
        return playerName;
    }

    public Color getColor() {
        return playerColor;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Is the player basic bot or mcts bot?

    public boolean isBot() {
        return isBot;
    }

    public boolean isMCTSBot() {
        return isMCTSBot;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Get and Set territory/continent data

    public void setTerritoriesOwned(int territories) { territoriesOwned = territories;    }

    public int getTerritoriesOwned() {
        return territoriesOwned;
    }

    public void increaseTerritoriesOwned() {
        territoriesOwned++;
    }

    public void decreaseTerritoriesOwned() {
        territoriesOwned--;
    }

    public void setContinentsOwned(ArrayList<String> continents) { continentsOwned.addAll(continents);    }

    public ArrayList<String> getContinentsOwned() {
        return continentsOwned;
    }

    public void addContinent(String c) {
        continentsOwned.add(c);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Get and Set card data
    public ArrayList<Card> getHand() {
        return hand;
    }

    public void addToHand(Card c) {
        hand.add(c);
    }

    public void addToHand(ArrayList<Card> cards) {
        hand.addAll(cards);
    }

    public int getSetsTurnedIn() {
        return setsTurnedIn;
    }

    public void setSetsTurnedIn(int sets) { setsTurnedIn = sets; }

    public void incrementSetsOwned() {
        setsTurnedIn++;
    }

    // MCTS BOT
    private ArrayList<Vertex> ownedTerritories = new ArrayList<>();

    public ArrayList<Vertex> getOwnedTerritories() {
        return ownedTerritories;
    }
}
