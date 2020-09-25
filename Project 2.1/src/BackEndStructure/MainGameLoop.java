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
                System.out.println("it is " + p.getName() + " turn to place down 1 troop");
                while (map.getSelectedTerritory().equals("")) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                map.resetSelectedTerritory();
            }
            round++;
        }
    }

    private void attackingStage() {
    }
}
