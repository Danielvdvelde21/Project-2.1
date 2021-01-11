package BackEndStructure.Simulation;

import AI.MCTS.MCTS;
import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Simulation.Stages.SimulatedGameStage;

import java.util.ArrayList;

public class SimulatedGameLoop {

    // -----------------------------------------------------------------------------------------------------------------
    // Gameplay variables
    private final Game game;
    private Player winner;
    private MCTS tree;

    // -----------------------------------------------------------------------------------------------------------------

    public SimulatedGameLoop(Graph g, ArrayList<Player> order) {
        this.game = new Game(g, order);

        // The game starts by every player rolling die to determine who goes first
        setOrder(order);

        // The game is about attacking, using cards, fortifying, etc.
        mainGameStage();

        // Game over
        System.out.println("Game over! Winner is " + winner.getName());
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