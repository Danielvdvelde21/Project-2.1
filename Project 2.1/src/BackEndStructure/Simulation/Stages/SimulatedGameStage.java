package BackEndStructure.Simulation.Stages;

import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;
import BackEndStructure.Graph.Graph;
import BackEndStructure.Graph.Vertex;
import BackEndStructure.Simulation.Stages.SimulatedEvents.SimAttackEvent;
import BackEndStructure.Simulation.Stages.SimulatedEvents.SimFortifyEvent;
import BackEndStructure.Simulation.Stages.SimulatedEvents.SimReceiveTroops;

import java.util.Iterator;

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

    public void mainGameStage(Vertex[] firstAttack) {
        // First turn
        // MCTS bot make their move
        // REQUIRES MCTS BOT TO BE THE FIRST IN THE PLAYERORDER!

        boolean MCTSFirstMove = true;
        Iterator<Player> it1 = game.getPlayers().iterator();
        while (it1.hasNext()) {
            Player player = it1.next();
            if (attack.getEliminatedPlayers().contains(player)) {
                attack.getEliminatedPlayers().remove(player);
                it1.remove();
            } else {
                if (MCTSFirstMove) {
                    MCTSFirstMove = false;
                    receiveTroops.placeReceivedTroops(player, receiveTroops.receivedTroops(player));

                    // Calculated attack
                    attack.MCTSAttack(player, firstAttack[0], firstAttack[1]);
                    gameOver = attack.getGameState();

                    if (!gameOver) {
                        fortify.randomFortification(player);
                    }
                } else {
                    playerTurn(player);
                }
                if (gameOver) {
                    break;
                }
            }
        }

        // All the next turns
        while (!gameOver) {
            Iterator<Player> it2 = game.getPlayers().iterator();
            while (it2.hasNext()) {
                Player player = it2.next();
                if (attack.getEliminatedPlayers().contains(player)) {
                    attack.getEliminatedPlayers().remove(player);
                    it2.remove();
                } else {
                    playerTurn(player);

                    if (gameOver) {
                        break;
                    }
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
