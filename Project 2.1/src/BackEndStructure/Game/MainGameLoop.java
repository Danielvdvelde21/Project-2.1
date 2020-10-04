package BackEndStructure.Game;

import BackEndStructure.Entities.Player;
import BackEndStructure.Graph.Territory;

public class MainGameLoop {
    private final Game game;
    private boolean gameOver = false;

    public MainGameLoop(int players, String[] playerNames) {
        this.game = new Game(players, playerNames);
        placementStage();
        while(!gameOver) {
            for (Player p : game.getPlayers()) {
                playerTurn(p);
            }
        }
    }

    private void placementStage() {
        System.out.println("PLACEMENT PHASE");

        // For each player, for StartingTroops amount of rounds
        int round = 1;
        while (round != game.getStartingTroops()) {
            for (Player p : game.getPlayers()) {
                System.out.println("It's " + p.getName() + "'s turn to place down 1 troop");
                placeTroop(p);
            }
            round++;
        }
    }

    // Logic for whether a player can place down a troop on a territory
    private void placeTroop(Player player) {
        boolean validTerritoryChosen = false;

        while (!validTerritoryChosen) {
            try { Thread.sleep(100); } catch (InterruptedException e) {}
            // If a territory is selected
            if (game.getMap().getTerritoryNumber() != -1) {
                // Has the player selected one of his own territories or has he selected an unowned territory
                if (game.getTerritories()[game.getMap().getTerritoryNumber()].getOwner().equals("unowned") || game.getTerritories()[game.getMap().getTerritoryNumber()].getOwner().equals(player.getName())) {
                    validTerritoryChosen = true;
                } else {
                    game.getMap().deselectTerritory();
                    System.out.println("This territory already belongs to a player!");
                }
            }
        }
        System.out.println(player.getName() + " put a troop on " + game.getTerritories()[game.getMap().getTerritoryNumber()].getTerritoryName());
        // If the territory did not have an owner, set it to player
        if (game.getTerritories()[game.getMap().getTerritoryNumber()].getOwner().equals("unowned")) {
            game.getTerritories()[game.getMap().getTerritoryNumber()].setOwner(player.getName());
        }
        game.getMap().deselectTerritory();
    }

    private void playerTurn(Player p) {
        Territory attackingTerritory;

        while(!game.getMap().hasTurnEnded()) {
           // Cards TODO

           // Attacking TODO
            if (game.getMap().getTerritoryNumber() != -1 ) {
                if (game.getTerritories()[game.getMap().getTerritoryNumber()].getOwner().equals(p.getName())) {
                    attackingTerritory = game.getTerritories()[game.getMap().getTerritoryNumber()];
                    // attack, check adjacency!
                } else {
                    System.out.println("please choose a territory that belongs to you to attack another player!");
                }
            }
        }
        fortifyTerritories(p);
        game.getMap().resetTurnEnd();
    }

    private void fortifyTerritories(Player p) {
        // TODO
        // while (!fortified) {
        //      logic for fortification
        // }
    }
}
