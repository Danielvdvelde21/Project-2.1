package BackEndStructure.Game.Stages.MainGameStageEvents;

import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Territory;
import BackEndStructure.Graph.Vertex;
import Visualisation.Map.Components.*;
import Visualisation.Map.Map;

public class FortifyEvent {

    private final Game game;
    private final Map map;
    private final Narrator narrator;
    private final PlayerTurn playerTurn;
    private final Graph graph;

    public FortifyEvent(Game game) {
        this.game = game;
        this.map = game.getMap();
        this.narrator = game.getNarrator();
        this.playerTurn = game.getPlayerTurn();
        this.graph = game.getGraph();
    }

    public void fortifyTerritory(Player player) {
        map.deselectTerritory();
        narrator.addText("Player " + player.getName() + " is fortifying his territories!");
        boolean fortified = false;

        if (player.isBot()) {
            Vertex[] vertices = game.getAi().reinforce(graph, player);
            Vertex from = vertices[0];
            Vertex to = vertices[1];
            int troopsSend = game.getAi().getReinforcementTroops();

            from.getTerritory().setNumberOfTroops(from.getTerritory().getNumberOfTroops() - troopsSend);
            to.getTerritory().setNumberOfTroops(to.getTerritory().getNumberOfTroops() + troopsSend);
            map.updateTroopCount(from.getTerritory().getTerritoryNumber(), from.getTerritory().getNumberOfTroops());
            map.updateTroopCount(to.getTerritory().getTerritoryNumber(), to.getTerritory().getNumberOfTroops());
            narrator.addText("Player " + player.getName() + " send " + troopsSend + " troop(s) from " + from.getTerritory().getTerritoryName() + " to " + to.getTerritory().getTerritoryName());
        } else {
            while (!playerTurn.hasTurnEnded() && !fortified) {
                delay();
                if (territorySelected(map)) {
                    Vertex from = graph.get(map.getTerritoryNumber()); // Territory that sends troops
                    narrator.addText("Player " + player.getName() + " is fortifying with troops from " + from.getTerritory().getTerritoryName());
                    if (from.getTerritory().getNumberOfTroops() > 1) {
                        if (isTerritoryOwnedBy(from.getTerritory(), player.getName())) {

                            // Wait until a different territory is selected
                            while (graph.get(map.getTerritoryNumber()) == from) {
                                delay();
                            }
                            Vertex to = graph.get(map.getTerritoryNumber()); // Territory that gets troops
                            narrator.addText("Player " + player.getName() + " is trying to fortify " + to.getTerritory().getTerritoryName() + " with troops from " + from.getTerritory().getTerritoryName());

                            if (isTerritoryOwnedBy(to.getTerritory(), player.getName())) {
                                if (graph.isAdjecent(from, to)) {
                                    FortifyTroops popUp = new FortifyTroops(from.getTerritory());
                                    if (!popUp.isCanceled()) {
                                        from.getTerritory().setNumberOfTroops(from.getTerritory().getNumberOfTroops() - popUp.getTroops());
                                        to.getTerritory().setNumberOfTroops(to.getTerritory().getNumberOfTroops() + popUp.getTroops());
                                        map.updateTroopCount(from.getTerritory().getTerritoryNumber(), from.getTerritory().getNumberOfTroops());
                                        map.updateTroopCount(to.getTerritory().getTerritoryNumber(), to.getTerritory().getNumberOfTroops());
                                        fortified = true;
                                        narrator.addText("Player " + player.getName() + " send " + popUp.getTroops() + " troop(s) from " + from.getTerritory().getTerritoryName() + " to " + to.getTerritory().getTerritoryName());
                                    }
                                    map.deselectTerritory();
                                } else {
                                    map.deselectTerritory();
                                    narrator.addText("These territories are not adjacent to each other");
                                }
                            } else {
                                map.deselectTerritory();
                                narrator.addText("Choose a territory that belongs to you!");
                            }
                        } else {
                            map.deselectTerritory();
                            narrator.addText("Choose a territory that belongs to you!");
                        }
                    } else {
                        map.deselectTerritory();
                        narrator.addText("Choose a territory that has more than 1 troops!");
                    }
                }
            }
        }
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
    private boolean isTerritoryOwnedBy(Territory t, String name) {
        return t.getOwner().equals(name);
    }

}
