package BackEndStructure.Entities;

import BackEndStructure.Entities.Cards.Card;

import java.awt.*;
import java.util.ArrayList;

public class Player {
    // Player data
    private final String playerName;
    private final Color playerColor;

    // Territories and Continents
    private int territoriesOwned;
    private final ArrayList<String> continentsOwned = new ArrayList<>();

    // Cards
    private ArrayList<Card> hand = new ArrayList<>();
    private int setsTurnedIn;

    public Player(String name, Color col) {
        this.playerName = name;
        this.playerColor = col;
    }

    public String getName() {
        return playerName;
    }
    public Color getColor() { return playerColor; }

    public int getTerritoriesOwned() { return territoriesOwned; }
    public void increaseTerritoriesOwned() {
        territoriesOwned++;
    }
    public void decreaseTerritoriesOwned() { territoriesOwned--; }
    public ArrayList<String> getContinentsOwned() {
        return continentsOwned;
    }
    public void addContinent(String c) { continentsOwned.add(c); }

    public ArrayList<Card> getHand() { return hand; }
    public void addToHand(Card c) { hand.add(c); }
    public void addToHand(ArrayList<Card> cards) { hand.addAll(cards); }
    public int getSetsTurnedIn() { return setsTurnedIn; }
    public void incrementSetsOwned() { setsTurnedIn++;}
}
