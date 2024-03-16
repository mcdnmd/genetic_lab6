package lab5;

import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.islands.IslandEvolution;
import org.uncommons.watchmaker.framework.islands.IslandEvolutionObserver;
import org.uncommons.watchmaker.framework.islands.Migration;
import org.uncommons.watchmaker.framework.islands.RingMigration;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class IslandsAlg {

    public static Config config;

    public static int dimension = 50; // dimension of problem

    public static int populationSize = 100; // size of population
    public static int  generations = 100; // number of generations

    public static void main(String[] args) {
        config = new Config(
                0.35,
                0.3,
                0.9,
                0.01
        );

        for (int complexity = 0; complexity <= 5; complexity++) {
            List<Double> fitnesses = new ArrayList<>();
            List<Long> estimatedTime = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                AlgorithmResult result = evolve(complexity);
                fitnesses.add(result.fitness);
                estimatedTime.add(result.estimateTime);
            }
            double averageFit = Estimator.meanDouble(fitnesses);
            int averageTime = Estimator.meanLong(estimatedTime);
            System.out.print("Complexity=" + complexity + " AvgFitness=" + String.format("%.2f", averageFit) + " AvgTime=" + averageTime);
            System.out.println();
        }
    }

    private static AlgorithmResult evolve(int complexity) {
        Random random = new Random(); // random

        CandidateFactory<double[]> factory = new MyFactory(dimension); // generation of solutions

        // Создание операторов и пайплайна эволюции
        ArrayList<EvolutionaryOperator<double[]>> operators = createOperators();
        EvolutionPipeline<double[]> pipeline = new EvolutionPipeline<double[]>(operators);

        SelectionStrategy<Object> selection = new RouletteWheelSelection(); // Selection operator

        FitnessEvaluator<double[]> evaluator = new MultiFitnessFunction(dimension, complexity); // Fitness function

        int islandAmount = 3;
        Migration migration = new RingMigration();
        IslandEvolution<double[]> island_model = new IslandEvolution<>(
                islandAmount, migration, factory, pipeline, evaluator, selection, random);


        return execute(island_model);
    }

    private static AlgorithmResult execute(IslandEvolution<double[]> islandModel){
        FitnessStorage fitnessStorage = new FitnessStorageMultithreading();

        islandModel.addEvolutionObserver(new IslandEvolutionObserver() {
            public void populationUpdate(PopulationData populationData) {
                double bestFit = populationData.getBestCandidateFitness();
                fitnessStorage.update(bestFit);
            }

            public void islandPopulationUpdate(int i, PopulationData populationData) {
                double bestFit = populationData.getBestCandidateFitness();
                fitnessStorage.update(bestFit);

            }
        });

        TerminationCondition terminate = new GenerationCount(generations);

        long algorithmStartTime = System.currentTimeMillis();
        islandModel.evolve(populationSize, 1, 50, 2, terminate);
        long algorithmDurationMs = (System.currentTimeMillis() - algorithmStartTime);

        return new AlgorithmResult(fitnessStorage.getBestFitness(), algorithmDurationMs);
    }

    private static ArrayList<EvolutionaryOperator<double[]>> createOperators() {
        // Создание операторов над множеством особей в популяции
        ArrayList<EvolutionaryOperator<double[]>> operators = new ArrayList<EvolutionaryOperator<double[]>>();
        operators.add(new MyCrossover(config.CROSSOVER_ALPHA)); // Crossover
        operators.add(new MyMutation(config.MUTATION_PROBABILITY, config.MUTATION_DECAY_RATE, config.MIN_MUTATION_PROBABILITY)); // Mutation
        return operators;
    }

}
