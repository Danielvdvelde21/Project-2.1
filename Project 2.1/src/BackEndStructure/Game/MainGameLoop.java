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

    // -----------------------------------------------------------------------------------------------------------------
    // Updating visual variables
    private final Narrator narrator = new Narrator();

    // For updating the player turn label (current player)
    private PlayerTurn playerTurn = new PlayerTurn();

    // For updating the card inventory
    private ArrayList<Card> cardInventory = new ArrayList<>();
    private CardInventory ci = new CardInventory(cardInventory);   //TODO get cards

    // -----------------------------------------------------------------------------------------------------------------

    public MainGameLoop(int players, String[] playerNames) {
        this.game = new Game(players, playerNames);
        this.map = game.getMap();
        this.graph = game.getGraph();

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
        // TODO IF THERE EXIST AN UNOCUPIED TERRITORY THIS MUST BE CHOSE FIRST
        placeTroop(player, getSelectedTerritoryNumber(player), 1);
    }

    // Logic for whether a player can place down a troop on a territory
    private int getSelectedTerritoryNumber(Player player) {
        boolean validTerritoryChosen = false;

        while (!validTerritoryChosen) {
            delay();
            if (territorySelected()) {
                Territory t = graph.get(map.getTerritoryNumber()).getTerritory();
                if (isTerritoryOwnedBy(t, "unowned") || isTerritoryOwnedBy(t, player.getName())) {
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
        if (isTerritoryOwnedBy(t, "unowned")) {
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
        // Gain troops at start of turn
        placeReceivedTroops(recievedTroops(player));

        // Player can start attacking different territories
        attacking(player);
        playerTurn.resetTurn();

        // Player can fortify 1 territory if he chooses to do so at the end of his turn
        fortifyTerritory(player);
        playerTurn.resetTurn();
    }

    private void placeReceivedTroops(int troops) {
        // TODO PLACE TROOPS
    }

    private int recievedTroops(Player player) {
        int value = 0;
        // Troops for turning in cards
        value += turningInCards(player);
        // Troops for territories and continents owned
        value += player.getTerritoriesOwned()/3;
        value += game.getValueOfContinentsOwned(player.getContinentsOwned());
        return value;
    }

    private int turningInCards(Player player) {
        // Player has to or can choose to turn in set of cards
        // Check if the player got more than 4 cards in his hand
        if (player.getHand().size() > 4) {
            // Player must turn in at least 1 set
            // TODO Player must selected cards from his hand and turn in a set requires need frontend
        }
        // TODO IF YOU HAVE A CARD WITH A TERRITORY ON IT THAT YOU OWN RECEIVE +2 TROOPS ON THAT TERRITORY
        return 1;
    }

    private void attacking(Player player) {
        narrator.addText("Brace yourself! Player " + player.getName() + " is attacking different players!");

        while(!playerTurn.hasTurnEnded()) {
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
                            // TODO CHECK IF GAME IS OVER
                            isGameOver(player);
                            // TODO if player eliminates a player he receives their cards
                            // TODO if player gets more then 6 cards --> turn in sets such that he has less than 4 cards but ones he has 4,3 or less cards stop trading
                            // TODO IF LESS THEN 6 CARDS HE CANT TRADE!
                            // TODO if at least one territory is captured player receives a card
                            // attacker.getTerritory().setNumberOfTroops(attacker.getTerritory().getNumberOfTroops() +- dice);
                            // defender.getTerritory().setNumberOfTroops(defender.getTerritory().getNumberOfTroops() +- dice);
                            map.updateTroopCount(attacker.getTerritory().getTerritoryNumber(),  attacker.getTerritory().getNumberOfTroops());
                            map.updateTroopCount(defender.getTerritory().getTerritoryNumber(),  defender.getTerritory().getNumberOfTroops());
                            map.deselectTerritory();
                            narrator.addText("Player " + player.getName() + " attacked " + attacker.getTerritory().getTerritoryName() + "(-numtroops) with " + defender.getTerritory().getTerritoryName());
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
    }

    private void fortifyTerritory(Player player) {
        narrator.addText("Player " + player.getName() + " is fortifying his territories!");
        boolean fortified = false;

        while (!playerTurn.hasTurnEnded() && !fortified) {
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

}
