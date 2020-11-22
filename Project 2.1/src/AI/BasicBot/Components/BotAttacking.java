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
    public Vertex[] attack(Graph g, Player p) {
        // Get the continent that you own the most (getting a continent)
        String cont = mostOwnedContinent(g, p);

        // Calculate bsr for bot-owned territories
        double[] bsr = new double[g.getSize()];
        for (int i = 0; i < g.getSize(); i++) {
            // Only calculate BSR for bot-owned territories
            if (g.get(i).getTerritory().getOwner().getPlayerIndex() == p.getPlayerIndex()) {
                bsr[i] = g.get(i).getBSR();
            } else {
                bsr[i] = -1;
            }
        }

        int atkIndex = getLowestInCont(bsr, cont);

        // Attack the neighbouring country with the least amount of troops
        LinkedList<Edge> edges = g.get(atkIndex).getEdges();
        int lowesttroops = 10000;
        int defIndex = 0;
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getVertex().getTerritory().getOwner().getPlayerIndex() != p.getPlayerIndex()) {
                if (edges.get(i).getVertex().getTerritory().getNumberOfTroops() < lowesttroops) {
                    lowesttroops = edges.get(i).getVertex().getTerritory().getNumberOfTroops();
                    defIndex = i;
                }
            }
        }
        int friendlyTroops = g.get(atkIndex).getTerritory().getNumberOfTroops();
        if (friendlyTroops > 3) {
            attackerDie = 3;
        }
        else if (friendlyTroops == 3) {
            attackerDie = 2;
        }
        else {
            attackerDie = 1;
        }

        Vertex[] duo = {g.get(atkIndex), edges.get(defIndex).getVertex()};
        return duo;
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
