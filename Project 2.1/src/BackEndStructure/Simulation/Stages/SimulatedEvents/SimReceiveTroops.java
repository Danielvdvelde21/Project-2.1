package BackEndStructure.Simulation.Stages.SimulatedEvents;

import BackEndStructure.Entities.Cards.Card;
import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Territory;
import BackEndStructure.Graph.Vertex;

import java.util.ArrayList;
import java.util.Random;

public class SimReceiveTroops {

    private final Game game;
    private final Graph graph;

    public SimReceiveTroops(Game game) {
        this.game = game;
        this.graph = game.getGraph();
    }

    public int receivedTroops(Player player) {
        // Troops for turning in cards
        int cards = 0;

        // Cards are neglected in MCTS for now
//        // Set of cards the bot is going to turn in
//        ArrayList<Card> turnInSet = game.getAi().getBotCards().cards(graph, player);
//
//        // Return cards to stack
//        if (turnInSet != null) {
//            game.getCardStack().returnCards(turnInSet);
//
//            // Remove cards from player hand
//            player.getHand().removeAll(turnInSet);
//
//            // Player has 1 more completed set
//            player.incrementSetsOwned();
//
//            cards = game.getSetValue(player.getSetsTurnedIn());
//        } else {
//            cards = 0;
//        }

        // Troops for territories owned
        int terri = player.getTerritoriesOwned() / 3;
        // Min 3 troops
        if (terri < 3) {
            terri = 3;
        }

        // Troops for continents owned
        game.hasContinents(player);
        int conti = game.getValueOfContinentsOwned(player.getContinentsOwned());

        return cards + terri + conti;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Placing troops

    public void placeReceivedTroops(Player player, int troops) {
        for (int i = 0; i < troops; i++) {
            placeTroopRandomly(player);
        }
    }

    private void placeTroopRandomly(Player player) {
        // Get all the owned territories for this player
        ArrayList<Vertex> ownedTerritories = new ArrayList<>();
        for (Vertex v : graph.getArrayList()) {
            if (v.getTerritory().getOwner() == player) {
                ownedTerritories.add(v);
            }
        }

        // Select a random territory
        Random random = new Random();
        Territory t = ownedTerritories.get(random.nextInt(ownedTerritories.size())).getTerritory();

        // place a troop on the random territory
        t.setNumberOfTroops(t.getNumberOfTroops() + 1);
    }

}
