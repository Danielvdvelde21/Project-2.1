package BackEndStructure.Simulation.Stages;

import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;
import BackEndStructure.Simulation.Stages.SimulatedEvents.SimAttackEvent;
import BackEndStructure.Simulation.Stages.SimulatedEvents.SimFortifyEvent;
import BackEndStructure.Simulation.Stages.SimulatedEvents.SimReceiveTroops;
import Visualisation.Map.Components.CardInventory;
import Visualisation.Map.Components.PlayerTurn;

public class SimulatedGameStage {

    private final Game game;
    private final CardInventory cardInventory;
    private final PlayerTurn playerTurn;

    private final SimAttackEvent attack;
    private final SimFortifyEvent fortify;
    private final SimReceiveTroops receiveTroops;

    // Game state
    private boolean gameOver = false;
    private Player winner;

    public SimulatedGameStage(Game game) {
        this.game = game;
        this.cardInventory = game.getCardInventory();
        this.playerTurn = game.getPlayerTurn();
        cardInventory.setGame(game);

        this.attack = new SimAttackEvent(game);
        this.fortify = new SimFortifyEvent(game);
        this.receiveTroops = new SimReceiveTroops(game);
    }

    public void mainGameStage() {
        while (!gameOver) {
            for (Player p : game.getPlayers()) {
                // Set the player's inventory and turn
                playerTurn.setPlayerTurn(p);
                cardInventory.setCurrentPlayer(p);
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
        playerTurn.resetTurn();
        attack.attacking(player);
        gameOver = attack.getGameState();

        // Player can fortify 1 territory if he chooses to do so at the end of his turn
        if (!gameOver) {
            playerTurn.resetTurn();
            fortify.fortifyTerritory(player);
        }
    }

    public Player getWinner() {
        return winner;
    }
}
