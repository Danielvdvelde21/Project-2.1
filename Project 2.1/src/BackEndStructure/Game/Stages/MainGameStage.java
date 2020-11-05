package BackEndStructure.Game.Stages;

import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Events.AttackEvent;
import BackEndStructure.Game.Events.FortifyEvent;
import BackEndStructure.Game.Events.ReceiveTroops;
import BackEndStructure.Game.Game;
import Visualisation.Map.Components.*;

public class MainGameStage {

    private final Game game;
    private final CardInventory cardInventory;
    private final PlayerTurn playerTurn;

    private final AttackEvent attack;
    private final FortifyEvent fortify;
    private final ReceiveTroops receiveTroops;

    // Game state
    private boolean gameOver = false;
    private Player winner;

    public MainGameStage(Game game) {
        this.game = game;
        this.cardInventory = game.getCardInventory();
        this.playerTurn = game.getPlayerTurn();
        cardInventory.setGame(game);

        this.attack = new AttackEvent(game);
        this.fortify = new FortifyEvent(game);
        this.receiveTroops = new ReceiveTroops(game);
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
