package BackEndStructure;

import Visual.Map;

public class MainGameLoop {
    private Game game;
    private Map map;

    public MainGameLoop(int players, String[] playerNames) {
        this.game = new Game(players, playerNames);
        this.map = game.getMap();
        map.createMap();
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

    private boolean validTerritoryChosen = false;

    private void placeTroop(Player player) {
        System.out.println("it is " + player.getName() + "'s turn to place down 1 troop");

        while(!validTerritoryChosen) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}

//            if(!map.getSelectedTerritory().equals("")) {
//                // TODO Check if the player can use the territory (aka unowed or playername matches territtory owner name
//                if(map.getSelectedTerritory().equals("unowned") || map.getSelectedTerritory().equals(player.getName())) {
//                    validTerritoryChosen = true;
//                }
//            }
        }
    }

    private void attackingStage() {
    }
}
