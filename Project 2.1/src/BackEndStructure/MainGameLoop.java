package BackEndStructure;

import Visual.Map;

public class MainGameLoop {
    private final Game game;

    private boolean validTerritoryChosen = false;

    public MainGameLoop(int players, String[] playerNames) {
        this.game = new Game(players, playerNames);
        placementStage();
        attackingStage();
    }

    private void placementStage() {
        System.out.println("PLACEMENT PHASE");

        // For each player, for StartingTroops amount of rounds
        int round = 1;
        while(round != game.getStartingTroops()) {
            for (Player p : game.getPlayers()) {
                placeTroop(p);
            }
            round++;
        }
    }

    private void placeTroop(Player player) {
        System.out.println("it is " + player.getName() + "'s turn to place down 1 troop");

        while(!validTerritoryChosen) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}

            if(game.getMap().getTerritoryNumber() != 0) {
                if(game.getTerritories()[game.getMap().getTerritoryNumber()].getOwner().equals("unowned") || game.getTerritories()[game.getMap().getTerritoryNumber()].getOwner().equals(player.getName())) {
                    validTerritoryChosen = true;
                }
            }
        }
    }

    private void attackingStage() {
    }
}
