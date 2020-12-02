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
        // TODO make sure the attack finishes -> in other words, don't want to switch to other target in middle of attack

        double[] grades = new double[g.getSize()];
        // Get the countries you're able to attack: enemy-owned and adjacent to a friendly territory
        boolean attackable;
        LinkedList<Edge> edges;
        for (int i = 0; i < g.getSize(); i++) {
            if (g.get(i).getTerritory().getOwner() != p) {
                attackable = false;
                edges = g.get(i).getEdges();
                for (Edge e : edges) {
                    if (e.getVertex().getTerritory().getOwner() == p) {
                        attackable = true;
                        break;
                    }
                }
                if (!attackable) {
                    grades[i] = -999.0;
                }
            }
            else {
                grades[i] = -999.0;
            }
        }


        // Continent
        // Extra continent = extra troops
        String cont = mostOwnedContinent(g, p);
        int[] countriesInCont = continentDetector(cont);
        for (int i = 0; i < countriesInCont.length; i++) {
            grades[i] += 2.0;
        }

        // Continent denial
        String[] allContinents = getContinents();
        int[] contIndex;
        Player enemy;
        boolean hundred;
        for (int i = 0; i < allContinents.length; i++) {
            if (!allContinents[i].equals(cont)) {
                hundred = true;
                contIndex = continentDetector(allContinents[i]);
                enemy = g.get(contIndex[0]).getTerritory().getOwner();
                for (int j = 1; j < contIndex.length; j++) {
                    if (g.get(contIndex[j]).getTerritory().getOwner() != enemy) {
                        hundred = false;
                    }
                }
                if (hundred) {
                    for (int j = 0; j < contIndex.length; j++) {
                        grades[contIndex[j]] += 1.0;
                    }
                }
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
                        grades[i] += 0.5;
                    }
                }
            }
        }

        for(int i = 0; i < g.getSize(); i++){
            // A flat bonus for the enemy player being weak, weak being defined as few countries.
            Player temp = g.get(i).getTerritory().getOwner();
            int tempLands = 0;
            for(int j = 0; j < g.getSize(); j++){
                if(g.get(j).getTerritory().getOwner() == temp){
                    tempLands += 1;
                }
            }
            if(tempLands < 2){
                grades[i] += 3;
            }
            else if(tempLands <4){
                grades[i] += 1;
            }
            else if(tempLands < 6){
                grades[i] += 0.5;
            }
            // A flat bonus for owning neighbouring countries to the one you are attacking.
            for (int j = 0; j < g.getSize(); j++) {
                if (g.get(j).getTerritory().getOwner() == p && g.isAdjecent(g.get(j), g.get(i))) {
                    grades[i] += 0.4;
                }
            }
            // A flat bonus for there being very little troops on the country
            if(g.get(i).getTerritory().getNumberOfTroops() < 3){
                grades[i] += 0.6;
            }
        }


        /*double maxAtk = 0.0;
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
        double minDef = 9999.0;
        int minDefIndex = 0;
        for(int i = 0; i < maxAtkNeighbours.size(); i++) {
            if (maxAtkNeighbours.get(i).getVertex().getTerritory().getOwner() != p) {
                if (grades[i] < minDef && grades[i] > 0.0) {
                    minDef = grades[i];
                    minDefIndex = i;
                }
            }
        }*/

        // end goal chooser
        boolean bestAttack = false;
        int bestAttackers = 0;
        int bestTarget = 0;
        int bestAttackIsFrom = 0;
        while(bestAttack){
            bestTarget = getHighest(grades);
            for(int i = 0; i < g.getSize(); i++) {
                if (g.get(i).getTerritory().getOwner() == p && g.isAdjecent(g.get(bestTarget), g.get(i))) {
                    int attackers = g.get(i).getTerritory().getNumberOfTroops();
                    bestAttackIsFrom = i;
                    if (attackers > bestAttackers) {
                        bestAttackers = g.get(i).getTerritory().getNumberOfTroops();
                    }
                }
            }
            int defenders = g.get(bestTarget).getTerritory().getNumberOfTroops();
            if(defenders > bestAttackers){
                grades[bestTarget] += -1000;
            }
            else if(getHighest(grades) == bestTarget){
                grades[bestTarget] += (bestAttackers - defenders) * 0.1;
                grades[bestTarget] += (bestAttackers/defenders) * 0.5;
                bestAttack = true;
            }
            else{
                bestAttack = false;
            }
        }

        setAttackerDie(g.get(bestAttackIsFrom));

        if(grades[bestTarget] > 2) {
            return new Vertex[]{g.get(bestAttackIsFrom), g.get(bestTarget)};
        }
        return new Vertex[]{null, null};

    }

    // Evaluate when bot stops attacking
    public boolean botWantsToAttack(Graph g, Player p) {
        // I don't like the way I did this
        ArrayList<Vertex> territories = getOwnedVertices(g, p);
        boolean minTroop = false;
        for (int i = 0; i < territories.size(); i++) {
            if (territories.get(i).getTerritory().getNumberOfTroops() > 3) {
                minTroop = true;
                break;
            }
        }
        return minTroop;
        // don't suicidebomb
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
        // TODO 2.  Figure out whether other neighbour enemy territories are worth capturing, for now just send 1 over
        else {
            return 1;
        }
    }

    // If a bot eliminates a player and gets his cards --> the bot needs to turn in a set
    public ArrayList<Card> attackingCard(Graph g, Player p) {
        // TODO Can't we use the basic method we're already using for turning in cards?
        if (p.getHand().size() >= 6) {
            // trade in cards
            return null;
        }
        else {
            return null;
        }
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