package BackEndStructure.Simulation;

import AI.MCTS.MCTS;
import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Simulation.Stages.SimulatedGameStage;
import Visualisation.Map.Components.CardInventory;
import Visualisation.Map.Components.DicePanel;
import Visualisation.Map.Components.Narrator;
import Visualisation.Map.Components.PlayerTurn;

import java.util.ArrayList;

public class SimulatedGameLoop {

    // -----------------------------------------------------------------------------------------------------------------
    // Gameplay variables
    private final Game game;
    private Player winner;
    private MCTS tree;

    // -----------------------------------------------------------------------------------------------------------------
    // Updating visual variables
    private final Narrator narrator;

    // For updating the card inventory
    private final CardInventory cardInventory;

    // For updating the dice panel
    private final DicePanel dicePanel;

    // -----------------------------------------------------------------------------------------------------------------

    public SimulatedGameLoop(Graph g, ArrayList<Player> order) {
        this.game = new Game(g, order);
        this.narrator = game.getNarrator();
        this.cardInventory = game.getCardInventory();
        this.dicePanel = game.getDicePanel();
        cardInventory.setGame(game);
        dicePanel.setGame(game);

        // The game starts by every player rolling die to determine who goes first
        setOrder(order);

        // The game is about attacking, using cards, fortifying, etc.
        mainGameStage();

        // Game over
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

    public Player getWinner() { return winner; }

    public void setOrder(ArrayList<Player> order) { game.setPlayerOrder(order); }
}
