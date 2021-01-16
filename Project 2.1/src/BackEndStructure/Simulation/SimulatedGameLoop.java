package BackEndStructure.Simulation;

import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Simulation.Stages.SimulatedGameStage;
import AI.MCTS.State;

public class SimulatedGameLoop {

    // -----------------------------------------------------------------------------------------------------------------
    // Gameplay variables
    private final Game game;
    private Player winner;

    // -----------------------------------------------------------------------------------------------------------------

    public SimulatedGameLoop(State s) {
        AI.GlobalVariables.SIMULATED=true;
        this.game = new Game(s.getGraph(), s.getOrder());
        mainGameStage();
        AI.GlobalVariables.SIMULATED=false;
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
