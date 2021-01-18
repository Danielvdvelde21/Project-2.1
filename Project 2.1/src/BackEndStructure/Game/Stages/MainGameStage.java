package BackEndStructure.Game.Stages;

import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Stages.MainGameStageEvents.AttackEvent;
import BackEndStructure.Game.Stages.MainGameStageEvents.FortifyEvent;
import BackEndStructure.Game.Stages.MainGameStageEvents.ReceiveTroops;
import BackEndStructure.Game.Game;
import Visualisation.Map.Components.*;

import java.util.Iterator;

public class MainGameStage {

    private final Game game;
    private final CardInventory cardInventory;
    private final PlayerTurn playerTurn;

    private final AttackEvent attack;
    private final FortifyEvent fortify;
    private final ReceiveTroops receiveTroops;
    public int turnNo=0;

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

    public void mainGameStage(boolean randomPlayer) {
        turnNo=0;
        while (!gameOver&&turnNo<100) {
            Iterator<Player> itr = game.getPlayers().iterator();
            while (itr.hasNext()){
                Player player = itr.next();
                if (attack.getEliminatedPlayers().contains(player)) {
                    attack.getEliminatedPlayers().remove(player);
                    itr.remove();
                } else {
                    // Set the player's inventory and turn
                    playerTurn.setPlayerTurn(player);
                    cardInventory.setCurrentPlayer(player);
                    playerTurn(player, randomPlayer);

                    if (gameOver) {
                        break;
                    }
                }
            }
            turnNo++;
        }
        if(gameOver) winner = attack.getWinner();
        else winner = null;
    }

    private void playerTurn(Player player, boolean randomPlayer) {
        // Gain troops at start of turn
        receiveTroops.placeReceivedTroops(player, receiveTroops.receivedTroops(player, randomPlayer), randomPlayer);

        // Player can start attacking different territories
        playerTurn.resetTurn();
        attack.attacking(player, randomPlayer);
        gameOver = attack.getGameState();

        // Player can fortify 1 territory if he chooses to do so at the end of his turn
        if (!gameOver) {
            playerTurn.resetTurn();
            fortify.fortifyTerritory(player, randomPlayer);
        }
    }

    public Player getWinner() {
        return winner;
    }

}
