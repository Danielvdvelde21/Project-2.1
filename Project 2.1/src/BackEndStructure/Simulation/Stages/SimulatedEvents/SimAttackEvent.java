package BackEndStructure.Simulation.Stages.SimulatedEvents;

import AI.MCTS.Node;
import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;
import BackEndStructure.Graph.Edge;
import BackEndStructure.Graph.Vertex;

import java.util.ArrayList;
import java.util.SplittableRandom;

public class SimAttackEvent {

    private final Game game;

    private boolean gameOver = false;
    private Player winner;
    SplittableRandom splittableRandom = new SplittableRandom();

    private final ArrayList<Player> eliminatedPlayers = new ArrayList<>();

    public SimAttackEvent(Game game) {
        this.game = game;
    }

    public void randomAttack(Player player) {
        // Get all the owned territories for this player
        ArrayList<Vertex> ownedTerritories = player.getOwnedTerritories();
        // Can't attack with only 1 troop or attack if the territory is surrounded by friendly territories
        Vertex[] validAttackers = new Vertex[10];
        int validAttackersNo=0;

        for (Vertex v : ownedTerritories) {
            if (v.getTerritory().getNumberOfTroops() > 1) {
                boolean enemy = false;
                Edge[] neighbours = v.getEdges();
                int neighboursNo = v.getEdgeNo();
                for (int i=0;i<neighboursNo;i++) {
                    Edge e=neighbours[i];
                    if (e.getVertex().getTerritory().getOwner() != player) {
                        enemy = true;
                        break;
                    }
                }
                if (enemy) {
                    if(validAttackersNo==validAttackers.length-1){
                        Vertex[] validAttackers2= new Vertex[validAttackers.length * 2];
                        copyAintoB(validAttackers,validAttackers2);
                        validAttackers=validAttackers2;
                    }
                    validAttackers[validAttackersNo]=v;
                    validAttackersNo++;
                }
            }
        }

        

        // Randomly select attacker if there is a valid one
        if (validAttackersNo!=0) {
            Vertex attacker = validAttackers[splittableRandom.nextInt(validAttackersNo)];

            // Randomly select defender
            Edge[] validDefenders = new Edge[10];
            int validDefendersNo=0;
            Edge[] neighbours = attacker.getEdges();
            int neighboursNo = attacker.getEdgeNo();
            for (int i=0;i<neighboursNo;i++) {
                Edge e=neighbours[i];
                if (e.getVertex().getTerritory().getOwner() != player) {
                    if(validDefendersNo==validDefenders.length-1){
                        Edge[] validDefenders2= new Edge[validDefenders.length * 2];
                        copyAintoB(validDefenders,validDefenders2);
                        validDefenders=validDefenders2;
                    }
                    validDefenders[validDefendersNo]=e;
                    validDefendersNo++;
                }
            }
            Vertex defender = validDefenders[splittableRandom.nextInt(validDefendersNo)].getVertex();

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
                        attackerDiceValues[0] = splittableRandom.nextInt(6) + 1;
                        break;
                    case 3:
                        numberOfAttackerDice = 2;
                        attackerDiceValues[0] = splittableRandom.nextInt(6) + 1;
                        attackerDiceValues[1] = splittableRandom.nextInt(6) + 1;
                        break;
                    default:
                        numberOfAttackerDice = 3;
                        attackerDiceValues[0] = splittableRandom.nextInt(6) + 1;
                        attackerDiceValues[1] = splittableRandom.nextInt(6) + 1;
                        attackerDiceValues[2] = splittableRandom.nextInt(6) + 1;
                }

                // Setting the defender dice
                if (defender.getTerritory().getNumberOfTroops() > 1) {
                    numberOfDefenderDice = 2;
                    defenderDiceValues[0] = splittableRandom.nextInt(6) + 1;
                    defenderDiceValues[1] = splittableRandom.nextInt(6) + 1;
                } else {
                    numberOfDefenderDice = 1;
                    defenderDiceValues[0] = splittableRandom.nextInt(6) + 1;
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
                }
            }
        }
    }

    private void copyAintoB(Vertex[]a,Vertex[]b){
        for (int i=0;i<a.length;i++) {
            b[i]=a[i];
        }
    }

    private void copyAintoB(Edge[]a,Edge[]b){
        for (int i=0;i<a.length;i++) {
            b[i]=a[i];
        }
    }
    
    // Logic that needs to happen after a territory is captured
    private void territoryCaptured(Player player, Vertex defender, Vertex attack) {
        // MCTS BOT ONLY UPDATE OWNED TROOPS
        defender.getTerritory().getOwner().getOwnedTerritories().remove(defender);
        attack.getTerritory().getOwner().getOwnedTerritories().add(defender);

        // Player gets the territory
        Player defenderOwner = defender.getTerritory().getOwner();
        if (!(attack.getTerritory().getOwner() == player)) {
            throw new IllegalArgumentException("test");
        }
        player.increaseTerritoriesOwned();
        defender.getTerritory().setOwner(player);

        // defender loses his territory
        decreaseTerritories(defenderOwner);

        isGameOver(player);
        if (isEliminated(defenderOwner)) {
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

    public void setGameOver(boolean b) { gameOver = b; }
}
