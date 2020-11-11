package AI;

import BackEndStructure.Entities.Cards.Card;
import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Territory;
import BackEndStructure.Graph.Vertex;

import java.util.ArrayList;

public class AiTemplate {

    private int reinforcementTroops;
    private int attackerDie;

    // -----------------------------------------------------------------------------------------------------------------
    // Placing troops

    /**
     * Based on the current board and the amount of troops the bot can place down
     * Let the bot make a decision on where it should place troops
     *
     * @param g      This is the current board
     * @param p      This is the current player turn
     * @param troops This is the number of troops the bot can place down
     */
    public void placeTroop(Graph g, Player p, int troops) {
        // Each territory gets a bsr score
        double[] bsr = new double[g.getSize()];

        // For each vertex (territory) that is owned by the bot calculate its BSR
        for(int i = 0; i < g.getSize(); i++) {
            // Only calculate BSR for bot-owned territories
            if (g.get(i).getTerritory().getOwner().equals(p.getName())) {
                bsr[i] = g.get(i).getBSR();
            } else {
                bsr[i] = -1;
            }
        }
        // Get the index of the lowest BSR in bsr[]
        int indexLowestBSR = getLowest(bsr);

        // Put the troops on the territory with the highest score
        g.get(indexLowestBSR).getTerritory().setNumberOfTroops(g.get(indexLowestBSR).getTerritory().getNumberOfTroops() + troops);
    }

    /**
     * This method is specifically for placing troops at the start of the game
     * Based on the current board
     * Let the bot make a decision on where it should place a troop
     *
     * @param g This is the current board
     * @param p This is the current player turn
     */
    public void placeTroopStartOfGame(Graph g, Player p) {
        // TODO
    }

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
        // TODO
        // Ways to determine attack:
        // BSR (problem is when country is surrounded by a lot of countries)
        double[] bsr = new double[g.getSize()];
        // Bot-owned
        for(int i = 0; i < g.getSize(); i++) {
            // Only calculate BSR for bot-owned territories
            if (g.get(i).getTerritory().getOwner().equals(p.getName())) {
                bsr[i] = g.get(i).getBSR();
            }
            else { bsr[i] = -1; }
        }

        double index = getLowest(bsr);
        // select has the bsr and vertex-id (number) of the territory with the most offensive power/lowest bsr



        // Relative amount of troops compared to enemy countries around it (more 1 on 1 comparison)

        // Also need to decide when to stop attacking
        // If you keep on attacking indefinetely, would end up with 1 troop on every country
        attackerDie = 1;
        return null;
    }

    // Evaluate when bot stops attacking
    public boolean botWantsToAttack(Graph g, Player p) {
        // TODO
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

    // -----------------------------------------------------------------------------------------------------------------
    // Fortifying

    /**
     * Based on the current board
     * Let the AI make a decision on if and how it wants to reinforce its position
     *
     * @param g This is the current board
     * @param p This is the current player turn
     * @return A vertex array with position 0 from and position 1 to
     */
    public Vertex[] reinforce(Graph g, Player p) {
        // TODO
        reinforcementTroops = 1;
        return null;
    }

    public int getReinforcementTroops() {
        return reinforcementTroops;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Trading cards

    /**
     * Based on the bots hand (p.getHand()) and the current board
     * Let the bot make a decision on if and how it wants to return its cards
     *
     * @param g This is the current board
     * @param p This is the current player turn
     * @return a list of cards that the bot wants to turn in
     */
    public ArrayList<Card> cards(Graph g, Player p) {
        // TODO
        return null;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Extra methods

    // Find index of lowest value in double[]
    private int getLowest(double[] list) {
        double lowest = list[0];
        int indexLowestScore = 0;
        for (int i = 1; i < list.length; i++) {
            if (list[i] != -1) {
                if (lowest == 0 || lowest > list[i]) {
                    lowest = list[i];
                    indexLowestScore = i;
                }
            }
        }
        return indexLowestScore;
    }

}
