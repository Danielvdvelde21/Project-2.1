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

    public SimulatedGameLoop(Graph g, ArrayList<Player> order) {
        this.game = new Game(g, order);

        // The game is about attacking, using cards, fortifying, etc.
        mainGameStage();
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

    public Graph getGraph() { return game.getGraph(); }

}
