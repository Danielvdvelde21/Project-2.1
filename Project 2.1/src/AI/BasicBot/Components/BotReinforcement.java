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
        ArrayList<ArrayList<Vertex> > clusterList = new ArrayList<>();   //list of all clusters
        Map<Vertex, Vertex> buyerSellerMap = new HashMap<>();    //hashMap of possible 'buyers - sellers' from all clusters (K:buyer, V:seller)

        //finding clusterList
        ArrayList<Vertex> ownedTerritories = getOwnedVertices(g, p);
        for(Vertex ownedTerritory : ownedTerritories) {
            if(clusterDoesNotExist(clusterList, ownedTerritory)) {
                ArrayList<Vertex> cluster = new ArrayList<>();   //chain of all connected owned territories
                cluster.add(ownedTerritory);
                fillCluster(g, ownedTerritories, cluster, ownedTerritory);
                clusterList.add(cluster);
            }
        }

        for(ArrayList<Vertex> cluster : clusterList) {
            ArrayList<Vertex> possibleSellers = new ArrayList<>();  //list of all possible 'sellers' in the cluster
            for (Vertex vertex : cluster) {
                if (hasTroopsToSpare(vertex, g, p)) {
                    possibleSellers.add(vertex);
                }
            }
            if(possibleSellers.size() != 0) {
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

        if(buyerSellerMap.size() != 0) {
            //choosing 'buyer' from all possible 'buyers' in all clusters
            Vertex finalBuyer = buyerAuction(new ArrayList<>(buyerSellerMap.keySet()));
            Vertex finalSeller = buyerSellerMap.get(finalBuyer);
            setReinforcementTroops(finalSeller);
            return new Vertex[] {finalSeller, finalBuyer};
        }

        return null;
    }

    private boolean clusterDoesNotExist(ArrayList<ArrayList<Vertex>> clusterList, Vertex territory) {
        if(clusterList.size() > 0) {
            for(ArrayList<Vertex> cluster : clusterList) {
                for(Vertex vertex : cluster) {
                    if(vertex == territory) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void fillCluster(Graph g, ArrayList<Vertex> ownedTerritories, ArrayList<Vertex> cluster, Vertex ownedTerritory) {
        for(Vertex vertex : ownedTerritories) {
            if(g.isAdjecent(ownedTerritory, vertex) && !cluster.contains(vertex) ) {
                cluster.add(vertex);
                fillCluster(g, ownedTerritories, cluster, vertex);
            }
        }
    }

    private boolean hasTroopsToSpare(Vertex vertex, Graph g, Player p) {
        //returns true if all adjacent territories are owned by the player
        if (vertex.getTerritory().getNumberOfTroops() < 2) {
            return false;
        }
        for (int j = 0; j < g.getSize(); j++) {
            if (g.isAdjecent(vertex, g.get(j)) && g.get(j).getTerritory().getOwner() != p) {
                return false;
            }
        }
        return true;
    }

    private Vertex buyerAuction(ArrayList<Vertex> buyers) {
        if(buyers.size() == 1) {
            return buyers.get(0);
        }
        else {
            double[] territoryScores = new double[buyers.size()];
            int i = 0;
            for(Vertex buyer : buyers) {
                territoryScores[i] = buyer.getBSR();
                i++;
            }
            //returns vertex with the highest score
            return buyers.get(getHighest(territoryScores));
        }
    }

    private void setReinforcementTroops(Vertex finalSeller) {
        int sellerTroops = finalSeller.getTerritory().getNumberOfTroops();

        if(sellerTroops == 2) {
            reinforcementTroops = 1;
        }
        else {
            reinforcementTroops = sellerTroops - 2; //leaving 2 troops on 'seller' territory
        }
    }

    public int getReinforcementTroops() {
        return reinforcementTroops;
    }
}
