package AI.GeneticAlgorithm;

import java.awt.*;
import java.util.ArrayList;

public class Population {

    ArrayList<Individual> individuals = new ArrayList<>();

    public Population(int populationSize) {
        for (int i = 0; i < populationSize / 2; i++) {
            individuals.add(new Individual("bot " + i, i, Color.red));
        }
        for (int i = populationSize / 2; i < populationSize; i++) {
            individuals.add(new Individual("bot " + i, i, Color.blue));
        }
    }

    private Individual getFittest() {
        Individual fittest = individuals.get(0);
        int fittestValue = 0;

        for (Individual individual : individuals) {
            if(individual.getFitness() > fittestValue) {
                fittest = individual;
                fittestValue = individual.getFitness();
            }
        }

        return fittest;
    }

    public ArrayList<Individual> getIndividuals() {
        return individuals;
    }

}
