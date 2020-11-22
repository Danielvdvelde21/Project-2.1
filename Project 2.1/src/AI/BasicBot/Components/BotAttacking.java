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
        // Idea: Give countries a grade based on how likely you want to attack them/which country you want to use to attack
        // Negative scores for enemy countries, positive scores for owned countries
        double[] countries = new double[g.getSize()];
        int atk = 0;
        int def = 0;

        //
        // Troop count
        // Negative score
        for (int i = 0; i < countries.length; i++) {
            if (g.get(i).getTerritory().getOwner() != p) {
                countries[i] -= g.get(i).getTerritory().getNumberOfTroops() / 10.0;
            }
        }

        // BSR
        // Border Security Risk = troops in surrounding enemy territories / troops on territory itself
        /*double[] bsr = new double[g.getSize()];
        for (int i = 0; i < g.getSize(); i++) {
            // Only calculate BSR for bot-owned territories
            if (g.get(i).getTerritory().getOwner() == p) {
                bsr[i] = g.get(i).getBSR();
            } else {
                bsr[i] = -1;
            }
        }*/

        // Continent
        // Extra continent = extra troops
        String cont = mostOwnedContinent(g, p);
        System.out.println(cont);
        int[] countriesInCont = continentDetector(cont);
        for (int i = 0; i < countriesInCont.length; i++) {
            // Check which countries are not yet owned by the bot in the continent
            if (g.get(countriesInCont[i]).getTerritory().getOwner() != p) {
                countries[countriesInCont[i]] += 5.0;
            }
        }


        // Cards
        // When turning a set in, get (max) 2 extra armies if you own one of the territories on the card
        // low weight
        ArrayList<Card> cards = p.getHand();
        if (p.getHand() != null) {
            for (int i = 0; i < cards.size(); i++) {
                for (int j = 0; j < g.getSize(); j++) {
                    // Check if an unowned territory is on one of the bot's cards
                    if (g.get(j).getTerritory().getTerritoryName().equals(cards.get(i).getCardName()) && g.get(j).getTerritory().getOwner() != p) {
                        countries[i] += 1.0;
                    }
                }
            }
        }

        double maxDef = countries[0];
        int maxDefIndex = 0;
        for (int i = 1; i < countries.length; i++) {
            if (countries[i] > maxDef) {
                maxDef = countries[i];
                maxDefIndex = i;
            }
        }
        LinkedList<Edge> edges = g.get(maxDefIndex).getEdges();
        int maxAtk = 0;
        int maxAtkIndex = 0;
        for(int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getVertex().getTerritory().getOwner() == p) {
                if (edges.get(i).getVertex().getTerritory().getNumberOfTroops() > maxAtk) {
                    maxAtk = edges.get(i).getVertex().getTerritory().getNumberOfTroops();
                    maxAtkIndex = i;
                }
            }
        }
        getAttackerDie();

        return new Vertex[]{edges.get(maxAtkIndex).getVertex(), g.get(maxDefIndex)};
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
        attackerDie = 3;
        return attackerDie;
    }
}
