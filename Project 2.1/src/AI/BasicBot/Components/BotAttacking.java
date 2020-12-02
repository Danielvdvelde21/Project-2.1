package AI.BasicBot.Components;

import BackEndStructure.Entities.Cards.Card;
import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Edge;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Territory;
import BackEndStructure.Graph.Vertex;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

public class BotAttacking extends UsefulMethods {

    private int attackerDie;

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
        // Attack and defensive strength based on troops
        // Find weakest defender around strongest attacker and strongest attacker around weakest defender
        // Decide which of the two would be the better attack
        // TODO make sure the attack finishes -> in other words, don't want to switch to other target in middle of attack

        double[] grades = new double[g.getSize()];

        // Eliminate enemy territories that are not connected to any friendly territories and
        // friendly territories only surrounded by other friendly territories
        // Also eliminate owned territories with just 1 troop
        boolean owned;
        boolean surrounded;
        boolean singleTroop;
        LinkedList<Edge> edges;
        for (int i = 0; i < grades.length; i++) {
            surrounded = true;
            edges = g.get(i).getEdges();
            owned = g.get(i).getTerritory().getOwner() == p;
            for (Edge e : edges) {
                if (owned) {
                    if (e.getVertex().getTerritory().getOwner() != p) {
                        surrounded = false;
                    }
                }
                else {
                    if (e.getVertex().getTerritory().getOwner() == p) {
                        surrounded = false;
                    }
                }
            }
            singleTroop = g.get(i).getTerritory().getNumberOfTroops() <= 1 && g.get(i).getTerritory().getOwner() == p;
            if (surrounded || singleTroop) {
                // Arbitrarily big negative score; any negative scores will be disregarded during decision making
                grades[i] = -9999;
            }
        }

        // Troop count
        // Add to score for the amount of troops on the territory
        for (int i = 0; i < grades.length; i++) {
            grades[i] += g.get(i).getTerritory().getNumberOfTroops();
        }

        // Continent
        // Extra continent = extra troops
        String cont = mostOwnedContinent(g, p);
        int[] countriesInCont = continentDetector(cont);
        for (int i = 0; i < countriesInCont.length; i++) {
            // Check which countries are not yet owned by the bot in the continent
            if (g.get(countriesInCont[i]).getTerritory().getOwner() != p) {
                grades[countriesInCont[i]] *= 0.8;
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
                        grades[i] *= 0.9;
                    }
                }
            }
        }

        double maxAtk = 0.0;
        int maxAtkIndex = 0;

        ArrayList<Vertex> ownedTerritories = getOwnedVertices(g, p);
        for (int i = 1; i < grades.length; i++) {
            for (Vertex v : ownedTerritories) {
                if (g.get(i) == v) {
                    if (grades[i] > maxAtk) {
                        maxAtk = grades[i];
                        maxAtkIndex = i;
                    }
                }
            }
        }

        LinkedList<Edge> maxAtkNeighbours = g.get(maxAtkIndex).getEdges();
        int minDef = 9999;
        int minDefIndex = 0;
        for(int i = 0; i < maxAtkNeighbours.size(); i++) {
            if (maxAtkNeighbours.get(i).getVertex().getTerritory().getOwner() != p) {
                if (grades[i] < minDef && grades[i] > 0.0) {
                    minDef = maxAtkNeighbours.get(i).getVertex().getTerritory().getNumberOfTroops();
                    minDefIndex = i;
                }
            }
        }

        setAttackerDie(g.get(maxAtkIndex));
        return new Vertex[]{g.get(maxAtkIndex), maxAtkNeighbours.get(minDefIndex).getVertex()};
    }

    // Evaluate when bot stops attacking
    public boolean botWantsToAttack(Graph g, Player p) {
        ArrayList<Vertex> territories = getOwnedVertices(g, p);
        boolean minTroop = false;
        for (int i = 0; i < territories.size(); i++) {
            if (territories.get(i).getTerritory().getNumberOfTroops() > 3) {
                minTroop = true;
                break;
            }
        }
        return minTroop;
    }

    // How many troops will be sent over when a territory is captured
    public int getTroopCarryOver(Vertex attacker) {
        // Two situations
        LinkedList<Edge> neighbours = attacker.getEdges();
        boolean friendlyNeighbours = true;
        for (Edge e : neighbours) {
            if (e.getVertex().getTerritory().getOwner() != attacker.getTerritory().getOwner()) {
                friendlyNeighbours = false;
                break;
            }
        }
        // 1. Captured territory was only enemy territory connected to attacking territory; can send over all but 1
        if (friendlyNeighbours) {
            return attacker.getTerritory().getNumberOfTroops() - 1;
        }
        // TODO 2.  Figure out whether other neighbour enemy territories are worth capturing
        else {
            return 1;
        }
    }

    // If a bot eliminates a player and gets his cards --> the bot needs to turn in a set
    public ArrayList<Card> attackingCard(Graph g, Player p) {
        // TODO Can't we use the basic method we're already using for turning in cards?
        return null;
    }

    public void setAttackerDie(Vertex v) {
        int troops = v.getTerritory().getNumberOfTroops();
        if (troops > 3) {
            attackerDie = 3;
        }
        else if(troops == 3) {
            attackerDie = 2;
        }
        else {
            attackerDie = 1;
        }
    }

    public int getAttackerDie() { return attackerDie; }
}
