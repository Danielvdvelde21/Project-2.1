package BackEndStructure.Simulation;

import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Vertex;
import BackEndStructure.Simulation.Stages.SimulatedGameStage;

import java.util.ArrayList;

public class SimulatedGameLoop {

    // -----------------------------------------------------------------------------------------------------------------
    // Gameplay variables
    private final Game game;
    private Player winner;

    // -----------------------------------------------------------------------------------------------------------------

    public SimulatedGameLoop(Graph g, ArrayList<Player> order, Vertex[] firstAttack) {
        this.game = new Game(g, order);

        // The game is about attacking, using cards, fortifying, etc.
        mainGameStage(firstAttack);

        // Game over
        System.out.println("Game over! Winner is " + winner.getName());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // MainGameStage

    public void mainGameStage(Vertex[] firstAttack) {
        SimulatedGameStage stage = new SimulatedGameStage(game);
        stage.mainGameStage(firstAttack);
        winner = stage.getWinner();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Extra methods

    public Player getWinner() { return winner; }

    public Graph getGraph() { return game.getGraph(); }

}
