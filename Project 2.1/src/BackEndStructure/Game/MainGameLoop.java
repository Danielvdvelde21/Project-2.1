package BackEndStructure.Game;

import AI.MCTS.MCTS;
import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Stages.MainGameStage;
import BackEndStructure.Game.Stages.PlacementStage;
import Visualisation.Map.Components.*;
import java.util.ArrayList;
import java.util.Collections;

public class MainGameLoop {

    // -----------------------------------------------------------------------------------------------------------------
    // Gameplay variables
    private final Game game;
    private Player winner;
    private ArrayList<Player> order;
    private MCTS tree;

    // -----------------------------------------------------------------------------------------------------------------
    // Updating visual variables
    private final Narrator narrator;

    // For updating the player turn label (current player)
    private final PlayerTurn playerTurn;

    // For updating the card inventory
    private final CardInventory cardInventory;

    // For updating the dice panel
    private final DicePanel dicePanel;

    // -----------------------------------------------------------------------------------------------------------------

    public MainGameLoop(int players, String[] playerNames, boolean[] bots, boolean simulatedGame) {
        this.game = new Game(players, playerNames, bots);
        this.narrator = game.getNarrator();
        this.playerTurn = game.getPlayerTurn();
        this.cardInventory = game.getCardInventory();
        this.dicePanel = game.getDicePanel();
        cardInventory.setGame(game);
        dicePanel.setGame(game);

        tree = new MCTS();

        // The game starts by every player rolling die to determine who goes first
        determinePlayerOrder();
        tree.setPlayerOrder(order);

        // The game starts by every player starting to place troops on the board
        placementStage();

        // The game is about attacking, using cards, fortifying, etc.
        mainGameStage();

        // Game over
        narrator.addText("GAME OVER! PLAYER " + winner.getName() + " IS VICTORIOUS");
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Player Order

    // If players throw the same number the one that first threw that number goes first
    private void determinePlayerOrder() {
        dicePanel.allowRolling(true);
        order = new ArrayList<>();
        ArrayList<Integer> values = new ArrayList<>();

        // For each player in the game wait until he rolled the dice
        for (int i = 0; i < game.getPlayers().size(); i++) {
            narrator.addText("Player " + game.getPlayers().get(i).getName() + " may roll the dice!");
            playerTurn.setPlayerTurn(game.getPlayers().get(i));
            if (game.getPlayers().get(i).isBot()) {
                dicePanel.rollAttackDie();
                dicePanel.resetDiceRolled();
            } else {
                while (!dicePanel.isDiceRolled()) {
                    delay();
                }
            }
            values.add(dicePanel.getEyesPlayerOrderDice());
            narrator.addText("Player " + game.getPlayers().get(i).getName() + "  rolled " + dicePanel.getEyesPlayerOrderDice());
        }
        for (int i = 0; i < game.getPlayers().size(); i++) {
            order.add(game.getPlayers().get(values.indexOf(Collections.max(values))));
            values.set(values.indexOf(Collections.max(values)), 0);
        }
        game.setPlayerOrder(order);
        dicePanel.allowRolling(false);
        dicePanel.playerOrderObtained();
        narrator.addText("Player order is " + game.getPlayerOrder());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // PlacementStage

    private void placementStage() {
        PlacementStage stage = new PlacementStage(game);
        stage.placementStage();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // MainGameStage

   public void mainGameStage() {
        MainGameStage stage = new MainGameStage(game);
        stage.mainGameStage();
        winner = stage.getWinner();
   }

    // -----------------------------------------------------------------------------------------------------------------
    // Extra methods

    // Creates a delay
    private void delay() {
        try { Thread.sleep(100); } catch (InterruptedException ignored) {}
    }

    public Player getWinner() { return winner; }

    public ArrayList<Player> getOrder() {
        return order;
    }

    public void setOrder(ArrayList<Player> order) {
        this.order = order;
    }
}
