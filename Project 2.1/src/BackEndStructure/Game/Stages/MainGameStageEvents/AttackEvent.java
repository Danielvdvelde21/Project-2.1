package BackEndStructure.Game.Stages.MainGameStageEvents;

import BackEndStructure.Entities.Cards.Card;
import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Territory;
import BackEndStructure.Graph.Vertex;
import Visualisation.Map.Components.*;
import Visualisation.Map.Map;

import java.util.ArrayList;

public class AttackEvent {

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

    public AttackEvent(Game game) {
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

        if (player.isBot()) {
            while (game.getAi().botWantsToAttack(graph, player)) {
                botAttack(player);
            }
        } else {
            while (!playerTurn.hasTurnEnded() && !gameOver) {
                delay();
                if (territorySelected(map)) {
                    Vertex attacker = graph.get(map.getTerritoryNumber()); // Attacking territory
                    narrator.addText("Player " + player.getName() + " is trying to attack with " + attacker.getTerritory().getTerritoryName());
                    if (isTerritoryOwnedBy(attacker.getTerritory(), player.getName())) {
                        if (attacker.getTerritory().getNumberOfTroops() > 1) {
                            // Wait until a different territory is selected
                            while (graph.get(map.getTerritoryNumber()) == attacker) {
                                delay();
                            }
                            Vertex defender = graph.get(map.getTerritoryNumber()); // Defending territory
                            narrator.addText("Player " + player.getName() + " is trying to attack " + defender.getTerritory().getTerritoryName() + " with " + attacker.getTerritory().getTerritoryName());
                            if (!isTerritoryOwnedBy(defender.getTerritory(), player.getName())) {
                                if (graph.isAdjecent(attacker, defender)) {
                                    dicePanel.allowRolling(true);
                                    dicePanel.resetDiceRolls();
                                    narrator.addText("Roll the dice to determine the fight!");
                                    // Wait until die are rolled and valid amount of die is selected
                                    while (!dicePanel.diceRolled()) {
                                        delay();
                                    }
                                    if (dicePanel.validAmountOfDiceSelected(attacker.getTerritory().getNumberOfTroops(), defender.getTerritory().getNumberOfTroops())) {
                                        // Perform a fight
                                        game.getAttackingHandeler().oneFight(dicePanel.getNumberOfAttackingDice(), dicePanel.getAttackDieValues(), dicePanel.getNumberOfDefendingDice(), dicePanel.getDefendDieValues());

                                        // Update troops counts
                                        attacker.getTerritory().setNumberOfTroops(attacker.getTerritory().getNumberOfTroops() - game.getAttackingHandeler().getLostTroopsAttackers());
                                        defender.getTerritory().setNumberOfTroops(defender.getTerritory().getNumberOfTroops() - game.getAttackingHandeler().getLostTroopsDefenders());
                                        map.updateTroopCount(attacker.getTerritory().getTerritoryNumber(), attacker.getTerritory().getNumberOfTroops());
                                        map.updateTroopCount(defender.getTerritory().getTerritoryNumber(), defender.getTerritory().getNumberOfTroops());

                                        narrator.addText("Player " + player.getName() + " attacked " + defender.getTerritory().getTerritoryName() + "(-" + game.getAttackingHandeler().getLostTroopsDefenders() + ") with " + attacker.getTerritory().getTerritoryName() + "(-" + game.getAttackingHandeler().getLostTroopsAttackers() + ")");

                                        // Reset classes
                                        map.deselectTerritory();
                                        game.getAttackingHandeler().resetTroopsLost();
                                        dicePanel.allowRolling(false);

                                        // If a territory is captured
                                        if (defender.getTerritory().getNumberOfTroops() < 1) {
                                            oneTerritoryCaptured = true;
                                            territoryCaptured(player, defender, attacker);
                                        }
                                    } else {
                                        map.deselectTerritory();
                                        narrator.addText("Invalid amount of dice selected!");
                                    }
                                } else {
                                    map.deselectTerritory();
                                    narrator.addText("These territories are not adjacent to each other");
                                }
                            } else {
                                map.deselectTerritory();
                                narrator.addText("You cant attack yourself!");
                            }
                        } else {
                            map.deselectTerritory();
                            narrator.addText("Please choose a territory that has more than 1 troop!");
                        }
                    } else {
                        map.deselectTerritory();
                        narrator.addText("Please choose a territory that belongs to you to attack another player!");
                    }
                }
            }
        }
        // if at least one territory is captured player receives a card
        if (oneTerritoryCaptured) {
            player.addToHand(game.getCardStack().draw());
        }
    }

    private void botAttack(Player player) {
        Vertex[] vertices = game.getAi().attack(graph, player);
        Vertex attacker = vertices[0];
        Vertex defender = vertices[1];

        // TODO set die to game.getAi().getAttackerDie();

        if (ownedByBot(defender)) {
            if (defender.getTerritory().getNumberOfTroops() > 1) {
                // TODO set dice 2 defender dice
            } else {
                // TODO set dice to 1 defender dice
            }
        } else {
            // TODO wait for user input (user selects amount of die
            // TODO lock attacker die
            // TODO error handling
        }

        // Attacking Logic
        dicePanel.allowRolling(true);
        dicePanel.resetDiceRolls();

        // Perform a fight
        game.getAttackingHandeler().oneFight(dicePanel.getNumberOfAttackingDice(), dicePanel.getAttackDieValues(), dicePanel.getNumberOfDefendingDice(), dicePanel.getDefendDieValues());

        // Update troops counts
        attacker.getTerritory().setNumberOfTroops(attacker.getTerritory().getNumberOfTroops() - game.getAttackingHandeler().getLostTroopsAttackers());
        defender.getTerritory().setNumberOfTroops(defender.getTerritory().getNumberOfTroops() - game.getAttackingHandeler().getLostTroopsDefenders());
        map.updateTroopCount(attacker.getTerritory().getTerritoryNumber(), attacker.getTerritory().getNumberOfTroops());
        map.updateTroopCount(defender.getTerritory().getTerritoryNumber(), defender.getTerritory().getNumberOfTroops());

        narrator.addText("Player " + player.getName() + " attacked " + defender.getTerritory().getTerritoryName() + "(-" + game.getAttackingHandeler().getLostTroopsDefenders() + ") with " + attacker.getTerritory().getTerritoryName() + "(-" + game.getAttackingHandeler().getLostTroopsAttackers() + ")");

        // Reset classes
        map.deselectTerritory();
        game.getAttackingHandeler().resetTroopsLost();
        dicePanel.allowRolling(false);

        // If a territory is captured
        if (defender.getTerritory().getNumberOfTroops() < 1) {
            oneTerritoryCaptured = true;
            territoryCaptured(player, defender, attacker);
        }
    }

    // Logic that needs to happen after a territory is captured
    private void territoryCaptured(Player player, Vertex defender, Vertex attack) {
        // Player gets the territory
        player.increaseTerritoriesOwned();
        defender.getTerritory().setOwner(player.getName());
        map.setTroopCountColor(defender.getTerritory().getTerritoryNumber(), player);

        // defender loses his territory
        decreaseTerritories(defender);

        isGameOver(player);
        if (isEliminated(defender)) {
            receiveCards(player, defender);
            eliminatePlayer(defender.getTerritory().getOwner());
        }

        // How many troops are sent over
        int troops;
        if (player.isBot()) {
            troops = game.getAi().getTroopCarryOver();
        } else {
            TerritoryCaptured popUp = new TerritoryCaptured(attack.getTerritory());
            while (!popUp.getValidNumberInserted()) {
                delay();
            }
            troops = popUp.getTroops();
        }
        attack.getTerritory().setNumberOfTroops(attack.getTerritory().getNumberOfTroops() - troops);
        defender.getTerritory().setNumberOfTroops(defender.getTerritory().getNumberOfTroops() + troops);
        map.updateTroopCount(attack.getTerritory().getTerritoryNumber(), attack.getTerritory().getNumberOfTroops());
        map.updateTroopCount(defender.getTerritory().getTerritoryNumber(), defender.getTerritory().getNumberOfTroops());
        narrator.addText("Player " + player.getName() + " send " + troops + " troop(s) from " + attack.getTerritory().getTerritoryName() + " to " + defender.getTerritory().getTerritoryName());

        // When player receives cards from an elimination, if he has more then 5 cards he has to turn in a set
        if (player.getHand().size() >= 6) {
            turnInCardsAttacking(player);
        }
    }

    // Forcing a player to turn in a set during an attacking phase
    private void turnInCardsAttacking(Player player) {
        if (player.isBot()) {
            // Set of cards the bot is going to turn in
            ArrayList<Card> turnInSet = game.getAi().attackingCard(graph, player);

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
            game.getAi().placeTroop(graph, player, troops);
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
    private boolean isTerritoryOwnedBy(Territory t, String name) {
        return t.getOwner().equals(name);
    }

    // Logic for whether a player can place down a troop on a territory
    private int getSelectedTerritoryNumber(Player player) {
        map.deselectTerritory();
        boolean validTerritoryChosen = false;

        while (!validTerritoryChosen) {
            delay();
            if (territorySelected(map)) {
                Territory t = graph.get(map.getTerritoryNumber()).getTerritory();
                if (isTerritoryOwnedBy(t, player.getName())) {
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
        String name = defender.getTerritory().getOwner();
        for (Player p : game.getPlayers()) {
            if (p.getName().equals(name) && p.isBot()) {
                return true;
            }
        }
        return false;
    }

    // If a player is defeated
    private boolean isEliminated(Vertex v) {
        for (Player p : game.getPlayers()) {
            if (v.getTerritory().getOwner().equals(p.getName())) {
                if (p.getTerritoriesOwned() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    // Remove player from player list
    private void eliminatePlayer(String name) {
        game.getPlayers().removeIf(p -> p.getName().equals(name));
    }

    // Get cards from eliminated player
    private void receiveCards(Player player, Vertex v) {
        for (Player p : game.getPlayers()) {
            if (p.getName().equals(v.getTerritory().getOwner())) {
                player.addToHand(p.getHand());
            }
        }
    }

    private void decreaseTerritories(Vertex v) {
        for (Player p : game.getPlayers()) {
            if (p.getName().equals(v.getTerritory().getOwner())) {
                p.decreaseTerritoriesOwned();
            }
        }
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
