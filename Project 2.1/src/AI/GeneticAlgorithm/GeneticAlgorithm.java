package AI.GeneticAlgorithm;

import BackEndStructure.Game.MainGameLoop;

public class GeneticAlgorithm {

    private final int populationSize;
    private final int iterations;
    private Population population;

    public GeneticAlgorithm(int populationSize, int iterations) {
        this.populationSize = populationSize;
        this.iterations = iterations;
        this.population = new Population(populationSize);
    }

    public void runAlgorithm() {
        int generationCount = 0;
        while (generationCount < iterations) {
            System.out.println("Generation: " + generationCount);
            population = evolvePopulation(population);
            generationCount++;
        }
    }

    public Population evolvePopulation(Population population) {
        for (int i = 0, j = 1; i < populationSize; i++, j++) {
            // Play a game between 2 bots
            Individual bot1 = population.getIndividuals().get(i);
            Individual bot2 = population.getIndividuals().get(j);
            MainGameLoop game = new MainGameLoop(2, new String[]{bot1.getPlayer().getName(), bot2.getPlayer().getName()}, new boolean[]{true, true});

            // The individuals get a binary fitness value {0,1}
            if (game.getWinner()==bot1.getPlayer()) {
                bot1.setFitness(1);
            } else {
                bot2.setFitness(1);
            }
        }
        return population;
    }

    // Todo tournament selection


    // Todo mutation
    public void mutation() {

    }

    // Todo evaluation function


}
