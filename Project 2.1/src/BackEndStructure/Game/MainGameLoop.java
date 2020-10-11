package BackEndStructure.Game;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Territory;
import BackEndStructure.Graph.Vertex;
import Visualisation.Map;
import Visualisation.Narrator;
import Visualisation.PlayerTurn;

public class MainGameLoop {
    private final Game game;

    // Variables in game that get used a lot
    private final Map map;
    private final Graph graph;

    // Updating visual variables
    private final Narrator narrator = new Narrator();

    // For updating the player turn label (current player)
    private PlayerTurn playerTurn = new PlayerTurn();

    // Game state
    private boolean gameOver = false;

    public MainGameLoop(int players, String[] playerNames) {
        this.game = new Game(players, playerNames);
        this.map = game.getMap();
        this.graph = game.getGraph();

        // TODO who goes first?
        // The game starts by every player starting to place troops on the board
        placementStage();
        // The game is no about attacking, using cards, fortifying, etc.
        mainGameStage();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // PlacementStage

    private void placementStage() {
        narrator.addText("Placement phase");
        // For each player, for StartingTroops amount of rounds
        int round = 1;
        while (round != game.getStartingTroops()) {
            for (Player p : game.getPlayers()) {
                narrator.addText("It's " + p.getName() + "'s turn to place down 1 troop");
                playerTurn.setPlayerTurn(p);
                placementTurn(p);
            }
            round++;
        }
    }

    public void placementTurn(Player player) {
        placeTroop(player, getSelectedTerritoryNumber(player), 1);
    }

    // Logic for whether a player can place down a troop on a territory
    private int getSelectedTerritoryNumber(Player player) {
        boolean validTerritoryChosen = false;

        while (!validTerritoryChosen) {
            delay();
            // If a territory is selected
            if (map.getTerritoryNumber() != -1) {
                Territory t = graph.get(map.getTerritoryNumber()).getTerritory();
                // Has the player selected one of his own territories or has he selected an unowned territory
                if (t.getOwner().equals("unowned") || t.getOwner().equals(player.getName())) {
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

    // Places a number of troops on a territory
    private void placeTroop(Player player, int territoryNumber, int troops) {
        Territory t = graph.get(territoryNumber).getTerritory();

        // If the territory did not have an owner, set it to player
        if (t.getOwner().equals("unowned")) {
            t.setOwner(player.getName());
            map.setTroopCountColor(territoryNumber, player);
        }

        // Add the troops to the territory
        t.setNumberOfTroops(t.getNumberOfTroops() + troops);

        // Update the Map
        narrator.addText(player.getName() + " put a troop on " + t.getTerritoryName());
        map.updateTroopCount(territoryNumber,  t.getNumberOfTroops());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // MainGameStage

    private void mainGameStage() {
        while(!gameOver) {
            for (Player p : game.getPlayers()) {
                playerTurn.setPlayerTurn(p);
                playerTurn(p);
            }
        }
    }

    private void playerTurn(Player player) {
        // Check if the player got more than 4 cards in his hand
        if (player.getHand().size() > 4) {
            // Player must turn in at least 1 set
            // TODO Player must selected cards from his hand and turn in a set requires need frontend
        }

        Vertex attacker;
        // While Cases button pressed (cases are methods cards, attack)
        while(!map.hasTurnEnded()) {
           // Cards TODO if attacked once cards disabled (state management)
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
                    // TODO make into method
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

    // -----------------------------------------------------------------------------------------------------------------
    // Extra methods

    // Creates a delay
    public void delay() {
        try { Thread.sleep(100); } catch (InterruptedException ignored) { }
    }

}
