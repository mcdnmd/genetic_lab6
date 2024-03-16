package lab5;

import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.List;
import java.util.Random;

public class MyMutation implements EvolutionaryOperator<double[]> {
    private static double MUTATION_PROBABILITY;
    private static double DECAY_RATE;
    private static double MIN_MUTATION_PROBABILITY;
    private static final MyRandomGenerator randomGenerator = new MyRandomGenerator();

    public  MyMutation (double mutationProbability, double decayRate, double minMutationProbability) {
        MUTATION_PROBABILITY = mutationProbability;
        DECAY_RATE = decayRate;
        MIN_MUTATION_PROBABILITY = minMutationProbability;
    }

    public List<double[]> apply(List<double[]> population, Random random) {
        for (double[] individual : population) {
            mutate(individual);
        }

        //result population
        return population;
    }

    private static void mutate(double[] individual) {
        Random random = new Random();
        for (int i = 0; i < individual.length; i++) {
            // Генерация случайного числа для определения, произойдет ли мутация для данного гена
            if (random.nextDouble() < MUTATION_PROBABILITY) {
                // Замена значения гена случайным числом при помощи генератора случайных чисел
                individual[i] = randomGenerator.generateRandomDouble();
            }
            MUTATION_PROBABILITY *= DECAY_RATE;
            if (MUTATION_PROBABILITY < MIN_MUTATION_PROBABILITY) {
                MUTATION_PROBABILITY = MIN_MUTATION_PROBABILITY;
            }
        }

    }
}
