package BackEndStructure.Simulation.Stages;

import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;
import BackEndStructure.Simulation.Stages.SimulatedEvents.SimAttackEvent;
import BackEndStructure.Simulation.Stages.SimulatedEvents.SimFortifyEvent;
import BackEndStructure.Simulation.Stages.SimulatedEvents.SimReceiveTroops;

public class SimulatedGameStage {

    private final Game game;
    private boolean gameOver = false; // Game state
    private Player winner;

    private final SimAttackEvent attack;
    private final SimFortifyEvent fortify;
    private final SimReceiveTroops receiveTroops;

    //------------------------------------------------------------------------------------------------------------------
    // Constructor

    public SimulatedGameStage(Game game) {
        this.game = game;

        this.attack = new SimAttackEvent(game);
        this.fortify = new SimFortifyEvent(game.getGraph());
        this.receiveTroops = new SimReceiveTroops(game);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Gameplay

    public void mainGameStage() {
        while (!gameOver) {
            for (Player p : game.getPlayers()) {
                playerTurn(p);
                if(gameOver) {
                    break;
                }
            }
        }
        winner = attack.getWinner();
    }

    private void playerTurn(Player player) {
        // Gain troops at start of turn
        receiveTroops.placeReceivedTroops(player, receiveTroops.receivedTroops(player));

        // Player can start attacking different territories
        attack.attacking(player);
        gameOver = attack.getGameState();

        // Player can fortify 1 territory if he chooses to do so at the end of his turn
        if (!gameOver) {
            fortify.randomFortification(player);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // Extra methods

    public Player getWinner() {
        return winner;
    }
}
