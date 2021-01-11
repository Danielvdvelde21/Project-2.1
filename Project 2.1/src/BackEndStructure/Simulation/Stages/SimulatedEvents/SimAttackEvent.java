package BackEndStructure.Simulation.Stages.SimulatedEvents;

import BackEndStructure.Entities.Cards.Card;
import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;
import BackEndStructure.Graph.Edge;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Territory;
import BackEndStructure.Graph.Vertex;
import Visualisation.Map.Components.*;
import Visualisation.Map.Map;

import java.util.ArrayList;
import java.util.Random;

public class SimAttackEvent {

    private final Game game;
    private final Map map;
    private final Narrator narrator;
    private final CardInventory cardInventory;
    private final DicePanel dicePanel;
    private final PlayerTurn playerTurn;
    private final Graph graph;

    private boolean gameOver = false;
    private Player winner;

    private boolean oneTerritoryCaptured;

    public SimAttackEvent(Game game) {
        this.game = game;
        this.map = game.getMap();
        this.narrator = game.getNarrator();
        this.cardInventory = game.getCardInventory();
        this.dicePanel = game.getDicePanel();
        this.playerTurn = game.getPlayerTurn();
        this.graph = game.getGraph();
        cardInventory.setGame(game);
        dicePanel.setGame(game);
    }

    public void attacking(Player player) {
        oneTerritoryCaptured = false;
        narrator.addText("Brace yourself! Player " + player.getName() + " is attacking different players!");
        map.deselectTerritory();

        randomAttack(player);

        // if at least one territory is captured player receives a card
        /*if (oneTerritoryCaptured) {
            player.addToHand(game.getCardStack().draw());
        }*/
    }

    private boolean randomAttack(Player player) {
        ArrayList<Vertex> ownedTerritories = new ArrayList<>();
        ArrayList<Vertex> enemyNeighbours = new ArrayList<>();
        for (int i = 0; i < graph.getSize(); i++) {
            if (graph.get(i).getTerritory().getOwner() == player) {
                ownedTerritories.add(graph.get(i));
            }
        }
        // Select random attacker
        Random rn = new Random();
        int atkIndex = rn.nextInt(ownedTerritories.size());
        // Can't attack with only 1 troop
        while (ownedTerritories.get(atkIndex).getTerritory().getNumberOfTroops() < 2) {
            atkIndex = rn.nextInt(ownedTerritories.size());
        }
        // Select neighbouring enemy territories
        for (Edge e: ownedTerritories.get(atkIndex).getEdges()) {
            if (e.getVertex().getTerritory().getOwner() != player) {
                enemyNeighbours.add(e.getVertex());
            }
        }
        // Select random neighbour
        int defIndex = rn.nextInt(enemyNeighbours.size());


        Vertex attacker = ownedTerritories.get(atkIndex);
        Vertex defender = enemyNeighbours.get(defIndex);

        // Attack will finish either by ending up with 1 troop or capturing (capturing should return true anyway)
        while (attacker.getTerritory().getNumberOfTroops() >= 2 && defender.getTerritory().getOwner() != player) {

            int numberOfAttackerDice;
            int numberOfDefenderDice;
            int[] attackerDiceValues = new int[3];
            int[] defenderDiceValues = new int[2];

            // Settinng the attacker dice
            switch (attacker.getTerritory().getNumberOfTroops()) {
                case 1:
                    numberOfAttackerDice = 1;
                    attackerDiceValues[0] = (int) (Math.random() * 6) + 1;
                    break;
                case 2:
                    numberOfAttackerDice = 2;
                    attackerDiceValues[0] = (int) (Math.random() * 6) + 1;
                    attackerDiceValues[1] = (int) (Math.random() * 6) + 1;
                    break;
                default:
                    numberOfAttackerDice = 3;
                    attackerDiceValues[0] = (int) (Math.random() * 6) + 1;
                    attackerDiceValues[1] = (int) (Math.random() * 6) + 1;
                    attackerDiceValues[2] = (int) (Math.random() * 6) + 1;
            }

            // Setting the defender dice
            if (defender.getTerritory().getNumberOfTroops() > 1) {
                numberOfDefenderDice = 2;
                defenderDiceValues[0] = (int) (Math.random() * 6) + 1;
                defenderDiceValues[1] = (int) (Math.random() * 6) + 1;
            } else {
                numberOfDefenderDice = 1;
                defenderDiceValues[0] = (int) (Math.random() * 6) + 1;
            }

            // Perform a fight
            game.getAttackingHandler().oneFight(numberOfAttackerDice, attackerDiceValues, numberOfDefenderDice, defenderDiceValues);

            // Update troops counts
            attacker.getTerritory().setNumberOfTroops(attacker.getTerritory().getNumberOfTroops() - game.getAttackingHandler().getLostTroopsAttackers());
            defender.getTerritory().setNumberOfTroops(defender.getTerritory().getNumberOfTroops() - game.getAttackingHandler().getLostTroopsDefenders());

            // Reset classes
            game.getAttackingHandler().resetTroopsLost();

            // If a territory is captured
            if (defender.getTerritory().getNumberOfTroops() < 1) {
                oneTerritoryCaptured = true;
                territoryCaptured(player, defender, attacker);
                return true;
            }
        }
        return true;

    }

    // Logic that needs to happen after a territory is captured
    private void territoryCaptured(Player player, Vertex defender, Vertex attack) { //TODO fix the isEliminated order
        // Player gets the territory
        Player defenderOwner=defender.getTerritory().getOwner();
        player.increaseTerritoriesOwned();
        defender.getTerritory().setOwner(player);
        map.setTroopCountColor(defender.getTerritory().getTerritoryNumber(), player);

        // defender loses his territory
        decreaseTerritories(defenderOwner);

        isGameOver(player);
        if (isEliminated(defenderOwner)) {
            //receiveCards(player, defenderOwner);
            eliminatePlayer(defenderOwner);
        }

        // How many troops are sent over, for now all but 1
        int troops;
        troops = attack.getTerritory().getNumberOfTroops() - 1;

        attack.getTerritory().setNumberOfTroops(attack.getTerritory().getNumberOfTroops() - troops);
        defender.getTerritory().setNumberOfTroops(defender.getTerritory().getNumberOfTroops() + troops);
        map.updateTroopCount(attack.getTerritory().getTerritoryNumber(), attack.getTerritory().getNumberOfTroops());
        map.updateTroopCount(defender.getTerritory().getTerritoryNumber(), defender.getTerritory().getNumberOfTroops());
        narrator.addText("Player " + player.getName() + " send " + troops + " troop(s) from " + attack.getTerritory().getTerritoryName() + " to " + defender.getTerritory().getTerritoryName());

        // When player receives cards from an elimination, if he has more then 5 cards he has to turn in a set
        /*if (player.getHand().size() >= 6) {
            turnInCardsAttacking(player);
        }*/
    }

    // Forcing a player to turn in a set during an attacking phase
    private void turnInCardsAttacking(Player player) {
        if (player.isBot()) {
            // Set of cards the bot is going to turn in
            ArrayList<Card> turnInSet = game.getAi().getBotAttacking().attackingCard(graph, player);

            // Return cards to stack
            game.getCardStack().returnCards(turnInSet);

            // Remove cards from player hand
            player.getHand().removeAll(turnInSet);

            // Player has 1 more completed set
            player.incrementSetsOwned();
        } else {
            cardInventory.tradingAllowed(true);
            cardInventory.attacking(true);
            cardInventory.getInventory();
            while (cardInventory.getMenuClosed()) {
                delay();
            }
            cardInventory.tradingAllowed(false);
            cardInventory.attacking(false);
        }
        placeReceivedTroops(player, game.getSetValue(player.getSetsTurnedIn()));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Placing troops

    public void placeReceivedTroops(Player player, int troops) {
        narrator.addText("Player " + player.getName() + " can put " + troops + " troops on his territories");
        if (player.isBot()) {
            for (int i = 0; i < troops; i++) {
                placeTroop(player, game.getAi().getPlaceTroops().placeTroop(graph, player));
            }
        } else {
            for (int i = 0; i < troops; i++) {
                placeTroop(player, getSelectedTerritoryNumber(player));
            }
        }
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

    // Check if vertex is owned by bot
    private boolean ownedByBot(Vertex defender) {
        Player p = defender.getTerritory().getOwner();
        return p.isBot();
    }

    // If a player is defeated
    private boolean isEliminated(Player d) {
        return d.getTerritoriesOwned() == 0;
    }

    // Remove player from player list
    private void eliminatePlayer(Player p) {
        game.getPlayers().remove(p);
    }

    // Get cards from eliminated player
    private void receiveCards(Player receivingPlayer, Player donatingPlayer) {
        receivingPlayer.addToHand(donatingPlayer.getHand());
    }

    private void decreaseTerritories(Player p) {
        p.decreaseTerritoriesOwned();
    }

    private void isGameOver(Player player) {
        gameOver = player.getTerritoriesOwned() == 42;
        if (gameOver) {
            winner = player;
        }
    }

    public Player getWinner() {
        return winner;
    }

    public boolean getGameState() { return gameOver; }
}
