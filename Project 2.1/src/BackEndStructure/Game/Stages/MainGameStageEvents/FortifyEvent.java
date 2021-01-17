package BackEndStructure.Game.Stages.MainGameStageEvents;

import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;
import BackEndStructure.Graph.Edge;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Territory;
import BackEndStructure.Graph.Vertex;
import Visualisation.Map.Components.*;
import Visualisation.Map.Map;

import java.util.ArrayList;
import java.util.SplittableRandom;

public class FortifyEvent {
    SplittableRandom splittableRandom = new SplittableRandom();

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

    public void fortifyTerritory(Player player, boolean randomPlayer) {
        map.deselectTerritory();
        narrator.addText("Player " + player.getName() + " is fortifying his territories!");
        boolean fortified = false;

        // Both bots use rules based fortification
        if (player.isBot() || player.isMCTSBot()) {
            Vertex[] vertices = game.getAi().getBotReinforcement().reinforceDefense(graph, player);
            if (vertices != null) {
                Vertex from = vertices[0];
                Vertex to = vertices[1];
                int troopsSend = game.getAi().getBotReinforcement().getReinforcementTroops();

                from.getTerritory().setNumberOfTroops(from.getTerritory().getNumberOfTroops() - troopsSend);
                to.getTerritory().setNumberOfTroops(to.getTerritory().getNumberOfTroops() + troopsSend);
                map.updateTroopCount(from.getTerritory().getTerritoryNumber(), from.getTerritory().getNumberOfTroops());
                map.updateTroopCount(to.getTerritory().getTerritoryNumber(), to.getTerritory().getNumberOfTroops());
                narrator.addText("Player " + player.getName() + " send " + troopsSend + " troop(s) from " + from.getTerritory().getTerritoryName() + " to " + to.getTerritory().getTerritoryName());
            }
        } else if (randomPlayer) {
            randomFortification(player);
        } else {
            while (!playerTurn.hasTurnEnded() && !fortified) {
                delay();
                if (territorySelected(map)) {
                    Vertex from = graph.get(map.getTerritoryNumber()); // Territory that sends troops
                    narrator.addText("Player " + player.getName() + " is fortifying with troops from " + from.getTerritory().getTerritoryName());
                    if (from.getTerritory().getNumberOfTroops() > 1) {
                        if (isTerritoryOwnedBy(from.getTerritory(), player)) {

                            // Wait until a different territory is selected
                            while (graph.get(map.getTerritoryNumber()) == from) {
                                delay();
                            }
                            Vertex to = graph.get(map.getTerritoryNumber()); // Territory that gets troops
                            narrator.addText("Player " + player.getName() + " is trying to fortify " + to.getTerritory().getTerritoryName() + " with troops from " + from.getTerritory().getTerritoryName());

                            if (isTerritoryOwnedBy(to.getTerritory(), player)) {
                                if (graph.isAdjacent(from, to)) {
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
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {
        }
    }

    // If a territory is selected
    private boolean territorySelected(Map map) {
        return map.getTerritoryNumber() != -1;
    }

    // If a territory belongs to a player
    private boolean isTerritoryOwnedBy(Territory t, Player p) {
        return t.getOwner() == p;
    }

    public void randomFortification(Player player) {
        // Get all the owned territories for this player
        ArrayList<Vertex> ownedTerritories = new ArrayList<>();
        for (Vertex v : game.getGraph().getArrayList()) {
            if (v.getTerritory().getOwner() == player) {
                ownedTerritories.add(v);
            }
        }

        // For all owned territories select the ones that have another adjacent owned territory and have more than 1 troop
        Vertex[] validFroms = new Vertex[42];
        int validFromsNo = 0;

        for (Vertex v : ownedTerritories) {
            if (v.getTerritory().getNumberOfTroops() > 1) {
                boolean twoOwnedAdjacent = false;
                Edge[] neighbours = v.getEdges();
                int neighboursNo = v.getEdgeNo();
                for (int i=0;i<neighboursNo;i++) {
                    Edge e=neighbours[i];
                    if (e.getVertex().getTerritory().getOwner() == player) {
                        twoOwnedAdjacent = true;
                        break;
                    }
                }
                if (twoOwnedAdjacent) {
                    validFroms[validFromsNo]=v;
                    validFromsNo++;
                }
            }
        }

        if (validFromsNo!=0) {
            // Select a random territory from this list (this territory will send troops)

            Vertex from = validFroms[splittableRandom.nextInt(validFromsNo)];

            // Select a random territory that is adjacent to the from territory (this territory receives troops)
            Edge[] validTos = new Edge[10];
            int validTosNo=0;
            Edge[] neighbours = from.getEdges();
            int neighboursNo = from.getEdgeNo();
            for (int i=0;i<neighboursNo;i++) {
                Edge e=neighbours[i];
                if (e.getVertex().getTerritory().getOwner() == player) {
                    validTos[validTosNo]=e;
                    validTosNo++;
                }
            }
            Vertex to = validTos[splittableRandom.nextInt(validTosNo)].getVertex();

            // Send a random quantity of troops
            int troopsSend = (int) (Math.random() * (from.getTerritory().getNumberOfTroops() - 1)) + 1;

            // Update the troop counts in the graph
            from.getTerritory().setNumberOfTroops(from.getTerritory().getNumberOfTroops() - troopsSend);
            to.getTerritory().setNumberOfTroops(to.getTerritory().getNumberOfTroops() + troopsSend);
            map.updateTroopCount(from.getTerritory().getTerritoryNumber(), from.getTerritory().getNumberOfTroops());
            map.updateTroopCount(to.getTerritory().getTerritoryNumber(), to.getTerritory().getNumberOfTroops());
            narrator.addText("Player " + player.getName() + " send " + troopsSend + " troop(s) from " + from.getTerritory().getTerritoryName() + " to " + to.getTerritory().getTerritoryName());
        } else {
            narrator.addText("No valid fortifications");
        }
    }

}
