package BackEndStructure.Entities;

import BackEndStructure.Entities.Cards.Card;

import java.awt.*;
import java.util.ArrayList;

public class Player {
    private final String playerName;
    private final Color playerColor;

    private int territoriesOwned;
    private final ArrayList<String> continentsOwned = new ArrayList<>();

    private ArrayList<Card> hand = new ArrayList<>();
    private int setsTurnedIn;

    public Player(String name, Color col) {
        this.playerName = name;
        this.playerColor = col;
    }

    public void addCardToHand(Card c) {
        hand.add(c);
    }

    public void turnInSet(Card[] cards, ArrayList<Card> stack) {
        // TODO
    }

    public String getName() {
        return playerName;
    }
    public Color getColor() { return playerColor; }
    public ArrayList<Card> getHand() { return hand; }

    public int getTerritoriesOwned() {
        return territoriesOwned;
    }

    public void increaseTerritoriesOwned() {
        territoriesOwned++;
    }

    public ArrayList<String> getContinentsOwned() {
        return continentsOwned;
    }
    public void addContinent(String c) { continentsOwned.add(c); }
}
