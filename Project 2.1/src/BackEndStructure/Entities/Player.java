package BackEndStructure.Entities;

import BackEndStructure.Entities.Cards.Card;

import java.util.ArrayList;

public class Player {
    private String playerName;
    private String playerColor;
    private int setsTurnedIn;
    private ArrayList<Card> hand = new ArrayList<>();

    public Player(String name, String col) {
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
    public String getColor() { return playerColor; }
    public ArrayList<Card> getHand() { return hand; }
}
