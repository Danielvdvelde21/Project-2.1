package BackEndStructure;

public class MainGameLoop {
    private Game game;

    public MainGameLoop(int players, String[] playerNames) {
        this.game = new Game(players, playerNames);
        game.getMap().createMap();
        placementStage();
        attackingStage();
    }

    private void placementStage() {
        System.out.println("PLACEMENT FASE");
        for (Player p : game.getPlayers()) {
            // TODO player can pick territory and can put a troop there
        }
    }

    private void attackingStage() {
    }
}
