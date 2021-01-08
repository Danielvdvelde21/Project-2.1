package BackEndStructure.Simulation;

import AI.MCTS.MCTS;
import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;
import BackEndStructure.Game.Stages.MainGameStage;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Simulation.Stages.SimulatedGameStage;
import Visualisation.Map.Components.CardInventory;
import Visualisation.Map.Components.DicePanel;
import Visualisation.Map.Components.Narrator;
import Visualisation.Map.Components.PlayerTurn;

import java.util.ArrayList;

public class SimulatedGameLoop {

    private final boolean simulatedGame;

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

    public SimulatedGameLoop(Graph g, ArrayList<Player> order) {
        this.simulatedGame = true;
        this.game = new Game(g, order);
        this.narrator = game.getNarrator();
        this.playerTurn = game.getPlayerTurn();
        this.cardInventory = game.getCardInventory();
        this.dicePanel = game.getDicePanel();
        cardInventory.setGame(game);
        dicePanel.setGame(game);

        // The game starts by every player rolling die to determine who goes first
        setOrder(order);

        // The game is about attacking, using cards, fortifying, etc.
        mainGameStage();

        // Game over
        System.out.println("MAINGAMELOOP DISABLE ME! WINNER = " + winner.getName());
        narrator.addText("GAME OVER! PLAYER " + winner.getName() + " IS VICTORIOUS");
    }

    // -----------------------------------------------------------------------------------------------------------------
    // MainGameStage

    public void mainGameStage() {
        SimulatedGameStage stage = new SimulatedGameStage(game);
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


    public void setOrder(ArrayList<Player> order) {
        this.order = order;
    }
}
