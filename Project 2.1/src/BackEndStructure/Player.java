package BackEndStructure;

import BackEndStructure.Cards.Card;
import java.util.ArrayList;

public class Player {
    private int startingTroops;
    private String playerName;
    private int setsTurnedIn;
    private final ArrayList<Card> hand = new ArrayList<>();

    public Player(int troops, String name) {
        this.setsTurnedIn = 0;
        this.playerName = name;
        this.startingTroops = troops;
    }

    public void addCardToHand(Card c) {
        hand.add(c);
    }

    public void turnInSet(Card[] cards, ArrayList<Card> stack) {
        // TODO
    }
}
