package AI;

import BackEndStructure.Entities.Cards.Card;
import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Vertex;

import java.util.ArrayList;

public class AiTemplate {

    /**
     * Based on the current board and the amount of troops the bot can place down
     * Let the bot make a decision on where it should place troops
     * @param g This is the current board
     * @param p This is the current player turn
     * @param troops This is the number of troops the bot can place down
     * @return A vertex that a troop is placed on
     */
    public Vertex placeTroop(Graph g, Player p, int troops) {
        // TODO
        return null;
    }

    /**
     * Based on the current board
     * Let the bot make a decision on if and how it wants to attack
     * @param g This is the current board
     * @param p This is the current player turn
     * @return A vertex array with position 0 attacker and position 1 defender
     */
    public Vertex[] attack(Graph g, Player p) {
        // TODO
        // TODO set amount of die bot wants to use
        return null;
    }

    /**
     * Based on the current board
     * Let the AI make a decision on if and how it wants to reinforce its position
     * @param g This is the current board
     * @param p This is the current player turn
     * @return A vertex array with position 0 from and position 1 to
     */
    public Vertex[] reinforce(Graph g, Player p) {
        // TODO
        return null;
    }

    /**
     * Based on the bots hand (p.getHand()) and the current board
     * Let the bot make a decision on if and how it wants to return its cards
     * @param g This is the current board
     * @param p This is the current player turn
     * @return a list of cards that the bot wants to turn in
     */
    public ArrayList<Card> cards(Graph g, Player p) {
        // TODO
        return null;
    }

}
