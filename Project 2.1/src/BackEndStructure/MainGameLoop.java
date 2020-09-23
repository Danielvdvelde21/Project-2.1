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

        for (Player p : game.getPlayers()) {
            System.out.println(p.getName());
            while (map.getSelectedTerritory().equals("")) {
                // Apperantly we need sleep otherwise it doesn't work?
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            map.resetSelectedTerritory();
        }
    }

    private void attackingStage() {
    }
}
