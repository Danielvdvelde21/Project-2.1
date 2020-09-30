package BackEndStructure;

import Visual.Map;

public class MainGameLoop {
    private final Game game;

    public MainGameLoop(int players, String[] playerNames) {
        this.game = new Game(players, playerNames);
        placementStage();
        attackingStage();
    }

    private void placementStage() {
        System.out.println("PLACEMENT PHASE");

        // For each player, for StartingTroops amount of rounds
        int round = 1;
        while (round != game.getStartingTroops()) {
            for (Player p : game.getPlayers()) {
                placeTroop(p);
            }
            round++;
        }
    }

    private void placeTroop(Player player) {
        System.out.println();
        System.out.println("It's " + player.getName() + "'s turn to place down 1 troop"); // TODO make textfield in game
        boolean validTerritoryChosen = false;

        while (!validTerritoryChosen) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }

            if (game.getMap().getTerritoryNumber() != -1) {
                if (game.getTerritories()[game.getMap().getTerritoryNumber()].getOwner().equals("unowned") || game.getTerritories()[game.getMap().getTerritoryNumber()].getOwner().equals(player.getName())) {
                    validTerritoryChosen = true;
                } else {
                    game.getMap().deselectTerritory();
                    System.out.println("This territory already belongs to a player!");
                }
            }
        }
        System.out.println(player.getName() + " put a troop on " + game.getTerritories()[game.getMap().getTerritoryNumber()].getTerritoryName());
        game.getTerritories()[game.getMap().getTerritoryNumber()].setOwner(player.getName());
        game.getMap().deselectTerritory();
    }

    private void attackingStage() {
    }
}
