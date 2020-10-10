package BackEndStructure.Game;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Territory;
import Visualisation.Map;
import Visualisation.Narrator;

public class MainGameLoop {
    private final Game game;
    private final Map map;
    private final Graph graph;

    // For updating the storyteller
    private Narrator narrator = new Narrator();

    // Game state
    private boolean gameOver = false;

    public MainGameLoop(int players, String[] playerNames) {
        this.game = new Game(players, playerNames);
        this.map = game.getMap();
        this.graph = game.getGraph();

        placementStage();
        while(!gameOver) {
            for (Player p : game.getPlayers()) {
                playerTurn(p);
            }
        }
    }

    private void placementStage() {
        narrator.addText("Placement phase");

        // For each player, for StartingTroops amount of rounds
        int round = 1;
        while (round != game.getStartingTroops()) {
            for (Player p : game.getPlayers()) {
                narrator.addText("It's " + p.getName() + "'s turn to place down 1 troop");
                placeTroop(p);
            }
            round++;
        }
    }

    // Logic for whether a player can place down a troop on a territory
    private void placeTroop(Player player) {
        boolean validTerritoryChosen = false;

        while (!validTerritoryChosen) {
            try { Thread.sleep(100); } catch (InterruptedException e) {}
            // If a territory is selected
            if (map.getTerritoryNumber() != -1) {
                // Has the player selected one of his own territories or has he selected an unowned territory
                if(graph.get(map.getTerritoryNumber()).getTerritory().getOwner().equals("unowned") || graph.get(map.getTerritoryNumber()).getTerritory().getOwner().equals(player.getName())) {
                    validTerritoryChosen = true;
                } else {
                    map.deselectTerritory();
                    narrator.addText("This territory already belongs to a player!");
                }
            }
        }

        // Update Territories
        // If the territory did not have an owner, set it to player
        if (graph.get(map.getTerritoryNumber()).getTerritory().getOwner().equals("unowned")) {
            graph.get(map.getTerritoryNumber()).getTerritory().setOwner(player.getName());
        }
        graph.get(map.getTerritoryNumber()).getTerritory().setNumberOfTroops(graph.get(map.getTerritoryNumber()).getTerritory().getNumberOfTroops()+1);


        // Update the Map
        narrator.addText(player.getName() + " put a troop on " +  graph.get(map.getTerritoryNumber()).getTerritory().getTerritoryName());
        map.updateTroopCount(map.getTerritoryNumber(),  graph.get(map.getTerritoryNumber()).getTerritory().getNumberOfTroops());
        map.deselectTerritory();
    }

    private void playerTurn(Player player) {
        Territory attackingTerritory;

        while(!map.hasTurnEnded()) {
           // Cards TODO

           // Attacking TODO
            if (map.getTerritoryNumber() != -1 ) {
                if (graph.get(map.getTerritoryNumber()).getTerritory().getOwner().equals(player.getName())) {
                    attackingTerritory = graph.get(map.getTerritoryNumber()).getTerritory();
                    // attack, check adjacency!
                } else {
                    narrator.addText("please choose a territory that belongs to you to attack another player!");
                }
            }
        }
        fortifyTerritories(player);
        map.resetTurnEnd();
    }

    private void fortifyTerritories(Player p) {
        // TODO
        // while (!fortified) {
        //      logic for fortification
        // }
    }
}
