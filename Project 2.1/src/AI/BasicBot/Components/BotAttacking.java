package AI.BasicBot.Components;

import BackEndStructure.Entities.Cards.Card;
import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Edge;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Vertex;

import java.util.ArrayList;
import java.util.LinkedList;

public class BotAttacking extends UsefulMethods {

    private int attackerDie;
    private int temp;

    // -----------------------------------------------------------------------------------------------------------------
    // Attacking

    /**
     * Based on the current board
     * Let the bot make a decision on if and how it wants to attack
     *
     * @param g This is the current board
     * @param p This is the current player turn
     * @return A vertex array with position 0 attacker and position 1 defender
     */
    public Vertex[] attackNew(Graph g, Player p) {
        // Idea: Give countries a grade based on how likely you want to attack them/which country you want to use to attack
        // Negative scores for enemy countries, positive scores for owned countries
        double[] countries = new double[g.getSize()];
        int atk = 0;
        int def = 0;

        for (int i = 0; i < countries.length; i++) {
            if (g.get(i).getTerritory().getOwner() == p) {
                countries[i] = g.get(i).getTerritory().getNumberOfTroops();
                atk = i;
            }
            else {
                countries[i] = - g.get(i).getTerritory().getNumberOfTroops();
                def = i;
            }
        }
        return new Vertex[]{g.get(atk), g.get(def)};
    }


    // Evaluate when bot stops attacking
    public boolean botWantsToAttack(Graph g, Player p) {
        if (temp == 5) {
            temp = 0;
            return false;
        }
        temp++;
        return true;
    }

    // How many troops will be sent over when a territory is captured
    public int getTroopCarryOver() {
        // If the attacking country is now only surrounded by friendly territories; send over all but one

        // Otherwise, it'll be tougher (BST?)
        return 1;
    }

    // If a bot eliminates a player and gets his cards --> the bot needs to turn in a set
    public ArrayList<Card> attackingCard(Graph g, Player p) {
        // TODO
        return null;
    }

    public int getAttackerDie() {
        return attackerDie;
    }
}
