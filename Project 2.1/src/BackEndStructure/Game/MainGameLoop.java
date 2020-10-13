package BackEndStructure.Game;

import BackEndStructure.Entities.Cards.Card;
import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Territory;
import BackEndStructure.Graph.Vertex;
import Visualisation.*;

import java.util.ArrayList;

public class MainGameLoop {

    // -----------------------------------------------------------------------------------------------------------------
    // Gameplay variables
    private final Game game;

    // Variables in game that get used a lot
    private final Map map;
    private final Graph graph;

    // Game state
    private boolean gameOver = false;

    private int unownedTerritories = 42;
    private boolean noMoreUnownedTerritories = false;

    // -----------------------------------------------------------------------------------------------------------------
    // Updating visual variables
    private final Narrator narrator;

    // For updating the player turn label (current player)
    private PlayerTurn playerTurn = new PlayerTurn();

    // For updating the card inventory
    private CardInventory cardInventory = new CardInventory();

    // -----------------------------------------------------------------------------------------------------------------

    public MainGameLoop(int players, String[] playerNames) {
        this.game = new Game(players, playerNames);
        this.map = game.getMap();
        this.graph = game.getGraph();
        this.narrator = game.getNarrator();
        cardInventory.setGame(game);

        // TODO with actual dice
        game.setPlayerOrder(game.getDice().getPlayOrder(game.getPlayers()));
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
        int round = 34;
        while (round != game.getStartingTroops()) {
            for (Player p : game.getPlayers()) {
                cardInventory.setCurrentPlayer(p);
                narrator.addText("It's " + p.getName() + "'s turn to place down 1 troop");
                playerTurn.setPlayerTurn(p);
                placementTurn(p);
            }
            round++;
        }
    }

    public void placementTurn(Player player) {
        placeTroop(player, getSelectedTerritoryNumber(player));
    }

    // Logic for whether a player can place down a troop on a territory
    private int getSelectedTerritoryNumber(Player player) {
        boolean validTerritoryChosen = false;

        while (!validTerritoryChosen) {
            delay();
            if (territorySelected()) {
                Territory t = graph.get(map.getTerritoryNumber()).getTerritory();
                if (isTerritoryOwnedBy(t, "unowned")) {
                    validTerritoryChosen = true;
                } else if (isTerritoryOwnedBy(t, player.getName()) && noMoreUnownedTerritories){
                    validTerritoryChosen = true;
                } else if (isTerritoryOwnedBy(t, player.getName()) && !noMoreUnownedTerritories) {
                    map.deselectTerritory();
                    narrator.addText("Please select unowned territories first!");
                }  else {
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
        if (isTerritoryOwnedBy(t, "unowned")) {
            // How many unowned territories are left?
            unownedTerritories--;
            unownedTerritoriesLeft();
            // Increase player territories owned
            player.increaseTerritoriesOwned();
            // Owner is now player
            t.setOwner(player.getName());
            // Update map
            map.setTroopCountColor(territoryNumber, player);
        }

        // Add the troops to the territory
        t.setNumberOfTroops(t.getNumberOfTroops() + 1);

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
                // Set the player's inventory
                cardInventory.setCurrentPlayer(p);
                playerTurn(p);
            }
        }
    }

    private void playerTurn(Player player) {
        // Gain troops at start of turn
        placeReceivedTroops(player, receivedTroops(player));

        // Player can start attacking different territories
        playerTurn.resetTurn();
        attacking(player);

        // Player can fortify 1 territory if he chooses to do so at the end of his turn
        playerTurn.resetTurn();
        fortifyTerritory(player);
    }

    private void placeReceivedTroops(Player player, int troops) {
        for (int i = 0; i < troops; i++) {
            placementTurn(player);
        }
    }

    private int receivedTroops(Player player) {
        // Troops for turning in cards
        int cards = turningInCards(player);

        // Troops for territories owned
        int terri = player.getTerritoriesOwned()/3;

        // Troops for continents owned
        game.hasContinents(player);
        int conti = game.getValueOfContinentsOwned(player.getContinentsOwned());

        narrator.addText("Player " + player.getName() + " received " + terri + " troop(s) from Territories, " + conti + " troop(s) from Continents and " + cards + " troop(s) from Cards");
        return cards + terri + conti;
    }

    private int turningInCards(Player player) {
        // Only now allow trading, player is not attacking
        cardInventory.tradingAllowed(true);
        cardInventory.attacking(false);

        cardInventory.getInventory();
        while (!cardInventory.isTradingCompleted()) {
            delay();
        }

        // For the next player reset trading
        cardInventory.setTradingCompleted(false);
        cardInventory.tradingAllowed(false);

        return game.getSetValue(player.getSetsTurnedIn());
    }

    private void attacking(Player player) {
        boolean oneTerritoryCaptured = false;
        narrator.addText("Brace yourself! Player " + player.getName() + " is attacking different players!");

        while(!playerTurn.hasTurnEnded() && !gameOver) {
            delay();
            if (territorySelected()) {
                Vertex attacker = graph.get(map.getTerritoryNumber()); // Attacking territory
                narrator.addText("Player " + player.getName() + " is trying to attack with " + attacker.getTerritory().getTerritoryName());
                if (isTerritoryOwnedBy(attacker.getTerritory(), player.getName())) {
                    // Wait until a different territory is selected
                    while (graph.get(map.getTerritoryNumber()) == attacker) {
                        delay();
                    }
                    Vertex defender = graph.get(map.getTerritoryNumber()); // Defending territory
                    narrator.addText("Player " + player.getName() + " is trying to attack " + defender.getTerritory().getTerritoryName() + " with " + attacker.getTerritory().getTerritoryName());
                    if (!isTerritoryOwnedBy(defender.getTerritory(), player.getName())) {
                        if (graph.isAdjecent(attacker, defender)) {
                            // TODO COMBAT
                            map.updateTroopCount(attacker.getTerritory().getTerritoryNumber(),  attacker.getTerritory().getNumberOfTroops());
                            map.updateTroopCount(defender.getTerritory().getTerritoryNumber(),  defender.getTerritory().getNumberOfTroops());
                            map.deselectTerritory();
                            narrator.addText("Player " + player.getName() + " attacked " + attacker.getTerritory().getTerritoryName() + "(-numtroops) with " + defender.getTerritory().getTerritoryName());
                            // attacker.getTerritory().setNumberOfTroops(attacker.getTerritory().getNumberOfTroops() +- dice);
                            // defender.getTerritory().setNumberOfTroops(defender.getTerritory().getNumberOfTroops() +- dice);
                            // TODO if player wins do all of this
                            player.increaseTerritoriesOwned();
                            oneTerritoryCaptured = true;
                            decreaseTerritories(defender);
                            isGameOver(player);
                            if (isEliminated(defender)) {
                                receiveCards(player, defender);
                                eliminatePlayer(defender.getTerritory().getOwner());
                            }

                            // When player receives cards from an elimination, if he has more then 5 cards he has to turn in a set
                            if (player.getHand().size() >= 6) {
                                turnInCardsAttacking(player);
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
                    narrator.addText("Please choose a territory that belongs to you to attack another player!");
                }
            }
        }
        // if at least one territory is captured player receives a card
        if (oneTerritoryCaptured) {
            player.addToHand(game.getCardStack().draw());
        }
    }

    public void turnInCardsAttacking(Player player) {
        cardInventory.tradingAllowed(true);
        cardInventory.attacking(true);
        while (cardInventory.isTradingCompleted()) {
            delay();
        }
        placeReceivedTroops(player, game.getSetValue(player.getSetsTurnedIn()));
        cardInventory.attacking(false);
        cardInventory.tradingAllowed(false);
    }

    private void fortifyTerritory(Player player) {
        narrator.addText("Player " + player.getName() + " is fortifying his territories!");
        boolean fortified = false;

        while (!playerTurn.hasTurnEnded() && !fortified && !gameOver) {
            delay();
            if (territorySelected()) {
                Vertex from = graph.get(map.getTerritoryNumber()); // Territory that sends troops
                narrator.addText("Player " + player.getName() + " is fortifying with troops from " + from.getTerritory().getTerritoryName());
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
                                map.updateTroopCount(from.getTerritory().getTerritoryNumber(),  from.getTerritory().getNumberOfTroops());
                                map.updateTroopCount(to.getTerritory().getTerritoryNumber(),  to.getTerritory().getNumberOfTroops());
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
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Extra methods

    // Creates a delay
    private void delay() {
        try { Thread.sleep(100); } catch (InterruptedException ignored) { }
    }

    // If a territory is selected
    private boolean territorySelected() {
        return map.getTerritoryNumber() != -1;
    }

    // If a territory belongs to a player
    private boolean isTerritoryOwnedBy(Territory t, String name) {
        return t.getOwner().equals(name);
    }

    private void isGameOver(Player player) {
        gameOver = player.getTerritoriesOwned() == 42;
    }

    private void unownedTerritoriesLeft() {
        if (unownedTerritories == 0) {
            noMoreUnownedTerritories = true;
        }
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
    public void receiveCards(Player player, Vertex v) {
        for (Player p : game.getPlayers()) {
            if(p.getName().equals(v.getTerritory().getOwner())) {
                player.addToHand(p.getHand());
            }
        }
    }

    private void decreaseTerritories(Vertex v) {
        for (Player p : game.getPlayers()) {
            if(p.getName().equals(v.getTerritory().getOwner())) {
                p.decreaseTerritoriesOwned();
            }
        }
    }
}
