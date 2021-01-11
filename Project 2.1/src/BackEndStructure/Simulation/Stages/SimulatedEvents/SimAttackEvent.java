package BackEndStructure.Simulation.Stages.SimulatedEvents;

import BackEndStructure.Entities.Cards.Card;
import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Territory;
import BackEndStructure.Graph.Vertex;
import Visualisation.Map.Components.*;
import Visualisation.Map.Map;

import java.util.ArrayList;
import java.util.Random;

public class SimAttackEvent {

    private final Game game;
    private final DicePanel dicePanel;
    private final Graph graph;

    private boolean gameOver = false;
    private Player winner;

    private boolean oneTerritoryCaptured;

    public SimAttackEvent(Game game) {
        this.game = game;
        this.graph = game.getGraph();

        this.dicePanel = game.getDicePanel();
        dicePanel.setGame(game);
    }

    public void attacking(Player player) {
        oneTerritoryCaptured = false;

        randomAttack(player);

        // if at least one territory is captured player receives a card
        if (oneTerritoryCaptured) {
            player.addToHand(game.getCardStack().draw());
        }
    }

    private boolean randomAttack(Player player) {
        ArrayList<Vertex> ownedTerritories = new ArrayList<>();
        ArrayList<Vertex> unownedTerritories = new ArrayList<>();
        
        for (int i = 0; i < graph.getSize(); i++) {
            if (graph.get(i).getTerritory().getOwner() == player) {
                ownedTerritories.add(graph.get(i));
            } else {
                unownedTerritories.add(graph.get(i));
            }
        }
        Random rn = new Random();
        int atkIndex = rn.nextInt(ownedTerritories.size());
        int defIndex = rn.nextInt(unownedTerritories.size());
        while (ownedTerritories.get(atkIndex).getTerritory().getNumberOfTroops() < 2) {
            atkIndex = rn.nextInt(ownedTerritories.size());
        }


        Vertex attacker = ownedTerritories.get(atkIndex);
        Vertex defender = unownedTerritories.get(defIndex);
        // --------------------- worked on random attack till here, need to adjust further code ------------------------------

        int initialAttack = attacker.getTerritory().getNumberOfTroops();

        // Set the amount of dice that the bot wants to use
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

        // If the bot is attacking another bot, the defending bot will use a much defending dice
        if (defender.getTerritory().getOwner().isBot()) {
            if (defender.getTerritory().getNumberOfTroops() > 1) {
                dicePanel.addDefendDie();
            } else {
                dicePanel.removeDefendDie();
            }
        }

        // Perform a fight
        game.getAttackingHandeler().oneFight(dicePanel.getNumberOfAttackingDice(), dicePanel.getAttackDieValues(), dicePanel.getNumberOfDefendingDice(), dicePanel.getDefendDieValues());

        // Update troops counts
        attacker.getTerritory().setNumberOfTroops(attacker.getTerritory().getNumberOfTroops() - game.getAttackingHandeler().getLostTroopsAttackers());
        defender.getTerritory().setNumberOfTroops(defender.getTerritory().getNumberOfTroops() - game.getAttackingHandeler().getLostTroopsDefenders());

        // Reset classes
        game.getAttackingHandeler().resetTroopsLost();

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

        // defender loses his territory
        decreaseTerritories(defenderOwner);

        isGameOver(player);
        if (isEliminated(defenderOwner)) {
            receiveCards(player, defenderOwner);
            eliminatePlayer(defenderOwner);
        }

        // How many troops are sent over
        // TODO
        int troops = game.getAi().getBotAttacking().getTroopCarryOver(attack);

        attack.getTerritory().setNumberOfTroops(attack.getTerritory().getNumberOfTroops() - troops);
        defender.getTerritory().setNumberOfTroops(defender.getTerritory().getNumberOfTroops() + troops);

        // When player receives cards from an elimination, if he has more then 5 cards he has to turn in a set
        if (player.getHand().size() >= 6) {
            turnInCardsAttacking(player);
        }
    }

    // Forcing a player to turn in a set during an attacking phase
    private void turnInCardsAttacking(Player player) {
        // Set of cards the bot is going to turn in
        // TODO
        ArrayList<Card> turnInSet = game.getAi().getBotAttacking().attackingCard(graph, player);

        // Return cards to stack
        game.getCardStack().returnCards(turnInSet);

        // Remove cards from player hand
        player.getHand().removeAll(turnInSet);

        // Player has 1 more completed set
        player.incrementSetsOwned();

        placeReceivedTroops(player, game.getSetValue(player.getSetsTurnedIn()));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Placing troops

    public void placeReceivedTroops(Player player, int troops) {
        for (int i = 0; i < troops; i++) {
            placeTroopRandomly(player);
        }
    }

    private void placeTroopRandomly(Player player) {
        // Get all the owned territories for this player
        ArrayList<Vertex> ownedTerritories = new ArrayList<>();
        for (Vertex v : graph.getArrayList()) {
            if (v.getTerritory().getOwner() == player) {
                ownedTerritories.add(v);
            }
        }

        // Select a random territory
        Random random = new Random();
        Territory t = ownedTerritories.get(random.nextInt(ownedTerritories.size())).getTerritory();

        // place a troop on the random territory
        t.setNumberOfTroops(t.getNumberOfTroops() + 1);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Extra Methods

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

    public boolean getGameState() {
        return gameOver;
    }
}
