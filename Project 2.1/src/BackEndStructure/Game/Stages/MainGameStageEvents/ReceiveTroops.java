package BackEndStructure.Game.Stages.MainGameStageEvents;

import BackEndStructure.Entities.Cards.Card;
import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Territory;
import Visualisation.Map.Components.CardInventory;
import Visualisation.Map.Components.Narrator;
import Visualisation.Map.Map;

import java.util.ArrayList;

public class ReceiveTroops {

    private final Game game;
    private final Map map;
    private final Narrator narrator;
    private final CardInventory cardInventory;
    private final Graph graph;

    public ReceiveTroops(Game game) {
        this.game = game;
        this.map = game.getMap();
        this.narrator = game.getNarrator();
        this.cardInventory = game.getCardInventory();
        this.graph = game.getGraph();
        cardInventory.setGame(game);
    }

    public int receivedTroops(Player player) {
        // Troops for turning in cards
        int cards;
        if (player.isBot()) {
            // Set of cards the bot is going to turn in
            ArrayList<Card> turnInSet = game.getAi().getBotCards().cards(graph, player);

            // Return cards to stack
            if (turnInSet != null) {
                game.getCardStack().returnCards(turnInSet);

                // Remove cards from player hand
                player.getHand().removeAll(turnInSet);

                // Player has 1 more completed set
                player.incrementSetsOwned();

                cards = game.getSetValue(player.getSetsTurnedIn());
            } else {
                cards = 0;
            }
        } else {
            cards = turningInCards(player);
        }

        // Troops for territories owned
        int terri = player.getTerritoriesOwned() / 3;

        // Troops for continents owned
        game.hasContinents(player);
        int conti = game.getValueOfContinentsOwned(player.getContinentsOwned());


        narrator.addText("Player " + player.getName() + " received " + terri + " troop(s) from Territories, " + conti + " troop(s) from Continents and " + cards + " troop(s) from Cards");
        return cards + terri + conti;
    }

    private int turningInCards(Player player) {
        if(player.getHand().size() >= 3) {
            if (player.getHand().size() > 4) {
                narrator.addText("You have to turn in at least 1 set!");
            } else {
                narrator.addText("Do you want to turn in any sets?");
            }
            // Only now allow trading, player is not attacking
            cardInventory.tradingAllowed(true);
            cardInventory.attacking(false);

            cardInventory.getInventory();
            while (cardInventory.getMenuClosed()) {
                delay();
            }

            // For the next player reset trading
            cardInventory.tradingAllowed(false);

            return game.getSetValue(player.getSetsTurnedIn());
        }
        return 0;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Placing troops

    public void placeReceivedTroops(Player player, int troops) {
        narrator.addText("Player " + player.getName() + " can put " + troops + " troops on his territories");
        if (player.isBot()) {
            for (int i = 0; i < troops; i++) {
                game.getAi().getPlaceTroops().placeTroop(graph, player);
            }
        } else {
            for (int i = 0; i < troops; i++) {
                placementTurn(player);
            }
        }
    }

    private void placementTurn(Player player) {
        placeTroop(player, getSelectedTerritoryNumber(player));
    }

    private void placeTroop(Player player, int territoryNumber) {
        Territory t = graph.get(territoryNumber).getTerritory();

        // Add the troops to the territory
        t.setNumberOfTroops(t.getNumberOfTroops() + 1);

        // Update the Map
        narrator.addText(player.getName() + " put a troop on " + t.getTerritoryName());
        map.updateTroopCount(territoryNumber, t.getNumberOfTroops());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Extra Methods

    // Creates a delay
    private void delay() {
        try { Thread.sleep(100); } catch (InterruptedException ignored) {}
    }

    // If a territory is selected
    private boolean territorySelected(Map map) { return map.getTerritoryNumber() != -1; }

    // If a territory belongs to a player
    private boolean isTerritoryOwnedBy(Territory t, Player p) {
        return t.getOwner()==p;
    }

    // Logic for whether a player can place down a troop on a territory
    private int getSelectedTerritoryNumber(Player player) {
        map.deselectTerritory();
        boolean validTerritoryChosen = false;

        while (!validTerritoryChosen) {
            delay();
            if (territorySelected(map)) {
                Territory t = graph.get(map.getTerritoryNumber()).getTerritory();
                if (isTerritoryOwnedBy(t, player)) {
                    validTerritoryChosen = true;
                } else {
                    map.deselectTerritory();
                    narrator.addText("This territory already belongs to a player!");
                }
            }
        }

        // Update the map (so that no territory is selected)
        int num = map.getTerritoryNumber();
        map.deselectTerritory();
        return num;
    }
}
