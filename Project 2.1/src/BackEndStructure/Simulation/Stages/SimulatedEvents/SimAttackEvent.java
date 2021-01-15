package BackEndStructure.Simulation.Stages.SimulatedEvents;

import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;
import BackEndStructure.Graph.Edge;
import BackEndStructure.Graph.Vertex;

import java.util.ArrayList;
import java.util.Random;

public class SimAttackEvent {

    private final Game game;

    private boolean gameOver = false;
    private Player winner;

    private final ArrayList<Player> eliminatedPlayers = new ArrayList<>();

    public SimAttackEvent(Game game) {
        this.game = game;
    }

    public void randomAttack(Player player) {
        // Get all the owned territories for this player
        ArrayList<Vertex> ownedTerritories = player.getOwnedTerritories();

        // Can't attack with only 1 troop or attack if the territory is surrounded by friendly territories
        ArrayList<Vertex> validAttackers = new ArrayList<>();
        for (Vertex v : ownedTerritories) {
            if (v.getTerritory().getNumberOfTroops() > 1) {
                boolean enemy = false;
                for (Edge e : v.getEdges()) {
                    if (e.getVertex().getTerritory().getOwner() != player) {
                        enemy = true;
                        break;
                    }
                }
                if (enemy) {
                    validAttackers.add(v);
                }
            }
        }

        Random random = new Random();

        // Randomly select attacker if there is a valid one
        if (!validAttackers.isEmpty()) {
            Vertex attacker = validAttackers.get(random.nextInt(validAttackers.size()));

            // Randomly select defender
            ArrayList<Edge> validDefenders = new ArrayList<>();
            for (Edge e : attacker.getEdges()) {
                if (e.getVertex().getTerritory().getOwner() != player) {
                    validDefenders.add(e);
                }
            }
            Vertex defender = validDefenders.get(random.nextInt(validDefenders.size())).getVertex();

            // Attack will finish either by ending up with 1 troop or capturing (capturing should return true anyway)
            while (attacker.getTerritory().getNumberOfTroops() >= 2 && defender.getTerritory().getOwner() != player) {
                int numberOfAttackerDice;
                int numberOfDefenderDice;
                int[] attackerDiceValues = new int[3];
                int[] defenderDiceValues = new int[2];

                // Setting the attacker dice
                switch (attacker.getTerritory().getNumberOfTroops()) {
                    case 2:
                        numberOfAttackerDice = 1;
                        attackerDiceValues[0] = (int) (Math.random() * 6) + 1;
                        break;
                    case 3:
                        numberOfAttackerDice = 2;
                        attackerDiceValues[0] = (int) (Math.random() * 6) + 1;
                        attackerDiceValues[1] = (int) (Math.random() * 6) + 1;
                        break;
                    default:
                        numberOfAttackerDice = 3;
                        attackerDiceValues[0] = (int) (Math.random() * 6) + 1;
                        attackerDiceValues[1] = (int) (Math.random() * 6) + 1;
                        attackerDiceValues[2] = (int) (Math.random() * 6) + 1;
                }

                // Setting the defender dice
                if (defender.getTerritory().getNumberOfTroops() > 1) {
                    numberOfDefenderDice = 2;
                    defenderDiceValues[0] = (int) (Math.random() * 6) + 1;
                    defenderDiceValues[1] = (int) (Math.random() * 6) + 1;
                } else {
                    numberOfDefenderDice = 1;
                    defenderDiceValues[0] = (int) (Math.random() * 6) + 1;
                }
                // Perform a fight
                game.getAttackingHandler().oneFight(numberOfAttackerDice, attackerDiceValues, numberOfDefenderDice, defenderDiceValues);

                // Update troops counts
                attacker.getTerritory().setNumberOfTroops(attacker.getTerritory().getNumberOfTroops() - game.getAttackingHandler().getLostTroopsAttackers());
                defender.getTerritory().setNumberOfTroops(defender.getTerritory().getNumberOfTroops() - game.getAttackingHandler().getLostTroopsDefenders());
                // System.out.println("Player: " + player.getName() + " attacked " + defender.getTerritory().getTerritoryName() + "(-" + game.getAttackingHandler().getLostTroopsDefenders() + ")" + " Using " + attacker.getTerritory().getTerritoryName() + "(-" + game.getAttackingHandler().getLostTroopsAttackers() + ")");

                // Reset classes
                game.getAttackingHandler().resetTroopsLost();

                // If a territory is captured
                if (defender.getTerritory().getNumberOfTroops() < 1) {
                    // oneTerritoryCaptured = true;
                    territoryCaptured(player, defender, attacker);
                    return;
                }
            }
        }
    }

    // Logic that needs to happen after a territory is captured
    private void territoryCaptured(Player player, Vertex defender, Vertex attack) {
        // MCTS BOT ONLY UPDATE OWNED TROOPS
        defender.getTerritory().getOwner().getOwnedTerritories().remove(defender);
        attack.getTerritory().getOwner().getOwnedTerritories().add(defender);

        // Player gets the territory
        Player defenderOwner = defender.getTerritory().getOwner();
        player.increaseTerritoriesOwned();
        defender.getTerritory().setOwner(player);

        // defender loses his territory
        decreaseTerritories(defenderOwner);

        isGameOver(player);
        if (isEliminated(defenderOwner)) {
            //receiveCards(player, defenderOwner);
            eliminatePlayer(defenderOwner);
        }

        // How many troops are sent over, for now all but 1
        int troops;
        troops = attack.getTerritory().getNumberOfTroops() - 1;

        attack.getTerritory().setNumberOfTroops(attack.getTerritory().getNumberOfTroops() - troops);
        defender.getTerritory().setNumberOfTroops(defender.getTerritory().getNumberOfTroops() + troops);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Extra Methods

    // If a player is defeated
    private boolean isEliminated(Player d) {
        return d.getTerritoriesOwned() == 0;
    }

    // Remove player from player list
    private void eliminatePlayer(Player p) {
        eliminatedPlayers.add(p);
    }

    public ArrayList<Player> getEliminatedPlayers() {
        return eliminatedPlayers;
    }

    private void decreaseTerritories(Player p) {
        p.decreaseTerritoriesOwned();
    }

    private void isGameOver(Player player) {
        gameOver = player.getTerritoriesOwned() == 42;
        if (gameOver) {
            winner = player;
        }
    }

    public Player getWinner() {
        return winner;
    }

    public boolean getGameState() {
        return gameOver;
    }
}
