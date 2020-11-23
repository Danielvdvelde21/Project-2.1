package AI.BasicBot.Components;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BotReinforcement extends UsefulMethods {

    private int reinforcementTroops;

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

        reinforcementTroops = 0;
        Vertex to = null;
        Vertex from;
        int totalTroops;
        int totalTroopsMax = 0;

        //'aggressive' strategy
        for (int i = 0; i < g.getSize(); i++) {
            if (g.get(i).getTerritory().getOwner()==p) {    //checks territories owned by player
                totalTroops = g.get(i).getTerritory().getNumberOfTroops();
                for(int j = 0; j < g.getSize(); j++) {
                    if (g.isAdjecent(g.get(i), g.get(j)) && g.get(j).getTerritory().getNumberOfTroops() > 1 && g.get(j).getTerritory().getOwner()==p) {
                        //checks all adjacent territories owned by player with > 1 troops
                        totalTroops += g.get(j).getTerritory().getNumberOfTroops(); //total adjacent troops of the territory
                    }
                }
                if(totalTroops > totalTroopsMax) {  //chooses the 'to' territory with the most total adjacent troops
                    totalTroopsMax = totalTroops;
                    to = g.get(i);
                }
            }
        }

        //determining 'from' territory
        if(to != null) {
            for (int n = 0; n < g.getSize(); n++) {
                if (g.isAdjecent(to, g.get(n)) && g.get(n).getTerritory().getNumberOfTroops() > 1 && g.get(n).getTerritory().getOwner()==p) {
                    reinforcementTroops = g.get(n).getTerritory().getNumberOfTroops() - 1;
                    if(g.get(n).getTerritory().getNumberOfTroops() - reinforcementTroops > 0) {
                        from = g.get(n);
                        return new Vertex[] {from, to};
                    }
                }
            }
        }

        return null;
    }

    public Vertex[] reinforceDefense(Graph g, Player p) {
        //TODO find all clusters
        ArrayList<Vertex> cluster = new ArrayList<>();   //chain of all connected owned territories
        ArrayList<ArrayList<Vertex> > clusterList = new ArrayList<>();   //list of all owned clusters

        //hashMap of possible 'buyers - sellers' from all clusters
        Map<Vertex, Vertex> buyerSellerMap = new HashMap<>();    //key: buyer, value: seller

        for(ArrayList<Vertex> cl : clusterList) {
            ArrayList<Vertex> possibleSellers = new ArrayList<>();  //list of all possible 'sellers' in the cluster
            for (Vertex vertex : cl) {
                if (hasTroopsToSpare(vertex)) {
                    possibleSellers.add(vertex);
                }
            }
            if(possibleSellers != null) {
                for (Vertex posSeller : possibleSellers) {
                    ArrayList<Vertex> adj = new ArrayList<>();  //all adjacent territories to 'seller' owned by player
                    for (int j = 0; j < g.getSize(); j++) {
                        if (g.isAdjecent(posSeller, g.get(j)) && g.get(j).getTerritory().getOwner() == p) {
                            adj.add(g.get(j));
                        }
                    }
                    Vertex buyer = buyerAuction(adj);    //most needed reinforcement
                    if(buyer != null) {
                        buyerSellerMap.put(buyer, posSeller);
                    }
                }
            }
        }

        if(buyerSellerMap != null) {
            //choosing 'buyer' from all possible 'buyers' in all clusters
            Vertex finalBuyer = buyerAuction(new ArrayList<>(buyerSellerMap.keySet()));
            Vertex finalSeller = buyerSellerMap.get(finalBuyer);
            setReinforcementTroops(finalSeller, finalBuyer);
            return new Vertex[] {finalSeller, finalBuyer};
        }

        return null;
    }

    private boolean hasTroopsToSpare(Vertex vertex) {   //TODO consider threat
        if (vertex.getTerritory().getNumberOfTroops() > 1) {
            return true;
        }
        return false;
    }

    private Vertex buyerAuction(ArrayList<Vertex> buyers) {    //TODO
        //returns territory (vertex) that needs reinforcement the most
        if(buyers.size() == 1) {
            return buyers.get(0);
        }
        else {

        }
        return null;
    }

    private void setReinforcementTroops(Vertex finalSeller, Vertex finalBuyer) {
        //TODO
        int sellerTroops = finalSeller.getTerritory().getNumberOfTroops();
        int buyerTroops = finalBuyer.getTerritory().getNumberOfTroops();
        reinforcementTroops = 1;
    }

    public int getReinforcementTroops() {
        return reinforcementTroops;
    }
}
