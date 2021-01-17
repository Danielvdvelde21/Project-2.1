package BackEndStructure.Simulation.Stages.SimulatedEvents;

import BackEndStructure.Entities.Player;
import BackEndStructure.Game.Game;
import BackEndStructure.Graph.Territory;
import BackEndStructure.Graph.Vertex;

import java.util.ArrayList;
import java.util.SplittableRandom;

public class SimReceiveTroops {
    SplittableRandom splittableRandom = new SplittableRandom();

    private final Game game;

    public SimReceiveTroops(Game game) {
        this.game = game;
    }

    public int receivedTroops(Player player) {
        // Troops for territories owned
        int terri = player.getTerritoriesOwned() / 3;
        // Min 3 troops
        if (terri < 3) {
            terri = 3;
        }

        game.hasContinents(player);

        int conti = game.getValueOfContinentsOwned(player.getContinentsOwned());
        // System.out.println("Player " + player.getName() + " received " + terri + " troop(s) from Territories, " + conti + " troop(s) from Continents");
        return terri + conti;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Placing troops

    public void placeReceivedTroops(Player player, int troops) {
        for (int i = 0; i < troops; i++) {
            placeTroopRandomly(player);
        }
    }

    private void placeTroopRandomly(Player player) {
        // Get all the owned territories for this player
        ArrayList<Vertex> ownedTerritories = player.getOwnedTerritories();

        // Select a random territory

        if(ownedTerritories.size()<1){
            System.out.println(ownedTerritories.size());
            game.getGraph().printGraphShort();
        }
        Territory t = ownedTerritories.get(splittableRandom.nextInt(ownedTerritories.size())).getTerritory();

        // place a troop on the random territory
        t.setNumberOfTroops(t.getNumberOfTroops() + 1);
        // System.out.println("Player " + player.getName() + " placed 1 troop on " + t.getTerritoryName());
    }

}
