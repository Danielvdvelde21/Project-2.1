package AI.GeneticAlgorithm;

import BackEndStructure.Entities.Player;

import java.awt.*;

public class Individual {

    private final Player player;
    private int fitness;

    public Individual(String name, Color c) {
        this.player = new Player(name, c, true);
        this.fitness = 0;
    }

    public void setFitness(int v) {
        fitness = v;
    }

    public int getFitness() {
        return fitness;
    }

    public Player getPlayer() {
        return player;
    }
}
