package BackEndStructure;

import BackEndStructure.Cards.Card;
import java.util.ArrayList;

public class Player {
    private String playerName;
    private int setsTurnedIn;
    private ArrayList<Card> hand = new ArrayList<>();

    public Player(String name) {
        this.playerName = name;
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
}
