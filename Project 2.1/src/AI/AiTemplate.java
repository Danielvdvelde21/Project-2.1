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
        // Each territory gets a score, the bot will place troops on the territory with the highest score
        int[] scores = new int[g.getSize()];

        // Sum of all the enemy troops that a given territory is surrounded by
        int surroundingTroops = 0;

        // Iterate over each Vertex (territory)
        for (int i = 0; i < g.getSize(); i++) {
            // Iterate over each adjacent territory
            for (int j = 0; j < g.get(i).getEdges().size(); j++) {
                // If the territory does not belong to the player, add the troops of that territory to surrounding troops
                if (!p.getName().equals(g.get(i).getEdges().get(j).getVertex().getTerritory().getOwner())) {
                    surroundingTroops += g.get(i).getEdges().get(j).getVertex().getTerritory().getNumberOfTroops();
                }
            }
            // Border Security Threat
            scores[i] = g.get(i).getTerritory().getNumberOfTroops() / surroundingTroops;
            surroundingTroops = 0;
        }
        // Find highest score and the index of the highest score
        int maxScore = scores[0];
        int indexMaxScore = 0;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] > maxScore) {
                maxScore = scores[i];
                indexMaxScore = i;
            }
        }
        // TODO weights
        // Put the troops on the territory with the highest score
        g.get(indexMaxScore).getTerritory().setNumberOfTroops(g.get(indexMaxScore).getTerritory().getNumberOfTroops() + troops);
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
        double[] bsr = new double[42];
        // Bot-owned
        for(int i = 0; i < 42; i++) {
            // Only calculate BSR for bot-owned territories
            if (g.get(i).getTerritory().getOwner().equals(p.getName())) {
                bsr[i] = g.get(i).getBSR();
            }
            else { bsr[i] = -1; }
        }

        double[] select = {0, 0};
        for (int i = 0; i < 42; i++) {
            if (bsr[i] != -1) {
                if (select[0] == 0 || select[0] > bsr[i]) {
                    select[0] = bsr[i];
                    select[1] = i;
                }
            }
        }
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

}
