package BackEndStructure;

import java.util.ArrayList;

public class Player {
    private int startingTroops;
    private String playerName;
    private final ArrayList<Card> hand = new ArrayList<>();

    public Player(int troops, String name) {
        this.playerName = name;
        this.startingTroops = troops;
    }

    public void addCard(Card c) {
        hand.add(c);
    }
}
