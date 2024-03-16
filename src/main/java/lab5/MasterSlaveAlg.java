package lab5;

import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MasterSlaveAlg {

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
        System.out.println("Single-thread");
        magickLoop(false);
        System.out.println("Multi-slave");
        magickLoop(true);
    }

    public static void magickLoop(boolean isMultithreading){
        for (int complexity = 0; complexity <= 5; complexity++) {
            List<Double> fitnesses = new ArrayList<>();
            List<Long> estimatedTime = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                AlgorithmResult result = evolve(isMultithreading, complexity);
                fitnesses.add(result.fitness);
                estimatedTime.add(result.estimateTime);
            }
            double averageFit = Estimator.meanDouble(fitnesses);
            int averageTime = Estimator.meanLong(estimatedTime);
            System.out.print("Complexity=" + complexity + " AvgFitness=" + String.format("%.2f", averageFit) + " AvgTime=" + averageTime);
            System.out.println();
        }
    }

    public static AlgorithmResult evolve(boolean isMultithreading, int complexity) {
        Random random = new Random(); // random

        CandidateFactory<double[]> factory = new MyFactory(dimension); // generation of solutions

        // Создание операторов и пайплайна эволюции
        ArrayList<EvolutionaryOperator<double[]>> operators = createOperators();
        EvolutionPipeline<double[]> pipeline = new EvolutionPipeline<double[]>(operators);

        SelectionStrategy<Object> selection = new RouletteWheelSelection(); // Selection operator

        FitnessEvaluator<double[]> evaluator = new MultiFitnessFunction(dimension, complexity); // Fitness function

        AbstractEvolutionEngine<double[]> algorithm = new SteadyStateEvolutionEngine<double[]>(
                factory, pipeline, evaluator, selection, populationSize, false, random);

        return execute(isMultithreading, algorithm);
    }

    private static ArrayList<EvolutionaryOperator<double[]>> createOperators() {
        // Создание операторов над множеством особей в популяции
        ArrayList<EvolutionaryOperator<double[]>> operators = new ArrayList<EvolutionaryOperator<double[]>>();
        operators.add(new MyCrossover(config.CROSSOVER_ALPHA)); // Crossover
        operators.add(new MyMutation(config.MUTATION_PROBABILITY, config.MUTATION_DECAY_RATE, config.MIN_MUTATION_PROBABILITY)); // Mutation
        return operators;
    }

    public static AlgorithmResult execute(boolean isMultithreading, AbstractEvolutionEngine<double[]> algorithm) {
        //  Запуска алгоритма

        algorithm.setSingleThreaded(!isMultithreading); // Change trigger here
        FitnessStorage fitnessStorage;
        if (isMultithreading) {
            fitnessStorage = new FitnessStorageSingletThread();
        } else {
            fitnessStorage = new FitnessStorageMultithreading();
        }


        algorithm.addEvolutionObserver(new EvolutionObserver() {
            public void populationUpdate(PopulationData populationData) {
                double bestFit = populationData.getBestCandidateFitness();
                fitnessStorage.update(bestFit);
            }
        });

        TerminationCondition terminate = new GenerationCount(generations);

        long algorithmStartTime = System.currentTimeMillis();
        algorithm.evolve(populationSize, 1, terminate);
        long algorithmDurationMs = (System.currentTimeMillis() - algorithmStartTime);

        return new AlgorithmResult(fitnessStorage.getBestFitness(), algorithmDurationMs);
    }
}
