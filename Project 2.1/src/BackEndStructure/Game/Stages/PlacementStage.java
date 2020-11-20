package BackEndStructure.Game.Stages;

import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Territory;
import Visualisation.Map.Components.CardInventory;
import Visualisation.Map.Components.Narrator;
import Visualisation.Map.Components.PlayerTurn;
import Visualisation.Map.Map;

public class PlacementStage {

    private final Game game;
    private final Map map;
    private final Narrator narrator;
    private final CardInventory cardInventory;
    private final PlayerTurn playerTurn;
    private final Graph graph;

    private int unownedTerritories = 42;
    private boolean noMoreUnownedTerritories = false;

    public PlacementStage(Game game) {
        this.game = game;
        this.map = game.getMap();
        this.narrator = game.getNarrator();
        this.cardInventory = game.getCardInventory();
        this.playerTurn = game.getPlayerTurn();
        this.graph = game.getGraph();
        cardInventory.setGame(game);
    }

    public void placementStage() {
        // For each player till all countries are chosen
        int round = 1;

        // Till all troops are placed
        while (round != game.getStartingTroops()) {
            for (Player p : game.getPlayers()) {
                narrator.addText("It's " + p.getName() + "'s turn to place down 1 troop");
                cardInventory.setCurrentPlayer(p);
                playerTurn.setPlayerTurn(p);
                placementCountryTurn(p);
            }
            round++;
        }
    }

    private void placementCountryTurn(Player player) {
        if (player.isBot()) {
            placeTroop(player, game.getAi().placementDecider(graph, player));
        } else {
            placeTroop(player, getSelectedTerritoryNumber(player));
        }
    }

    // Logic for whether a player can place down a troop on a territory
    private int getSelectedTerritoryNumber(Player player) {
        map.deselectTerritory();
        boolean validTerritoryChosen = false;

        while (!validTerritoryChosen) {
            delay();
            if (territorySelected(map)) {
                Territory t = graph.get(map.getTerritoryNumber()).getTerritory();
                if (t.getOwner()==null) {
                    validTerritoryChosen = true;
                } else if (isTerritoryOwnedBy(t, player) && noMoreUnownedTerritories) {
                    validTerritoryChosen = true;
                } else if (isTerritoryOwnedBy(t, player) && !noMoreUnownedTerritories) {
                    map.deselectTerritory();
                    narrator.addText("Please select unowned territories first!");
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

    // Places a number of troops on a territory
    private void placeTroop(Player player, int territoryNumber) {
        Territory t = graph.get(territoryNumber).getTerritory();

        // If the territory did not have an owner
        if (t.getOwner()==null) {
            // How many unowned territories are left?
            unownedTerritories--;
            unownedTerritoriesLeft();
            // Increase player territories owned
            player.increaseTerritoriesOwned();
            // Owner is now player
            t.setOwner(player);
            // Update map
            map.setTroopCountColor(territoryNumber, player);
        }

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

    private void unownedTerritoriesLeft() {
        if (unownedTerritories == 0) {
            noMoreUnownedTerritories = true;
        }
    }
}
