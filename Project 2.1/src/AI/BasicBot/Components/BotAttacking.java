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
        Edge[] edges;
        for (int i = 0; i < g.getSize(); i++) {
            if (g.get(i).getTerritory().getOwner() != p) {
                attackable = false;
                Edge[] neighbours = g.get(i).getEdges();
                int neighboursNo = g.get(i).getEdgeNo();
                for (int j=0;j<neighboursNo;j++) {
                    Edge e=neighbours[j];
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
                if (g.get(j).getTerritory().getOwner() == p && g.isAdjacent(g.get(j), g.get(i))) {
                    grades[i] += 0.4;
                }
            }
            // A flat bonus for there being very little troops on the country
            if(g.get(i).getTerritory().getNumberOfTroops() < 3){
                grades[i] += 0.6;
            }
        }

        // end goal chooser
        boolean bestAttack = false;
        int bestAttackers = 0;
        int bestTarget = 0;
        int bestAttackIsFrom = 0;
        int counter = 0;
        while(!bestAttack && counter < 50){
            bestAttackers = 0;
            bestTarget = getHighest(grades);
            for(int i = 0; i < g.getSize(); i++) {
                if (g.get(i).getTerritory().getOwner() == p && g.isAdjacent(g.get(bestTarget), g.get(i))) {
                    int attackers = g.get(i).getTerritory().getNumberOfTroops();

                    if (attackers > bestAttackers && attackers > 1) {
                        bestAttackIsFrom = i;
                        bestAttackers = g.get(i).getTerritory().getNumberOfTroops();
                    }
                }
            }
            int defenders = g.get(bestTarget).getTerritory().getNumberOfTroops();
            if(defenders > bestAttackers){
                grades[bestTarget] += -1000;
            }
            else if(getHighest(grades) == bestTarget && g.isAdjacent(g.get(bestTarget), g.get(bestAttackIsFrom))){
                grades[bestTarget] += (bestAttackers - defenders) * 0.1;
                grades[bestTarget] += (bestAttackers/defenders) * 0.5;
                bestAttack = true;
            }
            else{
                bestAttack = false;
            }
            counter++;
        }

        setAttackerDie(g.get(bestAttackIsFrom));

        if(grades[bestTarget] > 2 && counter < 50) {
            // Checking for bugs
            if (!g.isAdjacent(g.get(bestAttackIsFrom), g.get(bestTarget))) {
                System.out.println(g.get(bestAttackIsFrom).getTerritory().getTerritoryName() + " is trying to attack " + g.get(bestTarget).getTerritory().getTerritoryName());
                throw new IllegalArgumentException("Attacker and Defender aren't adjacent");
            }
            if (g.get(bestAttackIsFrom) == g.get(bestTarget)) {
                throw new IllegalArgumentException("Attacker = Defender, index is " + bestAttackIsFrom);
            }
            return new Vertex[]{g.get(bestAttackIsFrom), g.get(bestTarget)};
        }
        return new Vertex[]{null, null};

    }

    // Evaluate when bot stops attacking
    public boolean botWantsToAttack(Graph g, Player p) {
        Vertex[] vertices = attack(g, p);
        return vertices[0] != null;
    }

    // How many troops will be sent over when a territory is captured
    public int getTroopCarryOver(Vertex attacker) {
        // Two situations
        Edge[] neighbours = attacker.getEdges();
        int neighboursNo = attacker.getEdgeNo();
        boolean friendlyNeighbours = true;
        for (int i=0;i<neighboursNo;i++) {
            Edge e=neighbours[i];
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
        ArrayList<Card> infSet = new ArrayList<>();
        ArrayList<Card> cavSet = new ArrayList<>();
        ArrayList<Card> artSet = new ArrayList<>();


        for (int i = 0; i < p.getHand().size(); i++) {
            switch (p.getHand().get(i).getCardType()) {
                case "WILDCARD":
                    infSet.add(p.getHand().get(i));
                    cavSet.add(p.getHand().get(i));
                    artSet.add(p.getHand().get(i));
                    break;
                case "Infantry":
                    infSet.add(p.getHand().get(i));
                    break;
                case "Cavalry":
                    cavSet.add(p.getHand().get(i));
                    break;
                case "Artillery":
                    artSet.add(p.getHand().get(i));
            }
        }
        //TODO: Clean this one up, look for owned territories when turning in, try to keep wildcards if possible

        if (infSet.size() == 3) {
            return infSet;
        }
        if (infSet.size() > 3) {
            while (infSet.size() > 3) {
                infSet.remove(3);
            }
            return infSet;
        }
        if (cavSet.size() == 3) {
            return cavSet;
        }
        if (cavSet.size() > 3) {
            while (cavSet.size() > 3) {
                cavSet.remove(3);
            }
            return cavSet;
        }
        if (artSet.size() == 3) {
            return artSet;
        }
        if (artSet.size() > 3) {
            while (artSet.size() > 3) {
                artSet.remove(3);
            }
            return artSet;
        }
        if (p.getHand().size() >= 5) {
            ArrayList<Card> mixed = new ArrayList<>();
            mixed.add(infSet.get(0));
            mixed.add(cavSet.get(0));
            mixed.add(artSet.get(0));
            return mixed;
        }
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