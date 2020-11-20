package BackEndStructure.Entities;

import BackEndStructure.Entities.Cards.Card;

import java.awt.*;
import java.util.ArrayList;

public class Player {
    // Player data
    private final String playerName;
    private final int playerIndex;
    private final Color playerColor;
    private final boolean isBot;

    // Territories and Continents
    private int territoriesOwned;
    private final ArrayList<String> continentsOwned = new ArrayList<>();

    // Cards
    private ArrayList<Card> hand = new ArrayList<>();
    private int setsTurnedIn;

    public Player(String name, int playerIndex,Color col, boolean isBot) {
        this.playerName = name;
        this.playerColor = col;
        this.isBot = isBot;
        this.playerIndex=playerIndex;
    }

    // Get Player data
    public String getName() {
        return playerName;
    }

    public Color getColor() {
        return playerColor;
    }

    public boolean isBot() {
        return isBot;
    }

    // Get and Set territory/continent data
    public int getTerritoriesOwned() {
        return territoriesOwned;
    }

    public void increaseTerritoriesOwned() {
        territoriesOwned++;
    }

    public void decreaseTerritoriesOwned() {
        territoriesOwned--;
    }

    public ArrayList<String> getContinentsOwned() {
        return continentsOwned;
    }

    public void addContinent(String c) {
        continentsOwned.add(c);
    }

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

    public void incrementSetsOwned() {
        setsTurnedIn++;
    }
}
