package BackEndStructure.Game;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Vertex;
import Visualisation.Map;
import Visualisation.Narrator;

public class MainGameLoop {
    private final Game game;
    // Variables in game that get used a lot
    private final Map map;
    private final Graph graph;

    // For updating the storyteller
    private final Narrator narrator = new Narrator();

    // Game state
    private boolean gameOver = false;

    public MainGameLoop(int players, String[] playerNames) {
        this.game = new Game(players, playerNames);
        this.map = game.getMap();
        this.graph = game.getGraph();

        // The game starts by every player starting to place troops on the board
        // TODO who goes first?
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
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
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
        // Check if the player got more than 4 cards in his hand
        if (player.getHand().size() > 4) {
            // Player must turn in at least 1 set
            // TODO Player must selected cards from his hand and turn in a set requires need frontend
        }

        Vertex attacker;
        while(!map.hasTurnEnded()) {
           // Cards TODO
            
            if (map.getTerritoryNumber() != -1 ) {
                if (graph.get(map.getTerritoryNumber()).getTerritory().getOwner().equals(player.getName())) {
                    attacker = graph.get(map.getTerritoryNumber());
                    while (graph.get(map.getTerritoryNumber()) == attacker) {
                        try { Thread.sleep(100); } catch (InterruptedException ignored) {}
                    }
                    Vertex defender = graph.get(map.getTerritoryNumber());
                    if (graph.isAdjecent(attacker, defender)) {
                        // TODO attack need visual buttons
                    } else {
                        map.deselectTerritory();
                        narrator.addText("These territories are not adjacent to each other");
                    }
                } else {
                    narrator.addText("please choose a territory that belongs to you to attack another player!");
                }
            }
        }
        // At the end of a turn a player can fortify 1 territory if he chooses
        fortifyTerritories(player);
        map.resetTurnEnd();
    }

    private void fortifyTerritories(Player player) {
        boolean fortified = false;

        while (!map.hasTurnEnded() || fortified) {
            if (graph.get(map.getTerritoryNumber()).getTerritory().getOwner().equals(player.getName()) && map.getTerritoryNumber() != -1) {
                Vertex from = graph.get(map.getTerritoryNumber());
                while (graph.get(map.getTerritoryNumber()) == from) {
                    try { Thread.sleep(100); } catch (InterruptedException ignored) {}
                }
                Vertex to = graph.get(map.getTerritoryNumber());

                if (graph.isAdjecent(from, to)) {
                    // TODO promt messeage how many troops do you want to fortify
                    // if he cancels fortified is false and reset from, to and selectedterritorynumber
                    // else fortified = true
                } else {
                    map.deselectTerritory();
                    narrator.addText("These territories are not adjacent to each other");
                }
            } else {
                  map.deselectTerritory();
                  narrator.addText("Choose a territory that belongs to you!");
              }
         }
    }

}
