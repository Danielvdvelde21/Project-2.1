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

    private final ArrayList<Player> eliminatedPlayers = new ArrayList<>();

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
            while (game.getAi().getBotAttacking().botWantsToAttack(graph, player)) {
                if (!botAttack(player, null)) {
                    break;
                }
            }
        } else if (player.isMCTSBot()) {
            // TODO how many attacks is mcts going to do?
            Vertex[] vertices = game.getAIMCTS().findNextMove(graph, game.getPlayers(),player);
            System.out.println("attack " + vertices[0].getTerritory().getTerritoryName());
            System.out.println("defend " + vertices[1].getTerritory().getTerritoryName());
            try {
                Thread.sleep(12000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            botAttack(player, vertices);
        } else {
            while (!playerTurn.hasTurnEnded() && !gameOver) {
                delay();
                if (territorySelected(map)) {
                    Vertex attacker = graph.get(map.getTerritoryNumber()); // Attacking territory
                    narrator.addText("Player " + player.getName() + " is trying to attack with " + attacker.getTerritory().getTerritoryName());
                    if (isTerritoryOwnedBy(attacker.getTerritory(), player)) {
                        if (attacker.getTerritory().getNumberOfTroops() > 1) {
                            // Wait until a different territory is selected
                            while (graph.get(map.getTerritoryNumber()) == attacker) {
                                delay();
                            }
                            Vertex defender = graph.get(map.getTerritoryNumber()); // Defending territory
                            narrator.addText("Player " + player.getName() + " is trying to attack " + defender.getTerritory().getTerritoryName() + " with " + attacker.getTerritory().getTerritoryName());
                            if (!isTerritoryOwnedBy(defender.getTerritory(), player)) {
                                if (graph.isAdjacent(attacker, defender)) {
                                    dicePanel.allowRolling(true);
                                    dicePanel.resetDiceRolls();
                                    narrator.addText("Roll the dice to determine the fight!");
                                    // Wait until die are rolled and valid amount of die is selected
                                    while (!dicePanel.diceRolled()) {
                                        delay();
                                    }
                                    if (dicePanel.validAmountOfDiceSelected(attacker.getTerritory().getNumberOfTroops(), defender.getTerritory().getNumberOfTroops())) {
                                        // Perform a fight
                                        game.getAttackingHandler().oneFight(dicePanel.getNumberOfAttackingDice(), dicePanel.getAttackDieValues(), dicePanel.getNumberOfDefendingDice(), dicePanel.getDefendDieValues());

                                        // Update troops counts
                                        attacker.getTerritory().setNumberOfTroops(attacker.getTerritory().getNumberOfTroops() - game.getAttackingHandler().getLostTroopsAttackers());
                                        defender.getTerritory().setNumberOfTroops(defender.getTerritory().getNumberOfTroops() - game.getAttackingHandler().getLostTroopsDefenders());
                                        map.updateTroopCount(attacker.getTerritory().getTerritoryNumber(), attacker.getTerritory().getNumberOfTroops());
                                        map.updateTroopCount(defender.getTerritory().getTerritoryNumber(), defender.getTerritory().getNumberOfTroops());

                                        narrator.addText("Player " + player.getName() + " attacked " + defender.getTerritory().getTerritoryName() + "(-" + game.getAttackingHandler().getLostTroopsDefenders() + ") with " + attacker.getTerritory().getTerritoryName() + "(-" + game.getAttackingHandler().getLostTroopsAttackers() + ")");

                                        // Reset classes
                                        map.deselectTerritory();
                                        game.getAttackingHandler().resetTroopsLost();
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

    private boolean botAttack(Player player, Vertex[] attackerDefender) {
        Vertex[] vertices;
        Vertex attacker;
        Vertex defender;

        if (player.isBot()) {
            vertices = game.getAi().getBotAttacking().attack(graph, player);
        } else {
            vertices = attackerDefender;
        }
        attacker = vertices[0];
        defender = vertices[1];

        if (vertices[0] == null) {
            // cancel the bot attack, there is no attack
            return false;
        }

        // Set the amount of dice that the bot wants to use
        if (player.isBot()) {
            switch (game.getAi().getBotAttacking().getAttackerDie()) {
                case 1:
                    dicePanel.removeAttackDie();
                    dicePanel.removeAttackDie();
                    break;
                case 2:
                    dicePanel.removeAttackDie();
                    dicePanel.removeAttackDie();
                    dicePanel.addAttackDie();
                    break;
                case 3:
                    dicePanel.addAttackDie();
                    dicePanel.addAttackDie();
            }
        } else {
            switch (attacker.getTerritory().getNumberOfTroops()) {
                case 1:
                    throw new IllegalArgumentException("Cannot attack with 1 troop");
                case 2:
                    dicePanel.removeAttackDie();
                    dicePanel.removeAttackDie();
                    break;
                case 3:
                    dicePanel.removeAttackDie();
                    dicePanel.removeAttackDie();
                    dicePanel.addAttackDie();
                    break;
                default:
                    dicePanel.addAttackDie();
                    dicePanel.addAttackDie();
            }
        }
        narrator.addText("Player " + player.getName() + " is trying to attack " + defender.getTerritory().getTerritoryName() + " with " + attacker.getTerritory().getTerritoryName() + " Using " + game.getAi().getBotAttacking().getAttackerDie() + " Dice(s)");

        // If the bot is attacking another bot, the defending bot will use a much defending dice
        if (ownedByBot(defender)) {
            if (defender.getTerritory().getNumberOfTroops() > 1) {
                dicePanel.addDefendDie();
            } else {
                dicePanel.removeDefendDie();
            }
            dicePanel.allowRolling(true);
            dicePanel.resetDiceRolls();
            dicePanel.rollAttackDie();
            dicePanel.rollDefDie();
            dicePanel.allowRolling(false);

        } else {
            // Lock the attacking die --> player cant change bots strategy
            dicePanel.lockAttackingDie();

            dicePanel.allowRolling(true);
            dicePanel.resetDiceRolls();
            narrator.addText("Roll the dice to determine the fight!");

            // Wait until die are rolled and valid amount of die is selected
            while (!dicePanel.diceRolled()) {
                delay();
            }

            // Reset classes
            map.deselectTerritory();
            dicePanel.allowRolling(false);
            dicePanel.unlockAttackingDie();

            // If the defender choose invalid amount of die --> stop this method --> a new iteration will do the same thing
            if (!dicePanel.validAmountOfDiceSelected(attacker.getTerritory().getNumberOfTroops(), defender.getTerritory().getNumberOfTroops())) {
                narrator.addText("Invalid amount of dice selected!");
                return true;
            }
        }

        // Perform a fight
        game.getAttackingHandler().oneFight(dicePanel.getNumberOfAttackingDice(), dicePanel.getAttackDieValues(), dicePanel.getNumberOfDefendingDice(), dicePanel.getDefendDieValues());

        // Update troops counts
        attacker.getTerritory().setNumberOfTroops(attacker.getTerritory().getNumberOfTroops() - game.getAttackingHandler().getLostTroopsAttackers());
        defender.getTerritory().setNumberOfTroops(defender.getTerritory().getNumberOfTroops() - game.getAttackingHandler().getLostTroopsDefenders());
        map.updateTroopCount(attacker.getTerritory().getTerritoryNumber(), attacker.getTerritory().getNumberOfTroops());
        map.updateTroopCount(defender.getTerritory().getTerritoryNumber(), defender.getTerritory().getNumberOfTroops());

        narrator.addText("Player " + player.getName() + " attacked " + defender.getTerritory().getTerritoryName() + "(-" + game.getAttackingHandler().getLostTroopsDefenders() + ") with " + attacker.getTerritory().getTerritoryName() + "(-" + game.getAttackingHandler().getLostTroopsAttackers() + ")");

        // Reset classes
        game.getAttackingHandler().resetTroopsLost();

        // If a territory is captured
        if (defender.getTerritory().getNumberOfTroops() < 1) {
            oneTerritoryCaptured = true;
            territoryCaptured(player, defender, attacker);
            return true;
        }
        return true;

    }

    // Logic that needs to happen after a territory is captured
    private void territoryCaptured(Player player, Vertex defender, Vertex attack) {
        // Player gets the territory
        Player defenderOwner = defender.getTerritory().getOwner();
        player.increaseTerritoriesOwned();
        defender.getTerritory().setOwner(player);
        map.setTroopCountColor(defender.getTerritory().getTerritoryNumber(), player);

        // defender loses his territory
        decreaseTerritories(defenderOwner);

        isGameOver(player);
        if (isEliminated(defenderOwner)) {
            receiveCards(player, defenderOwner);
            eliminatePlayer(defenderOwner);
        }

        // How many troops are sent over
        int troops;
        if (player.isBot() || player.isMCTSBot()) {
            troops = game.getAi().getBotAttacking().getTroopCarryOver(attack);
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
        if (player.isBot() || player.isMCTSBot()) {
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
        if (player.isBot() || player.isMCTSBot()) {
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
        return defender.getTerritory().getOwner().isMCTSBot() || defender.getTerritory().getOwner().isBot();
    }

    // If a player is defeated
    private boolean isEliminated(Player d) {
        return d.getTerritoriesOwned() == 0;
    }

    // Remove player from player list
    private void eliminatePlayer(Player p) {
        eliminatedPlayers.add(p);
    }

    public ArrayList<Player> getEliminatedPlayers() {
        return eliminatedPlayers;
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

    public boolean getGameState() {
        return gameOver;
    }

}
